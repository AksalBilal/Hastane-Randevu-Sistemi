package com.mehmetaydin.hastanerandevusistemi;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
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

import java.util.HashMap;

public class RegisterActivity extends AppCompatActivity {
    Button btnKayıt;
    TextInputLayout tilAd, tilTCKN, tilSoyad, tilSifre;
    Kisi kisi = new Kisi();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ilklendirme();
        kisi.setTCKN("");
        btnKayıt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(tcknDogrula() && adDogrula() && soyadDogrula() && sifreDogrula()){
                    kisi.setAd(tilAd.getEditText().getText().toString());
                    kisi.setSifre(tilSifre.getEditText().getText().toString());
                    kisi.setSoyad(tilSoyad.getEditText().getText().toString());
                    kisi.setTCKN(tilTCKN.getEditText().getText().toString());
                    hastaKontrol();
                }
            }
        });
    }

    private void hastaKontrol() {
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
            hashMap.put("rol","Hasta");
            hashMap.put("bolumID","0");

            reference.push().setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    kisi.setTCKN("0");
                    Toast.makeText(RegisterActivity.this, "Başarılı", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(RegisterActivity.this,LoginActivity.class);
                    startActivity(intent);
                    finish();
                }
            });
        }else
        {
            Toast.makeText(RegisterActivity.this, "Bu TCKN ile Kayıtlı Kullanıcı Mevcut!", Toast.LENGTH_SHORT).show();
        }
    }

    private void ilklendirme() {
        tilAd = findViewById(R.id.text_input_kayit_ad);
        tilSifre = findViewById(R.id.text_input_sifreDegistir_eskisifre);
        tilSoyad = findViewById(R.id.text_input_kayit_soyad);
        tilTCKN = findViewById(R.id.text_input_kayit_tckn);
        btnKayıt = findViewById(R.id.btnKayit);

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
        Intent intent = new Intent(RegisterActivity.this,MainActivity.class);
        startActivity(intent);
        finish();
    }
}
