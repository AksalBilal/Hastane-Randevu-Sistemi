package com.mehmetaydin.hastanerandevusistemi;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class BolumGuncelle extends AppCompatActivity {
    List<Hastane> hastaneList = new ArrayList<>();
    ArrayList<String> hastaneler = new ArrayList<String>();
    List<Bolum> bolumList = new ArrayList<>();
    ArrayList<String> bolumler = new ArrayList<String>();
    Spinner spHastane, spBolum;
    TextInputLayout tilAd;
    Button btnGuncelle;
    String hastaneID="",bolumID="";
    Bolum bolum = new Bolum();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bolum_guncelle);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Bölüm Güncelle");

        ilklendirme();
        hastaneGetir();

        spHastane.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                bolumler.clear();
                bolumList.clear();
                bolumID="";
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
                tilAd.getEditText().setText(bolumList.get(position).getBolumAdi());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        btnGuncelle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(hastaneID!="" && bolumID != "" && bolumAdDogrula()){
                    bolum.setBolumAdi(tilAd.getEditText().getText().toString());
                    bolumKontrol();
                }else
                {
                    Toast.makeText(BolumGuncelle.this, "Hastane ve Bölüm Seçiniz!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void bolumKontrol() {
        final DatabaseReference reference2 = FirebaseDatabase.getInstance().getReference("Bolum");
        reference2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    Bolum bolum3 = snapshot.getValue(Bolum.class);
                    if(bolum3.getBolumAdi().equalsIgnoreCase(bolum.getBolumAdi()) && bolum3.getHastaneID().equalsIgnoreCase(hastaneID)){
                        bolum.setBolumAdi("0");
                        Toast.makeText(BolumGuncelle.this, "Bu Bolumden Mevcut!", Toast.LENGTH_SHORT).show();
                        break;
                    }
                }
                bolumGuncelle();
                reference2.removeEventListener(this);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void bolumGuncelle() {
        if(bolum.getBolumAdi() != "0" && bolum.getBolumAdi() != ""){
            DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Bolum").child(bolumID);
            final HashMap<String,Object> hashMap = new HashMap<>();
            hashMap.put("bolumAdi",bolum.getBolumAdi());
            hashMap.put("hastaneID",hastaneID);

            reference.updateChildren(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    bolum.setBolumAdi("0");
                    Toast.makeText(BolumGuncelle.this, "Başarılı", Toast.LENGTH_SHORT).show();
                }
            });
        }else
        {
            Toast.makeText(BolumGuncelle.this, "Bu isimde Bölüm bulunmaktadır!", Toast.LENGTH_SHORT).show();
        }
    }


    private void bolumleriGetir() {
        if(hastaneID != "" && hastaneID != "0"){
            final DatabaseReference reference2 = FirebaseDatabase.getInstance().getReference("Bolum");
            reference2.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    bolumler.clear();
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                        Bolum bolum2 = snapshot.getValue(Bolum.class);

                        bolum2.setBolumID(snapshot.getKey().toString());
                        if(bolum2.getHastaneID().equalsIgnoreCase(hastaneID)){
                            bolumler.add(bolum2.getBolumAdi());
                            bolumList.add(bolum2);
                        }
                    }
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(BolumGuncelle.this ,android.R.layout.simple_list_item_1,bolumler);
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
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    Hastane hastane2 = snapshot.getValue(Hastane.class);

                    hastane2.id = snapshot.getKey().toString();
                    hastaneler.add(hastane2.getHastaneAdi());
                    hastaneList.add(hastane2);
                }
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(BolumGuncelle.this ,android.R.layout.simple_list_item_1,hastaneler);
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
        tilAd = findViewById(R.id.text_input_bolum_Guncelle_ad);
        spHastane = findViewById(R.id.spBolumGuncelleHastane);
        spBolum = findViewById(R.id.spBolumGuncelleBolum);
        btnGuncelle = findViewById(R.id.btnBolumGuncelleme);
    }
    private boolean bolumAdDogrula(){
        String adInput = tilAd.getEditText().getText().toString().trim();
        if(adInput.isEmpty()){
            tilAd.setError("Boş Geçilemez!");
            return false;
        }
        else {
            tilAd.setError(null);
            return true;
        }
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(BolumGuncelle.this,Anasayfa.class);
        startActivity(intent);
        finish();
    }
}
