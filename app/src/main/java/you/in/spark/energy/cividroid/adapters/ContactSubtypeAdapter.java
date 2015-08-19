package you.in.spark.energy.cividroid.adapters;

import android.support.v4.util.Pair;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import java.util.Map;
import java.util.Vector;

import you.in.spark.energy.cividroid.R;

/**
 * Created by dell on 8/16/2015.
 */
public class ContactSubtypeAdapter extends RecyclerView.Adapter<ContactSubtypeAdapter.MyViewHolder> implements CompoundButton.OnCheckedChangeListener {

    private Map<Integer, Pair<String,String>> subtypeNames;
    private Vector<Integer> checked;
    private boolean enabled;

    public ContactSubtypeAdapter(Map<Integer, Pair<String, String>> subtypeNames) {
        this.subtypeNames = subtypeNames;
        checked = new Vector<>();
        enabled = false;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.subtype_item, parent, false);
        MyViewHolder vh = new MyViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.cbSubtype.setOnCheckedChangeListener(null);
        if(checked.contains(position)) {
            holder.cbSubtype.setChecked(true);
        } else {
            holder.cbSubtype.setChecked(false);
        }
        holder.cbSubtype.setTag(position);
        holder.cbSubtype.setOnCheckedChangeListener(this);
        holder.tvSubtypeName.setText(subtypeNames.get(position).second);
        holder.cbSubtype.setEnabled(enabled);
    }

    @Override
    public int getItemCount() {
        return subtypeNames.size();
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if(isChecked) {
            checked.add((Integer) buttonView.getTag());
        } else {
            checked.remove(buttonView.getTag());
        }
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
        notifyDataSetChanged();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        CheckBox cbSubtype;
        TextView tvSubtypeName;

        public MyViewHolder(View v) {
            super(v);
            cbSubtype = (CheckBox) v.findViewById(R.id.cbSubtype);
            tvSubtypeName = (TextView) v.findViewById(R.id.tvSubtypeName);
        }
    }

    public Vector<Integer> getChecked() {
        return checked;
    }

    public Vector<String> getCheckedLabels() {
        Vector<String> names = new Vector<>();
        for(int sel : checked) {
            names.add(subtypeNames.get(sel).first);
        }
        return names;
    }

    public void setRealData(Map<Integer, Pair<String, String>> subtypeNames) {
        this.subtypeNames = subtypeNames;
        notifyDataSetChanged();
    }
}
