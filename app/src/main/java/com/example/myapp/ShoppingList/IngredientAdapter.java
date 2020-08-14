package com.example.myapp.ShoppingList;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapp.R;

import java.util.ArrayList;

public class IngredientAdapter extends RecyclerView.Adapter<IngredientAdapter.ShoppingListViewHolder> {

    private ArrayList<IngredientModel> items;
    private ItemClickedListener onItemClickListener;

    public static class ShoppingListViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView itemName, itemUnit, itemAmount;
        ItemClickedListener onItemClickListener;

        public ShoppingListViewHolder(@NonNull View itemView, ItemClickedListener onItemClickListener) {
            super(itemView);
            itemName = itemView.findViewById(R.id.textViewItemName);
            itemUnit = itemView.findViewById(R.id.textViewItemUnit);
            itemAmount = itemView.findViewById(R.id.textViewItemAmount);
            this.onItemClickListener = onItemClickListener;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            onItemClickListener.itemClicked(getAdapterPosition());
        }
    }

    public IngredientAdapter(ArrayList<IngredientModel> items, ItemClickedListener onItemClickListener) {
        this.items = items;
        this.onItemClickListener = onItemClickListener;

    }

    @NonNull
    @Override
    public ShoppingListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.shopping_list_item, parent, false);
        return new ShoppingListViewHolder(v, onItemClickListener);
    }


    @Override
    public void onBindViewHolder(@NonNull ShoppingListViewHolder holder, int position) {
        IngredientModel currentItem = items.get(position);

        holder.itemName.setText(currentItem.getName());
        holder.itemAmount.setText(String.valueOf(currentItem.getAmount()));
        holder.itemUnit.setText(currentItem.getUnit());
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public interface ItemClickedListener {
        void itemClicked (int position);
    }

}
