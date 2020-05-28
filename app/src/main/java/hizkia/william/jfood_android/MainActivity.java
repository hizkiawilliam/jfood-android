package hizkia.william.jfood_android;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import android.app.AlertDialog;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    ExpandableListAdapter listAdapter;
    ExpandableListView expListView;
    FloatingActionButton btnfoodCart;
    private ArrayList<Seller> listSeller = new ArrayList<>();
    private ArrayList<Food> foodIdList = new ArrayList<>();
    private ArrayList<Food> foodCart = new ArrayList<>();
    private HashMap<Seller, ArrayList<Food>> childMapping = new HashMap<>();
    private static int currentUserId;
    private static String currentUserName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Bundle extras = getIntent().getExtras();
        if(extras!=null){
            currentUserId = extras.getInt("currentUserId");
            currentUserName = extras.getString("currentUserName");
        }

        expListView = findViewById(R.id.lvExp);
        btnfoodCart = findViewById(R.id.foodCart);
        Button btnPesanan = findViewById(R.id.btnPesanan);
        refreshList();

        btnPesanan.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, SelesaiPesananActivity.class);
                intent.putExtra("currentUserId", currentUserId);
                intent.putExtra("currentUserName",currentUserName);
                startActivity(intent);
            }
        });

        expListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView expandableListView, View view, int i, int i1, long l) {

                Food food = childMapping.get(listSeller.get(i)).get(i1);
                foodCart.add(food);
                Toast.makeText(MainActivity.this, "1 x "+food.getName()+" added to cart", Toast.LENGTH_SHORT).show();
                return true;
            }
        });

        btnfoodCart.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                AlertDialog.Builder mBuilder = new AlertDialog.Builder(MainActivity.this);
                View mView = getLayoutInflater().inflate(R.layout.cart_items,null);

                ListView listView;
                Button order = mView.findViewById(R.id.foodCartOrder);
                listView = (ListView) mView.findViewById(R.id.foodCartList);

                final ArrayList<String> foodName = new ArrayList<>();
                for(Food foods:foodCart){
                    foodName.add(foods.getName() + ", Rp. " + foods.getPrice() + ", " + foods.getCategory());
                }

                ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(MainActivity.this,android.R.layout.simple_list_item_1,foodName);
                listView.setAdapter(arrayAdapter);

                mBuilder.setView(mView);
                AlertDialog dialog = mBuilder.create();
                dialog.show();

                order.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Intent intent = new Intent(MainActivity.this, BuatPesananActivity.class);
                        intent.putExtra("currentUserId", currentUserId);
                        intent.putExtra("currentUserName",currentUserName);
                        intent.putExtra("foodCart",foodName);
                        int foodTotalPrice = 0;
                        ArrayList<Integer> foodsId = new ArrayList<>();
                        for (Food foods:foodCart){
                            foodTotalPrice += foods.getPrice();
                            foodsId.add(foods.getId());
                        }
                        intent.putExtra("totalPrice",foodTotalPrice);
                        intent.putExtra("foodsId",foodsId);
                        startActivity(intent);
                    }
                });
            }
        });


//        btnPesanan.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(MainActivity.this, SelesaiPesananActivity.class);
//                startActivity(intent);
//            }
//        });
    }

    protected void refreshList() {
        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray jsonResponse = new JSONArray(response);
                    for (int i=0; i<jsonResponse.length(); i++) {

                        JSONObject food = jsonResponse.getJSONObject(i);
                        JSONObject seller = food.getJSONObject("seller");
                        JSONObject location = seller.getJSONObject("location");

                        Location newLocation = new Location(
                                location.getString("province"),
                                location.getString("description"),
                                location.getString("city")
                        );

                        Seller newSeller = new Seller(
                                seller.getInt("id"),
                                seller.getString("name"),
                                seller.getString("email"),
                                seller.getString("phoneNumber"),
                                newLocation
                        );

                        Log.e("SELLER", seller.getString("name"));

                        Food newFood = new Food(
                                food.getInt("id"),
                                food.getString("name"),
                                newSeller,
                                food.getInt("price"),
                                food.getString("category")
                        );

                        foodIdList.add(newFood);

                        //Check if the Supplier already Exists
                        boolean tempStatus = true;
                        for(Seller sellerPtr : listSeller) {
                            if(sellerPtr.getId() == newSeller.getId()){
                                tempStatus = false;
                            }
                        }
                        if(tempStatus==true){
                            listSeller.add(newSeller);
                        }
                    }

                    for(Seller sellerPtr : listSeller){
                        ArrayList<Food> tempFoodList = new ArrayList<>();
                        for(Food foodPtr : foodIdList){
                            if(foodPtr.getSeller().getId() == sellerPtr.getId()){
                                tempFoodList.add(foodPtr);
                            }
                        }
                        childMapping.put(sellerPtr, tempFoodList);
                    }

                    Log.e("SELLER", listSeller.toString());

                    listAdapter = new MainListAdapter(MainActivity.this, listSeller, childMapping);
                    expListView.setAdapter(listAdapter);
                }
                catch (JSONException e) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                    builder.setMessage("Load Data Failed.").create().show();
                }
            }
        };

        MenuRequest menuRequest = new MenuRequest(responseListener);
        RequestQueue queue = Volley.newRequestQueue(MainActivity.this);
        queue.add(menuRequest);
    }
}
