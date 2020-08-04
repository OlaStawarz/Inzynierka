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

public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.RecipeViewHolder> {

    private Context context;
    private List<RecipeModel> recipes;
    private OnItemClickedListener onItemClickedListener;
    //ListView listView;

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
        String displayIngredients = "";

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

    public class RecipeViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView recipeName, description, link, ingredients;
        public ImageView imageView;
        OnItemClickedListener onItemClickedListener;

        public RecipeViewHolder(@NonNull View itemView, OnItemClickedListener onItemClickedListener) {
            super(itemView);

            recipeName = itemView.findViewById(R.id.textViewRecipeNameBreakfast);
            imageView = itemView.findViewById(R.id.imageViewRecipeImageBreakfast);
            this.onItemClickedListener = onItemClickedListener;
            //ingredients = itemView.findViewById(R.id.textViewDisplayIngredients);
            //description = itemView.findViewById(R.id.textViewDisplayDescription);
            link = itemView.findViewById(R.id.textViewDisplayLink);

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

    /*public void setOnItemClickListener(OnItemClickedListener listener) {

    }*/
}
