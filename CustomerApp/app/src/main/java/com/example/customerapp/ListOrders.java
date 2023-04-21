package com.example.customerapp;

import static android.content.ContentValues.TAG;
import static androidx.core.content.ContentProviderCompat.requireContext;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.text.format.Formatter;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

public class ListOrders extends AppCompatActivity {
    TextView textViewOrders;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_orders);

        ActionBar actionBar = getSupportActionBar();

        // showing the back button in action bar
        actionBar.setDisplayHomeAsUpEnabled(true);

        textViewOrders = findViewById(R.id.textOrders);
        sharedPreferences = getSharedPreferences("CustomerApp", MODE_PRIVATE);
        textViewOrders.setMovementMethod(new ScrollingMovementMethod());
        fetchData("http://172.20.10.5/h_and_h-food-delivery-app/public/api/users/orders/list?email="
                +sharedPreferences.getString("email", ""));
        getLocalIpAddress();
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

    public String getLocalIpAddress() {
        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements();) {
                NetworkInterface intf = en.nextElement();
                for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements();) {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress()) {
                        String ip = Formatter.formatIpAddress(inetAddress.hashCode());
                        Log.i(TAG, "***** IP="+ ip);
                        return ip;
                    }
                }
            }
        } catch (SocketException ex) {
            Log.e(TAG, ex.toString());
        }
        return null;
    }
    public void parseJSON(String response){
        try {
            JSONArray jsonArray = new JSONArray(response);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject stu = jsonArray.getJSONObject(i);
                String id = stu.getString("id");
                String created_at = stu.getString("created_at");
                String status = stu.getString("status");
                String item_details = stu.getString("item_details");
                JSONArray jsonArrayItem = new JSONArray(item_details);
                textViewOrders.append("Order placed on: " + created_at + "\nStatus: " + status + "\n");
                for (int j = 0; j < jsonArrayItem.length(); j++) {
                    JSONObject jsonObjectItem = jsonArrayItem.getJSONObject(j);
                    textViewOrders.append("Items: \n" + jsonObjectItem.getString("name") + "\nPrice: "
                            + jsonObjectItem.getString("price") + "\n");
                }
                textViewOrders.append("\n\n");
            }
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

    public void fetchData(String apiUrl) {
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        StringRequest stringRequest = new StringRequest(Request.Method.GET, apiUrl,
                new Response.Listener<String>() {
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