package com.example.myapp.Recipes;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapp.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class HorizontalRecipeAdapter extends RecyclerView.Adapter<HorizontalRecipeAdapter.RecipeViewHolder> {

    private Context context;
    private List<RecipeModel> recipes;
    private OnHorizontalItemClickedListener onHorizontalItemClickedListener;

    public HorizontalRecipeAdapter(Context context, List<RecipeModel> recipes,
                                   OnHorizontalItemClickedListener onHorizontalItemClickedListener) {
        this.context = context;
        this.recipes = recipes;
        this.onHorizontalItemClickedListener = onHorizontalItemClickedListener;
    }

    @NonNull
    @Override
    public RecipeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.horizontal_recipe_item, parent, false);
        return new RecipeViewHolder(v, onHorizontalItemClickedListener);
    }

    @Override
    public void onBindViewHolder(@NonNull RecipeViewHolder holder, int position) {
        RecipeModel recipeModel = recipes.get(position);
        holder.recipeName.setText(recipeModel.getName());
        Picasso.with(context)
                .load(recipeModel.getImageUrl())
                .placeholder(R.mipmap.ic_launcher)
                .fit()
                .centerCrop()
                .into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return recipes.size();
    }

    public class RecipeViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView recipeName;
        public ImageView imageView;
        OnHorizontalItemClickedListener onHorizontalItemClickedListener;

        public RecipeViewHolder(@NonNull View itemView, OnHorizontalItemClickedListener onHorizontalItemClickedListener) {
            super(itemView);

            recipeName = itemView.findViewById(R.id.textViewHorizontalRecipeName);
            imageView = itemView.findViewById(R.id.imageViewHorizontal);
            this.onHorizontalItemClickedListener = onHorizontalItemClickedListener;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            onHorizontalItemClickedListener.horizontalItemClicked(getAdapterPosition());
        }
    }

    public interface OnHorizontalItemClickedListener {
        void horizontalItemClicked (int position);
    }


}
