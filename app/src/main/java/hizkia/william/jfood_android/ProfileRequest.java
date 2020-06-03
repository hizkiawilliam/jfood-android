package hizkia.william.jfood_android;

import android.util.Log;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

public class ProfileRequest extends StringRequest {

    private static final String URL = "http://192.168.1.105:8080/customer/";

    /**
     * Method to get current customer
     */
    public ProfileRequest(int id_customer, Response.Listener<String> listener) {
        super(Method.GET, URL+id_customer, listener, null);
    }
}
