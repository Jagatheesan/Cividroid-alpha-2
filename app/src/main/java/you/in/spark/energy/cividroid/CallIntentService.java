package you.in.spark.energy.cividroid;

import android.app.IntentService;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.ContentObserver;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.graphics.PixelFormat;
import android.net.Uri;
import android.os.Handler;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.provider.CallLog;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * Created by dell on 8/11/2015.
 */
public class CallIntentService extends IntentService {

    public CallIntentService() {
        super("LastSMSCallIntentService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        String lastConvDate = sp.getString(CiviContract.LAST_CONV_DATE_PREF, "0");
        Log.v("LASTCONVDATE", lastConvDate);

        Cursor callLog;
        do {
            callLog = getContentResolver().query(CallLog.Calls.CONTENT_URI, CiviContract.CALL_LOG_COLUMNS, CiviContract.DATE_CALL_LOG_COLUMN + " > ?", new String[]{lastConvDate}, CiviContract.DATE_CALL_LOG_COLUMN + " DESC");
            if (callLog.moveToFirst()) {

                //sp.edit().putString(CiviContract.LAST_CONV_DATE_PREF, callLog.getString(1)).apply();

                String number, date, duration, name;
                duration = callLog.getString(2);
                name = callLog.getString(3);
                int type = callLog.getInt(4);
                Log.v("Last caller name: ", "" + name);


                if ((type == CallLog.Calls.INCOMING_TYPE || type == CallLog.Calls.OUTGOING_TYPE) && !duration.equalsIgnoreCase("0") && !name.equalsIgnoreCase("null")) {

                    Log.v("CallIntentService", "valid call");
                    number = callLog.getString(0);
                    date = callLog.getString(1);
                    number = number.replaceAll("\\D", "");
                    String contactID = null;

                    //find if it's CiviCRM contact call

                    //find contact ID
                    Uri phoneNumberUri = Uri.withAppendedPath(ContactsContract.CommonDataKinds.Phone.CONTENT_FILTER_URI, Uri.encode(number));
                    Cursor res = getContentResolver().query(phoneNumberUri, new String[]{ContactsContract.CommonDataKinds.Phone.CONTACT_ID}, null, null, null);
                    if (res.moveToFirst()) {
                        contactID = res.getString(0);
                        Log.v("CONTACTID", contactID);

                        //check if rawContactID is present in Civi contacts table
                        Cursor civiCheck = getContentResolver().query(Uri.parse(CiviContract.CONTENT_URI + "/" + CiviContract.CONTACTS_FIELD_TABLE), new String[]{CiviContract.ID_COLUMN}, CiviContract.CONTACT_ID_FIELD + "=?", new String[]{contactID}, null);
                        if (civiCheck.moveToFirst()) {
                            Intent writeNotes = new Intent(this, WriteNotesService.class);
                            writeNotes.putExtra(CiviContract.NAME_CALL_LOG_COLUMN, name);
                            writeNotes.putExtra(CiviContract.DATE_CALL_LOG_COLUMN, date);
                            writeNotes.putExtra(CiviContract.CONTACT_ID_FIELD, contactID);
                            writeNotes.putExtra(CiviContract.DURATION_CALL_LOG_COLUMN, duration);
                            startService(writeNotes);
                        }
                        civiCheck.close();
                    }
                    res.close();
                }
            } else {
                Cursor test = getContentResolver().query(CallLog.Calls.CONTENT_URI, CiviContract.CALL_LOG_COLUMNS, null,null, CiviContract.DATE_CALL_LOG_COLUMN + " DESC");
                DatabaseUtils.dumpCursor(test);
                test.close();
                Log.v("CallIntentService","sleeping for a second");
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Log.v("CallIntentService","wokeup");
                continue;
            }
            break;
        } while (true);

        Log.v("CallIntentService","brokeup");


        DatabaseUtils.dumpCursor(callLog);
        callLog.close();
    }

}