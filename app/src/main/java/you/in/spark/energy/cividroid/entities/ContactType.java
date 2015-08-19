package you.in.spark.energy.cividroid.entities;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ContactType {

    @SerializedName("is_error")
    @Expose
    private int isError;
    @Expose
    private int version;
    @Expose
    private int count;
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
        private String name;
        @Expose
        private String label;

        /**
         *
         * @return
         * The name
         */
        public String getName() {
            return name;
        }

        /**
         *
         * @param name
         * The name
         */
        public void setName(String name) {
            this.name = name;
        }

        /**
         *
         * @return
         * The label
         */
        public String getLabel() {
            return label;
        }

        /**
         *
         * @param label
         * The label
         */
        public void setLabel(String label) {
            this.label = label;
        }

    }


}