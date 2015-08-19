package you.in.spark.energy.cividroid;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.preference.PreferenceManager;
import android.provider.CallLog;
import android.telephony.TelephonyManager;
import android.util.Log;

import java.util.Date;

/**
 * Created by dell on 8/11/2015.
 */
public class CallBroadcastListener extends BroadcastReceiver {

    //preventing execution twice - issue in lollipop
    private static boolean first;
    private static boolean offHookFirst;


    @Override
    public void onReceive(Context context, Intent intent) {

        Log.v("BROADCASTCALL",intent.getStringExtra(TelephonyManager.EXTRA_STATE));

        if(intent.getStringExtra(TelephonyManager.EXTRA_STATE).equalsIgnoreCase(TelephonyManager.EXTRA_STATE_IDLE)) {
            if(!first){
                first = true;
                return;
            }
            first=false;
            Log.v("Incoming Call", "Idle");
            Intent callService = new Intent(context, CallIntentService.class);
            callService.putExtras(intent);
            context.startService(callService);
        } else {
            if(!offHookFirst) {

                SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
                Cursor lastCallLog = context.getContentResolver().query(CallLog.Calls.CONTENT_URI, CiviContract.CALL_LOG_COLUMNS, null,null, CiviContract.DATE_CALL_LOG_COLUMN + " DESC");
                if(lastCallLog.moveToFirst()) {
                    sp.edit().putString(CiviContract.LAST_CONV_DATE_PREF, lastCallLog.getString(1)).apply();
                }
                lastCallLog.close();
                offHookFirst = true;
                return;
            }
            offHookFirst = false;
        }
    }

}
