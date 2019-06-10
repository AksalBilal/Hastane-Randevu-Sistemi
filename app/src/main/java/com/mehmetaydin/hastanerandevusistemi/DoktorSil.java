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
import com.mehmetaydin.hastanerandevusistemi.Model.User;

import java.util.ArrayList;
import java.util.List;

public class DoktorSil extends AppCompatActivity {
    List<Hastane> hastaneList = new ArrayList<>();
    ArrayList<String> hastaneler = new ArrayList<String>();
    List<Bolum> bolumList = new ArrayList<>();
    ArrayList<String> bolumler = new ArrayList<String>();
    List<Kisi> doktorList = new ArrayList<>();
    ArrayList<String> doktorlar = new ArrayList<String>();
    Spinner spHastane, spBolum, spDoktor;
    Button btnSil;
    String hastaneID = "", bolumID = "", doktorID = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doktor_sil);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Doktor Sil");

        hastaneID = "";
        doktorID = "";
        bolumID = "";

        ilklendirme();
        hastaneGetir();
        spHastane.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                bolumler.clear();
                bolumList.clear();
                doktorlar.clear();
                doktorList.clear();
                bolumID = "";
                doktorID = "";
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
                doktorID = "";
                doktorlar.clear();
                doktorList.clear();
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
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        btnSil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (hastaneID != "" && bolumID != "" && doktorID != "") {
                    doktorSil();
                } else {
                    Toast.makeText(DoktorSil.this, "Hastane, Bölüm ve Doktor Seçiniz!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void doktorSil() {
        if (bolumID != "" && hastaneID != "" && doktorID != "") {
            DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users").child(doktorID);
            reference.removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        Toast.makeText(DoktorSil.this, "Doktor Silindi", Toast.LENGTH_SHORT).show();
                        bolumID = "";
                        hastaneID = "";
                    } else {
                        Toast.makeText(DoktorSil.this, "Silme İşlemi Başarısız!", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        } else {
            Toast.makeText(this, "Doktor Bulunamadı!", Toast.LENGTH_SHORT).show();
        }
    }

    private void doktorGetir() {
        if (hastaneID != "" && bolumID != "") {
            final DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users");
            reference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    doktorlar.clear();
                    doktorList.clear();
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        Kisi kisi2 = snapshot.getValue(Kisi.class);
                        kisi2.setID(snapshot.getKey().toString());
                        if (kisi2.getBolumID().equalsIgnoreCase(bolumID)) {
                            doktorlar.add(kisi2.getAd() + " " + kisi2.getSoyad());
                            doktorList.add(kisi2);
                        }
                    }
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(DoktorSil.this, android.R.layout.simple_list_item_1, doktorlar);
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
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(DoktorSil.this, android.R.layout.simple_list_item_1, bolumler);
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
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(DoktorSil.this, android.R.layout.simple_list_item_1, hastaneler);
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
        spHastane = findViewById(R.id.spDoktorSilHastane);
        spBolum = findViewById(R.id.spDoktorSilBolum);
        spDoktor = findViewById(R.id.spDoktorSilDoktor);
        btnSil = findViewById(R.id.btnDoktorSilme);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(DoktorSil.this,Anasayfa.class);
        startActivity(intent);
        finish();
    }
}
