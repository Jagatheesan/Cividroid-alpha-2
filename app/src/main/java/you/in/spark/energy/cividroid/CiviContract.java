package you.in.spark.energy.cividroid;

import org.apache.http.auth.AUTH;

/**
 * Created by dell on 14-06-2015.
 */
public class CiviContract {

    public static final String SITE_KEY = "sitekey";
    public static final String API_KEY = "apikey";
    public static final String AUTHORITY = "com.android.contacts";
    public static final String PROVIDER_AUTHORITY = "you.in.spark.energy.cividroid.provider";
    public static final String CONTENT_URI = "content://"+PROVIDER_AUTHORITY;
    public static final String ACCOUNT_TYPE = "you.in.spark.cividroid.account";
    public static final String ACCOUNT = "CiviDroid";
    public static final String CALL_FROM_ACTIVITY = "callFromActivity";
    public static final String PHONE_FIELD = "phone";
    public static final String EMAIL_FIELD = "email";
    public static final String CONTACT_ID_FIELD = "contact_id_on_device";
    public static final String RAW_CONTACT_ID_FIELD = "raw_id";

    public static final String NAME_FIELD = "name";
    public static final String TITLE_FIELD = "title";
    public static final String MIME_TYPE = "vnd.android.cursor.item/you.in.spark.energy.cividroid.profile";

    public static final String CALL_FROM_CONTACTS_FIELD ="callFromContactsField";

    public static final String ID_COLUMN = "_id";
    public static final String CONTACT_COLUMN_NAME = "contact_field_name";
    public static final String CONTACT_COLUMN_TITLE = "contact_field_title";
    public static final String CONTACTS_FIELD_TABLE = "contacts_field_table";
    public static final String ACTIVITY_FIELD_TABLE = "activity_field_table";

    public static final String CONTACTS_FIELD_TITLE_PREF = "cftitlepref";
    public static final String CONTACTS_FIELD_NAME_PREF = "cfnamepref";
    public static final String LAST_CONV_DATE_PREF = "lastcallconvpref";
    public static final String ACTIVITY_FIELD_TITLE_PREF = "actitlepref";
    public static final String ACTIVITY_FIELD_NAME_PREF = "acnamepref";
    public static final String CONTACT_CONV_TABLE = "conconvtable";
    public static final String CONV_TIME_COLUMN = "convtimecolumn";

    public static final String NUMBER_CALL_LOG_COLUMN = "number";
    public static final String DATE_CALL_LOG_COLUMN = "date";
    public static final String DURATION_CALL_LOG_COLUMN = "duration";
    public static final String NAME_CALL_LOG_COLUMN = "name";
    public static final String TYPE_CALL_LOG_COLUMN = "type";

    public static final String[] CALL_LOG_COLUMNS = {NUMBER_CALL_LOG_COLUMN,DATE_CALL_LOG_COLUMN,DURATION_CALL_LOG_COLUMN,NAME_CALL_LOG_COLUMN,TYPE_CALL_LOG_COLUMN};

    //offset by one
    public static final String[] CONTACT_FIELD_NAMES = {"Contact ID","Phone Id", "Contact Type","Contact Subtype", "Display Name", "Birthdate", "Household Name", "Organization Name", "Street Address", "Supplemental Address 1", "Supplemental Address 2", "City", "Post Code Suffix", "Postal Code", "Phone", "Email", "IM", "State Province", "Country"};
    public static final String[] CONTACT_TABLE_COLUMNS = {"contact_id", "phone_id", "contact_type", "contact_sub_type", "display_name", "birth_date", "household_name", "organization_name", "street_address", "supplemental_address_1", "supplemental_address_2", "city", "postal_code_suffix", "postal_code", "phone", "email", "im", "state_province", "country" };
    public static final String ACTIVITY_TABLE = "activitytable";
    public static final String TARGET_CONTACT_ID_COLUMN = "target_contact_id";
    public static final String LAST_ACTIVITY_SYNC_ID = "lasacsyncid";

    public static final String[] ACTIVITY_FIELD_NAMES = {"Phone ID", "Activity ID", "Activity Type", "Subject", "Reminder", "Duration", "Location", "Details", "Status", "Priority", "Source Contact"};
    public static final String[] ACTIVITY_TABLE_COLUMNS = {"phone_id", "id", "activity_type_id", "subject", "activity_date_time", "duration", "location", "details", "status_id", "priority_id", "source_contact_id"};


    public static final String NOTES_TABLE ="notestable";
    public static final String NOTES_COLUMN = "notescolumn";
    public static final String NOTES_DATE_COLUMN = "notesdatecolumn";
    public static final String NOTES_DURATION_COLUMN = "notesdurationcolumn";
    public static final String CONTACT_NAME_COLUMN = "contactnamecolumn";
    public static final String SYNCED_PREF = "syncedpref";
    public static final String PHOTO_URI = "photouri";
    public static final String LAST_SYNC_DATE = "lastsyncdate";

    public static final String WEBSITE_URL = "websiteurl";
    public static final String PATH_TO_API = "pathtoapi";
}
