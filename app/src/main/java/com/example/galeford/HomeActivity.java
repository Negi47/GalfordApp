package com.example.galeford;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.ContextThemeWrapper;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.MenuItemCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
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
import com.synnapps.carouselview.CarouselView;
import com.synnapps.carouselview.ImageListener;

public class HomeActivity extends AppCompatActivity implements  NavigationView.OnNavigationItemSelectedListener{

    Toolbar toolbar;
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    LinearLayout babyboylayout, babyGirlLayout;
    TextView nav_header_username, nav_header_email, nav_header_tag_TV;

    CarouselView carouselView;

    private int[] mImages = new int[] {
            R.drawable.slide1,R.drawable.slide2,R.drawable.slide3

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        drawerLayout = findViewById(R.id.main_tag);
        navigationView = findViewById(R.id.nav_view);
        View navHeaderView = navigationView.getHeaderView(0);

        nav_header_email = navHeaderView.findViewById(R.id.nav_header_email);
        nav_header_username = navHeaderView.findViewById(R.id.nav_header_username);
        nav_header_tag_TV = navHeaderView.findViewById(R.id.nav_header_tag_TV);

        toolbar = findViewById(R.id.galeford_toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle("Galeford");
        toolbar.setNavigationIcon(R.drawable.ic_menu_black);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(HomeActivity.this,drawerLayout,toolbar,R.string.navigation_open,R.string.navigation_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        carouselView = (CarouselView) findViewById(R.id.productSingleCarouselView);
        carouselView.setPageCount(mImages.length);

        carouselView.setImageListener(new ImageListener() {
            @Override
            public void setImageForPosition(int position, ImageView imageView) {

                imageView.setImageResource(mImages[position]);

            }
        });

        babyboylayout = findViewById(R.id.babyboylayout);
        babyGirlLayout = findViewById(R.id.babyGirlLinearLayout);

        babyboylayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent babyboylayout = new Intent(HomeActivity.this,ProductDisplay.class).putExtra(Products.PRODUCT_GENDER,"Male");
                startActivity(babyboylayout);
            }
        });

        babyGirlLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent babyGirlLayout = new Intent(HomeActivity.this,ProductDisplay.class).putExtra(Products.PRODUCT_GENDER,"Female");
                startActivity(babyGirlLayout);
            }
        });


        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            nav_header_email.setVisibility(View.VISIBLE);
            nav_header_username.setVisibility(View.VISIBLE);
            nav_header_tag_TV.setVisibility(View.GONE);
            getLoggedInUserData();
        }


    }

    public void getLoggedInUserData() {
        FirebaseFirestore.getInstance().collection(TableNames.USERS)
                .document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        nav_header_username.setText(task.getResult().getString(Users.USERNAME));
                        nav_header_email.setText(task.getResult().getString(Users.EMAIL));
                    }
                });
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()){

            case R.id.nav_account: {
                Toast.makeText(HomeActivity.this,"clicked",Toast.LENGTH_LONG).show();
            }
        }
        return true;
    }

//    @Override
//    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
//
//        switch (item.getItemId()) {
//
//            case R.id.nav_account: {
//                Toast.makeText(HomeActivity.this,"clicked",Toast.LENGTH_LONG).show();
//                FirebaseAuth.getInstance().signOut();
//                finish();
//                startActivity(new Intent(HomeActivity.this, LoginActivity.class));
//                break;
//            }
//        }
//
//        return true;
//    }
}
