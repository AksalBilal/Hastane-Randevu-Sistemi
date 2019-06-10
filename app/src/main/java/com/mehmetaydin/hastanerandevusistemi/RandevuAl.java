package com.mehmetaydin.hastanerandevusistemi;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
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
import com.mehmetaydin.hastanerandevusistemi.Model.Favori;
import com.mehmetaydin.hastanerandevusistemi.Model.Hastane;
import com.mehmetaydin.hastanerandevusistemi.Model.Kisi;
import com.mehmetaydin.hastanerandevusistemi.Model.Kisit;
import com.mehmetaydin.hastanerandevusistemi.Model.Randevu;
import com.mehmetaydin.hastanerandevusistemi.Model.Saat;
import com.mehmetaydin.hastanerandevusistemi.Model.User;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class RandevuAl extends AppCompatActivity {

    List<Kisit> kisitList = new ArrayList<>();
    List<Saat> saatList = new ArrayList<>();
    ArrayList<String> saatler = new ArrayList<String>();
    List<Randevu> randevuList = new ArrayList<>();
    List<Randevu> randevuList2 = new ArrayList<>();
    List<Randevu> randevuList3 = new ArrayList<>();
    List<Randevu> ozelIstekRandevuList = new ArrayList<>();
    List<Hastane> hastaneList = new ArrayList<>();
    ArrayList<String> hastaneler = new ArrayList<String>();
    List<Bolum> bolumList = new ArrayList<>();
    ArrayList<String> bolumler = new ArrayList<String>();
    List<Kisi> doktorList = new ArrayList<>();
    List<Kisi> doktorList2 = new ArrayList<>();
    ArrayList<String> doktorlar = new ArrayList<String>();
    int[] randevuSayi;
    int[] liste;
    Spinner spHastane, spBolum, spDoktor, spSaat;
    TextView twTarih;
    Button btnRandevuAl, btnFavoriEkle;
    DatePickerDialog.OnDateSetListener dpdRandevuAl;
    String hastaneID = "", bolumID = "", doktorID = "", saatID = "", favoriDurumu = "";
    String hastaneAdi = "", bolumAdi = "", doktorAdi = "", saatBilgi = "", hastaneAdres = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_randevu_al);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Randevu Al");

        hastaneID = "";
        doktorID = "";
        bolumID = "";
        saatID = "";
        doktorAdi = "";
        bolumAdi = "";
        hastaneAdi = "";
        saatBilgi = "";
        hastaneAdres = "";
        favoriDurumu = "";

        ilklendirme();

        hastaneGetir();
        spHastane.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                bolumler.clear();
                bolumList.clear();
                saatler.clear();
                saatList.clear();
                randevuList.clear();
                kisitList.clear();
                twTarih.setText("Tarih Seçiniz");
                saatID = "";
                bolumID = "";
                doktorID = "";
                doktorAdi = "";
                bolumAdi = "";
                saatBilgi = "";
                favoriDurumu = "";
                hastaneID = hastaneList.get(position).id;
                hastaneAdi = hastaneList.get(position).getHastaneAdi().toString();
                hastaneAdres = hastaneList.get(position).getAdres().toString();
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
                bolumAdi = bolumList.get(position).getBolumAdi().toString();
                doktorID = "";
                doktorlar.clear();
                doktorList.clear();
                doktorList2.clear();
                saatler.clear();
                saatList.clear();
                randevuList.clear();
                kisitList.clear();
                twTarih.setText("Tarih Seçiniz");
                saatID = "";
                doktorAdi = "";
                favoriDurumu = "";
                saatBilgi = "";
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spDoktor.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                doktorID = doktorList2.get(position).getID();
                doktorAdi = doktorList2.get(position).getAd().toString() + " " + doktorList2.get(position).getSoyad().toString();
                saatID = "";
                saatBilgi = "";
                favoriDurumu = "";
                saatler.clear();
                saatList.clear();
                kisitList.clear();
                randevuGetir();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spSaat.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                saatID = saatList.get(position).getSaatID();
                saatBilgi = saatList.get(position).getSaatBilgisi().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        twTarih.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar cal = Calendar.getInstance();
                int yil = cal.get(Calendar.YEAR);
                int ay = cal.get(Calendar.MONTH);
                int gun = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(RandevuAl.this, android.R.style.Theme_Holo_Light_Dialog_MinWidth, dpdRandevuAl, yil, ay, gun);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });
        dpdRandevuAl = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                saatID = "";
                doktorID = "";
                saatBilgi = "";
                doktorAdi = "";
                favoriDurumu = "";
                saatler.clear();
                saatList.clear();
                doktorlar.clear();
                doktorList.clear();
                doktorList2.clear();

                month = month + 1;
                String tarih = dayOfMonth + "/" + month + "/" + year;
                Date simdikiZaman = new Date();
                DateFormat df_gun = new SimpleDateFormat("dd");
                DateFormat df_ay = new SimpleDateFormat("MM");
                DateFormat df_yil = new SimpleDateFormat("yyyy");
                int gun = Integer.parseInt(df_gun.format(simdikiZaman));
                int ay = Integer.parseInt(df_ay.format(simdikiZaman));
                int yil = Integer.parseInt(df_yil.format(simdikiZaman));
                if (yil == year) {
                    if (ay == month) {
                        if (gun <= dayOfMonth) {
                            twTarih.setText(tarih);
                            doktorID = "";
                            doktorAdi = "";
                            favoriDurumu = "";
                            ozelİstekRandevular();
                            doktorGetir();
                        }
                        else {
                            Toast.makeText(RandevuAl.this, "Geçerli Bir Tarih Giriniz!", Toast.LENGTH_SHORT).show();
                        }
                    }else if (ay < month) {
                        twTarih.setText(tarih);
                        doktorID = "";
                        doktorAdi = "";
                        favoriDurumu = "";
                        ozelİstekRandevular();
                        doktorGetir();
                    } else {
                        Toast.makeText(RandevuAl.this, "Geçerli Bir Tarih Giriniz!", Toast.LENGTH_SHORT).show();
                    }
                } else if (yil < year) {
                    twTarih.setText(tarih);
                    doktorID = "";
                    doktorAdi = "";
                    favoriDurumu = "";
                    ozelİstekRandevular();
                    doktorGetir();
                } else {
                    Toast.makeText(RandevuAl.this, "Geçerli Bir Tarih Giriniz!", Toast.LENGTH_SHORT).show();
                }
            }
        };

        btnRandevuAl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (hastaneID != "" && bolumID != "" && doktorID != "" && saatID != "" && !(twTarih.getText().equals("Tarih Seçiniz!"))) {
                    randevuOlustur();
                } else {
                    Toast.makeText(RandevuAl.this, "Bilgiler eksiksiz doldurulmalıdır!", Toast.LENGTH_SHORT).show();
                }
            }
        });
        btnFavoriEkle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (hastaneID != "" && bolumID != "" && doktorID != "") {
                    favoriKontrol();
                } else {
                    Toast.makeText(RandevuAl.this, "Bilgiler eksiksiz doldurulmalıdır!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void ozelİstekRandevular() {
        final DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Randevu");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ozelIstekRandevuList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Randevu randevu2 = snapshot.getValue(Randevu.class);
                    randevu2.setRandevuID(snapshot.getKey().toString());
                    if (randevu2.getTarih().equalsIgnoreCase(twTarih.getText().toString().trim())) {
                        ozelIstekRandevuList.add(randevu2);
                    }
                }
                reference.removeEventListener(this);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void favoriKontrol() {
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
        if (hastaneID != "" && bolumID != "" && doktorID != "" && User.uID != "" && favoriDurumu != "1") {
            DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Favori");
            final HashMap<String, Object> hashMap = new HashMap<>();
            hashMap.put("doktorID", doktorID);
            hashMap.put("hastaID", User.uID);
            hashMap.put("hastaneID", hastaneID);

            reference.push().setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    favoriDurumu = "";
                    Toast.makeText(RandevuAl.this, "Favorilere Eklendi", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Toast.makeText(RandevuAl.this, "Bu Doktor Zaten Favorilere Eklenmiş!", Toast.LENGTH_SHORT).show();
            favoriDurumu = "";
        }
    }

    private void randevuOlustur() {
        if (User.uID != "") {
            Intent intent = new Intent(RandevuAl.this, RandevuBilgi.class);
            intent.putExtra("hastaneID", hastaneID);
            intent.putExtra("hastaneAdi", hastaneAdi);
            intent.putExtra("hastaneAdres", hastaneAdres);
            intent.putExtra("bolumID", bolumID);
            intent.putExtra("bolumAdi", bolumAdi);
            intent.putExtra("doktorID", doktorID);
            intent.putExtra("doktorAdi", doktorAdi);
            intent.putExtra("saatID", saatID);
            intent.putExtra("saatBilgi", saatBilgi);
            intent.putExtra("tarihBilgisi", twTarih.getText().toString());
            startActivity(intent);
            finish();
        } else {
            Toast.makeText(RandevuAl.this, "İşlem Başarısız!", Toast.LENGTH_SHORT).show();
        }

    }

    private void randevuGetir() {
        if (hastaneID != "" && bolumID != "" && !(twTarih.getText().equals("Tarih Seçiniz")) && doktorID != "") {
            btnRandevuAl.setEnabled(true);
            final DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Randevu");
            reference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    randevuList.clear();
                    randevuList2.clear();
                    randevuList3.clear();
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        Randevu randevu2 = snapshot.getValue(Randevu.class);
                        randevu2.setRandevuID(snapshot.getKey().toString());
                        if (randevu2.getDoktorID().equalsIgnoreCase(doktorID) && randevu2.getTarih().equalsIgnoreCase(twTarih.getText().toString())) {
                            randevuList.add(randevu2);
                        }
                        if (randevu2.getTarih().equalsIgnoreCase(twTarih.getText().toString()) && randevu2.getHastaID().equalsIgnoreCase(User.uID)) {
                            randevuList2.add(randevu2);
                        }
                        if (randevu2.getTarih().equalsIgnoreCase(twTarih.getText().toString()) && randevu2.getHastaID().equalsIgnoreCase(User.uID) && randevu2.getDoktorID().equalsIgnoreCase(doktorID)) {
                            randevuList3.add(randevu2);
                        }
                    }
                    if (randevuList3.size() != 0) {
                        btnRandevuAl.setEnabled(false);
                        Toast.makeText(RandevuAl.this, "Aynı gün içinde aynı doktora birden fazla randevu alamazsınız!", Toast.LENGTH_SHORT).show();
                    }
                    kisitGetir();
                    reference.removeEventListener(this);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
    }

    private void kisitGetir() {
        if (hastaneID != "" && bolumID != "" && !(twTarih.getText().equals("Tarih Seçiniz")) && doktorID != "") {
            final DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Kisit");
            reference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    kisitList.clear();
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        Kisit kisit2 = snapshot.getValue(Kisit.class);
                        kisit2.setKisitID(snapshot.getKey().toString());
                        if (kisit2.getDoktorID().equalsIgnoreCase(doktorID)) {
                            kisitList.add(kisit2);
                        }
                    }
                    saatleriGetir();
                    reference.removeEventListener(this);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
    }

    private void saatleriGetir() {
        if (hastaneID != "" && bolumID != "" && !(twTarih.getText().equals("Tarih Seçiniz")) && doktorID != "") {
            final DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Saat");
            reference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    saatler.clear();
                    saatList.clear();

                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        Saat saat2 = snapshot.getValue(Saat.class);
                        saat2.setSaatID(snapshot.getKey().toString());
                        saatList.add(saat2);
                        saatler.add(saat2.getSaatBilgisi().toString());
                        if (randevuList.size() != 0) {
                            for (Randevu item : randevuList) {
                                saatiSil(item.getSaatID());
                            }
                        }
                        if (kisitList.size() != 0) {
                            for (Kisit item2 : kisitList) {
                                saatiSil(item2.getSaatID().toString());
                            }
                        }
                        if (randevuList2.size() != 0) {
                            for (Randevu item3 : randevuList2) {
                                saatiSil(item3.getSaatID());
                            }
                        }
                        saatKontrol(saat2.getSaatID(), saat2.getSaatBilgisi());
                    }
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(RandevuAl.this, android.R.layout.simple_list_item_1, saatler);
                    adapter.setDropDownViewResource(android.R.layout.simple_list_item_1);
                    spSaat.setAdapter(adapter);
                    reference.removeEventListener(this);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
    }

    private void saatKontrol(String saatID, String saatBilgisi) {
        String[] Tarih_ayir = twTarih.getText().toString().split("/");
        Date simdikiZaman = new Date();
        DateFormat df_gun = new SimpleDateFormat("dd");
        DateFormat df_ay = new SimpleDateFormat("MM");
        DateFormat df_yil = new SimpleDateFormat("yyyy");
        int gun = Integer.parseInt(df_gun.format(simdikiZaman));
        int ay = Integer.parseInt(df_ay.format(simdikiZaman));
        int yil = Integer.parseInt(df_yil.format(simdikiZaman));
        if (yil == Integer.parseInt(Tarih_ayir[2])) {
            if (ay == Integer.parseInt(Tarih_ayir[1])) {
                if (gun == Integer.parseInt(Tarih_ayir[0])) {
                    String[] Saat_ayir = saatBilgisi.split(":");
                    DateFormat df_saat = new SimpleDateFormat("H");
                    DateFormat df_dakika = new SimpleDateFormat("m");
                    int saat = Integer.parseInt(df_saat.format(simdikiZaman));
                    int dakika = Integer.parseInt(df_dakika.format(simdikiZaman));

                    if (saat > Integer.parseInt(Saat_ayir[0])) {
                        saatiSil(saatID);
                    } else if (saat == Integer.parseInt(Saat_ayir[0])) {
                        if (dakika >= Integer.parseInt(Saat_ayir[1])) {
                            saatiSil(saatID);
                        }
                    }
                }
            }
        }
    }

    private void saatiSil(String saatID) {
        String saatBilgi = "";
        for (Saat item : saatList) {
            if (item.getSaatID().equalsIgnoreCase(saatID)) {
                saatBilgi = item.getSaatBilgisi().toString();
                saatList.remove(item);
                break;
            }
        }
        for (String item2 : saatler) {
            if (item2.equalsIgnoreCase(saatBilgi)) {
                saatler.remove(item2);
                break;
            }
        }
    }

    private void doktorGetir() {
        if (hastaneID != "" && bolumID != "" && !(twTarih.getText().equals("Tarih Seçiniz"))) {
            final DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users");
            reference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    doktorlar.clear();
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        Kisi kisi2 = snapshot.getValue(Kisi.class);
                        kisi2.setID(snapshot.getKey().toString());
                        if (kisi2.getBolumID().equalsIgnoreCase(bolumID) && !(kisi2.getID().equalsIgnoreCase(User.uID))) {
                            doktorList.add(kisi2);
                        }
                    }
                    ozelIstek();
                    reference.removeEventListener(this);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
    }

    private void ozelIstek() {
        randevuSayi = new int[doktorList.size()];
        int sayac = 0;
        if (ozelIstekRandevuList.size() != 0) {
            for (Randevu item : ozelIstekRandevuList) {
                sayac = 0;
                for (Kisi item2 : doktorList) {
                    if (item.getDoktorID().equalsIgnoreCase(item2.getID())) {
                        randevuSayi[sayac] = randevuSayi[sayac] + 1;
                        break;
                    }
                    sayac++;
                }
            }
        }
        ozelIstek2();
    }

    private void ozelIstek2() {
        liste = new int[doktorList.size()];

        for(int i = 0; i<randevuSayi.length; i++){
            liste[i]=randevuSayi[i];
        }

        for (int i = 0; i < liste.length-1; i++) { //Dizimizin değerlerini sırası ile alıyoruz

            int sayi = liste[i]; //sıradaki değeri sayi değişkenine atıyoruz
            int temp = i; //sayi 'nin indeksini temp değerine atıyoruz

            for (int j = i+1; j < liste.length ; j++) { //dizimizde i' den sonraki elemanlara bakıyoruz
                if(liste[j]<sayi){ //sayi değişkeninden küçük sayı var mı
                    sayi = liste[j]; //varsa sayi değişkenimizide değiştiriyoruz
                    temp = j; //indeks değerinide değiştiriyoruz
                }
            }

            if(temp != i){ //temp değeri başlangıç değeri ile aynı değil ise , yani list[i]'nin değerinden küçük sayı varsa onları yer değiştiriyoruz
                liste[temp] = liste[i];
                liste[i] = sayi;
            }
        }
        spinnerDoldur();
    }

    private void spinnerDoldur() {
        for(int i = 0; i<liste.length; i++){
            for (int j = 0; j<randevuSayi.length; j++){
                if(liste[i]==randevuSayi[j]){
                    doktorlar.add(doktorList.get(j).getAd()+" "+doktorList.get(j).getSoyad()+" ("+liste[i]+")");
                    doktorList2.add(doktorList.get(j));
                    randevuSayi[j]=-1;
                    break;
                }
            }
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(RandevuAl.this, android.R.layout.simple_list_item_1, doktorlar);
        adapter.setDropDownViewResource(android.R.layout.simple_list_item_1);
        spDoktor.setAdapter(adapter);
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
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(RandevuAl.this, android.R.layout.simple_list_item_1, bolumler);
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
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(RandevuAl.this, android.R.layout.simple_list_item_1, hastaneler);
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
        spHastane = findViewById(R.id.spRandevuAlHastane);
        spBolum = findViewById(R.id.spRandevuAlBolum);
        spDoktor = findViewById(R.id.spRandevuAlDoktor);
        spSaat = findViewById(R.id.spRandevuAlSaat);
        twTarih = findViewById(R.id.txtRandevuTarih);
        btnRandevuAl = findViewById(R.id.btnRandevuAlma);
        btnFavoriEkle = findViewById(R.id.btnFavoriEkle);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(RandevuAl.this,RandevuGecmisi.class);
        startActivity(intent);
        finish();
    }
}
