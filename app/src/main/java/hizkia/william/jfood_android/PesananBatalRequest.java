package hizkia.william.jfood_android;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;


public class PesananBatalRequest extends StringRequest {
    private static final String REGIS_URL = "http://192.168.1.105:8080/invoice/cancel";
    private Map<String, String> params;

    /**
     * Method to change invoice status to 'Canceled' based on id
     */
    public PesananBatalRequest(String id, Response.Listener<String> listener) {
        super(Method.POST, REGIS_URL, listener, null);
        params = new HashMap<>();
        params.put("id",id);
    }
    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return params;
    }
}