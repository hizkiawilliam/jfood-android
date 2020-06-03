package hizkia.william.jfood_android;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class BuatPesananRequest extends StringRequest {
    private static String URLCash = "http://192.168.1.105:8080/invoice/createCashInvoice";
    private static String URLCashless = "http://192.168.1.105:8080/invoice/createCashlessInvoice";
    private Map<String, String> params;

    /**
     * Method for creating invoice based on the parameter
     */
    public BuatPesananRequest(ArrayList<Integer> foodList, String customerId ,Response.Listener<String> listener) {
        super(Method.POST, URLCash, listener, null);
        String foodListString = "";
        //String need to be adjusted
        //Changed from [1, 2, 3] set of foods id
        //Into "1,2,3"
        for(int i = 0; i < foodList.size(); i++){
            if (i == foodList.size()-1){
                foodListString += ""+foodList.get(i)+"";
            }
            else {
                foodListString += ""+foodList.get(i)+",";
            }
        }
        params = new HashMap<>();
        params.put("foodList", foodListString);
        params.put("customerId", customerId);
        params.put("deliveryFee", "0");
    }

    public BuatPesananRequest(ArrayList<Integer> foodList, String customerId, String promoCode ,Response.Listener<String> listener) {
        super(Method.POST, URLCashless, listener, null);
        String foodListString = "";
        //String need to be adjusted
        //Changed from [1, 2, 3] set of foods id
        //Into "1,2,3"
        for(int i = 0; i < foodList.size(); i++){
            if (i == foodList.size()-1){
                foodListString += ""+foodList.get(i)+"";
            }
            else {
                foodListString += ""+foodList.get(i)+",";
            }
        }
        params = new HashMap<>();
        params.put("foodList", foodListString);
        params.put("customerId", customerId);
        params.put("promoCode", promoCode);
    }

    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return params;
    }

}
