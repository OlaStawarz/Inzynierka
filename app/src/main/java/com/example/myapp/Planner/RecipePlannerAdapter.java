package com.example.myapp.Planner;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapp.R;
import com.example.myapp.Recipes.RecipeDetail;
import com.example.myapp.Recipes.RecipeModel;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class RecipePlannerAdapter extends RecyclerView.Adapter<RecipePlannerAdapter.RecipePlannerViewHolder> {

    private Context context;
    private List<RecipeModel> recipes;
    private OnButtonClickListener onButtonClickListener;

    public RecipePlannerAdapter(Context context, List<RecipeModel> recipes) {
        this.context = context;
        this.recipes = recipes;
    }

    @NonNull
    @Override
    public RecipePlannerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.recipe_planner_item, parent, false);
        return new RecipePlannerViewHolder(v, onButtonClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull RecipePlannerViewHolder holder, int position) {
        final RecipeModel recipeModel = recipes.get(position);
        holder.textView.setText(recipeModel.getName());
        Picasso.with(context)
                .load(recipeModel.getImageUrl())
                .placeholder(R.mipmap.ic_launcher)
                .fit()
                //.centerCrop()
                .into(holder.imageView);

        /*holder.btnShowDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, RecipeDetail.class);
                Bundle bundle = new Bundle();
                bundle.putString("name", recipeModel.getName());
                bundle.putString("image", recipeModel.getImageUrl());
                bundle.putStringArrayList("ingredients", recipeModel.getIngredients());
                bundle.putString("description", recipeModel.getDescription());
                bundle.putString("link", recipeModel.getLink());
                bundle.putString("key", recipeModel.getLink());
                intent.putExtras(bundle);
                context.startActivity(intent);
            }
        });*/
    }

    @Override
    public int getItemCount() {
        return recipes.size();
    }

    public void filterRecipeListPlanner(ArrayList<RecipeModel> filteredRecipes) {
        recipes = filteredRecipes;
        notifyDataSetChanged();
    }

    public static class RecipePlannerViewHolder extends RecyclerView.ViewHolder  {

        public TextView textView;
        public ImageView imageView;
        Button btnChoose, btnShowDetail;

        public RecipePlannerViewHolder(@NonNull View itemView, final OnButtonClickListener onButtonClickListener) {
            super(itemView);

            textView = itemView.findViewById(R.id.textViewRecipeNameBreakfastPlanner);
            imageView = itemView.findViewById(R.id.imageViewRecipeImageBreakfastPlanner);
            btnChoose = itemView.findViewById(R.id.buttonChooseRecipePlanner);
            btnShowDetail = itemView.findViewById(R.id.buttonSeeRecipePlanner);

            btnChoose.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (onButtonClickListener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            onButtonClickListener.onItemChoose(position);
                        }
                    }
                }
            });

            btnShowDetail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (onButtonClickListener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            onButtonClickListener.onItemShow(position);
                        }
                    }
                }
            });

        }

    }

    public interface OnButtonClickListener {
        void onItemChoose (int position);
        void onItemShow (int position);
    }

    public void setOnButtonClickListener(OnButtonClickListener onButtonClickListener) {
        this.onButtonClickListener = onButtonClickListener;
    }
}
