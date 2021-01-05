package com.example.cateringapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class AdapterProducts extends RecyclerView.Adapter<AdapterProducts.MyViewHolder> {

    List<Product> data;
    ItemClicked itemClicked;

    public AdapterProducts(List<Product> data, ItemClicked itemClicked) {
        this.data = data;
        this.itemClicked = itemClicked;
    }
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_product, parent, false);
        return new MyViewHolder(v);
    }
a
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.tvName.setText(data.get(position).getProductName());
        String price = data.get(position).getProductPrice() + "";
        holder.tvPrice.setText(price);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView tvName, tvPrice;
        ConstraintLayout layout;
        int position;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvName);
            tvPrice = itemView.findViewById(R.id.tvPrice);
            layout = itemView.findViewById(R.id.layoutProduct);

            layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    itemClicked.OnItemClicked(position);
                }
            });
        }
    }
}
