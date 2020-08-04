package com.example.myapp.Recipes;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.myapp.R;
import com.squareup.picasso.Picasso;

public class RecipeDetail extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recipe_detail_item);

        TextView textView = findViewById(R.id.textViewRecipeNameDetail);
        ImageView imageView = findViewById(R.id.imageViewRecipeImageDetail);

        Intent intent = getIntent();
        final Bundle bundle = intent.getExtras();
        String name = bundle.getString("name");
        String imageUrl = bundle.getString("image");

        textView.setText(name);
        Picasso.with(this)
                .load(imageUrl)
                .centerCrop()
                .fit()
                .into(imageView);

    }
}