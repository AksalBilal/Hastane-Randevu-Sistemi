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
import android.widget.TextView;
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

public class BolumEkle extends AppCompatActivity {
    List<Hastane> hastaneList = new ArrayList<>();
    ArrayList<String> hastaneler = new ArrayList<String>();
    public Hastane hastane =new Hastane();
    Spinner spinner;
    TextInputLayout tilBolumAdi;
    Button btnBolumEkle;
    Bolum bolum2 = new Bolum();
    String HastaneID = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bolum_ekle);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Bölüm Ekle");

        ilklendirme();
        hastaneGetir();

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                HastaneID = hastaneList.get(position).id;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        btnBolumEkle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(hastaneler!=null && bolumAdDogrula()){
                    bolum2.setBolumAdi(tilBolumAdi.getEditText().getText().toString());
                    bolumKontrol();
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
                    Bolum bolum = snapshot.getValue(Bolum.class);
                    if(bolum.getBolumAdi().equalsIgnoreCase(bolum2.getBolumAdi()) && bolum.getHastaneID().equalsIgnoreCase(HastaneID)){
                        bolum2.setBolumAdi("0");
                        Toast.makeText(BolumEkle.this, "Bu Bolumden Mevcut!", Toast.LENGTH_SHORT).show();
                        break;
                    }
                }
                bolumEkle();
                reference2.removeEventListener(this);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void bolumEkle() {
        if(bolum2.getBolumAdi() != "0" && bolum2.getBolumAdi() != ""){
            DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Bolum");
            final HashMap<String,Object> hashMap = new HashMap<>();
            hashMap.put("bolumAdi",bolum2.getBolumAdi());
            hashMap.put("hastaneID",HastaneID);

            reference.push().setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    bolum2.setBolumAdi("0");
                    Toast.makeText(BolumEkle.this, "Başarılı", Toast.LENGTH_SHORT).show();
                }
            });
        }else
        {
            Toast.makeText(BolumEkle.this, "Bu isimde Bölüm bulunmaktadır!", Toast.LENGTH_SHORT).show();
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
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(BolumEkle.this ,android.R.layout.simple_list_item_1,hastaneler);
                adapter.setDropDownViewResource(android.R.layout.simple_list_item_1);
                spinner.setAdapter(adapter);
                reference2.removeEventListener(this);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void ilklendirme() {
        spinner = findViewById(R.id.spinnerBolumEkle);
        tilBolumAdi = findViewById(R.id.text_input_bolum_ekle_ad);
        btnBolumEkle = findViewById(R.id.btnBolumEkleme);
    }
    private boolean bolumAdDogrula(){
        String adInput = tilBolumAdi.getEditText().getText().toString().trim();
        if(adInput.isEmpty()){
            tilBolumAdi.setError("Boş Geçilemez!");
            return false;
        }
        else {
            tilBolumAdi.setError(null);
            return true;
        }
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(BolumEkle.this,Anasayfa.class);
        startActivity(intent);
        finish();
    }
}
