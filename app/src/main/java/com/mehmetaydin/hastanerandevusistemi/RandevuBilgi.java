package com.mehmetaydin.hastanerandevusistemi;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mehmetaydin.hastanerandevusistemi.Model.Randevu;
import com.mehmetaydin.hastanerandevusistemi.Model.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class RandevuBilgi extends AppCompatActivity {
    TextView twHastane, twBolum, twDoktor, twTarih, twSaat;
    Button btnRandevuOnay;

    String hastaneID = "", bolumID = "", doktorID = "", saatID = "", tarihBilgisi = "";
    String hastaneAdi = "", bolumAdi = "", doktorAdi = "", saatBilgi = "", hastaneAdres = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_randevu_bilgi);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Randevu Bilgileri");
        ilklendirme();

        Intent ıntent = getIntent();
        hastaneID = ıntent.getStringExtra("hastaneID");
        bolumID = ıntent.getStringExtra("bolumID");
        doktorID = ıntent.getStringExtra("doktorID");
        saatID = ıntent.getStringExtra("saatID");
        tarihBilgisi = ıntent.getStringExtra("tarihBilgisi");
        hastaneAdi = ıntent.getStringExtra("hastaneAdi");
        bolumAdi = ıntent.getStringExtra("bolumAdi");
        hastaneAdres = ıntent.getStringExtra("hastaneAdres");
        doktorAdi = ıntent.getStringExtra("doktorAdi");
        saatBilgi = ıntent.getStringExtra("saatBilgi");
        atamaYap();

        btnRandevuOnay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                randevuAl();
            }
        });
    }


    private void randevuAl() {
        if (hastaneID != "" && bolumID != "" && doktorID != "" && User.uID != "" && saatID != "") {
            DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Randevu");
            final HashMap<String, Object> hashMap = new HashMap<>();
            hashMap.put("doktorID", doktorID);
            hashMap.put("hastaID", User.uID);
            hashMap.put("saatID", saatID);
            hashMap.put("tarih", tarihBilgisi);

            reference.push().setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    bolumID = "";
                    Toast.makeText(RandevuBilgi.this, "İşlem Başarılı", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(RandevuBilgi.this,RandevuGecmisi.class);
                    startActivity(intent);
                    finish();
                }
            });
        }
    }

    private void atamaYap() {
        twHastane.setText("Hastane Adı : " + hastaneAdi);
        twTarih.setText("Tarih : " + tarihBilgisi + "   Saat : " + saatBilgi);
        twSaat.setText("Hastane Adresi : " + hastaneAdres);
        twBolum.setText("Bölüm : " + bolumAdi);
        twDoktor.setText("Doktor : " + doktorAdi);
    }

    private void ilklendirme() {
        twHastane = findViewById(R.id.txtHastaneAdi);
        twDoktor = findViewById(R.id.txtDoktorAdi);
        twBolum = findViewById(R.id.txtBolumAdi);
        twSaat = findViewById(R.id.txtRandevuAdresi);
        twTarih = findViewById(R.id.txtRandevuTarihi);
        btnRandevuOnay = findViewById(R.id.btnRandevuOnayla);
    }

    @Override
    public void onBackPressed() {
        Intent ıntent = new Intent(RandevuBilgi.this, RandevuAl.class);
        startActivity(ıntent);
        finish();
    }
}
