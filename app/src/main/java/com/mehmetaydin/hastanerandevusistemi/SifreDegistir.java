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
import com.mehmetaydin.hastanerandevusistemi.Model.Kisi;
import com.mehmetaydin.hastanerandevusistemi.Model.User;

import java.util.HashMap;

public class SifreDegistir extends AppCompatActivity {
    TextInputLayout tilSifre, tilYeniSifre, tilSifreOnay;
    Button button;

    String sifreDurumu = "", rol="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sifre_degistir);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Şifre Güncelleme");

        ilklendirme();
        sifreDurumu="";
        rol="";
        Intent ıntent = getIntent();
        rol=ıntent.getStringExtra("rol");

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(sifreDogrula() && yeniSifreDogrula() && sifreOnayDogrula()){
                    if(tilYeniSifre.getEditText().getText().toString().trim().equalsIgnoreCase(tilSifreOnay.getEditText().getText().toString().trim())){
                        sifreDurumu="";
                        sifreKontrol();
                    }else{
                        Toast.makeText(SifreDegistir.this, "Şifreler Uyuşmuyor!", Toast.LENGTH_SHORT).show();
                    }
                }
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
                        if(kisi2.getSifre().equalsIgnoreCase(tilSifre.getEditText().getText().toString()))
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
                sifreGuncelle();
                reference.removeEventListener(this);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void sifreGuncelle() {
        if (sifreDurumu == "1") {

                DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users").child(User.uID);
                final HashMap<String, Object> hashMap = new HashMap<>();
                hashMap.put("sifre", tilYeniSifre.getEditText().getText().toString());

                reference.updateChildren(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        sifreDurumu="";
                        Toast.makeText(SifreDegistir.this, "Başarılı", Toast.LENGTH_SHORT).show();
                    }
                });
        } else {
            Toast.makeText(SifreDegistir.this, "Şifreniz Hatalı", Toast.LENGTH_SHORT).show();
        }
    }

    private void ilklendirme() {
        tilSifre = findViewById(R.id.text_input_sifreDegistir_eskisifre);
        tilYeniSifre = findViewById(R.id.text_input_sifreDegistir_yenisifre);
        tilSifreOnay = findViewById(R.id.text_input_sifreDegistir_yenisifreonay);
        button = findViewById(R.id.btnSifreGuncelleme);
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
    private boolean yeniSifreDogrula() {
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
    private boolean sifreOnayDogrula() {
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

    @Override
    public void onBackPressed() {
        Intent intent;
        if(rol.equalsIgnoreCase("Hasta")){
            intent = new Intent(SifreDegistir.this,Profilim.class);
        }else{

            intent = new Intent(SifreDegistir.this,DoktorProfil.class);
        }
        startActivity(intent);
        finish();
    }
}
