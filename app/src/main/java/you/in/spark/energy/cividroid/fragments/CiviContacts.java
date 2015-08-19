package you.in.spark.energy.cividroid.fragments;

import android.database.Cursor;
import android.database.DatabaseUtils;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import you.in.spark.energy.cividroid.CiviContract;
import you.in.spark.energy.cividroid.R;
import you.in.spark.energy.cividroid.adapters.RecyclerContactsAdapter;

/**
 * Created by dell on 8/11/2015.
 */
public class CiviContacts extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    private RecyclerContactsAdapter recyclerContactsAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.recycler_layout_fragment,container,false);
        RecyclerView content = (RecyclerView) v.findViewById(R.id.rvContent);
        content.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerContactsAdapter = new RecyclerContactsAdapter(null, getActivity());
        content.setAdapter(recyclerContactsAdapter);
        getActivity().getSupportLoaderManager().initLoader(1,null,this);
        return v;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(getActivity(),Uri.parse(CiviContract.CONTENT_URI + "/" + CiviContract.CONTACTS_FIELD_TABLE),new String[]{CiviContract.CONTACT_ID_FIELD,CiviContract.CONTACT_TABLE_COLUMNS[4]},null,null,null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        DatabaseUtils.dumpCursor(data);
        recyclerContactsAdapter.swapCursor(data);
        recyclerContactsAdapter.notifyDataSetChanged();
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        recyclerContactsAdapter.swapCursor(null);
        recyclerContactsAdapter.notifyDataSetChanged();
    }
}
