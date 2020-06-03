package hizkia.william.jfood_android;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ProfileActivity extends AppCompatActivity {

    private static int currentUserId;
    private static String currentUserName;
    private static String currentUserEmail;
    private static String currentUserJoinDate;

    /**
     * Method to be executed on create
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        final TextView tvUserId = findViewById(R.id.profileUserId);
        final TextView tvUserName = findViewById(R.id.profileUserName);
        final TextView tvUserEmail = findViewById(R.id.profileUserEmail);
        final TextView tvUserJoinDate = findViewById(R.id.profileUserJoinDate);

        Bundle extras = getIntent().getExtras();
        if(extras!=null){
            currentUserId = extras.getInt("currentUserId");
        }

        final Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject customer = new JSONObject(response);
                    currentUserName = customer.getString("name");
                    currentUserEmail = customer.getString("email");
                    currentUserJoinDate = customer.getString("joinDate").substring(0,10);
                    tvUserId.setText("User Id\t\t\t\t\t\t: "+currentUserId);
                    tvUserName.setText("User Name\t\t\t: "+currentUserName);
                    tvUserEmail.setText("User Email\t\t\t: "+currentUserEmail);
                    tvUserJoinDate.setText("User Join Date\t: "+currentUserJoinDate);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };
        ProfileRequest request = new ProfileRequest(currentUserId,responseListener);
        RequestQueue queue = new Volley().newRequestQueue(ProfileActivity.this);
        queue.add(request);
    }
}
