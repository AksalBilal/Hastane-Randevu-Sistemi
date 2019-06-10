package com.mehmetaydin.hastanerandevusistemi;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
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
import com.mehmetaydin.hastanerandevusistemi.Model.Favori;
import com.mehmetaydin.hastanerandevusistemi.Model.Hastane;
import com.mehmetaydin.hastanerandevusistemi.Model.User;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

public class RandevuGecmisDetay extends AppCompatActivity {
    String randevuID = "", bolumAdi = "", saatBilgi = "", tarihBilgisi = "";
    String doktorAdi = "", hastaneID = "",favoriDurumu = "",doktorID="",randevudurumu="";

    TextView twHastane, twBolum, twDoktor, twTarih, twSaat;
    Button btnIptal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_randevu_gecmis_detay);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Randevu Bilgileri");
        randevudurumu="";
        favoriDurumu = "";
        ilklendirme();
        doldur();
        hastaneGetir();
        btnIptal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TarihKontrol();
            }
        });
    }

    private void TarihKontrol() {
        randevudurumu="";
        String[] Tarih_ayir = tarihBilgisi.split("/");
        Date simdikiZaman = new Date();
        DateFormat df_gun = new SimpleDateFormat("dd");
        DateFormat df_ay = new SimpleDateFormat("MM");
        DateFormat df_yil = new SimpleDateFormat("yyyy");
        int gun = Integer.parseInt(df_gun.format(simdikiZaman));
        int ay = Integer.parseInt(df_ay.format(simdikiZaman));
        int yil = Integer.parseInt(df_yil.format(simdikiZaman));
        if (yil < Integer.parseInt(Tarih_ayir[2])) {
                    randevudurumu="";
                    RandevuIptalEt();
        }else if(yil == Integer.parseInt(Tarih_ayir[2])){
            randevudurumu="";
            if (ay == Integer.parseInt(Tarih_ayir[1])) {
                if (gun == Integer.parseInt(Tarih_ayir[0])) {
                    String[] Saat_ayir = saatBilgi.split(":");
                    DateFormat df_saat = new SimpleDateFormat("H");
                    DateFormat df_dakika = new SimpleDateFormat("m");
                    int saat = Integer.parseInt(df_saat.format(simdikiZaman));
                    int dakika = Integer.parseInt(df_dakika.format(simdikiZaman));
                    if(saat<Integer.parseInt(Saat_ayir[0])){
                        randevudurumu="";
                        RandevuIptalEt();
                    }
                    else if(saat==Integer.parseInt(Saat_ayir[0])){
                        if (dakika<Integer.parseInt(Saat_ayir[1])){
                            randevudurumu="";
                            RandevuIptalEt();
                        }
                    }else {
                        randevudurumu="1";
                    }
                } else {
                    randevudurumu="1";
                }
            }else if(ay<Integer.parseInt(Tarih_ayir[1])){
                randevudurumu="";
                RandevuIptalEt();
            }else {
                randevudurumu="1";
            }

        } else {
            randevudurumu="1";
        }
        if (randevudurumu.equalsIgnoreCase("1")){
            AlertDialog.Builder builder = new AlertDialog.Builder(RandevuGecmisDetay.this);
            builder.setTitle("Favori Ekleme");
            builder.setMessage("Randevu Tarihi Geçtiği İçin İptal Edilemez!\nDoktoru Favoriye Eklemek için Favori Ekle Butonuna Basınız");
            builder.setNegativeButton("Geri Gel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Toast.makeText(RandevuGecmisDetay.this, "İşlem İptal Edildi", Toast.LENGTH_SHORT).show();
                }
            });
            builder.setPositiveButton("Favorilere Ekle", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                   FavoriKontrol();
                }
            });
            builder.show();
        }
    }
    private void FavoriKontrol() {
        final DatabaseReference reference2 = FirebaseDatabase.getInstance().getReference("Favori");
        reference2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (favoriDurumu == "") {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        Favori favori2 = snapshot.getValue(Favori.class);
                        if (favori2.getDoktorID().equals(doktorID) && favori2.getHastaID().equalsIgnoreCase(User.uID)) {
                            favoriDurumu = "1";
                            break;
                        }
                    }
                    favorilereEkle();
                    reference2.removeEventListener(this);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void favorilereEkle() {
        if (hastaneID != "" && doktorID != "" && User.uID != "" && favoriDurumu != "1") {
            DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Favori");
            final HashMap<String, Object> hashMap = new HashMap<>();
            hashMap.put("doktorID", doktorID);
            hashMap.put("hastaID", User.uID);
            hashMap.put("hastaneID", hastaneID);
            reference.push().setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    favoriDurumu = "";
                }
            });
        } else {
            Toast.makeText(RandevuGecmisDetay.this, "Bu Doktor Zaten Favorilere Eklenmiş!", Toast.LENGTH_SHORT).show();
            favoriDurumu = "";
        }
    }

    private void RandevuIptalEt() {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Randevu").child(randevuID);
        reference.removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(RandevuGecmisDetay.this, "Randevu İptal Edildi!", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(RandevuGecmisDetay.this,RandevuGecmisi.class);
                    startActivity(intent);
                    finish();

                } else {
                    Toast.makeText(RandevuGecmisDetay.this, "Randevu İptal Etme Başarısız!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void doldur() {
        Intent ıntent = getIntent();
        doktorAdi = ıntent.getStringExtra("doktorAdi");
        bolumAdi = ıntent.getStringExtra("bolumAdi");
        doktorID=ıntent.getStringExtra("doktorID");
        saatBilgi = ıntent.getStringExtra("saatBilgi");
        tarihBilgisi = ıntent.getStringExtra("tarihBilgi");
        randevuID = ıntent.getStringExtra("randevuID");
        hastaneID = ıntent.getStringExtra("hastaneID");
    }

    private void hastaneGetir() {
        final DatabaseReference reference2 = FirebaseDatabase.getInstance().getReference("Hastane");
        reference2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Hastane hastane2 = snapshot.getValue(Hastane.class);

                    hastane2.id = snapshot.getKey().toString();
                    if (hastane2.id.equalsIgnoreCase(hastaneID)) {
                        twHastane.setText("Hastane Adı : " + hastane2.getHastaneAdi());
                        twBolum.setText("Bölüm : " + bolumAdi);
                        twDoktor.setText("Doktor : " + doktorAdi);
                        twTarih.setText("Tarih : " + tarihBilgisi);
                        twSaat.setText("Saat : " + saatBilgi);
                        break;
                    }

                }
                reference2.removeEventListener(this);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void ilklendirme() {
        twHastane = findViewById(R.id.txtRandevuGecmisHastane);
        twBolum = findViewById(R.id.txtRandevuGecmisBolum);
        twDoktor = findViewById(R.id.txtRandevuGecmisDoktor);
        twTarih = findViewById(R.id.txtRandevuGecmisTarih);
        twSaat = findViewById(R.id.txtRandevuGecmisSaat);
        btnIptal = findViewById(R.id.btnRandevuIptal);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(RandevuGecmisDetay.this,RandevuGecmisi.class);
        startActivity(intent);
        finish();
    }

}
