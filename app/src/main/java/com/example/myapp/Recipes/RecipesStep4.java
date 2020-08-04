package com.example.myapp.Recipes;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.myapp.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;

public class RecipesStep4 extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1;
    RecipeModel recipeModel;
    DatabaseReference databaseReference;
    StorageReference storageReference;

    ArrayList<String> category, ingredients;
    String name, description, link;
    ImageView image;
    Button buttonAddRecipe, buttonChooseFile;

    Uri imageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipes_step4);

        databaseReference = FirebaseDatabase.getInstance().getReference("Recipes");
        storageReference = FirebaseStorage.getInstance().getReference("Image");

        image = findViewById(R.id.imageViewRecipe);
        buttonAddRecipe = findViewById(R.id.buttonAddRecipe);
        buttonChooseFile = findViewById(R.id.buttonChooseFile);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        category = bundle.getStringArrayList("categoryNext");
        ingredients = bundle.getStringArrayList("ingredients");
        name = bundle.getString("nameNext");
        description = bundle.getString("description");
        link = bundle.getString("link");

        buttonChooseFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fileChooser();
            }
        });

        buttonAddRecipe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uploadImage();
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
            image.setImageURI(imageUri);

        }
    }

    private String getExtension(Uri uri) {
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }

    private void uploadImage() {

        if (imageUri != null) {
            StorageReference fileReference = storageReference.child(System.currentTimeMillis()
                    + "." + getExtension(imageUri));

            fileReference.putFile(imageUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Task<Uri> urlTask = taskSnapshot.getStorage().getDownloadUrl();
                            while (!urlTask.isSuccessful());
                            Uri downloadUrl = urlTask.getResult();

                            //List<String> listOfIngredients = Arrays.asList(ingredients.getText().toString().split(" "));
                            recipeModel = new RecipeModel(name, category, ingredients, description, link, downloadUrl.toString());

                            String uploadId = databaseReference.push().getKey();
                            databaseReference.child(uploadId).setValue(recipeModel);
                            Toast.makeText(RecipesStep4.this, "Correctly inserted image", Toast.LENGTH_LONG).show();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(RecipesStep4.this, e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    });
        }
    }


}