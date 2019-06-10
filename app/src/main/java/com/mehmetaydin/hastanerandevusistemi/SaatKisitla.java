package com.mehmetaydin.hastanerandevusistemi;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mehmetaydin.hastanerandevusistemi.Model.Bolum;
import com.mehmetaydin.hastanerandevusistemi.Model.Hastane;
import com.mehmetaydin.hastanerandevusistemi.Model.Kisi;
import com.mehmetaydin.hastanerandevusistemi.Model.Kisit;
import com.mehmetaydin.hastanerandevusistemi.Model.Saat;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SaatKisitla extends AppCompatActivity {
    List<Hastane> hastaneList = new ArrayList<>();
    ArrayList<String> hastaneler = new ArrayList<String>();
    List<Bolum> bolumList = new ArrayList<>();
    ArrayList<String> bolumler = new ArrayList<String>();
    List<Kisi> doktorList = new ArrayList<>();
    ArrayList<String> doktorlar = new ArrayList<String>();
    List<Saat> saatList = new ArrayList<>();
    ArrayList<String> saatler = new ArrayList<String>();
    List<Kisit> kisitList = new ArrayList<>();
    ArrayList<String> kisitlar = new ArrayList<String>();

    Spinner spHastane, spBolum, spDoktor, spSaat;
    Button btnKisitla;

    String hastaneID = "", bolumID = "", doktorID = "", saatID = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saat_kisitla);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Saat Kısıtla");

        hastaneID = "";
        doktorID = "";
        bolumID = "";
        saatID = "";

        ilklendirme();
        hastaneGetir();

        spHastane.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                bolumler.clear();
                bolumList.clear();
                bolumID = "";
                doktorID = "";
                saatID = "";
                hastaneID = hastaneList.get(position).id;
                bolumleriGetir();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spBolum.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                bolumID = bolumList.get(position).getBolumID();
                doktorList.clear();
                doktorlar.clear();
                doktorID = "";
                saatID = "";
                doktorGetir();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spDoktor.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                doktorID = doktorList.get(position).getID();
                saatList.clear();
                saatler.clear();
                saatID = "";
                kisitGetir();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spSaat.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                saatID = saatList.get(position).getSaatID();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        btnKisitla.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (hastaneID != "" && bolumID != "" && doktorID != "" && saatID != "") {
                    if(kisitKontrol()){
                        kisitEkle();
                    }else {
                        Toast.makeText(SaatKisitla.this, "Bu kısıt zaten mevcut", Toast.LENGTH_SHORT).show();
                    }
                }
                else{
                    Toast.makeText(SaatKisitla.this, "Bilgileri eksiksiz seçiniz", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    private boolean kisitKontrol() {
        if(kisitList.size()!=0){
            for (Kisit item : kisitList){
                if(item.getDoktorID().equalsIgnoreCase(doktorID) && item.getSaatID().equalsIgnoreCase(saatID)){
                    return false;
                }
            }
            return true;
        }else {
            return true;
        }
    }

    private void kisitEkle() {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Kisit");
        HashMap<String,Object> hashMap = new HashMap<>();
        hashMap.put("doktorID",doktorID);
        hashMap.put("saatID",saatID);
        reference.push().setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                bolumID="";
                Toast.makeText(SaatKisitla.this, "Başarılı", Toast.LENGTH_SHORT).show();
            }
        });

    }
    private void kisitGetir(){
        if (hastaneID != "" && bolumID != "" && doktorID != "" ) {
            final DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Kisit");
            reference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    kisitlar.clear();
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        Kisit kisit2 = snapshot.getValue(Kisit.class);
                        kisit2.setKisitID(snapshot.getKey().toString());
                        if (kisit2.getDoktorID().equalsIgnoreCase(doktorID)) {
                            kisitList.add(kisit2);
                        }
                    }
                    saatGetir();
                    reference.removeEventListener(this);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
    }
    private void saatGetir() {
        if (hastaneID != "" && bolumID != "" && doktorID != "") {
            final DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Saat");
            reference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    saatler.clear();
                    saatList.clear();
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        Saat saat2 = snapshot.getValue(Saat.class);
                        saat2.setSaatID(snapshot.getKey().toString());
                        saatler.add(saat2.getSaatBilgisi());
                        saatList.add(saat2);
                    }
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(SaatKisitla.this, android.R.layout.simple_list_item_1, saatler);
                    adapter.setDropDownViewResource(android.R.layout.simple_list_item_1);
                    spSaat.setAdapter(adapter);
                    reference.removeEventListener(this);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
    }


    private void doktorGetir() {
        if (hastaneID != "" && bolumID != "") {
            final DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users");
            reference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    doktorlar.clear();
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        Kisi kisi2 = snapshot.getValue(Kisi.class);
                        kisi2.setID(snapshot.getKey().toString());
                        if (kisi2.getBolumID().equalsIgnoreCase(bolumID)) {
                            doktorlar.add(kisi2.getAd() + " " + kisi2.getSoyad());
                            doktorList.add(kisi2);
                        }
                    }
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(SaatKisitla.this, android.R.layout.simple_list_item_1, doktorlar);
                    adapter.setDropDownViewResource(android.R.layout.simple_list_item_1);
                    spDoktor.setAdapter(adapter);
                    reference.removeEventListener(this);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
    }

    private void bolumleriGetir() {
        if (hastaneID != "" && hastaneID != "0") {
            final DatabaseReference reference2 = FirebaseDatabase.getInstance().getReference("Bolum");
            reference2.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    bolumler.clear();
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        Bolum bolum2 = snapshot.getValue(Bolum.class);

                        bolum2.setBolumID(snapshot.getKey().toString());
                        if (bolum2.getHastaneID().equalsIgnoreCase(hastaneID)) {
                            bolumler.add(bolum2.getBolumAdi());
                            bolumList.add(bolum2);
                        }
                    }
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(SaatKisitla.this, android.R.layout.simple_list_item_1, bolumler);
                    adapter.setDropDownViewResource(android.R.layout.simple_list_item_1);
                    spBolum.setAdapter(adapter);
                    reference2.removeEventListener(this);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
    }

    private void hastaneGetir() {
        final DatabaseReference reference2 = FirebaseDatabase.getInstance().getReference("Hastane");
        reference2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                hastaneler.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Hastane hastane2 = snapshot.getValue(Hastane.class);

                    hastane2.id = snapshot.getKey().toString();
                    hastaneler.add(hastane2.getHastaneAdi());
                    hastaneList.add(hastane2);
                }
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(SaatKisitla.this, android.R.layout.simple_list_item_1, hastaneler);
                adapter.setDropDownViewResource(android.R.layout.simple_list_item_1);
                spHastane.setAdapter(adapter);
                reference2.removeEventListener(this);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void ilklendirme() {
        spHastane = findViewById(R.id.spKisitHastane);
        spBolum = findViewById(R.id.spKisitBolum);
        spDoktor = findViewById(R.id.spKisitDoktor);
        spSaat = findViewById(R.id.spKisitSaat);
        btnKisitla = findViewById(R.id.btnSaatKisitlama);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(SaatKisitla.this,Anasayfa.class);
        startActivity(intent);
        finish();
    }
}
