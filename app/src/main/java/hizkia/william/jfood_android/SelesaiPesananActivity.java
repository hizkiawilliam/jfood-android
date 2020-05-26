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
    private int invoiceId;
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

        final TextView tvInvoiceId = findViewById(R.id.tvInvoiceId);
        final TextView tvInvoiceCustomerName = findViewById(R.id.tvInvoiceCustomerName);
        final TextView tvInvoiceDate = findViewById(R.id.tvInvoiceDate);
        //final TextView tvInvoicePaymentType = findViewById(R.id.tvInvoicePaymentType);
        final TextView tvInvoiceTotalPrice = findViewById(R.id.tvInvoiceTotalPrice);
        final Button btnInvoiceCancel = findViewById(R.id.btnInvoiceCancel);
        final Button btnInvoiceDone = findViewById(R.id.btnInvoiceDone);
        listView = (ListView) findViewById(R.id.foodsListView);

//        tvInvoiceId.setVisibility(View.GONE);
//        tvInvoiceCustomerName.setVisibility(View.GONE);
//        tvInvoiceFoodName.setVisibility(View.GONE);
//        tvInvoiceDate.setVisibility(View.GONE);
//        tvInvoicePaymentType.setVisibility(View.GONE);
//        tvInvoiceTotalPrice.setVisibility(View.GONE);
//        btnInvoiceCancel.setVisibility(View.GONE);
//        btnInvoiceDone.setVisibility(View.GONE);

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
                        String addTemp = ""+food.getName()+"\t\t\t\t\t\t\t\t\t\tRp. "+food.getPrice();
                        foodList.add(addTemp);
                    }

                    ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(SelesaiPesananActivity.this, android.R.layout.simple_list_item_1, foodList);
                    listView.setAdapter(arrayAdapter);

                    invoiceId = invoice.getInt("id");
                    invoiceDate = invoice.getString("date");
                    invoicePaymentType = invoice.getString("paymentType");
                    invoiceTotalPrice = invoice.getInt("totalPrice");
                    tvInvoiceId.setText(""+invoiceId);
                    tvInvoiceDate.setText(""+invoiceDate.substring(0,9));
                    tvInvoiceCustomerName.setText(""+currentUserName);
                    //tvInvoicePaymentType.setText(""+invoicePaymentType);
                    tvInvoiceTotalPrice.setText(""+invoiceTotalPrice);
                } catch (JSONException e) {
                    e.printStackTrace();
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
                            Toast.makeText(SelesaiPesananActivity.this, "This invoice is canceled", Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            intent.putExtra("currentUserId", currentUserId);
                            intent.putExtra("currentUserName", currentUserName);
                            startActivity(intent);
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