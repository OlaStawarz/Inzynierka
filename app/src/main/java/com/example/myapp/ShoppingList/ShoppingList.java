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
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuInflater;
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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator;

public class ShoppingList extends AppCompatActivity implements IngredientAdapter.ItemClickedListener,
        DeleteShoppingListDialog.DeleteShoppingListListener {

    private TextView amountTextView;
    private EditText searchItemEditText;
    private RecyclerView recyclerView;
    private IngredientAdapter arrayAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private FloatingActionButton addItem, deleteList;
    private ArrayList<IngredientModel> items, filteredList;
    private DatabaseReference databaseReference;
    BottomNavigationView bottomNavigationView;

    private FirebaseUser user;
    private FirebaseAuth auth;
    String uid;

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

        auth = FirebaseAuth.getInstance();

        user = FirebaseAuth.getInstance().getCurrentUser();
        assert user != null;
        uid = user.getUid();

        filteredList = new ArrayList<>();

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
                filteredList = new ArrayList<>();
                for (IngredientModel item : items) {
                    if (item.getName().toLowerCase().contains(text.toLowerCase())) {
                        filteredList.add(item);
                    }
                }
                arrayAdapter.filterList(filteredList);
            }
        });

        databaseReference = FirebaseDatabase.getInstance().getReference("ShoppingList").child(uid);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    items = new ArrayList<>();
                    for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                        IngredientModel item = postSnapshot.getValue(IngredientModel.class);
                        assert item != null;
                        item.setItemKey(postSnapshot.getKey());
                        items.add(item);
                    }
                    arrayAdapter = new IngredientAdapter(items, ShoppingList.this);
                    recyclerView.setAdapter(arrayAdapter);
                    if (arrayAdapter.getItemCount() == 1)
                        amountTextView.setText("Na twojej liście znajduje się 1 produkt");
                    else if (arrayAdapter.getItemCount() > 1 && arrayAdapter.getItemCount() < 5)
                        amountTextView.setText("Na twojej liście znajdują się " +
                                arrayAdapter.getItemCount() + " produkty");
                    else
                        amountTextView.setText("Na twojej liście znajduje się " +
                                arrayAdapter.getItemCount() + " produktów");
                } else {
                    amountTextView.setText("Na twojej liście nie znajdują się obecnie żadne produkty");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(ShoppingList.this, error.getMessage(), Toast.LENGTH_SHORT).show();
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
                        finish();
                        startActivity(new Intent(getApplicationContext(), Planner.class).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION));
                        overridePendingTransition(0, 0);
                        return true;
                    case R.id.recipes:
                        finish();
                        startActivity(new Intent(getApplicationContext(), Recipes.class).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION));
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
                openDialog();

            }
        });
    }

    public void openDialog() {
        DeleteShoppingListDialog dialog = new DeleteShoppingListDialog();
        dialog.show(getSupportFragmentManager(), "shopping list dialog");
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
        Intent intent = new Intent(ShoppingList.this, EditIngredient.class);
        Bundle bundle = new Bundle();
        if (filteredList.isEmpty()) {
            bundle.putString("name", items.get(position).getName());
            bundle.putString("key", items.get(position).getItemKey());

        } else {
            bundle.putString("name", filteredList.get(position).getName());
            bundle.putString("key", filteredList.get(position).getItemKey());
        }
        intent.putExtras(bundle);
        startActivity(intent);
    }

    @Override
    public void confirmAction() {
        databaseReference.removeValue();
        items.clear();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_signout, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.signout) {
            auth.signOut();
            finish();
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

}