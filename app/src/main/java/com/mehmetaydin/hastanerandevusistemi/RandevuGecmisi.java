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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mehmetaydin.hastanerandevusistemi.Model.Bolum;
import com.mehmetaydin.hastanerandevusistemi.Model.Favori;
import com.mehmetaydin.hastanerandevusistemi.Model.Hastane;
import com.mehmetaydin.hastanerandevusistemi.Model.Kisi;
import com.mehmetaydin.hastanerandevusistemi.Model.Kisit;
import com.mehmetaydin.hastanerandevusistemi.Model.Randevu;
import com.mehmetaydin.hastanerandevusistemi.Model.Saat;
import com.mehmetaydin.hastanerandevusistemi.Model.User;

import java.util.ArrayList;

public class RandevuGecmisi extends AppCompatActivity {
    ArrayList<Randevu> randevuList = new ArrayList<>();
    ArrayList<String> randevular = new ArrayList<String>();
    ArrayList<Kisi> doktorList = new ArrayList<>();
    ArrayList<String> doktorlar = new ArrayList<String>();
    ArrayList<Bolum> bolumList = new ArrayList<>();
    ArrayList<String> bolumler = new ArrayList<String>();
    ArrayList<Saat> saatList = new ArrayList<>();
    ArrayList<String> saatler = new ArrayList<String>();
    ArrayList<String> gidecek = new ArrayList<>();
    ArrayList<Kisi> getDoktorList = new ArrayList<>();
    ArrayList<Bolum> getBolumList = new ArrayList<>();

    String doktorID = "", randevuID = "", bolumAdi = "", saatBilgi = "", bolumID = "";
    String doktorAdi = "", hastaneID = "";

