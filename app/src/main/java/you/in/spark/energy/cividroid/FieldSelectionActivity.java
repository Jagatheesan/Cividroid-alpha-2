package you.in.spark.energy.cividroid;

import android.content.ContentProviderOperation;
import android.content.ContentProviderResult;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import android.util.Log;
import android.util.Pair;
import android.view.View;

import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.JsonObject;
import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

import retrofit.RestAdapter;
import you.in.spark.energy.cividroid.api.ICiviApi;
import you.in.spark.energy.cividroid.entities.Contact;
import you.in.spark.energy.cividroid.fragments.ContactsSubTypeSelectionFragment;
import you.in.spark.energy.cividroid.sync.SyncAdapter;


public class FieldSelectionActivity extends AppCompatActivity {

    public static String apiKey, siteKey, websiteUrl;
    public static LinearLayout waitScreen = null;
    public static ImageView civiRotate = null;
    private int waitTime;
    private int splitTime;
    public static int fragmentStatus = 0;


    private static int[] types = new int[3];
    private ViewPagerAdapter viewPagerAdapter;
    private Vector<String>[] subTypes = new Vector[3];





    private ViewPager super -

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.field_selection);

        waitScreen = (LinearLayout) findViewById(R.id.waitScreen);
        final TextView waitText = (TextView) findViewById(R.id.waitText);
        civiRotate = (ImageView) findViewById(R.id.civilogoRotate);

        final Animation rotater = new RotateAnimation(0.0f, 360.0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        //rotater.setInterpolator(new AccelerateInterpolator());

        rotater.setDuration(3000);
        rotater.setRepeatCount(Animation.INFINITE);
        rotater.setFillAfter(true);

        civiRotate.setAnimation(rotater);

        Intent callerIntent = getIntent();
        siteKey = callerIntent.getStringExtra(CiviContract.SITE_KEY);
        apiKey = callerIntent.getStringExtra(CiviContract.API_KEY);
        websiteUrl = callerIntent.getStringExtra(CiviContract.WEBSITE_URL);

        ViewPager viewPager = (ViewPager) findViewById(R.id.fieldPager);
        viewPager.setOffscreenPageLimit(2);
        viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(viewPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.fieldTabLayout);
        tabLayout.setupWithViewPager(viewPager);


        Button fabFields = (Button) findViewById(R.id.fabFields);
        fabFields.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                waitScreen.setVisibility(View.VISIBLE);
                civiRotate.setAnimation(rotater);
                waitText.setText(getString(R.string.syncing_contacts));

                final Vector<String>[] labels = viewPagerAdapter.getLabels();
                waitTime = labels[0].size() + labels[1].size();
                splitTime = labels[0].size();


                //Logging
                for (Vector<String> oneLabels : labels) {
                    for (String value : oneLabels) {
                        Log.v("LABEL", "" + value);
                    }
                }

                //perform recursive synchronous sync in background thread
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        RestAdapter adapter = new RestAdapter.Builder().setLogLevel(RestAdapter.LogLevel.FULL).
                                setEndpoint(websiteUrl).build();

                        ICiviApi iCiviApi = adapter.create(ICiviApi.class);

                        CiviAndroid.createSyncAccount(FieldSelectionActivity.this);
                        PhoneNumberUtil phoneUtil = PhoneNumberUtil.getInstance();

                        Map<String, String> fields = new HashMap<>();
                        String contactType, contactSubtype;



                        Map<Integer, Pair<Integer,Integer>> toSyncPositions = new HashMap<Integer, Pair<Integer, Integer>>();

                        //get All phone numbers
                        Map<String, Pair<Integer,Integer>> phoneNumbers = new HashMap<String, Pair<Integer, Integer>>();

                        Cursor phones = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, new String[]{ContactsContract.CommonDataKinds.Phone.NUMBER, ContactsContract.CommonDataKinds.Phone.RAW_CONTACT_ID, ContactsContract.CommonDataKinds.Phone.CONTACT_ID}, null, null, null);
                        while (phones.moveToNext()) {
                            try {
                                String rawNumber = "" + (phones.getString(0));
                                String normalizedNumber = "";
                                Log.v("Contact Detail","ID: "+ phones.getString(1) + " NAME: "+phones.getString(2));
                                if (!rawNumber.isEmpty()) {
                                    if (rawNumber.charAt(0) != '+') {
                                        //manual normalize
                                        normalizedNumber = rawNumber.replaceAll("\\D", "");
                                        Log.v("Manual Normalize:", normalizedNumber);

                                    } else {
                                        normalizedNumber = String.valueOf(phoneUtil.parse(rawNumber, "").getNationalNumber());
                                        Log.v("Library normalized:", normalizedNumber);
                                    }
                                }
                                phoneNumbers.put(normalizedNumber, new Pair<Integer, Integer>(phones.getInt(1),phones.getInt(2)));
                            } catch (NumberParseException e) {
                                System.err.println("NumberParseException was thrown: " + e.toString());
                            }

                        }
                        phones.close();
                        int phoneCount = phoneNumbers.size();

                        //get All email addresses
                        Map<String, Pair<Integer,Integer>> emailAddresses = new HashMap<String, Pair<Integer, Integer>>();
                        Cursor eAds = getContentResolver().query(ContactsContract.CommonDataKinds.Email.CONTENT_URI, new String[]{ContactsContract.CommonDataKinds.Email.ADDRESS, ContactsContract.CommonDataKinds.Contactables.RAW_CONTACT_ID, ContactsContract.CommonDataKinds.Email.CONTACT_ID}, null, null, null);
                        while (eAds.moveToNext()) {
                            emailAddresses.put(eAds.getString(0), new Pair<Integer, Integer>(eAds.getInt(1),eAds.getInt(2)));
                            Log.v("Contact Detail", "ID: " + eAds.getString(1) + " NAME: " + eAds.getString(2));
                        }
                        eAds.close();
                        int emailCount = emailAddresses.size();
                        Contact contacts;


                        JsonObject jsonObject;

                        for (int i = 0; i < waitTime; i++) {

                            emailCount = emailAddresses.size();
                            phoneCount = phoneNumbers.size();

                            //resetting for fresh loop use
                            toSyncPositions.clear();
                            fields.clear();
                            contactType=null;
                            contactSubtype=null;

                            fields.put("key", siteKey);
                            fields.put("api_key", apiKey);
                            jsonObject = new JsonObject();
                            jsonObject.addProperty("sequential", "1");
                            if (i < splitTime) {
                                contactType = labels[0].get(i);
                                jsonObject.addProperty("contact_type",contactType);
                                Log.v("PARENT SYNC", contactType);
                            } else {
                                contactSubtype = labels[1].get(Math.abs(splitTime - i));
                                jsonObject.addProperty("contact_sub_type", contactSubtype);
                                Log.v("SUB TYPE SYNC", contactSubtype);

                            }
                            fields.put("json", jsonObject.toString());


                            contacts = iCiviApi.getContacts(fields);

                            if (contacts != null) {
                                if (contacts.getIsError() != 1) {

                                    int contactSize = contacts.getValues().size();

                                    if (contactSize > 0) {


                                        //running loop until emailCount and phoneCount exhaust or the responseList exhausts

                                        for (int c = 0; c < contactSize; c++) {
                                            Log.v("Loop index", c + "");
                                            Log.v("contactSize", "" + contactSize);
                                            if (emailCount == 0 && phoneCount == 0) {
                                                break;
                                            }

                                            if (phoneCount > 0) {
                                                try {
                                                    String civiRawNumber = "" + (contacts.getValues().get(c).getPhone());
                                                    String civiNormalizedNumber = "";
                                                    if (!civiRawNumber.isEmpty()) {
                                                        if (civiRawNumber.charAt(0) != '+') {
                                                            //manual normalize
                                                            civiNormalizedNumber = civiRawNumber.replaceAll("\\D", "");
                                                            Log.v("Civi Manual Normalize:", civiNormalizedNumber);

                                                        } else {
                                                            Log.v("Civi Raw Number", civiRawNumber);
                                                            civiNormalizedNumber = String.valueOf(phoneUtil.parse(civiRawNumber, "").getNationalNumber());
                                                            Log.v("Civi lib normalized:", civiNormalizedNumber);
                                                        }
                                                    }
                                                    if (phoneNumbers.containsKey(civiNormalizedNumber)) {
                                                        phoneCount--;
                                                        toSyncPositions.put(c, new Pair<Integer, Integer>(phoneNumbers.get(civiNormalizedNumber).first,phoneNumbers.get(civiNormalizedNumber).second));
                                                        Log.v("ToSync","key: "+c+" value: "+phoneNumbers.get(civiNormalizedNumber)+" Number: "+civiNormalizedNumber);
                                                        c++;
                                                        continue;
                                                    }
                                                } catch (NumberParseException e) {
                                                    e.printStackTrace();
                                                }
                                            }

                                            if (emailCount > 0) {
                                                String email = contacts.getValues().get(c).getEmail();
                                                if (emailAddresses.containsKey(email)) {
                                                    emailCount--;
                                                    toSyncPositions.put(c, new Pair<Integer, Integer>(emailAddresses.get(email).first,emailAddresses.get(email).second));
                                                    Log.v("ToSync", "key: " + c + " value: " + emailAddresses.get(email)+" Email: "+email);
                                                    c++;
                                                    continue;
                                                }
                                            }
                                        }


                                        for (Map.Entry<Integer, Pair<Integer, Integer>> entry : toSyncPositions.entrySet()) {
                                            ArrayList<ContentProviderOperation> ops = new ArrayList<ContentProviderOperation>();

                                            ops.add(ContentProviderOperation.newInsert(addCallerIsSyncAdapterParameter(ContactsContract.RawContacts.CONTENT_URI, true))
                                                    .withValue(ContactsContract.RawContacts.ACCOUNT_NAME, CiviContract.ACCOUNT)
                                                    .withValue(ContactsContract.RawContacts.ACCOUNT_TYPE, CiviContract.ACCOUNT_TYPE)
                                                    .withValue(ContactsContract.RawContacts.AGGREGATION_MODE, ContactsContract.RawContacts.AGGREGATION_MODE_DISABLED)
                                                    .build());

                                            ops.add(ContentProviderOperation.newInsert(addCallerIsSyncAdapterParameter(ContactsContract.Data.CONTENT_URI, true))
                                                    .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
                                                    .withValue(ContactsContract.Data.MIMETYPE, "vnd.android.cursor.item/you.in.spark.energy.cividroid.profile")
                                                    .withValue(ContactsContract.Data.DATA2, "Civicrm contact")
                                                    .withValue(ContactsContract.Data.DATA3, "Open contact in Cividroid")
                                                    .build());

                                            try {
                                                ContentProviderResult[] results = getContentResolver().applyBatch(ContactsContract.AUTHORITY, ops);
                                                long contactID = ContentUris.parseId(results[0].uri);
                                                Log.v("Inserted ID: ", "" + contactID);
                                                Log.v("Aggregation:"," Entry value "+entry.getValue());
                                                ContentValues cv = new ContentValues();
                                                cv.put(ContactsContract.AggregationExceptions.TYPE, ContactsContract.AggregationExceptions.TYPE_KEEP_TOGETHER);
                                                cv.put(ContactsContract.AggregationExceptions.RAW_CONTACT_ID1, contactID);
                                                cv.put(ContactsContract.AggregationExceptions.RAW_CONTACT_ID2, entry.getValue().first);

                                                //insert other values


                                                int aggregationResult = getContentResolver().update(ContactsContract.AggregationExceptions.CONTENT_URI, cv, null, null);
                                                Log.v("Aggregation Result", "" + aggregationResult);

                                                Cursor ag = getContentResolver().query(ContactsContract.Contacts.CONTENT_URI,null,null,null,null);
                                                DatabaseUtils.dumpCursor(ag);
                                                ag.close();

                                                ContentValues vals = new ContentValues();


                                                vals.put(CiviContract.CONTACT_ID_FIELD, String.valueOf(entry.getValue().second));

                                                ContentValues allValues = contacts.getValues().get(entry.getKey()).getAllValues(contactType,contactSubtype);
                                                vals.putAll(allValues);


                                                Uri uri = getContentResolver().insert(Uri.parse(CiviContract.CONTENT_URI + "/" + CiviContract.CONTACTS_FIELD_TABLE), vals);
                                                getContentResolver().notifyChange(uri,null);
                                            } catch (Exception e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    }

                                }
                            }
                        }
                        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(FieldSelectionActivity.this);
                        sp.edit().putString(CiviContract.API_KEY, apiKey).apply();
                        sp.edit().putString(CiviContract.SITE_KEY, siteKey).apply();
                        sp.edit().putString(CiviContract.WEBSITE_URL,websiteUrl).apply();
                        Intent intent = new Intent(FieldSelectionActivity.this, CiviAndroid.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_NO_HISTORY);
                        startActivity(intent);
                        finish();
                    }
                }).start();

            }
        });

    }

    private static Uri addCallerIsSyncAdapterParameter(Uri uri, boolean isSyncOperation) {
        if (isSyncOperation) {
            return uri.buildUpon()
                    .appendQueryParameter(ContactsContract.CALLER_IS_SYNCADAPTER, "true")
                    .build();
        }
        return uri;
    }

    private class ViewPagerAdapter extends FragmentPagerAdapter {

        private ContactsSubTypeSelectionFragment[] fragments;

        public ViewPagerAdapter(FragmentManager fm) {
            super(fm);
            fragments = new ContactsSubTypeSelectionFragment[3];
        }

        @Override
        public Fragment getItem(int position) {
            fragments[position] = new ContactsSubTypeSelectionFragment(position);
            return fragments[position];
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return getString(R.string.individual_contacts_tab);
                case 1:
                    return getString(R.string.household_contacts_tab);
                case 2:
                    return getString(R.string.organization_contacts_tab);
                default:
                    return "";
            }
        }

        @Override
        public int getCount() {
            return 3;
        }


        public Vector<String>[] getLabels() {
            Vector<String> contactLabels = new Vector<>();
            Vector<String> subtypeLabels = new Vector<>();
            for (int i = 0; i < 3; i++) {
                if (fragments[i].selectedChoice == 0) {
                    contactLabels.add(getPageTitle(i).toString());
                } else if (fragments[i].selectedChoice == 2) {
                    subtypeLabels.addAll(fragments[i].contactSubtypeAdapter.getCheckedLabels());
                }
            }

            return new Vector[]{contactLabels, subtypeLabels};
        }

    }


}
