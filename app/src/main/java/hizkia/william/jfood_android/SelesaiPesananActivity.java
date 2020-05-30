package hizkia.william.jfood_android;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class SelesaiPesananActivity extends AppCompatActivity {

    private int currentUserId;
    private String currentUserName;
    Button btnInvoiceCancel;
    Button btnInvoiceDone;
    private int invoiceId = 0;
    ListView listView;
    private String invoiceDate;
    private Integer invoiceTotalPrice;
    private String invoiceCustomer;
    private String invoiceStatus;
    private String invoicePaymentType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selesai_pesanan);

        final TextView stInvoice = findViewById(R.id.staticInvoice);
        final TextView stInvoiceId = findViewById(R.id.staticInvoiceId);
        final TextView stCustomerName = findViewById(R.id.staticInvoiceCustomerName);
        final TextView stFoodsOrdered = findViewById(R.id.staticInvoiceFoodName);
        final TextView stInvoiceCreated = findViewById(R.id.staticInvoiceDate);
        final TextView stPaymentType = findViewById(R.id.staticInvoicePayementType);
        final TextView stTotalPrice = findViewById(R.id.staticInvoiceTotalPrice);
        final TextView tvInvoiceId = findViewById(R.id.tvInvoiceId);
        final TextView tvInvoiceCustomerName = findViewById(R.id.tvInvoiceCustomerName);
        final TextView tvInvoiceDate = findViewById(R.id.tvInvoiceDate);
        final TextView tvInvoicePaymentType = findViewById(R.id.tvInvoicePaymentType);
        final TextView tvInvoiceTotalPrice = findViewById(R.id.tvInvoiceTotalPrice);
        final Button btnInvoiceCancel = findViewById(R.id.btnInvoiceCancel);
        final Button btnInvoiceDone = findViewById(R.id.btnInvoiceDone);
        listView = (ListView) findViewById(R.id.foodsListView);

        stInvoice.setText("Data fetching, please wait...");
        stInvoiceId.setVisibility(View.GONE);
        stCustomerName.setVisibility(View.GONE);
        stFoodsOrdered.setVisibility(View.GONE);
        stInvoiceCreated.setVisibility(View.GONE);
        stPaymentType.setVisibility(View.GONE);
        stTotalPrice.setVisibility(View.GONE);
        tvInvoiceId.setVisibility(View.GONE);
        tvInvoiceCustomerName.setVisibility(View.GONE);
        tvInvoiceDate.setVisibility(View.GONE);
        tvInvoicePaymentType.setVisibility(View.GONE);
        tvInvoiceTotalPrice.setVisibility(View.GONE);
        btnInvoiceCancel.setVisibility(View.GONE);
        btnInvoiceDone.setVisibility(View.GONE);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            currentUserName = extras.getString("currentUserName");
            currentUserId = extras.getInt("currentUserId");
        }

        //Fetch data
        final Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    ArrayList<Food> temp = new ArrayList<Food>();
                    JSONObject invoice = new JSONObject(response);
                    JSONArray foods = invoice.getJSONArray("foods");
                    for (int j = 0; j < foods.length(); j++)
                    {
                        JSONObject food = foods.getJSONObject(j);
                        JSONObject seller = food.getJSONObject("seller");
                        JSONObject location = seller.getJSONObject("location");
                        temp.add(   new Food(food.getInt("id"),
                                    food.getString("name"),
                                    new Seller(seller.getInt("id"),
                                    seller.getString("name"),
                                    seller.getString("email"),
                                    seller.getString("phoneNumber"),
                                    new Location(location.getString("province"), location.getString("description"),
                                    location.getString("city"))),
                                    food.getInt("price"), food.getString("category")));
                    }
                    ArrayList<String> foodList = new ArrayList<String>();

                    for(Food food:temp) {
                        String addTemp = "Food name\t\t\t: " + food.getName() +
                                "\nFood Price\t\t\t\t: Rp. " + food.getPrice() +
                                "\nFood Category\t: " + food.getCategory();
                        foodList.add(addTemp);
                    }

                    ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(SelesaiPesananActivity.this, android.R.layout.simple_list_item_1, foodList);
                    listView.setAdapter(arrayAdapter);

                    invoiceId = invoice.getInt("id");
                    invoiceDate = invoice.getString("date");
                    invoicePaymentType = invoice.getString("paymentType");
                    invoiceTotalPrice = invoice.getInt("totalPrice");

