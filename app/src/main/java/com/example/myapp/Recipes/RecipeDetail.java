package com.example.myapp.Recipes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapp.R;
import com.example.myapp.ShoppingList.IngredientAdapter;
import com.example.myapp.ShoppingList.IngredientModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class RecipeDetail extends AppCompatActivity implements DeleteRecipeFragment.DeleteListener, IngredientAdapter.ItemClickedListener{

    private DatabaseReference databaseReference, databaseReferencePlanner, databaseReferenceIngredients;
    private String key;
    IngredientAdapter arrayAdapter;
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    ArrayList<String> days = new ArrayList<>();
    ArrayList<IngredientModel> ingredients;
    boolean isInPlanner = false;

    private FirebaseUser user;
    String uid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_detail);

        final TextView textViewName = findViewById(R.id.textViewRecipeNameDetail);
        final TextView textViewDescription = findViewById(R.id.textViewDisplayDescription);
        final TextView textViewLink = findViewById(R.id.textViewDisplayLink);
        textViewLink.setMovementMethod(LinkMovementMethod.getInstance());
        final ImageView imageView = findViewById(R.id.imageViewRecipeImageDetail);
        recyclerView = findViewById(R.id.recycler_view_ingredients);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        user = FirebaseAuth.getInstance().getCurrentUser();
        assert user != null;
        uid = user.getUid();

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        assert bundle != null;
        key = bundle.getString("key");

        databaseReference = FirebaseDatabase.getInstance().getReference("Recipes").child(uid).child(key);
        databaseReferencePlanner = FirebaseDatabase.getInstance().getReference("Planner").child(uid);

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    textViewName.setText(snapshot.child("name").getValue().toString());
                    textViewDescription.setText(snapshot.child("description").getValue().toString());
                    textViewLink.setText(snapshot.child("link").getValue().toString());
                    Picasso.with(RecipeDetail.this)
                            .load(snapshot.child("imageUrl").getValue().toString())
                            .centerCrop()
                            .fit()
                            .into(imageView);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        databaseReferenceIngredients = FirebaseDatabase.getInstance().getReference("Recipes").child(uid)
                .child(key).child("ingredientModels");

        databaseReferenceIngredients.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    ingredients = new ArrayList<>();
                    for (int i = 0; i < snapshot.getChildrenCount(); i++) {
                        final String name = snapshot.child(String.valueOf(i))
                                .child("name").getValue().toString();
                        final double amount = Double.parseDouble(snapshot.child(String.valueOf(i))
                                .child("amount").getValue().toString());
                        final String unit = snapshot.child(String.valueOf(i))
                                .child("unit").getValue().toString();
                        IngredientModel item = new IngredientModel(name, amount, unit);
                        ingredients.add(item);
                    }
                    arrayAdapter = new IngredientAdapter(ingredients, RecipeDetail.this);
                    recyclerView.setAdapter(arrayAdapter);
                }

            }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        databaseReferencePlanner.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {

                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        days.add(dataSnapshot.getKey());
                    }
                    for (int i = 0; i < days.size(); i++) {
                        databaseReferencePlanner.child(days.get(i)).child("breakfast").addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if (snapshot.exists()) {
                                    String plannerKey = snapshot.child("key").getValue().toString();
                                    if (plannerKey.equals(key)) {
                                        //Toast.makeText(RecipeDetail.this, "Nie można usunąć!", Toast.LENGTH_SHORT).show();
                                        isInPlanner = true;
                                    }
                                }

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                        databaseReferencePlanner.child(days.get(i)).child("dinner").addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if (snapshot.exists()) {
                                    String plannerKey = snapshot.child("key").getValue().toString();
                                    if (plannerKey.equals(key)) {
                                        //Toast.makeText(RecipeDetail.this, "Nie można usunąć!", Toast.LENGTH_SHORT).show();
                                        isInPlanner = true;
                                    }
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                        databaseReferencePlanner.child(days.get(i)).child("supper").addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if (snapshot.exists()) {
                                    String plannerKey = snapshot.child("key").getValue().toString();
                                    if (plannerKey.equals(key)) {
                                        //
                                        //Toast.makeText(RecipeDetail.this, "Nie można usunąć!", Toast.LENGTH_SHORT).show();
                                        isInPlanner = true;
                                    }
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                        databaseReferencePlanner.child(days.get(i)).child("snack").addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if (snapshot.exists()) {
                                    String plannerKey = snapshot.child("key").getValue().toString();
                                    if (plannerKey.equals(key)) {
                                        //
                                        //Toast.makeText(RecipeDetail.this, "Nie można usunąć!", Toast.LENGTH_SHORT).show();
                                        isInPlanner = true;
                                    }
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                        databaseReferencePlanner.child(days.get(i)).child("secondBreakfast").addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if (snapshot.exists()) {
                                    String plannerKey = snapshot.child("key").getValue().toString();
                                    if (plannerKey.equals(key)) {
                                        //
                                        //Toast.makeText(RecipeDetail.this, "Nie można usunąć!", Toast.LENGTH_SHORT).show();
                                        isInPlanner = true;
                                    }
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

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
                Intent intent = new Intent(RecipeDetail.this, EditRecipe.class);
                Bundle bundle = new Bundle();
                bundle.putString("key", key);
                intent.putExtras(bundle);
                startActivity(intent);
                break;
            case R.id.delete:
                openDialog();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void openDialog() {
        DeleteRecipeFragment deleteRecipeFragment = new DeleteRecipeFragment();
        deleteRecipeFragment.show(getSupportFragmentManager(), "Confirmation");
    }


    @Override
    public void confirm() {
        if (!isInPlanner) {
            Toast.makeText(this, key, Toast.LENGTH_SHORT).show();
            databaseReference.removeValue();
            finish();
        } else {
            Toast.makeText(this, "Nie można usunąć", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void itemClicked(int position) {

    }

}