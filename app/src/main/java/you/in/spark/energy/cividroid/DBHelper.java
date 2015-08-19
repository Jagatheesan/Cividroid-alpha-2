package you.in.spark.energy.cividroid;

/**
 * Created by dell on 22-06-2015.
 */


import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.preference.PreferenceManager;

import java.util.Set;


public class DBHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "CiviDatabase";
    private static final int DB_VER = 1;
    private final Context context;

    private static final String CREATE_NOTES_TABLE = "CREATE TABLE " + CiviContract.NOTES_TABLE + " (" + CiviContract.ID_COLUMN +
            " INTEGER PRIMARY KEY AUTOINCREMENT, " + CiviContract.CONTACT_ID_FIELD + " VARCHAR, " + CiviContract.NOTES_DATE_COLUMN+ " VARCHAR, "+
            CiviContract.NOTES_DURATION_COLUMN+" VARCHAR, "+CiviContract.NOTES_COLUMN + " VARCHAR);";

    public DBHelper(Context context) {
        super(context, DB_NAME, null, DB_VER);
        this.context = context;
    }


    @Override
    public void onCreate(SQLiteDatabase db) {

        String cols = "";

        for(String title : CiviContract.CONTACT_TABLE_COLUMNS) {
            cols += ", "+title+" VARCHAR";
        }

        String CREATE_CONTACTS_FIELD_TABLE = "CREATE TABLE " + CiviContract.CONTACTS_FIELD_TABLE + " (" + CiviContract.ID_COLUMN +
                " INTEGER PRIMARY KEY AUTOINCREMENT, "+CiviContract.CONTACT_ID_FIELD+" VARCHAR"+cols+");";

        cols = "";

        for(String title : CiviContract.ACTIVITY_TABLE_COLUMNS) {
            cols += ", "+title+" VARCHAR";
        }

        String CREATE_ACTIVITY_TABLE  = "CREATE TABLE " + CiviContract.ACTIVITY_TABLE + " (" + CiviContract.ID_COLUMN +
                " INTEGER PRIMARY KEY AUTOINCREMENT, " + CiviContract.TARGET_CONTACT_ID_COLUMN + " VARCHAR"+cols+");";



        db.execSQL(CREATE_CONTACTS_FIELD_TABLE);
        db.execSQL(CREATE_ACTIVITY_TABLE);
        db.execSQL(CREATE_NOTES_TABLE);
        //db.execSQL(CREATE_ACTIVITY_FIELD_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + CiviContract.CONTACTS_FIELD_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + CiviContract.CONTACT_CONV_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + CiviContract.ACTIVITY_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + CiviContract.NOTES_TABLE);
        //db.execSQL("DROP TABLE IF EXISTS " + CiviContract.ACTIVITY_FIELD_TABLE);
    }
}
