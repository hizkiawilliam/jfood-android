package hizkia.william.jfood_android;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class PromoRequest extends StringRequest {

    private static String URL = "http://192.168.1.105:8080/promo/";
    private Map<String, String> params;

    public PromoRequest(Response.Listener<String> listener) {
        super(Method.GET, URL, listener, null);
        params = new HashMap<>();
    }

    public PromoRequest(String code, Response.Listener<String> listener) {
        super(Method.GET, URL+code, listener, null);
        params = new HashMap<>();
    }

    @Override
    public Map<String, String> getParams() {
        return params;
    }
}
