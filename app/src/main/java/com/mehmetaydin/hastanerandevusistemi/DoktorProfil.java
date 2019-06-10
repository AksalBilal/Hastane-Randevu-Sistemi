package com.mehmetaydin.hastanerandevusistemi;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mehmetaydin.hastanerandevusistemi.Model.Bolum;
import com.mehmetaydin.hastanerandevusistemi.Model.Hastane;
import com.mehmetaydin.hastanerandevusistemi.Model.Kisi;
import com.mehmetaydin.hastanerandevusistemi.Model.User;

public class DoktorProfil extends AppCompatActivity {
    Button btnBilgiGuncelle, btnSifreGuncelle,btnDoktorExit;
    BottomNavigationView bottomNavigationView;
    TextView twAd, twTCKN, twHastane, twBolum;

    String bolumID="", hastaneID="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doktor_profil);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Profilim");

        bolumID="";
        hastaneID="";

        ilklendirme();
        bilgiDoldur();

        btnBilgiGuncelle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DoktorProfil.this, HastaBilgiGuncelle.class);
                intent.putExtra("rol","Doktor");
                startActivity(intent);
                finish();
            }
        });
        btnSifreGuncelle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DoktorProfil.this, SifreDegistir.class);
                intent.putExtra("rol","Doktor");
                startActivity(intent);
                finish();
            }
        });
        btnDoktorExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                User.uID="";
                Intent intent = new Intent(DoktorProfil.this, LoginActivity.class);
                startActivity(intent);
                finish();

            }
        });
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.nav_randevu:
                        Intent intent = new Intent(DoktorProfil.this, DoktorRandevu.class);
                        startActivity(intent);
                        finish();
                        break;
                }
                return true;
            }
        });
    }

    private void bilgiDoldur() {
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
                        bolumID=kisi2.getBolumID();
                    }

                }
                bolumGetir();
                reference.removeEventListener(this);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void bolumGetir() {
        final DatabaseReference reference2 = FirebaseDatabase.getInstance().getReference("Bolum");
        reference2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Bolum bolum2 = snapshot.getValue(Bolum.class);

                    bolum2.setBolumID(snapshot.getKey().toString());
                    if (bolum2.getBolumID().equalsIgnoreCase(bolumID)) {
                        twBolum.setText(bolum2.getBolumAdi());
                        hastaneID=bolum2.getHastaneID();
                    }
                }
                hastaneGetir();
                reference2.removeEventListener(this);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void hastaneGetir() {
        final DatabaseReference reference2 = FirebaseDatabase.getInstance().getReference("Hastane");
        reference2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Hastane hastane2 = snapshot.getValue(Hastane.class);
                    hastane2.id = snapshot.getKey().toString();
                    if(hastane2.id.equalsIgnoreCase(hastaneID)){
                        twHastane.setText(hastane2.getHastaneAdi());
                    }

                }
                reference2.removeEventListener(this);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void ilklendirme() {
        btnBilgiGuncelle=findViewById(R.id.btnBilgiGuncelleDoktor);
        btnSifreGuncelle=findViewById(R.id.btnSifreGuncelleDoktor);
        btnDoktorExit=findViewById(R.id.btnDoktorExit);
        bottomNavigationView = findViewById(R.id.bottomNavigationDoktor);
        twAd = findViewById(R.id.twDoktorProfilAd);
        twTCKN = findViewById(R.id.twDoktorProfilTCKN);
        twHastane = findViewById(R.id.twDoktorProfilHastane);
        twBolum = findViewById(R.id.twDoktorProfilBolum);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(DoktorProfil.this,DoktorAnasayfa.class);
        startActivity(intent);
        finish();
    }
}
