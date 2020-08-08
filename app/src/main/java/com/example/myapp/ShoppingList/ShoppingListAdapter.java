package com.example.myapp.ShoppingList;

import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapp.R;

import java.util.ArrayList;

public class ShoppingListAdapter extends RecyclerView.Adapter<ShoppingListAdapter.ShoppingListViewHolder> {

    private ArrayList<ItemModel> items;

    public static class ShoppingListViewHolder extends RecyclerView.ViewHolder {

        public TextView itemName, itemUnit, itemAmount;

        public ShoppingListViewHolder(@NonNull View itemView) {
            super(itemView);
            itemName = itemView.findViewById(R.id.textViewItemName);
            itemUnit = itemView.findViewById(R.id.textViewItemUnit);
            itemAmount = itemView.findViewById(R.id.textViewItemAmount);
        }
    }

    public ShoppingListAdapter(ArrayList<ItemModel> items) {
        this.items = items;
    }

    @NonNull
    @Override
    public ShoppingListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.shopping_list_item, parent, false);
        return new ShoppingListViewHolder(v);
    }


    @Override
    public void onBindViewHolder(@NonNull ShoppingListViewHolder holder, int position) {
        ItemModel currentItem = items.get(position);

        holder.itemName.setText(currentItem.getName());
        holder.itemAmount.setText(String.valueOf(currentItem.getAmount()));
        holder.itemUnit.setText(currentItem.getUnit());
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

}
