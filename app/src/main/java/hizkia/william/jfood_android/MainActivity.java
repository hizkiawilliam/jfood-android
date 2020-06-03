package hizkia.william.jfood_android;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;

import android.app.AlertDialog;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    ExpandableListAdapter listAdapter;
    ExpandableListView expListView;
    FloatingActionButton btnFoodCart;
    DrawerLayout drawerLayout;
    private ArrayList<Seller> listSeller = new ArrayList<>();
    private ArrayList<Food> foodIdList = new ArrayList<>();
    private ArrayList<Food> foodCart = new ArrayList<>();
    private HashMap<Seller, ArrayList<Food>> childMapping = new HashMap<>();
    private static int currentUserId;
    private static String currentUserName;
    ActionBarDrawerToggle mToggle;

    /**
     * Method that will be executed on create
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Bundle extras = getIntent().getExtras();
        if(extras!=null){
            currentUserId = extras.getInt("currentUserId");
            currentUserName = extras.getString("currentUserName");
        }

        //==============================================================================
        //Set drawer layout for navigation bar
        //==============================================================================
        drawerLayout = findViewById(R.id.main_act);
        mToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close);
        drawerLayout.addDrawerListener(mToggle);
        mToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //==============================================================================
        //Set declaration respond for expendable list on clicked
        //==============================================================================
        expListView = findViewById(R.id.lvExp);
        btnFoodCart = findViewById(R.id.foodCart);
        refreshList();

        expListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView expandableListView, View view, int i, int i1, long l) {

                Food food = childMapping.get(listSeller.get(i)).get(i1);
                foodCart.add(food);
                Toast.makeText(MainActivity.this, "1 x "+food.getName()+" added to cart", Toast.LENGTH_SHORT).show();
                return true;
            }
        });

        //==============================================================================
        //Respond when button food cart is pressed
        //==============================================================================
        btnFoodCart.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){

                if(foodCart.isEmpty()) {
                    Toast.makeText(MainActivity.this, "Your cart is empty", Toast.LENGTH_SHORT).show();
                }
                else {

                    AlertDialog.Builder mBuilder = new AlertDialog.Builder(MainActivity.this);
                    View mView = getLayoutInflater().inflate(R.layout.cart_items, null);

                    ListView listView;
                    Button order = mView.findViewById(R.id.foodCartOrder);
                    Button clear = mView.findViewById(R.id.foodCartClear);
                    listView = (ListView) mView.findViewById(R.id.foodCartList);

                    final ArrayList<String> foodName = new ArrayList<>();
                    for (Food foods : foodCart) {
                        foodName.add("\n"+"Food name\t\t\t: " + foods.getName() +
                                "\nFood Price\t\t\t\t: Rp. " + foods.getPrice() +
                                "\nFood Category\t: " + foods.getCategory()+"\n");
                    }

                    ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(MainActivity.this, android.R.layout.simple_list_item_1, foodName);
                    listView.setAdapter(arrayAdapter);

                    mBuilder.setView(mView);
                    AlertDialog dialog = mBuilder.create();
                    dialog.show();

                    //When button order inside food cart is pressed
                    order.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            Intent intent = new Intent(MainActivity.this, BuatPesananActivity.class);
                            intent.putExtra("currentUserId", currentUserId);
                            intent.putExtra("currentUserName", currentUserName);
                            intent.putExtra("foodCart", foodName);
                            int foodTotalPrice = 0;
                            ArrayList<Integer> foodsId = new ArrayList<>();
                            for (Food foods : foodCart) {
                                foodTotalPrice += foods.getPrice();
                                foodsId.add(foods.getId());
                            }
                            intent.putExtra("totalPrice", foodTotalPrice);
                            intent.putExtra("foodsId", foodsId);
                            startActivity(intent);
                        }
                    });

                    //When button clear on food cart is pressed
                    clear.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            Intent intent = new Intent(MainActivity.this, MainActivity.class);
                            Toast.makeText(MainActivity.this, "Your cart has been cleared", Toast.LENGTH_SHORT).show();
                            intent.putExtra("currentUserId", currentUserId);
                            intent.putExtra("currentUserName", currentUserName);
                            startActivity(intent);
                        }
                    });
                }
            }
        });
    }

    /**
     * Method to refresh expendable list of sellers and foods
     */
    protected void refreshList() {
        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray jsonResponse = new JSONArray(response);

                    //Read all array list of foods, then re-sort it based on seller
                    //Assign it on expandable list
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

    /**
     * Method to control navigation bar
     * @param menuItem variable of objects selected in navigation bar
     */
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        Intent intent;
        switch (menuItem.getItemId()){
            case R.id.nav_ongoingInvoice:
                intent = new Intent(MainActivity.this, SelesaiPesananActivity.class);
                intent.putExtra("currentUserId", currentUserId);
                intent.putExtra("currentUserName", currentUserName);
                startActivity(intent);
                break;
            case R.id.nav_promoList:
                intent = new Intent(MainActivity.this, PromoActivity.class);
                intent.putExtra("currentUserId", currentUserId);
                intent.putExtra("currentUserName", currentUserName);
                startActivity(intent);
                break;
            case R.id.nav_historyInvoice:
                intent = new Intent(MainActivity.this, InvoiceHistory.class);
                intent.putExtra("currentUserId", currentUserId);
                intent.putExtra("currentUserName", currentUserName);
                startActivity(intent);
                break;
            case R.id.nav_logout:
                intent = new Intent(MainActivity.this, LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                break;
            case R.id.nav_profile:
                intent = new Intent(MainActivity.this, ProfileActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra("currentUserId", currentUserId);
                intent.putExtra("currentUserName", currentUserName);
                startActivity(intent);
                break;
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    /**
     * Method to control button to trigger the navigation bar
     * @param item variable of objects selected in navigation bar
     */
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (mToggle.onOptionsItemSelected(item)){
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
