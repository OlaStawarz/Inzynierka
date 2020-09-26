package com.example.myapp.Recipes;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.myapp.R;
import com.example.myapp.ShoppingList.EditIngredient;
import com.example.myapp.ShoppingList.IngredientAdapter;
import com.example.myapp.ShoppingList.IngredientModel;
import com.example.myapp.ShoppingList.ShoppingList;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class EditRecipe extends AppCompatActivity implements IngredientAdapter.ItemClickedListener{

    private static final int PICK_IMAGE_REQUEST = 2;
    private Uri imageUri;

    private DatabaseReference databaseReference, databaseReferenceIngredients;
    private StorageReference storageReference;
    private ImageView imageView;
    private EditText editTextName, editTextDescription, editTextLink;
    private IngredientAdapter arrayAdapter;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private ArrayList<IngredientModel> ingredients;
    private ArrayList<String> category;
    private CheckBox breakfast, dinner, supper, snack;
    Button saveChanges, saveImage;
    FloatingActionButton addIngredient;
    String key;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_recipe);

        imageView = findViewById(R.id.imageViewRecipeImageDetailEdit);
        editTextName = findViewById(R.id.editTextRecipeNameDetailEdit);
        editTextDescription = findViewById(R.id.editTextDisplayDescriptionEdit);
        editTextLink = findViewById(R.id.editTextDisplayLinkEdit);
        saveChanges = findViewById(R.id.buttonSaveChanges);
        saveImage = findViewById(R.id.buttonEditRecipeSavePhoto);
        addIngredient = findViewById(R.id.floatingActionBarAddItemRecipe);
        breakfast = findViewById(R.id.checkBoxEditBreakfast);
        dinner = findViewById(R.id.checkBoxEditDinner);
        supper = findViewById(R.id.checkBoxEditSupper);
        snack = findViewById(R.id.checkBoxEditSnack);

        recyclerView = findViewById(R.id.recycler_view_ingredients_edit);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        category = new ArrayList<>();

        storageReference = FirebaseStorage.getInstance().getReference("Image");

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        key = bundle.getString("key");
        //Toast.makeText(this, key, Toast.LENGTH_SHORT).show();

        addIngredient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent addIntent = new Intent(EditRecipe.this, AddIngredientToRecipe.class);
                Bundle addBundle = new Bundle();
                addBundle.putString("key", key);
                addIntent.putExtras(addBundle);
                startActivity(addIntent);
            }
        });

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fileChooser();
            }
        });

        saveImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                StorageReference fileReference = storageReference.child(System.currentTimeMillis()
                        + "." + getExtension(imageUri));

                fileReference.putFile(imageUri)
                        .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                Task<Uri> urlTask = taskSnapshot.getStorage().getDownloadUrl();
                                while (!urlTask.isSuccessful());
                                Uri downloadUrl = urlTask.getResult();
                                Toast.makeText(EditRecipe.this, downloadUrl.toString(), Toast.LENGTH_SHORT).show();
                                databaseReference.child("imageUrl").setValue(downloadUrl.toString());
                                Toast.makeText(EditRecipe.this, "Correctly inserted image", Toast.LENGTH_LONG).show();
                                //finish();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(EditRecipe.this, e.getMessage(), Toast.LENGTH_LONG).show();
                            }
                        });
            }
        });

        databaseReference = FirebaseDatabase.getInstance().getReference("Recipes").child(key);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    //Toast.makeText(EditRecipe.this, snapshot.child("name").getValue().toString(), Toast.LENGTH_SHORT).show();
                    editTextName.setText(snapshot.child("name").getValue().toString());
                    editTextDescription.setText(snapshot.child("description").getValue().toString());
                    editTextLink.setText(snapshot.child("link").getValue().toString());
                    Picasso.with(EditRecipe.this)
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

        databaseReferenceIngredients = FirebaseDatabase.getInstance().getReference("Recipes")
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
                        //Toast.makeText(RecipeDetail.this, name, Toast.LENGTH_SHORT).show();
                        IngredientModel item = new IngredientModel(name, amount, unit);
                        ingredients.add(item);
                    }
                    arrayAdapter = new IngredientAdapter(ingredients, EditRecipe.this);
                    recyclerView.setAdapter(arrayAdapter);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        saveChanges.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(EditRecipe.this, "Edytowanie...", Toast.LENGTH_SHORT).show();
                databaseReference.child("name").setValue(editTextName.getText().toString());
                databaseReference.child("description").setValue(editTextDescription.getText().toString());
                databaseReference.child("link").setValue(editTextLink.getText().toString());
                if (breakfast.isChecked()) {
                    category.add(breakfast.getText().toString());
                }
                if (dinner.isChecked()) {
                    category.add(dinner.getText().toString());
                }
                if (supper.isChecked()) {
                    category.add(supper.getText().toString());
                }
                if (snack.isChecked()) {
                    category.add(snack.getText().toString());
                }
                databaseReference.child("category").setValue(category);
                finish();
            }
        });
    }

    private void fileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null) {
            imageUri = data.getData();
            imageView.setImageURI(imageUri);

        }
    }

    private String getExtension(Uri uri) {
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }

    private void uploadImage() {

        StorageReference fileReference = storageReference.child(System.currentTimeMillis()
                + "." + getExtension(imageUri));

        fileReference.putFile(imageUri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Task<Uri> urlTask = taskSnapshot.getStorage().getDownloadUrl();
                        while (!urlTask.isSuccessful());
                        Uri downloadUrl = urlTask.getResult();
                        Toast.makeText(EditRecipe.this, downloadUrl.toString(), Toast.LENGTH_SHORT).show();
                        databaseReference.child("imageUrl").setValue(downloadUrl);
                        Toast.makeText(EditRecipe.this, "Correctly inserted image", Toast.LENGTH_LONG).show();
                        //finish();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(EditRecipe.this, e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
    }

    @Override
    public void itemClicked(int position) {
        Intent intent = new Intent(EditRecipe.this, EditIngredientRecipe.class);
        Bundle bundle = new Bundle();
        bundle.putString("name", ingredients.get(position).getName());
        bundle.putString("position", String.valueOf(position));
        bundle.putString("key", key);
        intent.putExtras(bundle);
        Toast.makeText(this, String.valueOf(position), Toast.LENGTH_SHORT).show();
        startActivity(intent);
    }
}