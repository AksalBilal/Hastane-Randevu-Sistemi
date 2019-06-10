package com.mehmetaydin.hastanerandevusistemi;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;

public class DoktorAnasayfa extends AppCompatActivity {
    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doktor_anasayfa);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Doktor Anasayfa");

        bottomNavigationView = findViewById(R.id.bottomNavigationDoktor);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.nav_randevu_doktor:
                        Intent intent = new Intent(DoktorAnasayfa.this, DoktorRandevu.class);
                        startActivity(intent);
                        finish();
                        break;
                    case R.id.nav_profile_doktor:
                        Intent intent2 = new Intent(DoktorAnasayfa.this, DoktorProfil.class);
                        startActivity(intent2);
                        finish();
                        break;
                }
                return true;
            }
        });

    }

    @Override
    public void onBackPressed() {
        System.exit(0);
    }
}
