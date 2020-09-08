package com.example.myapp.ShoppingList;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapp.Planner.Planner;
import com.example.myapp.R;
import com.example.myapp.Recipes.Recipes;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator;

public class ShoppingList extends AppCompatActivity implements IngredientAdapter.ItemClickedListener{

    private TextView amountTextView;
    private EditText searchItemEditText;
    private RecyclerView recyclerView;
    private IngredientAdapter arrayAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private FloatingActionButton addItem, deleteList;
    ArrayList<IngredientModel> items;
    ArrayList<String> blabla;
    private DatabaseReference databaseReference;
    BottomNavigationView bottomNavigationView;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping_list);

        amountTextView = findViewById(R.id.textViewAmountOfItem);
        searchItemEditText = findViewById(R.id.editTextSearchItem);
        searchItemEditText.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_search, 0, 0, 0);
        recyclerView = findViewById(R.id.recycler_view_shopping_list);
        recyclerView.setHasFixedSize(true);
        addItem = findViewById(R.id.floatingActionButtonShoppingList);
        deleteList = findViewById(R.id.floatingActionButtonDeleteList);

        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        new ItemTouchHelper(itemTouchHelper).attachToRecyclerView(recyclerView);

        bottomNavigationView = findViewById(R.id.bottom_navigation);

        searchItemEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String text = editable.toString();
                ArrayList<IngredientModel> filteredList = new ArrayList<>();
                for (IngredientModel item : items) {
                    if (item.getName().toLowerCase().contains(text.toLowerCase())) {
                        filteredList.add(item);
                    }
                }
                arrayAdapter.filterList(filteredList);
            }
        });

        databaseReference = FirebaseDatabase.getInstance().getReference("ShoppingList");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                items = new ArrayList<>();
                for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                    IngredientModel item = postSnapshot.getValue(IngredientModel.class);
                    assert item != null;
                    item.setItemKey(postSnapshot.getKey());
                    items.add(item);
                }
                arrayAdapter = new IngredientAdapter(items, ShoppingList.this);
                recyclerView.setAdapter(arrayAdapter);
                if (arrayAdapter.getItemCount() == 0)
                    amountTextView.setText("Na twojej liście nie znajdują się obecnie żadne produkty");
                else if (arrayAdapter.getItemCount() == 1)
                    amountTextView.setText("Na twojej liście znajduje się jeden produkt");
                else if (arrayAdapter.getItemCount() > 1 && arrayAdapter.getItemCount() < 5)
                    amountTextView.setText("Na twojej liście znajdują się " +
                            arrayAdapter.getItemCount() + " produkty");
                else
                    amountTextView.setText("Na twojej liście znajduje się " +
                        arrayAdapter.getItemCount() + " produktów");

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

        deleteList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                databaseReference.removeValue();
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
                    final int position = viewHolder.getAdapterPosition();
                    final IngredientModel selectedItem = items.get(viewHolder.getAdapterPosition());
                    final String selectedKey = selectedItem.getItemKey();
                    databaseReference.child(selectedKey).removeValue();
                    items.remove(viewHolder.getAdapterPosition());
                    arrayAdapter.notifyDataSetChanged();
                    Snackbar.make(recyclerView, "Czy na pewno chcesz usunąć produkt z listy?", Snackbar.LENGTH_LONG)
                            .setAction("Cofnij", new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    items.add(position, selectedItem);
                                    arrayAdapter.notifyItemInserted(position);
                                    databaseReference.child(selectedKey).setValue(selectedItem);
                                }
                            }).show();
                    //databaseReference.removeValue();
                }

                @Override
                public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView,
                                        @NonNull RecyclerView.ViewHolder viewHolder, float dX,
                                        float dY, int actionState, boolean isCurrentlyActive) {
                    new RecyclerViewSwipeDecorator.Builder(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
                            .addBackgroundColor(ContextCompat.getColor(ShoppingList.this, R.color.delete))
                            .addActionIcon(R.drawable.ic_delete)
                            .create()
                            .decorate();
                    super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
                }
            };


    @Override
    public void itemClicked(int position) {
        Toast.makeText(ShoppingList.this, String.valueOf(items.get(position).getName()), Toast.LENGTH_LONG).show();
        Intent intent = new Intent(ShoppingList.this, EditIngredient.class);
        Bundle bundle = new Bundle();
        bundle.putString("name", items.get(position).getName());
        bundle.putString("key", items.get(position).getItemKey());
        intent.putExtras(bundle);
        startActivity(intent);
    }
}