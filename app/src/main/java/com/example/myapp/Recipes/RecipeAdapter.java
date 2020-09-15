package com.example.myapp.Recipes;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapp.R;
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
    private OnFavouriteButtonClickListener onFavouriteButtonClickListener;
    //ListView listView;

    public RecipeAdapter(Context context, List<RecipeModel> recipes, OnItemClickedListener onItemClickedListener,
                         OnFavouriteButtonClickListener onFavouriteButtonClickListener) {
        this.context = context;
        this.recipes = recipes;
        this.onItemClickedListener = onItemClickedListener;
        this.onFavouriteButtonClickListener = onFavouriteButtonClickListener;
    }

    @NonNull
    @Override
    public RecipeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.recipe_item, parent, false);
        return new RecipeViewHolder(v, onItemClickedListener, onFavouriteButtonClickListener);
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

       /* for (int i = 0; i < recipeModel.getIngredients().size(); i++) {
            String ingredient = recipeModel.getIngredients().get(i);
            displayIngredients += "\n" + ingredient;
        }
        holder.ingredients.setText(displayIngredients);
        holder.description.setText(recipeModel.getDescription());*/
        //holder.link.setText(recipeModel.getLink());

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

        OnFavouriteButtonClickListener onFavouriteButtonClickListener;
        public TextView recipeName, description, link, ingredients;
        public ImageView imageView, addToFavouriteImageView;
        OnItemClickedListener onItemClickedListener;
        boolean isFavourite = false;
        DatabaseReference databaseReference;


        public RecipeViewHolder(@NonNull View itemView, OnItemClickedListener onItemClickedListener,
                                final OnFavouriteButtonClickListener onFavouriteButtonClickListener) {
            super(itemView);
            recipeName = itemView.findViewById(R.id.textViewRecipeNameBreakfast);
            imageView = itemView.findViewById(R.id.imageViewRecipeImageBreakfast);
            addToFavouriteImageView = itemView.findViewById(R.id.imageViewAddToFavourite);
            this.onItemClickedListener = onItemClickedListener;
            this.onFavouriteButtonClickListener = onFavouriteButtonClickListener;
            databaseReference = FirebaseDatabase.getInstance().getReference("Recipes");

            addToFavouriteImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if (onFavouriteButtonClickListener != null) {
                        int position = getAdapterPosition();
                        RecipeModel recipeModel = recipes.get(position);
                        if (position != RecyclerView.NO_POSITION) {
                            if (!isFavourite) {
                                addToFavouriteImageView.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_favorite));
                                databaseReference.child(recipeModel.getRecipeKey()).child("favourite").setValue("true");
                                isFavourite = true;
                            } else {
                                addToFavouriteImageView.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_favorite_add));
                                databaseReference.child(recipeModel.getRecipeKey()).child("favourite").setValue("false");
                                isFavourite = false;
                            }
                            /*boolean isFavourite = false;
                            //Drawable d = addToFavouriteImageView.getDrawable();
                            //Drawable b = context.getResources().getDrawable(R.drawable.ic_favorite_add);
                            if (isFavourite) {

                            } else {
                                addToFavouriteImageView.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_favorite_add));
                            }*/
                            onFavouriteButtonClickListener.addToFavourite(position);
                        }
                    }
                }
            });

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            onItemClickedListener.itemClicked(getAdapterPosition());
        }


        // public void OnClick()
    }

    public interface OnItemClickedListener {
        void itemClicked (int position);
    }

    public interface OnFavouriteButtonClickListener {
        void addToFavourite (int position);
    }

    public void setOnButtonClickListener (OnFavouriteButtonClickListener onButtonClickListener) {
        this.onFavouriteButtonClickListener = onButtonClickListener;
    }

    /*public void setOnItemClickListener(OnItemClickedListener listener) {

    }*/
}
