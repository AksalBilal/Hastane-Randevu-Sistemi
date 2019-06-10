package com.mehmetaydin.hastanerandevusistemi;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mehmetaydin.hastanerandevusistemi.Model.Kisi;
import com.mehmetaydin.hastanerandevusistemi.Model.Randevu;
import com.mehmetaydin.hastanerandevusistemi.Model.Saat;
import com.mehmetaydin.hastanerandevusistemi.Model.User;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DoktorRandevu extends AppCompatActivity {
    ArrayList<Randevu> randevuList = new ArrayList<>();
    List<Kisi> hastaList = new ArrayList<>();
    List<Kisi> gethastaList = new ArrayList<>();
    ArrayList<String> hastalar = new ArrayList<>();
    ArrayList<Saat> saatList = new ArrayList<>();
    ArrayList<Saat> getsaatist = new ArrayList<>();
    ArrayList<String> saatler = new ArrayList<>();
    ArrayList<String> gidecek = new ArrayList<>();

    ListView list;
    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doktor_randevu);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Randevular");

        ilklendirme();
        randevulariGetir();

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.nav_profile_doktor:
                        Intent intent = new Intent(DoktorRandevu.this, DoktorProfil.class);
                        startActivity(intent);
                        finish();
                        break;
                }
                return true;
            }
        });
    }

    private void randevulariGetir() {
        Date simdikiZaman = new Date();
        DateFormat df_gun = new SimpleDateFormat("dd");
        DateFormat df_ay = new SimpleDateFormat("MM");
        DateFormat df_yil = new SimpleDateFormat("yyyy");
        final int gun = Integer.parseInt(df_gun.format(simdikiZaman));
        final int ay = Integer.parseInt(df_ay.format(simdikiZaman));
        final int yil = Integer.parseInt(df_yil.format(simdikiZaman));
        final DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Randevu");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                randevuList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Randevu randevu2 = snapshot.getValue(Randevu.class);
                    randevu2.setRandevuID(snapshot.getKey().toString());
                    String[] Tarih_ayir = randevu2.getTarih().split("/");
                    if (randevu2.getDoktorID().equalsIgnoreCase(User.uID)) {
                        if (yil == Integer.parseInt(Tarih_ayir[2])) {
                            if (ay == Integer.parseInt(Tarih_ayir[1])) {
                                if (gun <= Integer.parseInt(Tarih_ayir[0])) {
                                    randevuList.add(randevu2);
                                }
                            } else if (Integer.parseInt(Tarih_ayir[1]) == (ay + 1)) {
                                if (gun >= Integer.parseInt(Tarih_ayir[0])) {
                                    randevuList.add(randevu2);
                                }
                            }
                        }
                    }
                }
                hastaGetir();
                reference.removeEventListener(this);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void hastaGetir() {
        final DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                hastaList.clear();
                hastalar.clear();
                gethastaList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Kisi kisi2 = snapshot.getValue(Kisi.class);
                    kisi2.setID(snapshot.getKey().toString());
                    if (kisi2.getRol().equalsIgnoreCase("Hasta")) {
                        hastalar.add(kisi2.getAd() + " " + kisi2.getSoyad());
                        hastaList.add(kisi2);
                    }
                }
                hastaSirala();
                reference.removeEventListener(this);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void hastaSirala() {
        gethastaList.clear();
        if (randevuList.size() != 0) {
            for (Randevu item : randevuList) {
                for (Kisi item2 : hastaList) {
                    if (item.getHastaID().equalsIgnoreCase(item2.getID())) {
                        gethastaList.add(item2);
                        break;
                    }
                }
            }
        }
        saatGetir();
    }

    private void saatGetir() {
        final DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Saat");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                saatler.clear();
                saatList.clear();
                getsaatist.clear();

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Saat saat2 = snapshot.getValue(Saat.class);
                    saat2.setSaatID(snapshot.getKey().toString());
                    saatList.add(saat2);
                    saatler.add(saat2.getSaatBilgisi());
                }
                saatSirala();
                reference.removeEventListener(this);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void saatSirala() {
        int sayac = 0;
        getsaatist.clear();
        if (randevuList.size() != 0) {
            for (Randevu item : randevuList) {
                for (Saat item2 : saatList) {
                    if (item.getSaatID().equalsIgnoreCase(item2.getSaatID())) {
                        gidecek.add(gethastaList.get(sayac).getAd().toUpperCase()+" "+ gethastaList.get(sayac).getSoyad().toUpperCase()+"\n"+item.getTarih()+"       "+item2.getSaatBilgisi());
                        sayac++;
                    }
                }
            }
        }
        listeyiDoldur();
    }

    private void listeyiDoldur() {
        CustomAdapterRandevu customAdapterRandevu = new CustomAdapterRandevu(this,gidecek,randevuList);
        list.setAdapter(customAdapterRandevu);

    }

    private void ilklendirme() {
        list = findViewById(R.id.listViewDoktorRandevu);
        bottomNavigationView = findViewById(R.id.bottomNavigationDoktor);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(DoktorRandevu.this,DoktorAnasayfa.class);
        startActivity(intent);
        finish();
    }
}
