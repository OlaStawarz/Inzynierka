package com.example.myapp.Recipes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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
        Bundle bundle = intent.getExtras();
        assert bundle != null;
        String name = bundle.getString("name");
        String imageUrl = bundle.getString("image");

        textView.setText(name);
        Picasso.with(this)
                .load(imageUrl)
                .centerCrop()
                .fit()
                .into(imageView);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_recipe, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.edit:
                Toast.makeText(RecipeDetail.this, "Edycja", Toast.LENGTH_LONG).show();
            case R.id.delete:
                Toast.makeText(RecipeDetail.this, "Usuwanie", Toast.LENGTH_LONG).show();
        }
        return super.onOptionsItemSelected(item);
    }
}