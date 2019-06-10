package com.mehmetaydin.hastanerandevusistemi;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.ActionBar;
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
import com.mehmetaydin.hastanerandevusistemi.Model.Hastane;
import com.mehmetaydin.hastanerandevusistemi.Model.Kisi;

import java.util.HashMap;

public class HastaneEkle extends AppCompatActivity {
    TextInputLayout tilHastaneEkleAd, tilHastaneEkleAdres;
    Button btnHastaneE;
    public Hastane hastane =new Hastane();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hastane_ekle);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Hastane Ekle");
        ilklendirme();
        hastane.setHastaneAdi("0");
        btnHastaneE.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(hastaneAdDogrula() && hastaneAdresDogrula()){
                    hastane.setHastaneAdi(tilHastaneEkleAd.getEditText().getText().toString());
                    hastane.setAdres(tilHastaneEkleAdres.getEditText().getText().toString());
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
                if(hastane.getHastaneAdi()==""){
                    hastane.setHastaneAdi("0");
                }
                if(hastane.getHastaneAdi()!="0"){
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                        Hastane hastane2 = snapshot.getValue(Hastane.class);
                        if(hastane2.getHastaneAdi().equalsIgnoreCase(hastane.getHastaneAdi())){
                            hastane.setHastaneAdi("0");
                            break;
                        }
                    }
                    hataneEkle();
                    reference2.removeEventListener(this);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void hataneEkle() {
        if(hastane.getHastaneAdi()!= "0" && hastane.getHastaneAdi() != ""){
            DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Hastane");
            final HashMap<String,Object> hashMap = new HashMap<>();
            hashMap.put("hastaneAdi",hastane.getHastaneAdi());
            hashMap.put("adres",hastane.getAdres());

            reference.push().setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    hastane.setHastaneAdi("0");
                    Toast.makeText(HastaneEkle.this, "Başarılı", Toast.LENGTH_SHORT).show();
                }
            });
        }else
        {
            Toast.makeText(HastaneEkle.this, "Bu isimde Hastane bulunmaktadır!", Toast.LENGTH_SHORT).show();
        }
    }

    private void ilklendirme() {
        tilHastaneEkleAd = findViewById(R.id.text_input_hastane_ekle_ad);
        tilHastaneEkleAdres = findViewById(R.id.text_input_hastane_ekle_adres);
        btnHastaneE = findViewById(R.id.btnH_Ekle);
    }

    private boolean hastaneAdDogrula(){
        String adInput = tilHastaneEkleAd.getEditText().getText().toString().trim();
        if(adInput.isEmpty()){
            tilHastaneEkleAd.setError("Boş Geçilemez!");
            return false;
        }
        else {
            tilHastaneEkleAd.setError(null);
            return true;
        }
    }
    private boolean hastaneAdresDogrula(){
        String adresInput = tilHastaneEkleAdres.getEditText().getText().toString().trim();
        if(adresInput.isEmpty()){
            tilHastaneEkleAdres.setError("Boş Geçilemez!");
            return false;
        }
        else {
            tilHastaneEkleAdres.setError(null);
            return true;
        }
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(HastaneEkle.this,Anasayfa.class);
        startActivity(intent);
        finish();
    }
}
