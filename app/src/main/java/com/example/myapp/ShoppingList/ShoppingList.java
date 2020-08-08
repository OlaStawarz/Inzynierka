package com.example.myapp.ShoppingList;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.myapp.Planner.Planner;
import com.example.myapp.R;
import com.example.myapp.Recipes.RecipeModel;
import com.example.myapp.Recipes.Recipes;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ShoppingList extends AppCompatActivity {

    private TextView amountTextView;
    private RecyclerView recyclerView;
    private ShoppingListAdapter arrayAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private FloatingActionButton addItem;
    ArrayList<ItemModel> items;
    private DatabaseReference databaseReference;
    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping_list);

        amountTextView = findViewById(R.id.textViewAmountOfItem);
        recyclerView = findViewById(R.id.recycler_view_shopping_list);
        recyclerView.setHasFixedSize(true);
        addItem = findViewById(R.id.floatingActionButtonShoppingList);

        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        new ItemTouchHelper(itemTouchHelper).attachToRecyclerView(recyclerView);

        bottomNavigationView = findViewById(R.id.bottom_navigation);


        databaseReference = FirebaseDatabase.getInstance().getReference("ShoppingList");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                items = new ArrayList<>();
                for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                    ItemModel item = postSnapshot.getValue(ItemModel.class);
                    item.setItemKey(postSnapshot.getKey());
                    items.add(item);
                }
                arrayAdapter = new ShoppingListAdapter(items);
                recyclerView.setAdapter(arrayAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        bottomNavigationView.setSelectedItemId(R.id.shopping_list);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.shopping_list:
                        return true;
                    case R.id.meal_planner:
                        startActivity(new Intent(getApplicationContext(), Planner.class));
                        overridePendingTransition(0, 0);
                        return true;
                    case R.id.recipes:
                        startActivity(new Intent(getApplicationContext(), Recipes.class));
                        overridePendingTransition(0, 0);
                        return true;
                }
                return false;
            }

        });




        addItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ShoppingList.this, AddItem.class);
                startActivity(intent);
            }
        });
    }


    ItemTouchHelper.SimpleCallback itemTouchHelper =
            new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT | ItemTouchHelper.LEFT) {
                @Override
                public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                    return false;
                }

                @Override
                public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                    ItemModel selectedItem = items.get(viewHolder.getAdapterPosition());
                    String selectedKey = selectedItem.getItemKey();
                    databaseReference.child(selectedKey).removeValue();
                    items.remove(viewHolder.getAdapterPosition());
                    arrayAdapter.notifyDataSetChanged();

                    //databaseReference.removeValue();
                }
            };


}