package you.in.spark.energy.cividroid;

import android.content.ContentUris;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import you.in.spark.energy.cividroid.fragments.AboutFragment;
import you.in.spark.energy.cividroid.fragments.NotesFragment;

/**
 * Created by dell on 30-06-2015.
 */
public class ContactView extends AppCompatActivity {

    public static String contactID;
    private Uri contactUri;
    public static CollapsingToolbarLayout collapsingToolbarLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.contact_view);

        collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsingToolbarLayout);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);


        setSupportActionBar(toolbar);

        ImageView ivContactPhoto = (ImageView) findViewById(R.id.ivContactPhoto);


        Intent intent = getIntent();

        if(intent.getData()!=null) {
            Cursor detailExtractor = getContentResolver().query(intent.getData(), new String[]{ContactsContract.Contacts.PHOTO_URI, ContactsContract.CommonDataKinds.Contactables.CONTACT_ID}, null, null, null);
            if (detailExtractor.moveToFirst()) {
                Glide.with(this).load(Uri.parse(""+detailExtractor.getString(0))).asBitmap().centerCrop().into(ivContactPhoto);
                contactID = detailExtractor.getString(1);
            }
            detailExtractor.close();
        } else {
            contactID = intent.getStringExtra(CiviContract.CONTACT_ID_FIELD);
            Glide.with(this).load(Uri.parse(intent.getStringExtra(CiviContract.PHOTO_URI))).asBitmap().centerCrop().into(ivContactPhoto);
        }

        contactUri = ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI, Long.valueOf(contactID));




        ViewPager viewPager = (ViewPager) findViewById(R.id.viewPager);
        CardPagerAdapter cardPagerAdapter = new CardPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(cardPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabLayout);
        tabLayout.setupWithViewPager(viewPager);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_contact_view,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.action_view_in_address_book:
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(contactUri);
                startActivity(intent);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


    private class CardPagerAdapter extends FragmentPagerAdapter {


        public CardPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return new AboutFragment();
                case 1:
                    return new NotesFragment();
                default:
                    return null;
            }
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return getString(R.string.about_tab);
                case 1:
                    return getString(R.string.notes_tab);
                default:
                    return "";
            }
        }

        @Override
        public int getCount() {
            return 2;
        }
    }

}
