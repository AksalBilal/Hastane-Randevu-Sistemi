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
import com.mehmetaydin.hastanerandevusistemi.Model.Kisi;
import com.mehmetaydin.hastanerandevusistemi.Model.Kisit;
import com.mehmetaydin.hastanerandevusistemi.Model.Saat;

import java.util.ArrayList;
import java.util.List;

public class KisitKaldir extends AppCompatActivity {
    List<Kisi> doktorList = new ArrayList<>();
    ArrayList<String> doktorlar = new ArrayList<String>();
    List<Saat> saatList = new ArrayList<>();
    ArrayList<String> saatler = new ArrayList<String>();
    List<Kisit> kisitList = new ArrayList<>();
    ArrayList<String> kisitlar = new ArrayList<String>();

    Spinner spDoktor, spSaat;
    Button btnKisitla;

    String doktorID = "", saatID = "", kisitID = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kisit_kaldir);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Kısıt Kaldırma");

        doktorID = "";
        saatID = "";
        kisitID = "";
        ilklendirme();
        kisitGetir();
        doktorList.clear();
        doktorlar.clear();

        spDoktor.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                saatler.clear();
                saatList.clear();
                saatID = "";
                kisitID = "";
                doktorID = doktorList.get(position).getID().toString();
                saatGetir();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spSaat.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                saatID = saatList.get(position).getSaatID().toString();
                kisitID = "";
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        btnKisitla.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (saatID != "" && doktorID != "") {
                    kisitBul();
                }
            }
        });
    }

    private void kisitBul() {
        final DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Kisit");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                kisitlar.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Kisit kisit2 = snapshot.getValue(Kisit.class);
                    if (kisit2.getDoktorID().equalsIgnoreCase(doktorID) && kisit2.getSaatID().equalsIgnoreCase(saatID)) {
                        kisitID = snapshot.getKey().toString();
                    }

                }
                kisitKaldir();
                reference.removeEventListener(this);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void kisitKaldir() {
        if (doktorID != "0" && saatID != "" && kisitID != "") {
            DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Kisit").child(kisitID);
            reference.removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        Toast.makeText(KisitKaldir.this, "Kısıt Kaldırıldı", Toast.LENGTH_SHORT).show();

                    } else {
                        Toast.makeText(KisitKaldir.this, "Kaldırma İşlemi Başarısız!", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        } else {
            Toast.makeText(this, "Kısıt Bulunamadı!", Toast.LENGTH_SHORT).show();
        }
    }

    private void saatGetir() {
        if (doktorID != "") {
            final DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Saat");
            reference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    saatler.clear();
                    saatList.clear();
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        Saat saat2 = snapshot.getValue(Saat.class);
                        saat2.setSaatID(snapshot.getKey().toString());
                        for (Kisit item : kisitList) {
                            if (item.getDoktorID().equalsIgnoreCase(doktorID) && item.getSaatID().equalsIgnoreCase(saat2.getSaatID().toString())) {
                                saatler.add(saat2.getSaatBilgisi());
                                saatList.add(saat2);
                            }
                        }
                    }
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(KisitKaldir.this, android.R.layout.simple_list_item_1, saatler);
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

    private void kisitGetir() {
        final DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Kisit");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                kisitlar.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Kisit kisit2 = snapshot.getValue(Kisit.class);
                    kisit2.setKisitID(snapshot.getKey().toString());
                    kisitList.add(kisit2);
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
        if (kisitList.size() != 0) {
            final DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users");
            reference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    doktorlar.clear();
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        Kisi kisi2 = snapshot.getValue(Kisi.class);
                        kisi2.setID(snapshot.getKey().toString());
                        for (Kisit item : kisitList) {
                            if (kisi2.getID().equalsIgnoreCase(item.getDoktorID())) {
                                doktorlar.add(kisi2.getAd() + " " + kisi2.getSoyad());
                                doktorList.add(kisi2);
                            }
                        }
                    }
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(KisitKaldir.this, android.R.layout.simple_list_item_1, doktorlar);
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

    private void ilklendirme() {
        spDoktor = findViewById(R.id.spKisitKaldirDoktor);
        spSaat = findViewById(R.id.spKisitKaldirSaat);
        btnKisitla = findViewById(R.id.btnSaatKisitKaldirma);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(KisitKaldir.this,Anasayfa.class);
        startActivity(intent);
        finish();
    }
}


