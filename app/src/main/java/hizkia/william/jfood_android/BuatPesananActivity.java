package hizkia.william.jfood_android;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class BuatPesananActivity extends AppCompatActivity {

    private int currentUserId;
    private String currentUserName;
    private double totalPrice;
    private String promoCode;
    ListView listView;
    private ArrayList<String> foodCartArray = new ArrayList<>();
    private ArrayList<Integer> foodsId = new ArrayList<>();

    /**
     * Method that will be executed on create
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buat_pesanan);

        //==============================================================================
        //Fetch data from main activity
        //==============================================================================
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            currentUserName = extras.getString("currentUserName");
            currentUserId = extras.getInt("currentUserId");
            totalPrice = extras.getInt("totalPrice");
            foodCartArray = extras.getStringArrayList("foodCart");
            foodsId = extras.getIntegerArrayList("foodsId");
        }
        //==============================================================================
        //Initiate xml objects
        //==============================================================================
        final EditText etPromoCode = findViewById(R.id.promo_code);
        final TextView tvTextCode = findViewById(R.id.text_code);
        final TextView tvTotalPrice = findViewById(R.id.total_price);
        final TextView tvStDiscount = findViewById(R.id.staticDiscount);
        final TextView tvDiscount = findViewById(R.id.discount);
        final Button btnOrder = findViewById(R.id.order);
        final Button btnCount = findViewById(R.id.count);
        final RadioGroup radioGroup = findViewById(R.id.radioGroup);
        listView = (ListView) findViewById(R.id.foodsListView);

        //==============================================================================
        //Assign initial value
        //==============================================================================
        etPromoCode.setVisibility(View.GONE);
        tvTextCode.setVisibility(View.GONE);
        tvDiscount.setVisibility(View.GONE);
        tvStDiscount.setVisibility(View.GONE);
        btnOrder.setVisibility(View.GONE);
        btnCount.setVisibility(View.GONE);
        tvTotalPrice.setText("Rp. " + "0");

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(BuatPesananActivity.this, android.R.layout.simple_list_item_1, foodCartArray);
        listView.setAdapter(arrayAdapter);

        //==============================================================================
        //Respond when radio group is selected
        //==============================================================================
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton radioButton = findViewById(checkedId);
                String selected = radioButton.getText().toString().trim();
                switch (selected) {
                    case "Via CASHLESS":
                        tvTextCode.setVisibility(View.VISIBLE);
                        etPromoCode.setVisibility(View.VISIBLE);
                        btnCount.setVisibility(View.VISIBLE);
                        break;
                    case "Via CASH":
                        etPromoCode.setVisibility(View.GONE);
                        tvTextCode.setVisibility(View.GONE);
                        btnCount.setVisibility(View.VISIBLE);
                        break;
                }
            }
        });

        //==============================================================================
        //Respond when button count is pressed
        //==============================================================================
        btnCount.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int selectedRadioId = radioGroup.getCheckedRadioButtonId();
                    RadioButton selectedRadio = findViewById(selectedRadioId);
                    String selected = selectedRadio.getText().toString().trim();
                    switch (selected) {
                        case "Via CASH":
                            tvTotalPrice.setText("Rp. " + totalPrice);
                            btnCount.setVisibility(View.GONE);
                            btnOrder.setVisibility(View.VISIBLE);
                            break;

                        case "Via CASHLESS":
                            //Get Promo Code String
                            promoCode = etPromoCode.getText().toString();
                            //Listener Promo
                            final Response.Listener<String> promoResponse = new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    //No promo code applied
                                    if (promoCode.isEmpty()) {
                                        Toast.makeText(BuatPesananActivity.this, "No Promo Code Applied", Toast.LENGTH_LONG).show();
                                        tvTotalPrice.setText("Rp. " + totalPrice);
                                        //Button VIsibility
                                        btnCount.setVisibility(View.GONE);
                                        btnOrder.setVisibility(View.VISIBLE);
                                    } else {
                                        try {
                                            JSONObject jsonResponse = new JSONObject(response);
                                            //Get Discount Price
                                            int promoDiscountPrice = jsonResponse.getInt("discount");
                                            int minimalDiscountPrice = jsonResponse.getInt("minPrice");
                                            boolean promoStatus = jsonResponse.getBoolean("active");
                                            //Case if Promo can be Applied
                                            if (promoStatus == false) {
                                                Toast.makeText(BuatPesananActivity.this, "Promo Code can no longer used", Toast.LENGTH_LONG).show();
                                            } else if (promoStatus == true) {
                                                //Promo cannot be applied
                                                if (totalPrice < promoDiscountPrice || totalPrice < minimalDiscountPrice) {
                                                    Toast.makeText(BuatPesananActivity.this, "Promo Code cannot be Applied", Toast.LENGTH_LONG).show();
                                                } else {
                                                    Toast.makeText(BuatPesananActivity.this, "Promo Code Applied", Toast.LENGTH_LONG).show();
                                                    tvDiscount.setText("Rp. "+ promoDiscountPrice);
                                                    tvTotalPrice.setText("Rp. " + (totalPrice - promoDiscountPrice));
                                                    tvStDiscount.setVisibility(View.VISIBLE);
                                                    tvDiscount.setVisibility(View.VISIBLE);
                                                    btnCount.setVisibility(View.GONE);
                                                    btnOrder.setVisibility(View.VISIBLE);
                                                }
                                            }
                                        } catch (JSONException e) {
                                            Toast.makeText(BuatPesananActivity.this, "Promo Code not found", Toast.LENGTH_LONG).show();
                                        }
                                    }
                                }
                            };

                            //Volley Request for Promo Request
                            PromoRequest promoRequest = new PromoRequest(promoCode, promoResponse);
                            RequestQueue queue = Volley.newRequestQueue(BuatPesananActivity.this);
                            queue.add(promoRequest);
                            break;
                    }
                }
        });

        //==============================================================================
        //Respond when button order is pressed
        //==============================================================================
        btnOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int selectedRadioId = radioGroup.getCheckedRadioButtonId();
                RadioButton selectedRadio = findViewById(selectedRadioId);
                String selected = selectedRadio.getText().toString().trim();
                BuatPesananRequest request = null;

                final Response.Listener<String> responseListener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                                JSONObject jsonObject = new JSONObject(response);
                                Toast.makeText(BuatPesananActivity.this, "You order has been saved", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                intent.putExtra("currentUserId", currentUserId);
                                intent.putExtra("currentUserName", currentUserName);
                                startActivity(intent);
                        } catch (JSONException e) {
                            e.printStackTrace();
                                btnOrder.setVisibility(View.GONE);
                                Toast.makeText(BuatPesananActivity.this, "Order failed. Please finish your invoice", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                intent.putExtra("currentUserId", currentUserId);
                                intent.putExtra("currentUserName", currentUserName);
                                startActivity(intent);
                        }
                    }
                };

                if(selected.equals("Via CASH")){
                    request = new BuatPesananRequest(foodsId, currentUserId+"" ,responseListener);
                }else if(selected.equals("Via CASHLESS")){
                    request = new BuatPesananRequest(foodsId, currentUserId+"", promoCode ,responseListener);}

                RequestQueue queue = Volley.newRequestQueue(BuatPesananActivity.this);
                queue.add(request);
            }
        });
    }
}
