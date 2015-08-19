package you.in.spark.energy.cividroid.fragments;

import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import you.in.spark.energy.cividroid.CiviContract;
import you.in.spark.energy.cividroid.ContactView;
import you.in.spark.energy.cividroid.R;
import you.in.spark.energy.cividroid.adapters.RecyclerNotesAdapter;

/**
 * Created by dell on 8/11/2015.
 */
public class NotesFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>{


    private RecyclerNotesAdapter recyclerNotesAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.recycler_layout_fragment,container,false);
        RecyclerView content = (RecyclerView) v.findViewById(R.id.rvContent);
        content.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerNotesAdapter = new RecyclerNotesAdapter(null);
        content.setAdapter(recyclerNotesAdapter);
        getActivity().getSupportLoaderManager().initLoader(3,null,this);
        return v;
    }


    @Override
    public android.support.v4.content.Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(getActivity(), Uri.parse(CiviContract.CONTENT_URI + "/" + CiviContract.ACTIVITY_TABLE),new String[]{CiviContract.ACTIVITY_TABLE_COLUMNS[6],CiviContract.ACTIVITY_TABLE_COLUMNS[3],CiviContract.ACTIVITY_TABLE_COLUMNS[4]},CiviContract.ACTIVITY_TABLE_COLUMNS[0]+" IS NOT NULL",null,CiviContract.ACTIVITY_TABLE_COLUMNS[3]+" DESC");
    }

    @Override
    public void onLoadFinished(android.support.v4.content.Loader<Cursor> loader, Cursor data) {
        recyclerNotesAdapter.swapCursor(data);
        recyclerNotesAdapter.notifyDataSetChanged();
    }

    @Override
    public void onLoaderReset(android.support.v4.content.Loader<Cursor> loader) {
        recyclerNotesAdapter.swapCursor(null);
        recyclerNotesAdapter.notifyDataSetChanged();
    }
}
