package you.in.spark.energy.cividroid.authentication;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import you.in.spark.energy.cividroid.CiviContract;
import you.in.spark.energy.cividroid.FieldSelectionActivity;
import you.in.spark.energy.cividroid.R;

/**
 * Created by dell on 14-06-2015.
 */
public class AuthenticatorActivity extends AppCompatActivity{

    private Boolean callFromActivity = false;
    private static final String TAG = "AuthenticatorActivity";
    private EditText etSiteKey, etApiKey, etWebsiteUrl;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        callFromActivity = getIntent().getBooleanExtra(CiviContract.CALL_FROM_ACTIVITY, false);
        Log.v(TAG, callFromActivity.toString());

        setContentView(R.layout.authenticator_activity);

        etSiteKey = (EditText) findViewById(R.id.etSiteKey);
        etApiKey = (EditText) findViewById(R.id.etApiKey);
        etWebsiteUrl = (EditText) findViewById(R.id.etWebsiteUrl);

        if(savedInstanceState!=null) {
            etSiteKey.setText(savedInstanceState.getString(CiviContract.SITE_KEY));
            etApiKey.setText(savedInstanceState.getString(CiviContract.API_KEY));
            etWebsiteUrl.setText(savedInstanceState.getString(CiviContract.WEBSITE_URL));

        }

        FloatingActionButton fabSaveKeys = (FloatingActionButton) findViewById(R.id.fabSaveKeys);
        if (Build.VERSION.SDK_INT >= 21) {
            fabSaveKeys.setImageDrawable(getResources().getDrawable(R.drawable.ic_save_white_24dp, null));
        } else {
            fabSaveKeys.setImageDrawable(getResources().getDrawable(R.drawable.ic_save_white_24dp));
        }

        fabSaveKeys.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.v(TAG, "fabSaveKeys OnClick");
                if(!TextUtils.isEmpty(etApiKey.getText()) && !TextUtils.isEmpty(etSiteKey.getText())) {
                    Log.v(TAG, "not empty");
                    Intent intent = new Intent(AuthenticatorActivity.this, FieldSelectionActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.putExtra(CiviContract.SITE_KEY, etSiteKey.getText().toString());
                    intent.putExtra(CiviContract.API_KEY, etApiKey.getText().toString());
                    intent.putExtra(CiviContract.WEBSITE_URL,etWebsiteUrl.getText().toString());
                    startActivity(intent);
                    finish();
                }
            }
        });
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putString(CiviContract.SITE_KEY,etSiteKey.getText().toString());
        outState.putString(CiviContract.API_KEY,etApiKey.getText().toString());
        outState.putString(CiviContract.WEBSITE_URL,etWebsiteUrl.getText().toString());
        super.onSaveInstanceState(outState);
    }
}
