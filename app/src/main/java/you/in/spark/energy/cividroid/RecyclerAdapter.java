package you.in.spark.energy.cividroid;

import android.content.ContentValues;
import android.content.Intent;
import android.support.v4.util.Pair;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.Vector;

/**
 * Created by dell on 21-06-2015.
 */
public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.MyViewHolder> {

    private static final String TAG = "RecyclerAdapter";

    private List<Pair<String, String>> titleNamePairValues;
    private Vector<Integer> selected;
    private Vector<Integer> defaultFields;
    private int activityType;

    public RecyclerAdapter(List<Pair<String, String>> titleNamePairValues, int activityType) {
        Log.v(TAG, "constructor");
        selected = new Vector<>();
        defaultFields = new Vector<>();
        this.titleNamePairValues = titleNamePairValues;
        this.activityType = activityType;

        if(activityType==0) {
            //find positions of default fields
            int size = titleNamePairValues.size();
            for (int i = 0; i < size; i++) {
                if (titleNamePairValues.get(i).second.equalsIgnoreCase("display_name") || titleNamePairValues.get(i).second.equalsIgnoreCase("email") || titleNamePairValues.get(i).second.equalsIgnoreCase("phone")) {
                    selected.add(i);
                    defaultFields.add(i);
                }
                if (defaultFields.size()==3) {
                    break;
                }
            }
        }

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.field_item, viewGroup, false);
        MyViewHolder vh = new MyViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(MyViewHolder viewHolder, int i) {
        viewHolder.title.setText(titleNamePairValues.get(i).first);
        viewHolder.checkBox.setOnCheckedChangeListener(null);
        if (selected.contains(i)) {
            viewHolder.checkBox.setChecked(true);
        } else {
            viewHolder.checkBox.setChecked(false);
        }

        if(activityType==0) {
            if (defaultFields.contains(i)) {
                viewHolder.checkBox.setEnabled(false);
                return;
            }
        }

        viewHolder.checkBox.setEnabled(true);


        viewHolder.checkBox.setTag(i);
        viewHolder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    selected.add(Integer.valueOf(buttonView.getTag().toString()));
                } else {
                    selected.remove(Integer.valueOf(buttonView.getTag().toString()));
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return titleNamePairValues.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView title;
        public CheckBox checkBox;

        public MyViewHolder(View v) {
            super(v);
            title = (TextView) v.findViewById(R.id.tvFieldTitle);
            checkBox = (CheckBox) v.findViewById(R.id.cbFieldCheckBox);
            Log.v(TAG, "constructor loaded");
        }
    }


    public String[] getTitleNameFields() {
        String title = "", name = "";
        int selectedSize = selected.size();
        if(selectedSize>0) {
            title = titleNamePairValues.get(selected.get(0)).first;
            name = titleNamePairValues.get(selected.get(0)).second;
            for (int i = 1; i < selectedSize; i++) {
                title = title + "," + titleNamePairValues.get(selected.get(i)).first;
                name = name + "," + titleNamePairValues.get(selected.get(i)).second;
            }
        }
        return new String[]{title, name};
    }


}
