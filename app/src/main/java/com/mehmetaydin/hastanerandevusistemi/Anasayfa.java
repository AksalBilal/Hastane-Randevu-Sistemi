package com.mehmetaydin.hastanerandevusistemi;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.mehmetaydin.hastanerandevusistemi.Model.Hastane;
import com.mehmetaydin.hastanerandevusistemi.Model.User;

public class Anasayfa extends AppCompatActivity {
    private Button btnHastaneEkle, btnHastaneGuncelle, btnHastaneSil, btnBolumEkle, btnBolumGuncelle, btnBolumSil, btnDoktorEkle, btnDoktorGuneclle, btnDoktorSil, btnSaatKisitla, btnSaatAc, btnExit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_anasayfa);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Admin Ana Sayfası");
        ilklendirme();
        btnHastaneEkle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Anasayfa.this,HastaneEkle.class);
                startActivity(intent);
                finish();
            }
        });
        btnHastaneSil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Anasayfa.this,HastaneSil.class);
                startActivity(intent);
                finish();
            }
        });
        btnBolumEkle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Anasayfa.this, BolumEkle.class);
                startActivity(intent);
                finish();
            }
        });
        btnHastaneGuncelle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Anasayfa.this,HastaneGuncelle.class);
                startActivity(intent);
                finish();
            }
        });
        btnBolumSil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Anasayfa.this,BolumSil.class);
                startActivity(intent);
            }
        });
        btnBolumGuncelle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Anasayfa.this,BolumGuncelle.class);
                startActivity(intent);
                finish();
            }
        });
        btnDoktorEkle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Anasayfa.this,DoktorEkle.class);
                startActivity(intent);
                finish();
            }
        });
        btnDoktorSil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Anasayfa.this,DoktorSil.class);
                startActivity(intent);
                finish();
            }
        });
        btnDoktorGuneclle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Anasayfa.this,DoktorGuncelle.class);
                startActivity(intent);
                finish();
            }
        });
        btnSaatKisitla.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Anasayfa.this,SaatKisitla.class);
                startActivity(intent);
                finish();
            }
        });
        btnSaatAc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent ıntent = new Intent(Anasayfa.this,KisitKaldir.class);
                startActivity(ıntent);
                finish();
            }
        });
        btnExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                User.uID="";
                Intent intent = new Intent(Anasayfa.this,LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void ilklendirme() {
        btnHastaneEkle = findViewById(R.id.btnHastaneEkle);
        btnHastaneGuncelle = findViewById(R.id.btnHastaneGuncelle);
        btnHastaneSil = findViewById(R.id.btnHastaneSil);
        btnBolumEkle = findViewById(R.id.btnBolumEkle);
        btnBolumGuncelle = findViewById(R.id.btnBolumGuncelle);
        btnBolumSil = findViewById(R.id.btnBolumSil);
        btnDoktorEkle = findViewById(R.id.btnDoktorEkle);
        btnDoktorGuneclle = findViewById(R.id.btnDoktorGuncelle);
        btnDoktorSil = findViewById(R.id.btnDoktorSil);
        btnSaatKisitla = findViewById(R.id.btnSaatKisitla);
        btnSaatAc = findViewById(R.id.btnSaatKisitla2);
        btnExit = findViewById(R.id.btnAdminExit);
    }

    @Override
    public void onBackPressed() {
        System.exit(0);
    }
}
