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

public class DoktorGuncelle extends AppCompatActivity {
  List<Hastane> hastaneList = new ArrayList<>();
  ArrayList<String> hastaneler = new ArrayList<String>();
  List<Bolum> bolumList = new ArrayList<>();
  ArrayList<String> bolumler = new ArrayList<String>();
  List<Kisi> doktorList = new ArrayList<>();
  ArrayList<String> doktorlar = new ArrayList<String>();
  Spinner spHastane, spBolum, spDoktor;
  TextInputLayout tilAd, tilSoyad, tilSifre;
  Button btnGuncelle;
  String hastaneID = "", bolumID = "", doktorID = "";
  Kisi kisi = new Kisi();
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_doktor_guncelle);
    ActionBar actionBar = getSupportActionBar();
    actionBar.setTitle("Doktor Güncelle");

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
        doktorList.clear();
        doktorlar.clear();
        bolumID = "";
        doktorID = "";
        hastaneID = hastaneList.get(position).id;

        tilSifre.getEditText().setText("");
        tilSoyad.getEditText().setText("");
        tilAd.getEditText().setText("");
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
        doktorlar.clear();
        doktorList.clear();
        doktorID = "";
        tilSifre.getEditText().setText("");
        tilSoyad.getEditText().setText("");
        tilAd.getEditText().setText("");
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
        tilAd.getEditText().setText(doktorList.get(position).getAd());
        tilSoyad.getEditText().setText(doktorList.get(position).getSoyad());
        tilSifre.getEditText().setText(doktorList.get(position).getSifre());
      }

      @Override
      public void onNothingSelected(AdapterView<?> parent) {

      }
    });
    btnGuncelle.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        if (hastaneID != "" && bolumID != "" && doktorID != "" && adDogrula() && soyadDogrula() && sifreDogrula()) {
          kisi.setAd(tilAd.getEditText().getText().toString());
          kisi.setSoyad(tilSoyad.getEditText().getText().toString());
          kisi.setSifre(tilSifre.getEditText().getText().toString());
          doktorKontrol();

        } else {
          Toast.makeText(DoktorGuncelle.this, "Hastane, Bölüm ve Doktor Seçiniz!", Toast.LENGTH_SHORT).show();
        }
      }
    });
  }

  private void doktorKontrol() {
    final DatabaseReference reference2 = FirebaseDatabase.getInstance().getReference("Users");
    reference2.addValueEventListener(new ValueEventListener() {
      @Override
      public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

        for (DataSnapshot snapshot : dataSnapshot.getChildren()){
          Kisi kisi2 = snapshot.getValue(Kisi.class);
          if(kisi2.getAd().equalsIgnoreCase(kisi.getAd()) && kisi2.getSoyad().equalsIgnoreCase(kisi.getSoyad()) && kisi2.getSifre().equalsIgnoreCase(kisi.getSifre())){
            kisi.setAd("0");
            Toast.makeText(DoktorGuncelle.this, "Bu Doktordan Mevcut!", Toast.LENGTH_SHORT).show();
            break;
          }
        }
        doktorGuncelle();
        reference2.removeEventListener(this);
      }

      @Override
      public void onCancelled(@NonNull DatabaseError databaseError) {

      }
    });
  }

  private void doktorGuncelle() {
    if(kisi.getAd() != "0" && kisi.getAd() != ""){
      DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users").child(doktorID);
      final HashMap<String,Object> hashMap = new HashMap<>();
      hashMap.put("ad",kisi.getAd());
      hashMap.put("soyad",kisi.getSoyad());
      hashMap.put("sifre",kisi.getSifre());

      reference.updateChildren(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
        @Override
        public void onComplete(@NonNull Task<Void> task) {
          kisi.setAd("0");
          Toast.makeText(DoktorGuncelle.this, "Başarılı", Toast.LENGTH_SHORT).show();
        }
      });
    }else
    {
      Toast.makeText(DoktorGuncelle.this, "Bu bilgilerde doktor bulunmaktadır!", Toast.LENGTH_SHORT).show();
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
          ArrayAdapter<String> adapter = new ArrayAdapter<String>(DoktorGuncelle.this, android.R.layout.simple_list_item_1, doktorlar);
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
          ArrayAdapter<String> adapter = new ArrayAdapter<String>(DoktorGuncelle.this, android.R.layout.simple_list_item_1, bolumler);
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
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(DoktorGuncelle.this, android.R.layout.simple_list_item_1, hastaneler);
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
    spHastane = findViewById(R.id.spDoktorGuncelleHastane);
    spBolum = findViewById(R.id.spDoktorGuncelleBolum);
    spDoktor = findViewById(R.id.spDoktorGuncelleDoktor);
    tilAd = findViewById(R.id.text_input_HastaGuncelle_ad);
    tilSoyad = findViewById(R.id.text_input_HastaGuncelle_soyad);
    tilSifre = findViewById(R.id.text_input_HastaGuncelle_sifre);
    btnGuncelle = findViewById(R.id.btnDoktorGuncelleme);
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
    Intent intent = new Intent(DoktorGuncelle.this,Anasayfa.class);
    startActivity(intent);
    finish();
  }
}
