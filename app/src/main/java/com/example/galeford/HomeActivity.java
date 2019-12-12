package com.example.galeford;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.ImageView;

import com.google.android.material.navigation.NavigationView;
import com.synnapps.carouselview.CarouselView;
import com.synnapps.carouselview.ImageListener;

public class HomeActivity extends AppCompatActivity {

    Toolbar toolbar;
    DrawerLayout drawerLayout;
    NavigationView navigationView;

    CarouselView carouselView;

    private int[] mImages = new int[] {
            R.drawable.slide1,R.drawable.slide2,R.drawable.slide3

    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);



        carouselView = (CarouselView) findViewById(R.id.carouselView);
        carouselView.setPageCount(mImages.length);

        carouselView.setImageListener(new ImageListener() {
            @Override
            public void setImageForPosition(int position, ImageView imageView) {

                imageView.setImageResource(mImages[position]);

            }
        });
//        drawerLayout = findViewById(R.id.main_tag);
////        navigationView = findViewById(R.id.nav_view);
//
//        toolbar = findViewById(R.id.galeford_toolbar);
//        setSupportActionBar(toolbar);
////        toolbar.setTitle("Galeford");
//        toolbar.setNavigationIcon(R.drawable.ic_menu_black);
//
//        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(HomeActivity.this,drawerLayout,toolbar,R.string.navigation_open,R.string.navigation_close);
//        drawerLayout.addDrawerListener(toggle);
//        toggle.syncState();
    }
}