//                    if (invoicePaymentType.equals("Cashless")){
//                        try{
//                            invoicePaymentType = invoice.getJSONObject("promo").getString("code");
//                        }catch(JSONException e) {
//                            invoicePaymentType = "none";
//                        }
//                    }
                    stInvoice.setVisibility(View.GONE);
                    stInvoiceId.setVisibility(View.VISIBLE);
                    stCustomerName.setVisibility(View.VISIBLE);
                    stFoodsOrdered.setVisibility(View.VISIBLE);
                    stInvoiceCreated.setVisibility(View.VISIBLE);
                    stPaymentType.setVisibility(View.VISIBLE);
                    stTotalPrice.setVisibility(View.VISIBLE);
                    tvInvoiceId.setVisibility(View.VISIBLE);
                    tvInvoiceCustomerName.setVisibility(View.VISIBLE);
                    tvInvoiceDate.setVisibility(View.VISIBLE);
                    tvInvoiceTotalPrice.setVisibility(View.VISIBLE);
                    btnInvoiceCancel.setVisibility(View.VISIBLE);
                    btnInvoiceDone.setVisibility(View.VISIBLE);
                    tvInvoicePaymentType.setVisibility(View.VISIBLE);

                    tvInvoiceId.setText(""+invoiceId);
                    tvInvoiceDate.setText(""+invoiceDate.substring(0,10));
                    tvInvoiceCustomerName.setText(""+currentUserName);
                    tvInvoicePaymentType.setText(""+invoicePaymentType);
                    tvInvoiceTotalPrice.setText("Rp. "+invoiceTotalPrice);
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(SelesaiPesananActivity.this, "You don't have Ongoing Invoice", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.putExtra("currentUserId", currentUserId);
                    intent.putExtra("currentUserName", currentUserName);
                    startActivity(intent);
                }
            }
        };
        PesananFetchRequest request = new PesananFetchRequest(currentUserId, responseListener);
        RequestQueue queue = new Volley().newRequestQueue(SelesaiPesananActivity.this);
        queue.add(request);



        btnInvoiceCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Response.Listener<String> responseListener = new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            if (jsonObject != null) {
                                Toast.makeText(SelesaiPesananActivity.this, "This invoice is canceled", Toast.LENGTH_LONG).show();
                                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                intent.putExtra("currentUserId", currentUserId);
                                intent.putExtra("currentUserName", currentUserName);
                                startActivity(intent);
                            } else {
                                AlertDialog.Builder builder = new AlertDialog.Builder(SelesaiPesananActivity.this);
                                builder.setMessage("Operation Failed! Please try again").create().show();
                            }
                        } catch (JSONException e) {
                            AlertDialog.Builder builder = new AlertDialog.Builder(SelesaiPesananActivity.this);
                            builder.setMessage("Operation Failed! Please try again").create().show();
                        }
                    }
                };

                PesananBatalRequest request = new PesananBatalRequest(String.valueOf(invoiceId), responseListener);
                RequestQueue queue = Volley.newRequestQueue(SelesaiPesananActivity.this);
                queue.add(request);
            }
        });


        btnInvoiceDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Response.Listener<String> responseListener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            if (jsonObject != null) {
                                Toast.makeText(SelesaiPesananActivity.this, "This invoice is finished", Toast.LENGTH_LONG).show();
                                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                intent.putExtra("currentUserId", currentUserId);
                                intent.putExtra("currentUserName", currentUserName);
                                startActivity(intent);
                            } else {
                                AlertDialog.Builder builder = new AlertDialog.Builder(SelesaiPesananActivity.this);
                                builder.setMessage("Operation Failed! Please try again").create().show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            AlertDialog.Builder builder = new AlertDialog.Builder(SelesaiPesananActivity.this);
                            builder.setMessage("Operation Failed! Please try again").create().show();
                        }
                    }
                };
                PesananSelesaiRequest request = new PesananSelesaiRequest(String.valueOf(invoiceId), responseListener);
                RequestQueue queue = Volley.newRequestQueue(SelesaiPesananActivity.this);
                queue.add(request);
            }
        });
    }
}