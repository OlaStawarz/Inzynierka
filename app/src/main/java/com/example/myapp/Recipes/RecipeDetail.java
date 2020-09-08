package com.example.myapp.Recipes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapp.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class RecipeDetail extends AppCompatActivity implements DeleteRecipeFragment.DeleteListener{

    private DatabaseReference databaseReference, databaseReferencePlanner;
    private String key;
    ArrayList<String> days = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_detail);

        TextView textViewName = findViewById(R.id.textViewRecipeNameDetail);
        TextView textViewDescription = findViewById(R.id.textViewDisplayDescription);
        TextView textViewLink = findViewById(R.id.textViewDisplayLink);
        TextView textViewIngredients = findViewById(R.id.textViewDisplayIngredients);
        ImageView imageView = findViewById(R.id.imageViewRecipeImageDetail);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        assert bundle != null;
        String name = bundle.getString("name");
        String imageUrl = bundle.getString("image");
        String description = bundle.getString("description");
        String link = bundle.getString("link");
        ArrayList<String> ingredients = bundle.getStringArrayList("ingredients");
        key = bundle.getString("key");

        databaseReference = FirebaseDatabase.getInstance().getReference("Recipes").child(key);
        databaseReferencePlanner = FirebaseDatabase.getInstance().getReference("Planner");

       // Toast.makeText(RecipeDetail.this, description, Toast.LENGTH_LONG).show();
        textViewName.setText(name);
        textViewDescription.setText(description);
        textViewLink.setText(link);
        String ingredient = "";
        for (int i = 0; i < ingredients.size(); i++) {
            ingredient += ingredients.get(i) + "\n";
        }
        textViewIngredients.setText(ingredient);
        Picasso.with(this)
                .load(imageUrl)
                .centerCrop()
                .fit()
                .into(imageView);

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(RecipeDetail.this, "Edycja zdjęcia", Toast.LENGTH_LONG).show();
            }
        });
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
                Toast.makeText(RecipeDetail.this, key, Toast.LENGTH_LONG).show();
                openDialog();
                //databaseReference.child(key).removeValue();
        }
        return super.onOptionsItemSelected(item);
    }

    private void openDialog() {
        DeleteRecipeFragment deleteRecipeFragment = new DeleteRecipeFragment();
        deleteRecipeFragment.show(getSupportFragmentManager(), "Confirmation");
    }


    @Override
    public void confirm() {
        // testowanie usuwanie klucza też z planera
        databaseReferencePlanner.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    days.add(dataSnapshot.getKey());
                }
                for (int i = 0; i < days.size(); i++) {
                    final int finalI = i;
                    databaseReferencePlanner.child(days.get(i)).child("breakfast").addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            String plannerKey = snapshot.child("key").getValue().toString();
                            if (plannerKey.equals(key)) {
                                //databaseReferencePlanner.child(days.get(finalI)).child("breakfast").child("key").removeValue();
                                Toast.makeText(RecipeDetail.this, "Nie można usunąć!", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                    databaseReferencePlanner.child(days.get(i)).child("dinner").addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            String plannerKey = snapshot.child("key").getValue().toString();
                            if (plannerKey.equals(key)) {
                                //databaseReferencePlanner.child(days.get(finalI)).child("dinner").child("key").removeValue();
                                //finish();
                                Toast.makeText(RecipeDetail.this, "Nie można usunąć!", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                    databaseReferencePlanner.child(days.get(i)).child("supper").addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            String plannerKey = snapshot.child("key").getValue().toString();
                            if (plannerKey.equals(key)) {
                                //databaseReferencePlanner.child(days.get(finalI)).child("supper").child("key").removeValue();
                                Toast.makeText(RecipeDetail.this, "Nie można usunąć!", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        //databaseReference.removeValue();
        finish();
        //Toast.makeText(RecipeDetail.this, key, Toast.LENGTH_LONG).show();
    }
}