package you.in.spark.energy.cividroid.api;

import java.util.Map;

import retrofit.Callback;
import retrofit.http.POST;
import retrofit.http.Path;
import retrofit.http.Query;
import retrofit.http.QueryMap;
import you.in.spark.energy.cividroid.entities.CiviActivity;
import you.in.spark.energy.cividroid.entities.Contact;
import you.in.spark.energy.cividroid.entities.ContactType;

/**
 * Created by dell on 12-06-2015.
 */
public interface ICiviApi {

        @POST("/rest.php?entity=ContactType&action=get")
        public void getContactSubtypes(@QueryMap Map<String,String> fields, Callback<ContactType> response);

        @POST("/rest.php?entity=Contact&action=get")
        public Contact getContacts(@QueryMap Map<String,String> fields);

        @POST("/rest.php?entity=Activity&action=get")
        public CiviActivity getActivity(@QueryMap Map<String,String> fields);

}
