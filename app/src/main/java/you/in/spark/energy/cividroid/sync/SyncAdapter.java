package you.in.spark.energy.cividroid.sync;

import android.accounts.Account;
import android.annotation.TargetApi;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.ContentProviderOperation;
import android.content.ContentProviderResult;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SyncResult;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.preference.PreferenceManager;
import android.provider.ContactsContract;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;
import you.in.spark.energy.cividroid.CiviContract;
import you.in.spark.energy.cividroid.R;
import you.in.spark.energy.cividroid.api.ICiviApi;
import you.in.spark.energy.cividroid.authentication.AuthenticatorActivity;
import you.in.spark.energy.cividroid.entities.CiviActivity;
import you.in.spark.energy.cividroid.entities.Contact;

/**
 * Created by dell on 18-06-2015.
 */
public class SyncAdapter extends AbstractThreadedSyncAdapter {

    private ContentResolver contentResolver;
    private static final String TAG = "SyncAdapter";


    public SyncAdapter(Context context, boolean autoInitialize) {
        super(context, autoInitialize);
        contentResolver = context.getContentResolver();
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public SyncAdapter(Context context, boolean autoInitialize, boolean allowParallelSyncs) {
        super(context, autoInitialize, allowParallelSyncs);
        contentResolver = context.getContentResolver();
    }


    @Override
    public void onPerformSync(Account account, final Bundle extras, String authority, ContentProviderClient provider, SyncResult syncResult) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getContext());
        String apiKey, siteKey, websiteUrl, lastScheduledID;
        apiKey = sp.getString(CiviContract.API_KEY, null);
        siteKey = sp.getString(CiviContract.SITE_KEY, null);
        websiteUrl = sp.getString(CiviContract.WEBSITE_URL, null);
        lastScheduledID = sp.getString(CiviContract.LAST_ACTIVITY_SYNC_ID,"-1");

        RestAdapter adapter = new RestAdapter.Builder().setLogLevel(RestAdapter.LogLevel.FULL).
                setEndpoint(websiteUrl).build();

        ICiviApi iCiviApi = adapter.create(ICiviApi.class);

        Map<String, String> fields = new HashMap<>();
        fields.put("key", siteKey);
        fields.put("api_key", apiKey);
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("sequential", "1");
        JsonObject activityIDObject = new JsonObject();
        activityIDObject.addProperty(">", lastScheduledID);
        jsonObject.add("id", activityIDObject);
        fields.put("json", jsonObject.toString());

        //Sync Activities
        CiviActivity activity=null;
        try {
            activity = iCiviApi.getActivity(fields);
        }catch (RetrofitError re) {

        }

        if (activity != null) {
            if(activity.getIsError()!=1) {
                if(activity.getValues().size()>0) {
                    ContentValues values = null;

                    for (CiviActivity.Value value : activity.getValues()) {
                        values = value.getAllValues();
                    }
                    sp.edit().putString(CiviContract.LAST_ACTIVITY_SYNC_ID, activity.getValues().get(activity.getValues().size() - 1).getId()).apply();
                    Uri uri = contentResolver.insert(Uri.parse(CiviContract.CONTENT_URI + "/" + CiviContract.ACTIVITY_TABLE), values);
                    contentResolver.notifyChange(uri, null);
                }
            } else {
                invalidate();
            }
        }

        //Sync Notes on Calls
        fields.clear();
        fields.put("key", siteKey);
        fields.put("api_key", apiKey);

        Vector<String> ids = new Vector<>();

        Cursor phoneIds = contentResolver.query(Uri.parse(CiviContract.CONTENT_URI+"/"+CiviContract.CONTACTS_FIELD_TABLE),new String[]{CiviContract.CONTACT_TABLE_COLUMNS[0]},null,null,null);
        while(phoneIds.moveToNext()) {
            ids.add(phoneIds.getString(0));
        }
        phoneIds.close();
        if(ids.size()>0) {

            String jsonValue = "{\"sequential\":1,\"phone_id\":{\"IN\":[" + TextUtils.join(",", ids) + "]}}";
            Log.v("JSON", jsonValue);

            fields.put("json", jsonValue);


            try {
                activity = iCiviApi.getActivity(fields);
            } catch (RetrofitError re) {

            }

            if (activity != null) {
                if (activity.getIsError() != 1) {
                    if (activity.getValues().size() > 0) {
                        ContentValues values[] = null;
                        int size = activity.getValues().size();
                        for (int i = 0; i < size; i++) {
                            values[i] = activity.getValues().get(i).getAllValues();
                        }
                        sp.edit().putString(CiviContract.LAST_ACTIVITY_SYNC_ID, activity.getValues().get(activity.getValues().size() - 1).getId()).apply();
                        contentResolver.bulkInsert(Uri.parse(CiviContract.CONTENT_URI + "/" + CiviContract.ACTIVITY_TABLE), values);
                        contentResolver.notifyChange(Uri.parse(CiviContract.CONTENT_URI + "/" + CiviContract.ACTIVITY_TABLE), null);
                    }
                } else {
                    invalidate();
                }
            }
        }




    }

    private void invalidate() {

        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(getContext())
                        .setSmallIcon(R.drawable.cividroid_logo)
                        .setContentTitle(getContext().getString(R.string.connection_error))
                        .setContentText(getContext().getString(R.string.connection_error_desc));

        Intent resultIntent = new Intent(getContext(), AuthenticatorActivity.class);

        // The stack builder object will contain an artificial back stack for the
        // started Activity.
        // This ensures that navigating backward from the Activity leads out of
        // your application to the Home screen.
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(getContext());
        // Adds the back stack for the Intent (but not the Intent itself)
        stackBuilder.addParentStack(AuthenticatorActivity.class);
        // Adds the Intent that starts the Activity to the top of the stack
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent =
                stackBuilder.getPendingIntent(
                        0,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );
        mBuilder.setContentIntent(resultPendingIntent);
        NotificationManager mNotificationManager =
                (NotificationManager) getContext().getSystemService(Context.NOTIFICATION_SERVICE);
        // mId allows you to update the notification later on.
        mNotificationManager.notify(0, mBuilder.build());

    }

}
