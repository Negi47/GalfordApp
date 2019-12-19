package com.example.galeford;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.galeford.adapters.ProductAdapter;
import com.example.galeford.models.Products;
import com.example.galeford.models.Users;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class ProductDisplay extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    FirebaseFirestore firestore;

    RecyclerView productRecyclerView;
    ProductAdapter productAdapter;
    LinearLayout progressBar;
    Users user;
    Toolbar toolbar;

    ImageButton productFilterBtn,productSortBtn;

    List<Products> productsList;

    String SEARCH_PATTERN;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_display);

        firestore = FirebaseFirestore.getInstance();
        productsList = new ArrayList<>();

        SEARCH_PATTERN = getIntent().getStringExtra("searchPattern");

        toolbar = findViewById(R.id.galeford_toolbar);
//        galeford_toolbar.setBackgroundColor(getResources().);

        setSupportActionBar(toolbar);
        toolbar.setTitle("Products");

        toolbar.setNavigationIcon(R.drawable.ic_menu_black);


        productRecyclerView = findViewById(R.id.productRecyclerView);
        productRecyclerView.setHasFixedSize(true);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(getApplicationContext(),2,LinearLayoutManager.VERTICAL,false);
        productRecyclerView.setLayoutManager(gridLayoutManager);

        progressBar = findViewById(R.id.progressBarWrapper);

        firestore.collection("products")
                    .whereEqualTo(Products.PRODUCT_GENDER,getIntent().getStringExtra(Products.PRODUCT_GENDER))
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful() && task.getResult() != null) {
                                for (QueryDocumentSnapshot doc : task.getResult()) {
                                    Products products = new Products();
                                    products._setProductId(doc.getId());
                                    products.setProductItemName(doc.getString(Products.PRODUCT_ITEM_NAME));
                                    products.setProductImage((List<String>)doc.get(Products.PRODUCT_IMAGE));
                                    products.setProductItemPrice(doc.getString(Products.PRODUCT_ITEM_PRICE));
                                    products.setProductItemDescription(doc.getString(Products.PRODUCT_ITEM_DESCRIPTION));

                                    productsList.add(products);
                                }

                                firestore.collection(TableNames.USERS)
                                        .document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                        .get()
                                        .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                            @Override
                                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                if (task.getResult() != null) {
                                                    user = new Users();

                                                    user._setUserId(task.getResult().getId());
                                                    user.setFavProducts((List)task.getResult().get(Users.FAV_PRODUCTS));

                                                    productAdapter = new ProductAdapter(ProductDisplay.this, productsList, user);
                                                    productRecyclerView.setAdapter(productAdapter);

                                                    progressBar.setVisibility(View.GONE);
                                                }
                                            }
                                        });
                            }
                        }
                    });


        productFilterBtn = findViewById(R.id.productFilterBtn);

        productFilterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder filterBuilder = new AlertDialog.Builder(ProductDisplay.this);
                View filterView = getLayoutInflater().inflate(R.layout.filter_list,null);




            }
        });


        productSortBtn = findViewById(R.id.productSortBtn);

        productSortBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder sortBuilder = new AlertDialog.Builder(ProductDisplay.this);
                View sortView = getLayoutInflater().inflate(R.layout.product_list_sort,null);

                sortBuilder.setView(sortView);
                final AlertDialog dialog = sortBuilder.create();

                RadioGroup radioGroupProductSort = findViewById(R.id.radioGroupProductSort);

                RadioButton productHightoLow = sortView.findViewById(R.id.radioButtonhightolow);
                RadioButton productPopularity = sortView.findViewById(R.id.radioButtonpopularity);
                RadioButton productLowtoHigh = sortView.findViewById(R.id.radioButtonlowtohigh);

                productHightoLow.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        Collections.sort(productsList, new Comparator<Products>() {
                            @Override
                            public int compare(Products o1, Products o2) {
                                return o1.getProductItemPrice().compareTo(o2.getProductItemPrice());
                            }
                        });

                        productAdapter = new ProductAdapter(ProductDisplay.this, productsList, user);
                        productRecyclerView.setAdapter(productAdapter);

                        dialog.cancel();
                    }
                });

                productLowtoHigh.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        Collections.sort(productsList, new Comparator<Products>() {
                            @Override
                            public int compare(Products o1, Products o2) {
                                return o2.getProductItemPrice().compareTo(o1.getProductItemPrice());
                            }
                        });

                        productAdapter = new ProductAdapter(ProductDisplay.this, productsList, user);
                        productRecyclerView.setAdapter(productAdapter);

                        dialog.cancel();
                    }
                });

                dialog.show();
            }
        });

    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);
        MenuItem searchItem = menu.findItem(R.id.nav_search);
        SearchView searchView = (SearchView) searchItem.getActionView();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {

                productAdapter.getFilter().filter(s);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                return false;
            }
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()){

            case R.id.like:
                startActivity(new Intent(ProductDisplay.this,WishlistActivity.class));
                break;

        }

//        Toast.makeText(MainActivity.this,msg+ "Latest checked",Toast.LENGTH_LONG).show();
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {

            case R.id.nav_account: {
                Toast.makeText(ProductDisplay.this,"clicked",Toast.LENGTH_LONG).show();
                FirebaseAuth.getInstance().signOut();
                finish();
                startActivity(new Intent(ProductDisplay.this, LoginActivity.class));
                break;
            }
        }

        return true;
    }

}
