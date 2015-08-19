package you.in.spark.energy.cividroid.adapters;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import you.in.spark.energy.cividroid.R;

/**
 * Created by dell on 8/17/2015.
 */
public class ActivitiesRecyclerAdapter extends RecyclerView.Adapter<ActivitiesRecyclerAdapter.MyViewHolder> {

    Cursor cursor;

    public ActivitiesRecyclerAdapter(Cursor cursor) {
        this.cursor = cursor;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_detail_layout, parent, false);
        MyViewHolder vh = new MyViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        cursor.moveToPosition(position);
        String subject, details, location, duration, reminder;
        subject = cursor.getString(0);
        details = cursor.getString(1);
        reminder = cursor.getString(2);
        location = cursor.getString(3);
        duration = cursor.getString(4);
        if(subject!=null){
            holder.tvSubject.setText(subject);
        } else {
            holder.tvSubject.setText("");
        }
        if(details!=null) {
            holder.tvDetails.setText(Html.fromHtml(details));
        } else {
            holder.tvDetails.setText("");
        }
        if(reminder!=null) {
            holder.tvReminder.setText(reminder);
        } else {
            holder.tvReminder.setText("");
        }
        if(duration!=null) {
            holder.tvReminder.setText(duration);
        } else {
            holder.tvReminder.setText("");
        }
        if(location!=null) {
            holder.tvLocation.setText(location);
        } else {
            holder.tvLocation.setText("");
        }
    }

    @Override
    public int getItemCount() {
        if(cursor==null) {
            return 0;
        } else {
            return cursor.getCount();
        }
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView tvSubject, tvDetails, tvLocation, tvDuration, tvReminder;

        public MyViewHolder(View v) {
            super(v);
            tvSubject = (TextView) v.findViewById(R.id.tvActivitySubject);
            tvDetails = (TextView) v.findViewById(R.id.tvActivityDetails);
            tvLocation = (TextView) v.findViewById(R.id.tvActivityLocation);
            tvDuration = (TextView) v.findViewById(R.id.tvDuration);
            tvReminder = (TextView) v.findViewById(R.id.tvActivityDateAndTime);
        }
    }

    public void swapCursor(Cursor cursor) {
        this.cursor = cursor;
    }


}
