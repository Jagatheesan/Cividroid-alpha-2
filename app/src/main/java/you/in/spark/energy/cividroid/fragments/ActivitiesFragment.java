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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import you.in.spark.energy.cividroid.CiviContract;
import you.in.spark.energy.cividroid.R;
import you.in.spark.energy.cividroid.adapters.ActivitiesRecyclerAdapter;

/**
 * Created by dell on 8/11/2015.
 */
public class ActivitiesFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>{

    private ActivitiesRecyclerAdapter activitiesRecyclerAdapter;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.recycler_layout_fragment,container,false);
        RecyclerView content = (RecyclerView) v.findViewById(R.id.rvContent);
        content.setLayoutManager(new LinearLayoutManager(getActivity()));
        activitiesRecyclerAdapter = new ActivitiesRecyclerAdapter(null);
        content.setAdapter(activitiesRecyclerAdapter);
        getActivity().getSupportLoaderManager().initLoader(2,null,this);
        return v;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(getActivity(), Uri.parse(CiviContract.CONTENT_URI + "/" + CiviContract.ACTIVITY_TABLE),new String[]{CiviContract.ACTIVITY_TABLE_COLUMNS[3],CiviContract.ACTIVITY_TABLE_COLUMNS[7],CiviContract.ACTIVITY_TABLE_COLUMNS[4],CiviContract.ACTIVITY_TABLE_COLUMNS[6],CiviContract.ACTIVITY_TABLE_COLUMNS[5]},null,null,CiviContract.ACTIVITY_TABLE_COLUMNS[4]+" DESC");
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        Log.v("onLoadFinished","finished");
        DatabaseUtils.dumpCursor(data);
        activitiesRecyclerAdapter.swapCursor(data);
        activitiesRecyclerAdapter.notifyDataSetChanged();
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        activitiesRecyclerAdapter.swapCursor(null);
        activitiesRecyclerAdapter.notifyDataSetChanged();
    }
}
