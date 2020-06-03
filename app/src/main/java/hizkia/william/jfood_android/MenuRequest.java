package hizkia.william.jfood_android;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class MenuRequest extends StringRequest {

    private static String URL = "http://192.168.1.105:8080/food";
    private Map<String, String> params;

    /**
     * Method to get request for food list
     */
    public MenuRequest(Response.Listener<String> listener) {
        super(Method.GET, URL, listener, null);
        params = new HashMap<>();
    }

    @Override
    public Map<String, String> getParams() {
        return params;
    }
}
