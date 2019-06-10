package com.mehmetaydin.hastanerandevusistemi;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mehmetaydin.hastanerandevusistemi.Model.Kisi;
import com.mehmetaydin.hastanerandevusistemi.Model.User;

public class Profilim extends AppCompatActivity {
    Button btnBilgiGuncelle, btnSifreGuncelle,btnExit;
    BottomNavigationView bottomNavigationView;
    TextView twAd, twTCKN;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profilim);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Profilim");

        ilklendirme();
        doldur();

        btnBilgiGuncelle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Profilim.this, HastaBilgiGuncelle.class);
                intent.putExtra("rol","Hasta");
                startActivity(intent);
                finish();
            }
        });
        btnSifreGuncelle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Profilim.this, SifreDegistir.class);
                intent.putExtra("rol","Hasta");
                startActivity(intent);
                finish();
            }
        });
        btnExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                User.uID="";
                Intent intent = new Intent(Profilim.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.nav_randevu:
                        Intent intent = new Intent(Profilim.this, RandevuGecmisi.class);
                        startActivity(intent);
                        finish();
                        break;
                    case R.id.nav_favoriler:
                        Intent intent2 = new Intent(Profilim.this, Favorilerim.class);
                        startActivity(intent2);
                        finish();
                        break;
                }
                return true;
            }
        });
    }

    private void doldur() {
        final DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Kisi kisi2 = snapshot.getValue(Kisi.class);
                    kisi2.setID(snapshot.getKey().toString());
                    if (kisi2.getID().equalsIgnoreCase(User.uID)){
                        twAd.setText(kisi2.getAd()+" " +kisi2.getSoyad());
                        twTCKN.setText(kisi2.getTCKN());
                    }

                }
                reference.removeEventListener(this);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void ilklendirme() {
        btnBilgiGuncelle=findViewById(R.id.btnBilgiGuncelle);
        btnSifreGuncelle=findViewById(R.id.btnSifreGuncelle);
        btnExit=findViewById(R.id.btnHastaExit);
        bottomNavigationView = findViewById(R.id.bottomNavigationHome);
        twAd = findViewById(R.id.twHastaProfilAd);
        twTCKN = findViewById(R.id.twHastaProfilTCKN);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(Profilim.this,HastaHomePage.class);
        startActivity(intent);
        finish();
    }
}
