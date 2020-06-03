package hizkia.william.jfood_android;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class InvoiceHistory extends AppCompatActivity {

    ListView listViewInvoice;
    int currentUserId;
    private String currentUserName;

    /**
     * Method that will be executed on create
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invoice_history);
        listViewInvoice = (ListView) findViewById(R.id.invoiceListView);
        final TextView stInvoiceHistory = findViewById(R.id.tvHistoryList);
        final TextView stDataFetching = findViewById(R.id.tvHistoryDataFetching);

        stInvoiceHistory.setVisibility(View.GONE);
        stDataFetching.setText("Data fetching, please wait...");

        //==============================================================================
        //Fetch data from main activity
        //==============================================================================
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            currentUserName = extras.getString("currentUserName");
            currentUserId = extras.getInt("currentUserId");
        }

        //==============================================================================
        //Fetch all invoice of the customer based on the currentUserId
        //==============================================================================
        final Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    ArrayList<String> invoiceList = new ArrayList<>();
                    JSONArray invoices = new JSONArray(response);
                    if(invoices.isNull(0))
                    {
                        Toast.makeText(InvoiceHistory.this, "You don't have any invoices", Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        intent.putExtra("currentUserId", currentUserId);
                        intent.putExtra("currentUserName", currentUserName);
                        startActivity(intent);
                    }
                    else {
                        for (int i = 0; i < invoices.length(); i++) {
                            String addTemp = "";
                            ArrayList<Food> temp = new ArrayList<Food>();
                            JSONObject invoice = invoices.getJSONObject(i);
                            JSONArray foods = invoice.getJSONArray("foods");
                            for (int j = 0; j < foods.length(); j++) {
                                JSONObject food = foods.getJSONObject(j);
                                JSONObject seller = food.getJSONObject("seller");
                                JSONObject location = seller.getJSONObject("location");
                                temp.add(new Food(food.getInt("id"),
                                        food.getString("name"),
                                        new Seller(seller.getInt("id"),
                                                seller.getString("name"),
                                                seller.getString("email"),
                                                seller.getString("phoneNumber"),
                                                new Location(location.getString("province"), location.getString("description"),
                                                        location.getString("city"))),
                                        food.getInt("price"), food.getString("category")));
                            }
                            for (Food food : temp) {
                                addTemp += "\n\t\tFood name\t\t\t: " + food.getName() +
                                        "\n\t\tFood Price\t\t\t\t: Rp. " + food.getPrice() +
                                        "\n\t\tFood Category\t: " + food.getCategory() + "\n";
                            }
                            invoiceList.add("\nInvoice id\t\t\t: " + invoice.getString("id") +
                                    "\nFood ordered\t: " + foods.length() + "\n" + addTemp +
                                    "\nDate Created\t\t: " + invoice.getString("date").substring(0, 10) +
                                    "\nTotal Price\t\t\t\t: Rp. " + invoice.getInt("totalPrice") +
                                    "\nPayment Type\t: " + invoice.getString("paymentType") +
                                    "\nInvoice Status\t: " + invoice.getString("invoiceStatus") + "\n");
                        }
                        stInvoiceHistory.setVisibility(View.VISIBLE);
                        stDataFetching.setVisibility(View.GONE);
                        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(InvoiceHistory.this, android.R.layout.simple_list_item_1, invoiceList);
                        listViewInvoice.setAdapter(arrayAdapter);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };
        InvoiceHistoryFetch request = new InvoiceHistoryFetch(currentUserId,responseListener);
        RequestQueue queue = new Volley().newRequestQueue(InvoiceHistory.this);
        queue.add(request);
    }
}
