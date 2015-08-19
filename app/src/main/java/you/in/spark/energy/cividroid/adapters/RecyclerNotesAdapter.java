package you.in.spark.energy.cividroid.adapters;

import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import you.in.spark.energy.cividroid.R;

/**
 * Created by dell on 8/13/2015.
 */
public class RecyclerNotesAdapter extends RecyclerView.Adapter<RecyclerNotesAdapter.MyViewHolder> {

    Cursor cursor;

    public RecyclerNotesAdapter(Cursor cursor) {
        this.cursor = cursor;
    }

    @Override
    public RecyclerNotesAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.note_layout, parent, false);
        MyViewHolder vh = new MyViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(RecyclerNotesAdapter.MyViewHolder holder, int position) {
        cursor.moveToPosition(position);

        Date callDate = new Date(cursor.getLong(0));
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");

        holder.noteDate.setText("Conversation date: " +dateFormat.format(callDate));

        Long lDuration = cursor.getLong(1);
        holder.noteDuration.setText("Duration: "+String.format("%d hr, %d min, %d sec",
                TimeUnit.SECONDS.toHours(lDuration),
               TimeUnit.SECONDS.toMinutes(lDuration - TimeUnit.HOURS.toSeconds(TimeUnit.SECONDS.toHours(lDuration))),
                lDuration - TimeUnit.MINUTES.toSeconds(TimeUnit.SECONDS.toMinutes(TimeUnit.HOURS.toSeconds(TimeUnit.SECONDS.toHours(lDuration))))
        ));
        holder.noteText.setText(cursor.getString(2));
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

        public TextView noteDate;
        public TextView noteDuration;
        public TextView noteText;

        public MyViewHolder(View v) {
            super(v);
            noteDate = (TextView) v.findViewById(R.id.noteDate);
            noteDuration = (TextView) v.findViewById(R.id.noteDuration);
            noteText = (TextView) v.findViewById(R.id.noteText);

        }
    }

    public void swapCursor(Cursor cursor) {
        this.cursor = cursor;
    }
}