package com.mehmetaydin.hastanerandevusistemi;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
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
import com.mehmetaydin.hastanerandevusistemi.Model.Bolum;

public class FavoriBilgi extends AppCompatActivity {
    String favoriID = "", bolumID = "";
    String doktorAdi = "", hastaneAdi = "";
    TextView twAd, twHastane, twBolum;
    Button btnKaldir;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favori_bilgi);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Favori Bilgileri");

        Intent ıntent = getIntent();
        doktorAdi = ıntent.getStringExtra("doktorAdi");
        hastaneAdi = ıntent.getStringExtra("hastaneAdi");
        bolumID = ıntent.getStringExtra("bolumID");
        favoriID = ıntent.getStringExtra("favoriID");
        ilklendirme();
        bolumGetir();

        btnKaldir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                favoridenKaldir();
            }
        });

    }

    private void favoridenKaldir() {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Favori").child(favoriID);
        reference.removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(FavoriBilgi.this, "Favorilerden Kaldırıldı", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(FavoriBilgi.this,Favorilerim.class);
                    startActivity(intent);
                    finish();

                } else {
                    Toast.makeText(FavoriBilgi.this, "Favorilerden Kaldırma Başarısız!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    private void bolumGetir() {
        final DatabaseReference reference2 = FirebaseDatabase.getInstance().getReference("Bolum");
        reference2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Bolum bolum2 = snapshot.getValue(Bolum.class);

                    bolum2.setBolumID(snapshot.getKey().toString());
                    if (bolum2.getBolumID().equalsIgnoreCase(bolumID)) {
                        twBolum.setText("Bölüm Adı : " + bolum2.getBolumAdi().toString());
                        twAd.setText("Doktor Adı : " + doktorAdi);
                        twHastane.setText("Hastane Adı : " + hastaneAdi);
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
        twAd = findViewById(R.id.txtFavoriBilgileriDoktor);
        twBolum = findViewById(R.id.txtFavoriBilgileriBolum);
        twHastane = findViewById(R.id.txtFavoriBilgiHastane);
        btnKaldir = findViewById(R.id.btnFavoriKaldir);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(FavoriBilgi.this,Favorilerim.class);
        startActivity(intent);
        finish();
    }
}
