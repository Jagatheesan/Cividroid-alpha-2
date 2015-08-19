package you.in.spark.energy.cividroid;

import android.app.Service;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.net.Uri;
import android.os.IBinder;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * Created by dell on 8/11/2015.
 */
public class WriteNotesService extends Service implements View.OnClickListener{

    private WindowManager wm=null;
    private RelativeLayout parent;
    private EditText notes;
    private String date, contactID, duration;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    @Override
    public void onDestroy() {
        if(wm!=null) {
            wm.removeView(parent);
            wm=null;
        }
        super.onDestroy();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        String name;

        name = intent.getStringExtra(CiviContract.NAME_CALL_LOG_COLUMN);
        date = intent.getStringExtra(CiviContract.DATE_CALL_LOG_COLUMN);
        contactID = intent.getStringExtra(CiviContract.CONTACT_ID_FIELD);
        duration = intent.getStringExtra(CiviContract.DURATION_CALL_LOG_COLUMN);


            wm = (WindowManager) this.getSystemService(WINDOW_SERVICE);


            WindowManager.LayoutParams params = new WindowManager.LayoutParams(
                    WindowManager.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT,
                    WindowManager.LayoutParams.TYPE_PHONE,
                    WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN,
                    PixelFormat.TRANSLUCENT);
            params.gravity = Gravity.BOTTOM;

            parent = new RelativeLayout(this);
            parent.setBackground(getDrawable(R.drawable.notes_gradient));

        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.write_note_layout, parent, true);


        TextView writeNotesTitle = (TextView) parent.findViewById(R.id.writeNotesTitle);
        notes = (EditText) parent.findViewById(R.id.writeNotes);

        writeNotesTitle.setText("Notes on last call with "+name);
        Button cancel, later, save;
        cancel = (Button) parent.findViewById(R.id.writeNotesCancel);
        later = (Button) parent.findViewById(R.id.writeNotesLater);
        save = (Button) parent.findViewById(R.id.writeNotesDone);

        cancel.setOnClickListener(this);
        later.setOnClickListener(this);
        save.setOnClickListener(this);

        wm.addView(parent, params);

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.writeNotesCancel:
                finish();
                return;
            case R.id.writeNotesDone:
                ContentValues cv = new ContentValues();

                cv.put(CiviContract.NOTES_DATE_COLUMN,date);
                cv.put(CiviContract.CONTACT_ID_FIELD,contactID);
                cv.put(CiviContract.NOTES_COLUMN,notes.getText().toString());
                cv.put(CiviContract.NOTES_DURATION_COLUMN,duration);
                getContentResolver().insert(Uri.parse(CiviContract.CONTENT_URI + "/" + CiviContract.NOTES_TABLE), cv);
                Log.v("Content Values: ", cv.toString());
                finish();
                return;
        }

    }

    private void finish() {
        if(wm!=null) {
            wm.removeView(parent);
            wm=null;
        }
        onDestroy();
    }
}
