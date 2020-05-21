package hizkia.william.jfood_android;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

public class BuatPesananActivity extends AppCompatActivity {

    private int currentUserId;
    private String currentUserName;
    private int foodId;
    private String foodName;
    private String foodCategory;
    private double foodPrice;
    private String promoCode;
    private int invoiceId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buat_pesanan);

        //Fetch data from main activity
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            currentUserName = extras.getString("currentUserName");
            currentUserId = extras.getInt("currentUserId");
            foodId = extras.getInt("item_id");
            foodName = extras.getString("item_name");
            foodCategory = extras.getString("item_category");
            foodPrice = extras.getInt("item_price");
        }

        //Initiate xml objects
        final EditText etPromoCode = findViewById(R.id.promo_code);
        final TextView tvTextCode = findViewById(R.id.text_code);
        final TextView tvFoodName = findViewById(R.id.food_name);
        final TextView tvFoodCategory = findViewById(R.id.food_category);
        final TextView tvFoodPrice = findViewById(R.id.food_price);
        final TextView tvTotalPrice = findViewById(R.id.total_price);
        final Button btnOrder = findViewById(R.id.order);
        final Button btnCount = findViewById(R.id.count);
        final RadioGroup radioGroup = findViewById(R.id.radioGroup);

        //Assign initial value
        etPromoCode.setVisibility(View.GONE);
        tvTextCode.setVisibility(View.GONE);
        btnOrder.setVisibility(View.GONE);

        tvFoodName.setText(foodName);
        tvFoodCategory.setText(foodCategory);
        tvFoodPrice.setText("Rp. " + (int) foodPrice);
        tvTotalPrice.setText("Rp. " + "0");

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton radioButton = findViewById(checkedId);
                String selected = radioButton.getText().toString().trim();
                switch (selected) {
                    case "Via CASHLESS":
                        tvTextCode.setVisibility(View.VISIBLE);
                        etPromoCode.setVisibility(View.VISIBLE);
                        break;
                    case "Via CASH":
                        etPromoCode.setVisibility(View.GONE);
                        tvTextCode.setVisibility(View.GONE);
                        break;
                }
            }
        });

        btnCount.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int selectedRadioId = radioGroup.getCheckedRadioButtonId();
                    RadioButton selectedRadio = findViewById(selectedRadioId);
                    String selected = selectedRadio.getText().toString().trim();
                    switch (selected) {
                        case "Via CASH":
                            tvTotalPrice.setText("Rp. " + foodPrice);
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
                                    //Check if promo code is filled or not
                                    if (promoCode.isEmpty()) {
                                        Toast.makeText(BuatPesananActivity.this, "No Promo Code Applied", Toast.LENGTH_LONG).show();
                                        tvTotalPrice.setText("Rp. " + foodPrice);
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
                                                if (foodPrice < promoDiscountPrice || foodPrice < minimalDiscountPrice) {
                                                    Toast.makeText(BuatPesananActivity.this, "Promo Code cannot be Applied", Toast.LENGTH_LONG).show();
                                                } else {
                                                    //Toast Feedback
                                                    Toast.makeText(BuatPesananActivity.this, "Promo Code Applied", Toast.LENGTH_LONG).show();
                                                    //Set Total Price
                                                    tvTotalPrice.setText("Rp. " + (foodPrice - promoDiscountPrice));
                                                    //Button VIsibility
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
//                int selectedRadioId = radioGroup.getCheckedRadioButtonId();
//                RadioButton selectedRadio = findViewById(selectedRadioId);
//                String selected = selectedRadio.getText().toString().trim();
//                promoCode = etPromoCode.getText().toString();
//                switch (selected) {
//                    case "Via CASH":
//                        tvTotalPrice.setText("Rp. " + foodPrice);
//                        break;
//                    case "Via CASHLESS":
//                        fetchPromo(promoCode);
//                        tvTotalPrice.setText("Rp. " + (foodPrice + promoPrice));
//                        break;
//                }
//                btnCount.setVisibility(View.GONE);
//                btnOrder.setVisibility(View.VISIBLE);
        });

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
                            if (response != null) {
                                Toast.makeText(BuatPesananActivity.this, "Your order has been saved", Toast.LENGTH_LONG).show();
                                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                intent.putExtra("currentUserId", currentUserId);
                                intent.putExtra("currentUserName", currentUserName);
                                startActivity(intent);
                            } else {
                                btnOrder.setVisibility(View.GONE);
                                Toast.makeText(BuatPesananActivity.this, "Order failed, you have ordered this item before", Toast.LENGTH_LONG).show();
                                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                intent.putExtra("currentUserId", currentUserId);
                                intent.putExtra("currentUserName", currentUserName);
                                startActivity(intent);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                };

                if(selected.equals("Via CASH")){
                    request = new BuatPesananRequest(foodId+"", currentUserId+"" ,responseListener);
                }else if(selected.equals("Via CASHLESS")){
                    request = new BuatPesananRequest(foodId+"", currentUserId+"", promoCode ,responseListener);}

                RequestQueue queue = Volley.newRequestQueue(BuatPesananActivity.this);
                queue.add(request);
            }
        });
    }
}
