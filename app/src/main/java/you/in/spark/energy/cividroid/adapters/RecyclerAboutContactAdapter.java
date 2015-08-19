package you.in.spark.energy.cividroid.adapters;

import android.support.v4.util.Pair;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import you.in.spark.energy.cividroid.R;

/**
 * Created by dell on 8/9/2015.
 */
public class RecyclerAboutContactAdapter extends RecyclerView.Adapter<RecyclerAboutContactAdapter.MyViewHolder> {

    private List<Pair<String, String>> detailPair;

    public RecyclerAboutContactAdapter(List<Pair<String, String>> detailPair) {
        this.detailPair = detailPair;
    }

    @Override
    public RecyclerAboutContactAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.contact_detail_item, parent, false);
        MyViewHolder vh = new MyViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(RecyclerAboutContactAdapter.MyViewHolder holder, int position) {
        holder.detailType.setText(detailPair.get(position).first);
        holder.detail.setText(detailPair.get(position).second);
    }

    @Override
    public int getItemCount() {
        return detailPair.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView detailType;
        public TextView detail;

        public MyViewHolder(View v) {
            super(v);
            detailType = (TextView) v.findViewById(R.id.detailType);
            detail = (TextView) v.findViewById(R.id.detail);
        }
    }
}
