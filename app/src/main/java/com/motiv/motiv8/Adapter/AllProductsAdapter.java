package com.motiv.motiv8.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.helper.widget.Layer;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.motiv.motiv8.R;
import com.motiv.motiv8.model.AllProductResponse;
import com.motiv.motiv8.model.ProductDetail;

import java.util.List;

import retrofit2.Callback;

public class AllProductsAdapter extends RecyclerView.Adapter<AllProductsAdapter.AllProductViewHolder> {
    Context  context;
    List<ProductDetail> products;
    public AllProductsAdapter(Context  context, List<ProductDetail> products) {
        this.context=context;
        this.products=products;
    }

    @NonNull
    @Override
    public AllProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.product_item_card,parent,false);
        return new AllProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AllProductViewHolder holder, int position) {
        holder.txtProductName.setText(products.get(position).getProductName());
        holder.txtCategory.setText(products.get(position).getStrCategoryName());
        holder.txtRate.setText("₹"+products.get(position).getMrp());
        Glide.with(context).load(products.get(position).getThumbnail().toString().trim()).into(holder.imageViewProduct);

    }

    @Override
    public int getItemCount() {
        return products.size();
    }

    public class AllProductViewHolder extends RecyclerView.ViewHolder {
        TextView txtProductName,txtCategory,txtRate;
        ImageView imageViewProduct;
        public AllProductViewHolder(@NonNull View itemView) {
            super(itemView);
            txtProductName=itemView.findViewById(R.id.txtProductName);
            txtCategory=itemView.findViewById(R.id.txtCategory);
            txtRate=itemView.findViewById(R.id.txtRate);
            imageViewProduct=itemView.findViewById(R.id.imageViewProduct);
        }
    }
}
