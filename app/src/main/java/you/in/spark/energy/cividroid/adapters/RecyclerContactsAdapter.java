package you.in.spark.energy.cividroid.adapters;

import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import you.in.spark.energy.cividroid.CiviContract;
import you.in.spark.energy.cividroid.ContactView;
import you.in.spark.energy.cividroid.R;

/**
 * Created by dell on 8/13/2015.
 */
public class RecyclerContactsAdapter extends RecyclerView.Adapter<RecyclerContactsAdapter.MyViewHolder> implements View.OnClickListener {

    Cursor cursor;
    Context context;

    public RecyclerContactsAdapter(Cursor cursor, Context context) {
        this.cursor = cursor;
        this.context = context;
    }

    @Override
    public RecyclerContactsAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.contact_item, parent, false);
        MyViewHolder vh = new MyViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(RecyclerContactsAdapter.MyViewHolder holder, int position) {
        cursor.moveToPosition(position);
        Uri contactUri = ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI, cursor.getLong(0));
        Uri displayPhotoUri = Uri.withAppendedPath(contactUri, ContactsContract.Contacts.Photo.DISPLAY_PHOTO);
        Glide.with(context).load(displayPhotoUri).asBitmap().centerCrop().fitCenter().into(holder.contactPhoto);
        holder.contactName.setText(cursor.getString(1));
        holder.v.setTag(position);
        holder.v.setOnClickListener(this);
    }

    @Override
    public int getItemCount() {
        if(cursor==null) {
            return 0;
        } else {
            return cursor.getCount();
        }
    }

    @Override
    public void onClick(View v) {
        cursor.moveToPosition((Integer) v.getTag());
        Uri contactUri = ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI, cursor.getLong(0));
        Uri displayPhotoUri = Uri.withAppendedPath(contactUri, ContactsContract.Contacts.Photo.DISPLAY_PHOTO);
        Intent intent  = new Intent(context,ContactView.class);
        intent.putExtra(CiviContract.CONTACT_ID_FIELD, cursor.getString(0));
        intent.putExtra(CiviContract.PHOTO_URI,displayPhotoUri.toString());
        context.startActivity(intent);
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        public ImageView contactPhoto;
        public TextView contactName;
        public View v;

        public MyViewHolder(View v) {
            super(v);
            contactPhoto = (ImageView) v.findViewById(R.id.contactPhoto);
            contactName = (TextView) v.findViewById(R.id.contactName);
            this.v = v;
        }
    }

    public void swapCursor(Cursor cursor) {
        this.cursor = cursor;
    }
}
