package you.in.spark.energy.cividroid.fragments;

import android.database.Cursor;
import android.database.DatabaseUtils;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.util.Pair;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import you.in.spark.energy.cividroid.CiviContract;
import you.in.spark.energy.cividroid.ContactView;
import you.in.spark.energy.cividroid.R;
import you.in.spark.energy.cividroid.adapters.RecyclerAboutContactAdapter;

/**
 * Created by dell on 8/11/2015.
 */
public class AboutFragment extends Fragment {


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.recycler_layout_fragment,container,false);


        Cursor detailCursor = getActivity().getContentResolver().query(Uri.parse(CiviContract.CONTENT_URI + "/" + CiviContract.CONTACTS_FIELD_TABLE), CiviContract.CONTACT_TABLE_COLUMNS, CiviContract.CONTACT_ID_FIELD + "=?", new String[]{ContactView.contactID}, null);
        if(detailCursor.moveToFirst()) {
Log.v("DUMP", "DUMP");
            DatabaseUtils.dumpCursor(detailCursor);
            Log.v("DUMP", "DUMP");

            ContactView.collapsingToolbarLayout.setTitle(detailCursor.getString(4));


            String detail = null;
            List<Pair<String,String>> detailPair = new ArrayList<>();

            //process result, remove blanks
            int len = CiviContract.CONTACT_FIELD_NAMES.length;
            for(int i = 2;i<len;i++) {
                    detail = detailCursor.getString(i);
                if(detail!=null && !detail.isEmpty()) {
                    detailPair.add(new Pair<>(CiviContract.CONTACT_FIELD_NAMES[i],detail));
                }
            }


            if(detailPair.size()>0) {
                Log.v("SIZE",detailPair.size()+"");
                Log.v("CONTENT",detailPair.toString());
                RecyclerAboutContactAdapter recyclerAboutContactAdapter = new RecyclerAboutContactAdapter(detailPair);
                RecyclerView content = (RecyclerView) v.findViewById(R.id.rvContent);
                content.setLayoutManager(new LinearLayoutManager(getActivity()));
                content.setAdapter(recyclerAboutContactAdapter);
            }
        }
        detailCursor.close();
        return v;
    }
}
