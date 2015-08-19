package you.in.spark.energy.cividroid.fragments;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.util.Pair;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.gson.JsonObject;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;
import you.in.spark.energy.cividroid.FieldSelectionActivity;
import you.in.spark.energy.cividroid.R;
import you.in.spark.energy.cividroid.adapters.ContactSubtypeAdapter;
import you.in.spark.energy.cividroid.api.ICiviApi;
import you.in.spark.energy.cividroid.entities.ContactType;

/**
 * Created by dell on 8/15/2015.
 */
public class ContactsSubTypeSelectionFragment extends Fragment implements RadioGroup.OnCheckedChangeListener {

    private int position;
    public String contactTypeName;
    public int selectedChoice;
    private int count;
    public ContactSubtypeAdapter contactSubtypeAdapter;
    private RadioButton rbAllContacts, rbNoContacts, rbOnlySubtypes;


    public ContactsSubTypeSelectionFragment() {
    }

    public ContactsSubTypeSelectionFragment(int position) {
        this.position = position;
        count = 0;
        selectedChoice = 0;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.contacts_subtype_selection_layout, container, false);
        RadioGroup rgContactTypes = (RadioGroup) v.findViewById(R.id.rgContactTypes);
        rgContactTypes.setOnCheckedChangeListener(this);

        rbAllContacts = (RadioButton) v.findViewById(R.id.rbAllContacts);
        rbNoContacts = (RadioButton) v.findViewById(R.id.rbNoContacts);
        rbOnlySubtypes = (RadioButton) v.findViewById(R.id.rbOnlySubtypes);

        switch (position) {
            case 0:
                rbAllContacts.setText(getString(R.string.all_individuals));
                rbNoContacts.setText(getString(R.string.no_individuals));
                break;
            case 1:
                rbAllContacts.setText(getString(R.string.all_households));
                rbNoContacts.setText(getString(R.string.no_households));

                break;
            case 2:
                rbAllContacts.setText(getString(R.string.all_organizations));
                rbNoContacts.setText(getString(R.string.no_organizations));
                break;
        }


        RestAdapter adapter = new RestAdapter.Builder().setLogLevel(RestAdapter.LogLevel.FULL).
                setEndpoint(FieldSelectionActivity.websiteUrl).build();

        ICiviApi iCiviApi = adapter.create(ICiviApi.class);

        Map<String, String> fields = new HashMap<>();
        fields.put("key",FieldSelectionActivity.siteKey);
        fields.put("api_key",FieldSelectionActivity.apiKey);
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("sequential", "1");
        jsonObject.addProperty("parent_id",position+1);
        fields.put("json",jsonObject.toString());

        final Map<Integer, Pair<String, String>> subtypeNames = new HashMap<>();

        RecyclerView rvContactSubtypes = (RecyclerView) v.findViewById(R.id.rvContactSubTypes);
        rvContactSubtypes.setLayoutManager(new LinearLayoutManager(getActivity()));
        contactSubtypeAdapter = new ContactSubtypeAdapter(subtypeNames);
        rvContactSubtypes.setAdapter(contactSubtypeAdapter);


        do {
            fields.put("options[offset]",""+count);
            iCiviApi.getContactSubtypes(fields, new Callback<ContactType>() {
                @Override
                public void success(ContactType contactType, Response response) {
                    if (contactType.getIsError() != 1) {
                        count = contactType.getCount();
                        if (count > 0) {
                            for(int i = 0; i < count; i++) {
                                subtypeNames.put(i,new Pair<>(contactType.getValues().get(i).getLabel(),contactType.getValues().get(i).getName()));
                            }
                            if(count<25 && !subtypeNames.isEmpty()) {
                                contactSubtypeAdapter.setRealData(subtypeNames);
                                rbOnlySubtypes.setEnabled(true);

                                FieldSelectionActivity.fragmentStatus++;
                                if(FieldSelectionActivity.fragmentStatus==3 && FieldSelectionActivity.civiRotate!=null){
                                    FieldSelectionActivity.civiRotate.setAnimation(null);
                                    FieldSelectionActivity.waitScreen.setVisibility(View.GONE);
                                }

                            }
                        } else {

                            FieldSelectionActivity.fragmentStatus++;
                            if(FieldSelectionActivity.fragmentStatus==3 && FieldSelectionActivity.civiRotate!=null){
                                FieldSelectionActivity.civiRotate.setAnimation(null);
                                FieldSelectionActivity.waitScreen.setVisibility(View.GONE);
                            }

                        }
                    } else {
                        //FieldSelectionActivity.progressDialog.dismiss();
                        Toast.makeText(getActivity(),"Authentication failed! Patience is the key, get the details right! :)",Toast.LENGTH_LONG).show();
                        getActivity().onBackPressed();
                    }
                }

                @Override
                public void failure(RetrofitError error) {
                    Toast.makeText(getActivity(),"Authentication failed! Patience is the key, get the details right! :)",Toast.LENGTH_LONG).show();
                    getActivity().onBackPressed();
                }
            });
        }while(count==25);


        return v;
    }


    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId) {
            case R.id.rbAllContacts:
                selectedChoice = 0;
                if(rbOnlySubtypes.isEnabled() && contactSubtypeAdapter.isEnabled())
                {
                    contactSubtypeAdapter.setEnabled(false);
                }
                break;
            case R.id.rbNoContacts:
                selectedChoice = 1;
                if(rbOnlySubtypes.isEnabled() && contactSubtypeAdapter.isEnabled())
                {
                    contactSubtypeAdapter.setEnabled(false);
                }
                break;
            case R.id.rbOnlySubtypes:
                selectedChoice = 2;
                contactSubtypeAdapter.setEnabled(true);
                break;
        }
    }
}
