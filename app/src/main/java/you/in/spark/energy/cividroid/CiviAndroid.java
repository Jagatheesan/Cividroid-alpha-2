package you.in.spark.energy.cividroid;


import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import you.in.spark.energy.cividroid.authentication.AuthenticatorActivity;
import you.in.spark.energy.cividroid.fragments.ActivitiesFragment;
import you.in.spark.energy.cividroid.fragments.CiviContacts;

/**
 * Created by dell on 13-06-2015.
 */
public class CiviAndroid extends AppCompatActivity {

    public static final String TAG = "CiviAndroid";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        checkAuthAndSetUp();

        setContentView(R.layout.cividroid_start_screen);
        ViewPager viewPager = (ViewPager) findViewById(R.id.civiPager);
        CardPagerAdapter cardPagerAdapter = new CardPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(cardPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.civiTablayout);
        tabLayout.setupWithViewPager(viewPager);

        Toolbar toolbar = (Toolbar) findViewById(R.id.civiToolbar);

        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayShowHomeEnabled(true);

        getSupportActionBar().setIcon(R.drawable.civilogo);


    }

    private void checkAuthAndSetUp() {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        String apiKey = sp.getString(CiviContract.API_KEY, null);
        String siteKey = sp.getString(CiviContract.SITE_KEY,null);
        String websiteUrl = sp.getString(CiviContract.WEBSITE_URL,null);
        if(apiKey==null && siteKey==null && websiteUrl==null) {
            //issues on Samsung devices
            //AccountManager.get(this).removeAccountExplicitly(new Account(CiviContract.ACCOUNT, CiviContract.ACCOUNT_TYPE));
            Intent intent = new Intent(this, AuthenticatorActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putExtra(CiviContract.CALL_FROM_ACTIVITY,true);
            startActivity(intent);
            finish();
        } else {
            syncNow(new Account(CiviContract.ACCOUNT,CiviContract.ACCOUNT_TYPE));
        }
    }

    private void syncNow(Account account) {
        Bundle settingsBundle = new Bundle();
        settingsBundle.putBoolean(
                ContentResolver.SYNC_EXTRAS_MANUAL, true);
        settingsBundle.putBoolean(
                ContentResolver.SYNC_EXTRAS_EXPEDITED, true);
        /*
         * Request the sync for the default account, authority, and
         * manual sync settings
         */
        ContentResolver.requestSync(account, CiviContract.AUTHORITY, settingsBundle);
    }

    private class CardPagerAdapter extends FragmentPagerAdapter {


        public CardPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return new CiviContacts();
                case 1:
                    return new ActivitiesFragment();
                default:
                    return null;
            }
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return getString(R.string.civi_contacts_tab);
                case 1:
                    return getString(R.string.civi_activities_tab);
                default:
                    return "";
            }
        }

        @Override
        public int getCount() {
            return 2;
        }
    }


    public static Account createSyncAccount(Context context) {
        // Create the account type and default account
        Account newAccount = new Account(
                CiviContract.ACCOUNT, CiviContract.ACCOUNT_TYPE);
        // Get an instance of the Android account manager
        AccountManager accountManager =
                (AccountManager) context.getSystemService(
                        ACCOUNT_SERVICE);

        if (accountManager.addAccountExplicitly(newAccount, null, null)) {
           //success
        } else {
            //error
        }

        return newAccount;
    }


}
