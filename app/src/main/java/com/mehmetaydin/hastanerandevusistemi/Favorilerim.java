package com.mehmetaydin.hastanerandevusistemi;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.FirebaseNetworkException;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mehmetaydin.hastanerandevusistemi.Model.Favori;
import com.mehmetaydin.hastanerandevusistemi.Model.Hastane;
import com.mehmetaydin.hastanerandevusistemi.Model.Kisi;
import com.mehmetaydin.hastanerandevusistemi.Model.User;

import java.util.ArrayList;
import java.util.List;

public class Favorilerim extends AppCompatActivity {
    ArrayList<Favori> favoriList = new ArrayList<>();
    ArrayList<Kisi> doktorList = new ArrayList<>();
    ArrayList<String> doktorlar = new ArrayList<String>();
    ArrayList<Hastane> hastaneList = new ArrayList<>();

    String doktorID="",  hastaneID="", favoriID="", bolumID="";
    String doktorAdi="", hastaneAdi="";

    ListView list;
    BottomNavigationView bottomNavigationView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorilerim);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Favorilerim");


        doktorList.clear();
        favoriList.clear();
        hastaneList.clear();

        favoriID="";
        doktorID="";
        hastaneID="";
        bolumID="";
        doktorAdi="";
        hastaneAdi="";

        list = findViewById(R.id.listViewFavorilerim);
        bottomNavigationView = findViewById(R.id.bottomNavigationHome);
        favorleriGetir();

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.nav_randevu:
                        Intent intent = new Intent(Favorilerim.this, RandevuGecmisi.class);
                        startActivity(intent);
                        finish();
                        break;
                    case R.id.nav_profile:
                        Intent intent2 = new Intent(Favorilerim.this, Profilim.class);
                        startActivity(intent2);
                        finish();
                        break;
                }
                return true;
            }
        });

    }

    private void favorleriGetir() {

        final DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Favori");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                doktorlar.clear();
                doktorList.clear();
                favoriList.clear();
                hastaneList.clear();

                favoriID="";
                doktorID="";
                hastaneID="";
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Favori favori2 = snapshot.getValue(Favori.class);
                    favori2.setFavoriID(snapshot.getKey().toString());
                    if (favori2.getHastaID().equalsIgnoreCase(User.uID)) {
                        favoriList.add(favori2);
                    }
                }
                doktorGetir();
                reference.removeEventListener(this);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void doktorGetir() {
        if(favoriList.size()!=0){
            final DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users");
            reference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    doktorlar.clear();
                    doktorList.clear();

                    doktorID="";
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        Kisi kisi2 = snapshot.getValue(Kisi.class);
                        kisi2.setID(snapshot.getKey().toString());
                        for (Favori item : favoriList){
                            if (kisi2.getID().equalsIgnoreCase(item.getDoktorID())) {
                                doktorlar.add(kisi2.getAd() + " " + kisi2.getSoyad());
                                doktorList.add(kisi2);
                            }
                        }
                    }
                    hastaneGetir();
                    reference.removeEventListener(this);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
    }

    private void hastaneGetir() {
        if(favoriList.size()!=0){
            final DatabaseReference reference2 = FirebaseDatabase.getInstance().getReference("Hastane");
            reference2.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    hastaneList.clear();
                    hastaneID="";
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        Hastane hastane2 = snapshot.getValue(Hastane.class);

                        hastane2.id = snapshot.getKey().toString();
                        for (Favori item : favoriList){
                            if(hastane2.id.equalsIgnoreCase(item.getHastaneID())){
                                hastaneList.add(hastane2);
                            }
                        }
                    }
                    listeyiDoldur();
                    reference2.removeEventListener(this);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
    }

    private void listeyiDoldur() {
        CustomAdapter customAdapter = new CustomAdapter(this,doktorlar,doktorList);
        list.setAdapter(customAdapter);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                hastaneAdi="";
                bolumID="";
                doktorAdi="";
                doktorID="";
                hastaneID="";
                favoriID="";
                favoriID=favoriList.get(position).getFavoriID();
                doktorID=favoriList.get(position).getDoktorID();
                hastaneID=favoriList.get(position).getHastaneID();
                for (Kisi item : doktorList){
                    if(item.getID().equalsIgnoreCase(doktorID)){
                        doktorAdi=item.getAd()+" "+item.getSoyad();
                        bolumID=item.getBolumID();
                        break;
                    }
                }
                for(Hastane item2 : hastaneList){
                    if(item2.id.equalsIgnoreCase(hastaneID)){
                        hastaneAdi=item2.getHastaneAdi();
                    }
                }

                Intent ıntent = new Intent(Favorilerim.this,FavoriBilgi.class);
                ıntent.putExtra("favoriID",favoriID);
                ıntent.putExtra("doktorAdi",doktorAdi);
                ıntent.putExtra("hastaneAdi",hastaneAdi);
                ıntent.putExtra("bolumID",bolumID);
                startActivity(ıntent);
                finish();
            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(Favorilerim.this,HastaHomePage.class);
        startActivity(intent);
        finish();
    }
}
