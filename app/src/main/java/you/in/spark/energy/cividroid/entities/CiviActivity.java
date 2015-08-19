package you.in.spark.energy.cividroid.entities;

import android.content.ContentValues;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

import you.in.spark.energy.cividroid.CiviContract;

public class CiviActivity {

    @SerializedName("is_error")
    @Expose
    private int isError;
    @Expose
    private int version;
    @Expose
    private int count;
    @Expose
    private int id;
    @Expose
    private List<Value> values = new ArrayList<Value>();

    /**
     *
     * @return
     * The isError
     */
    public int getIsError() {
        return isError;
    }

    /**
     *
     * @param isError
     * The is_error
     */
    public void setIsError(int isError) {
        this.isError = isError;
    }

    /**
     *
     * @return
     * The version
     */
    public int getVersion() {
        return version;
    }

    /**
     *
     * @param version
     * The version
     */
    public void setVersion(int version) {
        this.version = version;
    }

    /**
     *
     * @return
     * The count
     */
    public int getCount() {
        return count;
    }

    /**
     *
     * @param count
     * The count
     */
    public void setCount(int count) {
        this.count = count;
    }

    /**
     *
     * @return
     * The id
     */
    public int getId() {
        return id;
    }

    /**
     *
     * @param id
     * The id
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     *
     * @return
     * The values
     */
    public List<Value> getValues() {
        return values;
    }

    /**
     *
     * @param values
     * The values
     */
    public void setValues(List<Value> values) {
        this.values = values;
    }


    public class Value {

        @Expose
        private String id;
        @SerializedName("activity_type_id")
        @Expose
        private String activityTypeId;
        @Expose
        private String subject;
        @SerializedName("activity_date_time")
        @Expose
        private String activityDateTime;
        @Expose
        private String phoneId;
        @Expose
        private String duration;
        @Expose
        private String location;
        @Expose
        private String details;
        @SerializedName("status_id")
        @Expose
        private String statusId;
        @SerializedName("priority_id")
        @Expose
        private String priorityId;
        @SerializedName("source_contact_id")
        @Expose
        private String sourceContactId;
        private ContentValues allValues;


        /**
         *
         * @return
         * The id
         */
        public String getId() {
            return id;
        }

        /**
         *
         * @param id
         * The id
         */
        public void setId(String id) {
            this.id = id;
        }

        /**
         *
         * @return
         * The activityTypeId
         */
        public String getActivityTypeId() {
            return activityTypeId;
        }

        /**
         *
         * @param activityTypeId
         * The activity_type_id
         */
        public void setActivityTypeId(String activityTypeId) {
            this.activityTypeId = activityTypeId;
        }

        /**
         *
         * @return
         * The subject
         */
        public String getSubject() {
            return subject;
        }

        /**
         *
         * @param subject
         * The subject
         */
        public void setSubject(String subject) {
            this.subject = subject;
        }

        /**
         *
         * @return
         * The activityDateTime
         */
        public String getActivityDateTime() {
            return activityDateTime;
        }

        /**
         *
         * @param activityDateTime
         * The activity_date_time
         */
        public void setActivityDateTime(String activityDateTime) {
            this.activityDateTime = activityDateTime;
        }

        /**
         *
         * @return
         * The phoneId
         */
        public String getPhoneId() {
            return phoneId;
        }

        /**
         *
         * @param phoneId
         * The phone_id
         */
        public void setPhoneId(String phoneId) {
            this.phoneId = phoneId;
        }

        /**
         *
         * @return
         * The duration
         */
        public String getDuration() {
            return duration;
        }

        /**
         *
         * @param duration
         * The duration
         */
        public void setDuration(String duration) {
            this.duration = duration;
        }

        /**
         *
         * @return
         * The location
         */
        public String getLocation() {
            return location;
        }

        /**
         *
         * @param location
         * The location
         */
        public void setLocation(String location) {
            this.location = location;
        }

        /**
         *
         * @return
         * The details
         */
        public String getDetails() {
            return details;
        }

        /**
         *
         * @param details
         * The details
         */
        public void setDetails(String details) {
            this.details = details;
        }

        /**
         *
         * @return
         * The statusId
         */
        public String getStatusId() {
            return statusId;
        }

        /**
         *
         * @param statusId
         * The status_id
         */
        public void setStatusId(String statusId) {
            this.statusId = statusId;
        }

        /**
         *
         * @return
         * The priorityId
         */
        public String getPriorityId() {
            return priorityId;
        }

        /**
         *
         * @param priorityId
         * The priority_id
         */
        public void setPriorityId(String priorityId) {
            this.priorityId = priorityId;
        }

        /**
         *
         * @return
         * The sourceContactId
         */
        public String getSourceContactId() {
            return sourceContactId;
        }

        /**
         *
         * @param sourceContactId
         * The source_contact_id
         */
        public void setSourceContactId(String sourceContactId) {
            this.sourceContactId = sourceContactId;
        }

        public ContentValues getAllValues() {
            allValues = new ContentValues();
            int i = 0;
            allValues.put(CiviContract.ACTIVITY_TABLE_COLUMNS[i++],getPhoneId());
            allValues.put(CiviContract.ACTIVITY_TABLE_COLUMNS[i++],getId());
            allValues.put(CiviContract.ACTIVITY_TABLE_COLUMNS[i++],getActivityTypeId());
            allValues.put(CiviContract.ACTIVITY_TABLE_COLUMNS[i++],getSubject());
            allValues.put(CiviContract.ACTIVITY_TABLE_COLUMNS[i++],getActivityDateTime());
            allValues.put(CiviContract.ACTIVITY_TABLE_COLUMNS[i++],getDuration());
            allValues.put(CiviContract.ACTIVITY_TABLE_COLUMNS[i++],getLocation());
            allValues.put(CiviContract.ACTIVITY_TABLE_COLUMNS[i++],getDetails());
            allValues.put(CiviContract.ACTIVITY_TABLE_COLUMNS[i++],getStatusId());
            allValues.put(CiviContract.ACTIVITY_TABLE_COLUMNS[i++],getPriorityId());
            allValues.put(CiviContract.ACTIVITY_TABLE_COLUMNS[i++],getSourceContactId());
            return allValues;
        }
    }

}
