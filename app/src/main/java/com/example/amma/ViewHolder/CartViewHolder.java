package com.example.amma.ViewHolder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import com.example.amma.ItemClickListener;

import android.widget.ImageView;
import android.widget.TextView;

import com.example.amma.R;

public class CartViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
{
    public TextView txtProductName, txtProductQuantity;
    private ItemClickListener itemClickListener;
    public ImageView imageView;


    public CartViewHolder(View itemView)
    {
        super(itemView);

        txtProductName = itemView.findViewById(R.id.cart_product_name);
        imageView = (ImageView) itemView.findViewById(R.id.product_image);
        txtProductQuantity = itemView.findViewById(R.id.cart_product_quantity);
    }

    @Override
    public void onClick(View view)
    {
        itemClickListener.onClick(view, getAdapterPosition(), false);
    }

    public void setItemClickListner(ItemClickListener itemClickListner)
    {
        this.itemClickListener = itemClickListner;
    }
}