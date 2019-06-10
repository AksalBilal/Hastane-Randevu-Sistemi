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
import com.mehmetaydin.hastanerandevusistemi.Model.Kisi;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DoktorEkle extends AppCompatActivity {

    List<Hastane> hastaneList = new ArrayList<>();
    ArrayList<String> hastaneler = new ArrayList<String>();
    List<Bolum> bolumList = new ArrayList<>();
    ArrayList<String> bolumler = new ArrayList<String>();
    Spinner spHastane, spBolum;
    Button btnKayıt;
    TextInputLayout tilAd, tilTCKN, tilSoyad, tilSifre;
    String hastaneID = "", bolumID = "";
    Kisi kisi = new Kisi();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doktor_ekle);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Doktor Ekle");

        ilklendirme();
        hastaneGetir();

        spHastane.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                bolumler.clear();
                bolumList.clear();
                bolumID = "";
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
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        btnKayıt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (hastaneID != "" && bolumID != "" && tcknDogrula() && adDogrula() && soyadDogrula() && sifreDogrula()) {
                    kisi.setTCKN(tilTCKN.getEditText().getText().toString());
                    kisi.setSoyad(tilSoyad.getEditText().getText().toString());
                    kisi.setSifre(tilSifre.getEditText().getText().toString());
                    kisi.setAd(tilAd.getEditText().getText().toString());
                    kisi.setBolumID(bolumID);
                    doktorKontrol();
                }else{
                    Toast.makeText(DoktorEkle.this, "Hastane ve Bölüm Seçiniz!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void doktorKontrol() {
        final DatabaseReference reference2 = FirebaseDatabase.getInstance().getReference("Users");
        reference2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(kisi.getTCKN()== ""){
                    kisi.setTCKN("0");
                }
                if(kisi.getTCKN()!="0"){
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                        Kisi kisi2 = snapshot.getValue(Kisi.class);
                        if(kisi2.getTCKN().equals(kisi.getTCKN())){
                            kisi.setTCKN("0");
                            break;
                        }
                    }
                    kayitOl();
                    reference2.removeEventListener(this);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void kayitOl() {
        if(kisi.getTCKN()!= "0" && kisi.getTCKN() != ""){
            DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users");
            HashMap<String,Object> hashMap = new HashMap<>();
            hashMap.put("TCKN",kisi.getTCKN());
            hashMap.put("sifre",kisi.getSifre());
            hashMap.put("ad",kisi.getAd());
            hashMap.put("soyad",kisi.getSoyad());
            hashMap.put("rol","Doktor");
            hashMap.put("bolumID",kisi.getBolumID());

            reference.push().setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    kisi.setTCKN("0");
                    Toast.makeText(DoktorEkle.this, "Başarılı", Toast.LENGTH_SHORT).show();
                }
            });
        }else
        {
            Toast.makeText(DoktorEkle.this, "Bu TCKN ile Kayıtlı Kullanıcı Mevcut!", Toast.LENGTH_SHORT).show();
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
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(DoktorEkle.this, android.R.layout.simple_list_item_1, bolumler);
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
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(DoktorEkle.this, android.R.layout.simple_list_item_1, hastaneler);
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
        spHastane = findViewById(R.id.spDoktorEkleHastane);
        spBolum = findViewById(R.id.spDoktorEkleBolum);
        btnKayıt = findViewById(R.id.btnDoktorEkleme);
        tilAd = findViewById(R.id.text_input_DoktorEkle_ad);
        tilSifre = findViewById(R.id.text_input_DoktorEkle_sifre);
        tilSoyad = findViewById(R.id.text_input_DoktorEkle_soyad);
        tilTCKN = findViewById(R.id.text_input_DoktorEkle_tckn);
    }

    private boolean tcknDogrula() {
        String tcknInput = tilTCKN.getEditText().getText().toString().trim();
        if (tcknInput.isEmpty()) {
            tilTCKN.setError("Boş Geçilemez!");
            return false;
        } else if (tcknInput.length() != 11) {
            tilTCKN.setError("TCKN 11 Haneli Olmalıdır!");
            return false;
        } else {
            tilTCKN.setError(null);
            return true;
        }
    }

    private boolean sifreDogrula() {
        String sifreInput = tilSifre.getEditText().getText().toString().trim();
        if (sifreInput.isEmpty()) {
            tilSifre.setError("Boş Geçilemez!");
            return false;
        } else if (sifreInput.length() != 6) {
            tilSifre.setError("Şifre 6 Haneli Olmalıdır!");
            return false;
        } else {
            tilSifre.setError(null);
            return true;
        }
    }

    private boolean adDogrula() {
        String adInput = tilAd.getEditText().getText().toString().trim();
        if (adInput.isEmpty()) {
            tilAd.setError("Boş Geçilemez!");
            return false;
        } else {
            tilAd.setError(null);
            return true;
        }
    }

    private boolean soyadDogrula() {
        String soyadInput = tilSoyad.getEditText().getText().toString().trim();
        if (soyadInput.isEmpty()) {
            tilSoyad.setError("Boş Geçilemez!");
            return false;
        } else {
            tilSoyad.setError(null);
            return true;
        }
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(DoktorEkle.this,Anasayfa.class);
        startActivity(intent);
        finish();
    }
}