    ListView list;
    BottomNavigationView bottomNavigationView;
    FloatingActionButton floatingActionButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_randevu_gecmisi);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Randevu Geçmişi");

        doktorlar.clear();
        doktorList.clear();
        saatler.clear();
        saatList.clear();
        bolumler.clear();
        bolumList.clear();
        randevuList.clear();
        randevular.clear();
        gidecek.clear();
        getDoktorList.clear();
        getBolumList.clear();

        randevuID = "";
        saatBilgi = "";
        doktorID = "";
        bolumAdi = "";
        doktorAdi = "";
        hastaneID = "";
        bolumID = "";

        floatingActionButton = findViewById(R.id.floatingActionHome);
        list = findViewById(R.id.listViewRandevu);
        bottomNavigationView = findViewById(R.id.bottomNavigationHome);

        randevuGetir();
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RandevuGecmisi.this, RandevuAl.class);
                startActivity(intent);
                finish();
            }
        });

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.nav_favoriler:
                        Intent intent2 = new Intent(RandevuGecmisi.this, Favorilerim.class);
                        startActivity(intent2);
                        finish();
                        break;
                    case R.id.nav_profile:
                        Intent intent3 = new Intent(RandevuGecmisi.this, Profilim.class);
                        startActivity(intent3);
                        finish();
                        break;
                }
                return true;
            }
        });


    }

    private void randevuGetir() {
        final DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Randevu");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                doktorlar.clear();
                doktorList.clear();
                randevular.clear();
                randevuList.clear();
                bolumList.clear();
                getBolumList.clear();
                gidecek.clear();
                getDoktorList.clear();
                randevuID = "";
                bolumAdi = "";
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Randevu randevu2 = snapshot.getValue(Randevu.class);
                    randevu2.setRandevuID(snapshot.getKey().toString());
                    if (randevu2.getHastaID().equalsIgnoreCase(User.uID)) {
                        randevuList.add(randevu2);
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
        final DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                doktorlar.clear();
                doktorList.clear();
                getDoktorList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Kisi kisi2 = snapshot.getValue(Kisi.class);
                    kisi2.setID(snapshot.getKey().toString());
                    if (kisi2.getRol().equalsIgnoreCase("Doktor")) {
                        doktorlar.add(kisi2.getAd() + " " + kisi2.getSoyad());
                        doktorList.add(kisi2);
                    }
                }
                doktorSirala();
                reference.removeEventListener(this);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void doktorSirala() {
        getDoktorList.clear();
        if (randevuList.size() != 0) {
            for (Randevu item : randevuList) {
                for (Kisi item2 : doktorList) {
                    if (item.getDoktorID().equalsIgnoreCase(item2.getID())) {
                        getDoktorList.add(item2);
                        break;
                    }
                }
            }
        }
        bolumGetir();
    }

    private void bolumGetir() {
        if (doktorList.size() != 0) {
            final DatabaseReference reference2 = FirebaseDatabase.getInstance().getReference("Bolum");
            reference2.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    bolumler.clear();
                    gidecek.clear();
                    getBolumList.clear();
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        Bolum bolum2 = snapshot.getValue(Bolum.class);

                        bolum2.setBolumID(snapshot.getKey().toString());
                        bolumler.add(bolum2.getBolumAdi());
                        bolumList.add(bolum2);

                    }
                    bolumleriSirala();
                    reference2.removeEventListener(this);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
    }

    private void bolumleriSirala() {
        getBolumList.clear();
        if (getDoktorList.size() != 0) {
            for (Kisi item : getDoktorList) {
                for (Bolum item2 : bolumList) {
                    if (item.getBolumID().equalsIgnoreCase(item2.getBolumID())) {
                        getBolumList.add(item2);
                    }
                }
            }
        }
        tarihSirala();
    }

    private void tarihSirala() {
        int sayac = 0;
        gidecek.clear();
        if (randevuList.size() != 0 && getBolumList.size() != 0) {
            for (Randevu item : randevuList) {
                gidecek.add(getBolumList.get(sayac).getBolumAdi() + " Tarihi : " + item.getTarih());
                sayac++;
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

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Saat saat2 = snapshot.getValue(Saat.class);
                    saat2.setSaatID(snapshot.getKey().toString());

                    if (randevuList.size() != 0) {
                        for (Randevu item : randevuList) {
                            if (saat2.getSaatID().equalsIgnoreCase(item.getSaatID())) {
                                saatList.add(saat2);
                                saatler.add(saat2.getSaatBilgisi());
                            }
                        }
                    }
                }
                listeyiDoldur();
                reference.removeEventListener(this);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void listeyiDoldur() {
        CustomAdapterRandevu customAdapterRandevu = new CustomAdapterRandevu(this, gidecek, randevuList);
        list.setAdapter(customAdapterRandevu);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                randevuID = "";
                saatBilgi = "";
                bolumAdi = "";
                doktorAdi = "";
                hastaneID = "";
                bolumID = "";
                randevuID = randevuList.get(position).getRandevuID();
                doktorID = randevuList.get(position).getDoktorID();
                bolumAdi = getBolumList.get(position).getBolumAdi();
                bolumID = getBolumList.get(position).getBolumID();


                for (Bolum item : getBolumList) {
                    if (item.getBolumID().equalsIgnoreCase(bolumID)) {
                        hastaneID = item.getHastaneID();
                        break;
                    }
                }

                for (Randevu item2 : randevuList) {
                    if (randevuID.equalsIgnoreCase(item2.getRandevuID())) {

                        for (Saat item3 : saatList) {
                            if (item3.getSaatID().equalsIgnoreCase(item2.getSaatID())) {
                                saatBilgi = item3.getSaatBilgisi();
                                break;
                            }
                        }
                        for (Kisi kisi : doktorList) {
                            if (item2.getDoktorID().equalsIgnoreCase(kisi.getID())) {
                                doktorAdi = kisi.getAd() + " " + kisi.getSoyad();
                                break;
                            }
                        }
                    }
                }

                Intent ıntent = new Intent(RandevuGecmisi.this, RandevuGecmisDetay.class);
                ıntent.putExtra("randevuID", randevuID);
                ıntent.putExtra("doktorAdi", doktorAdi);
                ıntent.putExtra("doktorID", doktorID);
                ıntent.putExtra("hastaneID", hastaneID);
                ıntent.putExtra("saatBilgi", saatBilgi);
                ıntent.putExtra("bolumAdi", bolumAdi);
                ıntent.putExtra("tarihBilgi", randevuList.get(position).getTarih());
                startActivity(ıntent);
                finish();


            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(RandevuGecmisi.this,HastaHomePage.class);
        startActivity(intent);
        finish();
    }
}
