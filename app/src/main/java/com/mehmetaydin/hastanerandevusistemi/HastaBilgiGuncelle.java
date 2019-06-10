package com.mehmetaydin.hastanerandevusistemi;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mehmetaydin.hastanerandevusistemi.Model.Kisi;
import com.mehmetaydin.hastanerandevusistemi.Model.User;

import java.util.HashMap;

public class HastaBilgiGuncelle extends AppCompatActivity {
    TextInputLayout tilAd, tilSoyad, tilSifre;
    Button btnGuncelle;
    Kisi kisi = new Kisi();
    String sifreDurumu = "",rol="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hasta_bilgi_guncelle);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Bilgilerimi Güncelle");

        ilklendirme();
        hastaGetir();
        sifreDurumu = "";
        rol="";
        Intent ıntent = getIntent();
        rol=ıntent.getStringExtra("rol");

        btnGuncelle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (adDogrula() && soyadDogrula() && sifreDogrula()) {
                    kisi.setAd(tilAd.getEditText().getText().toString());
                    kisi.setSoyad(tilSoyad.getEditText().getText().toString());
                    kisi.setSifre(tilSifre.getEditText().getText().toString());
                    sifreKontrol();
                }
            }
        });
    }

    private void hastaGetir() {
        final DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Kisi kisi3 = snapshot.getValue(Kisi.class);
                    kisi3.setID(snapshot.getKey().toString());
                    if (kisi3.getID().equalsIgnoreCase(User.uID)) {
                        tilAd.getEditText().setText(kisi3.getAd());
                        tilSoyad.getEditText().setText(kisi3.getSoyad());
                        break;
                    }
                }
                kisiGuncelle();
                reference.removeEventListener(this);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void sifreKontrol() {
        sifreDurumu = "";
        final DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Kisi kisi2 = snapshot.getValue(Kisi.class);
                    kisi2.setID(snapshot.getKey().toString());
                    if (kisi2.getID().equalsIgnoreCase(User.uID)) {
                        if(kisi2.getSifre().equalsIgnoreCase(kisi.getSifre()))
                        {
                            sifreDurumu = "1";
                            break;
                        }
                        else {
                            sifreDurumu = "";
                            break;
                        }
                    }
                }
                kisiGuncelle();
                reference.removeEventListener(this);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void kisiGuncelle() {
        if (sifreDurumu == "1") {
            if (kisi.getAd() != "0" && kisi.getAd() != "") {
                DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users").child(User.uID);
                final HashMap<String, Object> hashMap = new HashMap<>();
                hashMap.put("ad", kisi.getAd());
                hashMap.put("soyad", kisi.getSoyad());

                reference.updateChildren(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        kisi.setAd("0");
                        Toast.makeText(HastaBilgiGuncelle.this, "Başarılı", Toast.LENGTH_SHORT).show();
                    }
                });
            } else {
                Toast.makeText(HastaBilgiGuncelle.this, "Bu bilgilerde hasta bulunmaktadır!", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(HastaBilgiGuncelle.this, "Şifreniz Hatalı", Toast.LENGTH_SHORT).show();
        }
    }


    private void ilklendirme() {
        tilAd = findViewById(R.id.text_input_HastaGuncelle_ad);
        tilSoyad = findViewById(R.id.text_input_HastaGuncelle_soyad);
        tilSifre = findViewById(R.id.text_input_HastaGuncelle_sifre);
        btnGuncelle = findViewById(R.id.btnHastaGuncelleme);
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
        Intent intent;
        if(rol.equalsIgnoreCase("Hasta")){
            intent = new Intent(HastaBilgiGuncelle.this,Profilim.class);
        }else{

            intent = new Intent(HastaBilgiGuncelle.this,DoktorProfil.class);
        }
        startActivity(intent);
        finish();
    }
}
