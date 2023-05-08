package com.example.customerapp;

import static android.content.Context.MODE_PRIVATE;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class RecyclerViewCartAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {
    ArrayList<ListCart> listdata;
    Context context;
    String removeUrl = "http://172.20.10.5/h_and_h-food-delivery-app/public/api/users/cart/remove";
    String addUrl = "http://172.20.10.5/h_and_h-food-delivery-app/public/api/users/cart/add";
    SharedPreferences sharedPreferences;

    public RecyclerViewCartAdapter(ArrayList<ListCart> listdata) {
        this.listdata = listdata;
    }

    @NonNull
    @Override
    public RecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        sharedPreferences = context.getSharedPreferences("CustomerApp", MODE_PRIVATE);
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem = layoutInflater.inflate(R.layout.list_cart, parent, false);
        return new RecyclerViewAdapter.ViewHolder(listItem);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(RecyclerViewAdapter.ViewHolder holder, int position) {
        ListCart list = listdata.get(position);
        holder.textViewTitle.setText(list.getName() + " - $" + list.getPrice());
        holder.textViewDes.setText("Quantity: " + list.getNumItem());
        Picasso.get().load(list.getImg()).into(holder.imageView);

        holder.buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.setEnabled(false);
                sendRequest(v, addUrl + "?item_id="+ list.getId() + "&user_email="+sharedPreferences.getString("email", ""));
            }
        });

        holder.buttonRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.setEnabled(false);
                sendRequest(v, removeUrl + "?item_id="+ list.getId() + "&user_email="+sharedPreferences.getString("email", ""));            }
        });
    }


    @Override
    public int getItemCount() {
        return listdata.toArray().length;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView imageView;
        public ImageButton buttonAdd, buttonRemove;
        public TextView textViewTitle, textViewDes;
        public CardView cardView;

        int numItem = 0;

        public ViewHolder(View itemView) {
            super(itemView);
            this.imageView = itemView.findViewById(R.id.imageView);
            this.textViewTitle = itemView.findViewById(R.id.textViewTitle);
            this.textViewDes = itemView.findViewById(R.id.textViewDes);
            this.buttonAdd = itemView.findViewById(R.id.add_item);
            this.buttonRemove = itemView.findViewById(R.id.remove_item);
            cardView = itemView.findViewById(R.id.cardView);
        }
    }

    public void sendRequest(View v, String apiUrl) {
        RequestQueue queue = Volley.newRequestQueue(context);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, apiUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                v.setEnabled(true);
                if (response.equals("success")) {
                    Toast.makeText(context, "Operation success", Toast.LENGTH_SHORT).show();
                } else
                    Toast.makeText(context, "Operation failed", Toast.LENGTH_SHORT).show();
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
}
