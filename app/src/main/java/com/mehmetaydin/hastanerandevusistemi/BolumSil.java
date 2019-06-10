package com.mehmetaydin.hastanerandevusistemi;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
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
import com.mehmetaydin.hastanerandevusistemi.Model.Hospital;
import com.mehmetaydin.hastanerandevusistemi.Model.Kisi;

import java.util.ArrayList;
import java.util.List;

public class BolumSil extends AppCompatActivity {
    List<Hastane> hastaneList = new ArrayList<>();
    ArrayList<String> hastaneler = new ArrayList<String>();
    List<Bolum> bolumList = new ArrayList<>();
    ArrayList<String> bolumler = new ArrayList<String>();
    List<Kisi> doktorList = new ArrayList<>();
    Spinner spHastane, spBolum;
    Button btnSil;
    String hastaneID = "", bolumID = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bolum_sil);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Bölüm Sil");

        hastaneID = "";
        bolumID = "";
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
                doktorGetir();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        btnSil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (hastaneID != "" && bolumID != "") {
                    AlertDialog.Builder builder = new AlertDialog.Builder(BolumSil.this);
                    builder.setTitle("Silme Uyarısı");
                    builder.setMessage("Bu bölümü silerseniz varsa bölüme ait doktorlar da silinecektir. Silme işlemine devam etmek istiyor musunuz?");
                    builder.setNegativeButton("Hayır", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Toast.makeText(BolumSil.this, "İşlem iptal edildi!", Toast.LENGTH_SHORT).show();
                        }
                    });
                    builder.setPositiveButton("Evet", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            bolumSil();
                        }
                    });
                    builder.show();
                } else {
                    Toast.makeText(BolumSil.this, "Hastane ve Bölüm Seçiniz!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void doktorGetir() {
        if (hastaneID != "" && bolumID != "") {
            final DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users");
            reference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    doktorList.clear();
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        Kisi kisi2 = snapshot.getValue(Kisi.class);
                        kisi2.setID(snapshot.getKey().toString());
                        if (kisi2.getBolumID().equalsIgnoreCase(bolumID)) {
                            doktorList.add(kisi2);
                        }
                    }
                    reference.removeEventListener(this);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
    }

    private void bolumSil() {
        if (bolumID != "" && hastaneID != "") {
            DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Bolum").child(bolumID);
            reference.removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        doktorSil();
                    } else {
                        Toast.makeText(BolumSil.this, "Silme İşlemi Başarısız!", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        } else {
            Toast.makeText(this, "Bölüm Bulunamadı!", Toast.LENGTH_SHORT).show();
        }
    }

    private void doktorSil() {
        if (bolumID != "" && hastaneID != "") {
            for (Kisi item : doktorList) {
                if (item.getBolumID().equalsIgnoreCase(bolumID)) {
                    DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users").child(item.getID());
                    reference.removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                bolumID = "";
                                hastaneID = "";
                            }
                        }
                    });
                }
            }
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
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(BolumSil.this, android.R.layout.simple_list_item_1, bolumler);
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
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(BolumSil.this, android.R.layout.simple_list_item_1, hastaneler);
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
        spHastane = findViewById(R.id.spBolumSilHastane);
        spBolum = findViewById(R.id.spBolumsilme);
        btnSil = findViewById(R.id.btnBolumSilme);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(BolumSil.this,Anasayfa.class);
        startActivity(intent);
        finish();
    }
}
