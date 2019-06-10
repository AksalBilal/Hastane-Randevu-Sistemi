package com.mehmetaydin.hastanerandevusistemi;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
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

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class HastaneSil extends AppCompatActivity {
    List<Hastane> hastaneList = new ArrayList<>();
    ArrayList<String> hastaneler = new ArrayList<String>();
    List<Bolum> bolumList = new ArrayList<>();
    List<Kisi> doktorList = new ArrayList<>();
    Spinner spHastane;
    Button btnHastaneSilAra;
    String hastaneID = "";
    public Hastane hastane = new Hastane();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hastane_sil);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Hastane Sil");

        ilklendirme();
        hastaneleriGetir();
        hastaneID = "";

        spHastane.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                hastaneID = hastaneList.get(position).id;
                bolumGetir();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        btnHastaneSilAra.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (hastaneID != "") {
                    AlertDialog.Builder builder = new AlertDialog.Builder(HastaneSil.this);
                    builder.setTitle("Silme Uyarısı");
                    builder.setMessage("Bu bölümü silerseniz varsa bölüme ait doktorlar da silinecektir. Silme işlemine devam etmek istiyor musunuz?");
                    builder.setNegativeButton("Hayır", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Toast.makeText(HastaneSil.this, "İşlem iptal edildi!", Toast.LENGTH_SHORT).show();
                        }
                    });
                    builder.setPositiveButton("Evet", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            hastaneSil();
                        }
                    });
                    builder.show();

                } else {
                    Toast.makeText(HastaneSil.this, "Hastane Seçiniz!", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void hastaneSil() {
        if(hastaneID != "0" && hastaneID != ""){
            DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Hastane").child(hastaneID);
            reference.removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful()){
                        Toast.makeText(HastaneSil.this, "Hastane Silindi", Toast.LENGTH_SHORT).show();
                        doktorSil();
                    }
                    else {
                        Toast.makeText(HastaneSil.this, "Silme İşlemi Başarısız!", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }else
        {
            Toast.makeText(this, "Hastane Bulunamadı!", Toast.LENGTH_SHORT).show();
        }
    }

    private void doktorSil() {
        if (hastaneID != "") {
            for(Bolum item2 : bolumList){
                for (Kisi item : doktorList) {
                    if (item.getBolumID().equalsIgnoreCase(item2.getBolumID())) {
                        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users").child(item.getID());
                        reference.removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    bolumSil();
                                }
                            }
                        });
                    }
                }
            }
        }
    }

    private void bolumSil() {
        if (hastaneID != "") {
            for(Bolum item : bolumList){
                DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Bolum").child(item.getBolumID());
                reference.removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                    }
                });
            }
        }
    }


    private void hastaneleriGetir() {
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
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(HastaneSil.this, android.R.layout.simple_list_item_1, hastaneler);
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
        spHastane = findViewById(R.id.spHastaneSil);
        btnHastaneSilAra = findViewById(R.id.btnHastaneSilAra);
    }

    private void bolumGetir() {
        if (hastaneID != "" && hastaneID != "0") {
            final DatabaseReference reference2 = FirebaseDatabase.getInstance().getReference("Bolum");
            reference2.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    bolumList.clear();
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        Bolum bolum2 = snapshot.getValue(Bolum.class);

                        bolum2.setBolumID(snapshot.getKey().toString());
                        if (bolum2.getHastaneID().equalsIgnoreCase(hastaneID)) {
                            bolumList.add(bolum2);
                        }
                    }
                    doktorGetir();
                    reference2.removeEventListener(this);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
    }

    private void doktorGetir() {
        if (hastaneID != "") {
            final DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users");
            reference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    doktorList.clear();
                    for(Bolum item : bolumList){
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            Kisi kisi2 = snapshot.getValue(Kisi.class);
                            kisi2.setID(snapshot.getKey().toString());
                            if (kisi2.getBolumID().equalsIgnoreCase(item.getBolumID())) {
                                doktorList.add(kisi2);
                            }
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

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(HastaneSil.this,Anasayfa.class);
        startActivity(intent);
        finish();
    }
}
