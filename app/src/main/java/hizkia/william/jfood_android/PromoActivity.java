package hizkia.william.jfood_android;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class PromoActivity extends AppCompatActivity {

    ListView listViewPromo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_promo);
        listViewPromo = (ListView) findViewById(R.id.promoListView);
        final Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    ArrayList<String> promoList = new ArrayList<>();
                    JSONArray promos = new JSONArray(response);
                    for (int i=0; i<promos.length(); i++) {
                        JSONObject objPromo = promos.getJSONObject(i);
                        promoList.add("Promo Code\t\t\t\t: " + objPromo.getString("code") +
                                "\nDiscount\t\t\t\t\t\t: Rp. " + objPromo.getInt("discount") +
                                "\nMinimum Price\t\t: Rp. " + objPromo.getInt("minPrice") +
                                "\nActive\t\t\t\t\t\t\t\t: " + objPromo.getBoolean("active") );
                    }
                    ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(PromoActivity.this, android.R.layout.simple_list_item_1, promoList);
                    listViewPromo.setAdapter(arrayAdapter);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };
        PromoRequest request = new PromoRequest(responseListener);
        RequestQueue queue = new Volley().newRequestQueue(PromoActivity.this);
        queue.add(request);
    }
}
