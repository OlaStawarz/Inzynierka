package com.example.myapp.Recipes;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapp.R;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import static com.example.myapp.R.drawable.ic_favorite;
import static com.example.myapp.R.drawable.ic_favorite_add;

public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.RecipeViewHolder> {

    private Context context;
    private List<RecipeModel> recipes;
    private OnItemClickedListener onItemClickedListener;

    public RecipeAdapter(Context context, List<RecipeModel> recipes, OnItemClickedListener onItemClickedListener) {
        this.context = context;
        this.recipes = recipes;
        this.onItemClickedListener = onItemClickedListener;
    }

    @NonNull
    @Override
    public RecipeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.recipe_item, parent, false);
        return new RecipeViewHolder(v, onItemClickedListener);
    }

    @Override
    public void onBindViewHolder(@NonNull RecipeViewHolder holder, int position) {
        // String displayIngredients = "";

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

    public void filterRecipeList(ArrayList<RecipeModel> filteredRecipes) {
        recipes = filteredRecipes;
        notifyDataSetChanged();
    }

    public class RecipeViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView recipeName, link, ingredients;
        public ImageView imageView, addToFavouriteImageView;
        OnItemClickedListener onItemClickedListener;
        boolean isFavourite;
        DatabaseReference databaseReference;

        public RecipeViewHolder(@NonNull View itemView, OnItemClickedListener onItemClickedListener) {
            super(itemView);
            recipeName = itemView.findViewById(R.id.textViewRecipeNameBreakfast);
            imageView = itemView.findViewById(R.id.imageViewRecipeImageBreakfast);
            this.onItemClickedListener = onItemClickedListener;
            databaseReference = FirebaseDatabase.getInstance().getReference("Recipes");

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            onItemClickedListener.itemClicked(getAdapterPosition());
        }

    }

    public interface OnItemClickedListener {
        void itemClicked (int position);
    }


}