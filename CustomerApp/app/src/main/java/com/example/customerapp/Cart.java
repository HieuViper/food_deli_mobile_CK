package com.example.customerapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Cart extends AppCompatActivity {
    DrawerLayout drawerLayout;
    ImageView menu;
    LinearLayout home, cart, address, order, logout;
    ArrayList<ListCart> arrayList1;
    TextView textViewCartData, textViewDisDur;
    ImageView imageView;
    SharedPreferences sharedPreferences;
    Button buttonConfirm, buttonRemove;

    int pricePerKM = 5;
    String urlConfirm =
            "http://172.20.10.5/h_and_h-food-delivery-app/public/api/users/cart/confirm?user_email=";

    String urlRemove =
            "http://172.20.10.5/h_and_h-food-delivery-app/public/api/users/cart/clear?user_email=";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        getSupportActionBar().hide();

        sharedPreferences = getSharedPreferences("CustomerApp", MODE_PRIVATE);


        drawerLayout = findViewById(R.id.drawerLayout);
        menu= findViewById(R.id.menu);
        home= findViewById(R.id.home);
        cart= findViewById(R.id.cart);
        address= findViewById(R.id.address);
        order= findViewById(R.id.order);
        logout = findViewById(R.id.logout);

        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDrawer(drawerLayout);
            }
        });

        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }
        });

        cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                recreate();
            }
        });

        address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), AddressDetails.class);
                startActivity(intent);
            }
        });

        order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ListOrders.class);
                startActivity(intent);
            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sharedPreferences = getSharedPreferences("CustomerApp",MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.clear();
                editor.apply();
                finish();

                Intent intent = new Intent(getApplicationContext(), Login.class);
                startActivity(intent);
            }
        });

        ActionBar actionBar = getSupportActionBar();


        actionBar.setDisplayHomeAsUpEnabled(true);

        textViewCartData = findViewById(R.id.textCartData);
        textViewDisDur = findViewById(R.id.textDisDur);
        buttonConfirm = findViewById(R.id.btnConfirmOrder);
        buttonRemove = findViewById(R.id.btnClearCart);
//        imageView = findViewById(R.id.imageItem);

        arrayList1 = new ArrayList<>();

        fetchData();
        buttonConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                sendRequest(view, urlConfirm + sharedPreferences.getString("email", ""));
            }
        });

        buttonRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendRequest(view, urlRemove + sharedPreferences.getString("email", ""));
            }
        });
    }

    public static void openDrawer(DrawerLayout drawerLayout) {
        drawerLayout.openDrawer(GravityCompat.START);
    }
    public static void closeDrawer(DrawerLayout drawerLayout) {
        if(drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START);
        }
    }
    @Override
    protected void onPause() {
        super.onPause();
        closeDrawer(drawerLayout);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void sendRequest(View v, String apiUrl) {
        Log.e("url", apiUrl);
        v.setEnabled(false);
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        StringRequest stringRequest = new StringRequest(Request.Method.GET, apiUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                v.setEnabled(true);
                if (response.equals("success")) {
                    Toast.makeText(getApplicationContext(), "Operation success", Toast.LENGTH_SHORT).show();
                    finish();
                } else
                    Toast.makeText(getApplicationContext(), "Operation failed", Toast.LENGTH_SHORT).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                v.setEnabled(true);
                error.printStackTrace();
            }
        });
        queue.add(stringRequest);
    }

    public void parseJSON(String data) {
        try {
            JSONArray jsonArray = new JSONArray(data);
            JSONObject jsonObject = jsonArray.getJSONObject(0);
            JSONArray jsonArray1 = jsonObject.getJSONArray("cart");
            for (int i = 0; i < jsonArray1.length(); i++) {
                JSONObject stu = jsonArray1.getJSONObject(i);
                String id = stu.getString("id");
                String name = stu.getString("name");
                String des = stu.getString("description");
                String numItem = stu.getString("numItem");
                String price = stu.getString("price");
                String img = stu.getString("image");

                textViewCartData.append("Item: " + name + "\nPrice: " + price + "$\nNumber of Item: " + numItem + "\n\n");
                arrayList1.add(new ListCart(id, name, des, img, price, numItem));
//                Picasso.get().load(imageURL).into(imageView);

            }
            @SuppressLint("WrongViewCast")
            RecyclerView recyclerView = findViewById(R.id.recyclerViewCart);
            RecyclerViewCartAdapter adapter = new RecyclerViewCartAdapter(arrayList1);
            recyclerView.setHasFixedSize(true);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            recyclerView.setAdapter(adapter);


            String distance = String.valueOf(jsonObject.getInt("distance") / 1000);
            String duration = String.valueOf((jsonObject.getInt("duration") / 60));
            int total_price = Integer.parseInt(distance) * pricePerKM;
            textViewDisDur.setText("Distance: " + distance + " KM"+ " (5$/KM) "+"\nDelivery Fee: "
                    + total_price + " $" + "\nDuration: " + duration + "Minutes");
        } catch (JSONException e) {
            Log.e("error", e.getLocalizedMessage());
            e.printStackTrace();
        }
    }

    public void fetchData() {
//        Log.i("email", sharedPreferences.getString("email",""));
        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.GET,
                "http://172.20.10.5/h_and_h-food-delivery-app/public/api/users/cart/list?user_email="
                        + sharedPreferences.getString("email", ""), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                parseJSON(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });
        queue.add(stringRequest);
    }
}