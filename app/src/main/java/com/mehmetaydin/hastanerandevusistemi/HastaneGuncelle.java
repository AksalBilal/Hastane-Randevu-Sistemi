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
import com.mehmetaydin.hastanerandevusistemi.Model.Hastane;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class HastaneGuncelle extends AppCompatActivity {
    List<Hastane> hastaneList = new ArrayList<>();
    ArrayList<String> hastaneler = new ArrayList<String>();
    Spinner spinner;
    TextInputLayout tilAd, tilAdres;
    Button btnGuncelle;
    Hastane hastane = new Hastane();
    String hastaneID = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hastane_guncelle);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Hastane Güncelle");
        hastane.setHastaneAdi("0");

        ilklendirme();
        hastaneGetir();

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                tilAd.getEditText().setText(hastaneList.get(position).getHastaneAdi());
                tilAdres.getEditText().setText(hastaneList.get(position).getAdres());
                hastaneID=hastaneList.get(position).id;

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        btnGuncelle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (hastaneler != null && hastaneAdDogrula() && hastaneAdresDogrula()) {
                    hastane.setHastaneAdi(tilAd.getEditText().getText().toString());
                    hastane.setAdres(tilAdres.getEditText().getText().toString());
                    hastaneKontrol();
                }
            }
        });
    }

    private void hastaneKontrol() {
        final DatabaseReference reference2 = FirebaseDatabase.getInstance().getReference("Hastane");
        reference2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (hastane.getHastaneAdi() == "") {
                    hastane.setHastaneAdi("0");
                }
                if (hastane.getHastaneAdi() != "0") {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        Hastane hastane2 = snapshot.getValue(Hastane.class);
                        if (hastane2.getHastaneAdi().equalsIgnoreCase(hastane.getHastaneAdi()) && hastane2.getAdres().equalsIgnoreCase(hastane.getAdres())) {
                            hastane.setHastaneAdi("0");
                            break;
                        }
                    }
                    hataneGuncelle();
                    reference2.removeEventListener(this);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void hataneGuncelle() {
        if(hastane.getHastaneAdi()!= "0" && hastane.getHastaneAdi() != ""){
            DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Hastane").child(hastaneID);
            final HashMap<String,Object> hashMap = new HashMap<>();
            hashMap.put("hastaneAdi",hastane.getHastaneAdi());
            hashMap.put("adres",hastane.getAdres());
            reference.updateChildren(hashMap);
            Toast.makeText(HastaneGuncelle.this, "İşlem Başarılı", Toast.LENGTH_SHORT).show();
        }else
        {
            Toast.makeText(HastaneGuncelle.this, "Bu isimde Hastane bulunmaktadır!", Toast.LENGTH_SHORT).show();
        }
    }


    private void ilklendirme() {
        spinner = findViewById(R.id.spinnerHastaneGuncelle);
        btnGuncelle = findViewById(R.id.btnH_Guncelleme);
        tilAd = findViewById(R.id.text_input_hastane_Guncelle_ad);
        tilAdres = findViewById(R.id.text_input_hastane_Guncelle_adres);
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
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(HastaneGuncelle.this, android.R.layout.simple_list_item_1, hastaneler);
                adapter.setDropDownViewResource(android.R.layout.simple_list_item_1);
                spinner.setAdapter(adapter);
                reference2.removeEventListener(this);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private boolean hastaneAdDogrula() {
        String adInput = tilAd.getEditText().getText().toString().trim();
        if (adInput.isEmpty()) {
            tilAd.setError("Boş Geçilemez!");
            return false;
        } else {
            tilAd.setError(null);
            return true;
        }
    }

    private boolean hastaneAdresDogrula() {
        String adresInput = tilAdres.getEditText().getText().toString().trim();
        if (adresInput.isEmpty()) {
            tilAdres.setError("Boş Geçilemez!");
            return false;
        } else {
            tilAdres.setError(null);
            return true;
        }
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(HastaneGuncelle.this,Anasayfa.class);
        startActivity(intent);
        finish();
    }
}
