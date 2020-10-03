package com.example.myapp.Recipes;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.myapp.R;
import com.example.myapp.ShoppingList.IngredientModel;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;

public class RecipesStep4 extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1;
    RecipeModel recipeModel;
    DatabaseReference databaseReference;
    StorageReference storageReference;
    ProgressBar progressBar;

    ArrayList<String> category, ingredients, names, amounts, units;
    String name, description, link;
    ImageView image;
    Button buttonAddRecipe, buttonChooseFile;

    Uri imageUri;

    private FirebaseUser user;
    String uid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipes_step4);

        user = FirebaseAuth.getInstance().getCurrentUser();
        assert user != null;
        uid = user.getUid();

        databaseReference = FirebaseDatabase.getInstance().getReference("Recipes").child(uid);
        storageReference = FirebaseStorage.getInstance().getReference("Image").child(uid);

        image = findViewById(R.id.imageViewRecipe);
        buttonAddRecipe = findViewById(R.id.buttonAddRecipe);
        buttonChooseFile = findViewById(R.id.buttonChooseFile);
        progressBar = findViewById(R.id.progressBarLoadImage);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        assert bundle != null;
        category = bundle.getStringArrayList("categoryNext");
        ingredients = bundle.getStringArrayList("ingredients");
        name = bundle.getString("nameNext");
        names = bundle.getStringArrayList("names");
        amounts = bundle.getStringArrayList("amounts");
        units = bundle.getStringArrayList("units");
        description = bundle.getString("description");
        link = bundle.getString("link");
        /*newBundle.putString("description", description.getText().toString());
        newBundle.putString("link", link.getText().toString());*/

        //Toast.makeText(this, names.get(0) + amounts.get(0) + units.get(0), Toast.LENGTH_SHORT).show();
        //description = bundle.getString("description");
        //link = bundle.getString("link");

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

        if (imageUri == null) {
            imageUri = Uri.parse(("android.resource://" + getApplicationContext().getPackageName() + "/drawable/no_photo"));
            image.setImageURI(imageUri);
        }
        StorageReference fileReference = storageReference.child(System.currentTimeMillis()
                + "." + getExtension(imageUri));

        fileReference.putFile(imageUri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                progressBar.setProgress(0);
                            }
                        }, 500);
                        Task<Uri> urlTask = taskSnapshot.getStorage().getDownloadUrl();
                        while (!urlTask.isSuccessful());
                        Uri downloadUrl = urlTask.getResult();

                        ArrayList<IngredientModel> ingredientModels = new ArrayList<>();

                        for (int i = 0; i < names.size(); i++) {
                            IngredientModel ingredientModel = new IngredientModel(names.get(i),
                                    Double.parseDouble(amounts.get(i)), units.get(i));
                            ingredientModels.add(ingredientModel);

                        }
                        if (description.isEmpty()) {
                            description = "---";
                        }
                        if(link.isEmpty()) {
                            link = "---";
                        }
                        //List<String> listOfIngredients = Arrays.asList(ingredients.getText().toString().split(" "));
                        recipeModel = new RecipeModel(name, category, ingredients,
                                description, link, downloadUrl.toString(), ingredientModels);

                        String uploadId = databaseReference.push().getKey();
                        databaseReference.child(uploadId).setValue(recipeModel);
                        Toast.makeText(RecipesStep4.this, "Correctly inserted image", Toast.LENGTH_LONG).show();
                        finish();
                    }
                })
                .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onProgress(@NonNull UploadTask.TaskSnapshot taskSnapshot) {
                        Toast.makeText(RecipesStep4.this, "Dodawanie przepisu...", Toast.LENGTH_LONG).show();
                        double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                        progressBar.setProgress((int) progress);
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