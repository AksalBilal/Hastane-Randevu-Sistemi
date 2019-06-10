package com.mehmetaydin.hastanerandevusistemi;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class HastaHomePage extends AppCompatActivity {
    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hasta_home_page);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Ana Sayfa");

        ilklendirme();

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                switch (menuItem.getItemId()) {
                    case R.id.nav_randevu:
                        Intent intent = new Intent(HastaHomePage.this, RandevuGecmisi.class);
                        startActivity(intent);
                        finish();
                        break;
                    case R.id.nav_favoriler:
                        Intent intent2 = new Intent(HastaHomePage.this, Favorilerim.class);
                        startActivity(intent2);
                        finish();
                        break;
                    case R.id.nav_profile:
                        Intent intent3 = new Intent(HastaHomePage.this, Profilim.class);
                        startActivity(intent3);
                        finish();
                        break;
                }
                return true;

            }
        });

    }

    private void ilklendirme() {
        bottomNavigationView = findViewById(R.id.bottomNavigationHome);
    }

    @Override
    public void onBackPressed() {
        System.exit(0);
    }
}
