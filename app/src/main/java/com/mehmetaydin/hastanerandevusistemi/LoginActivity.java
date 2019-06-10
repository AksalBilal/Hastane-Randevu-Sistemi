package com.mehmetaydin.hastanerandevusistemi;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mehmetaydin.hastanerandevusistemi.Model.Kisi;
import com.mehmetaydin.hastanerandevusistemi.Model.User;

public class LoginActivity extends AppCompatActivity {
    TextInputLayout tilTCKN, tilSifre;
    Button btnGiris;
    TextView twGiris;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        User.uID="0";
        ilklendirme();
        twGiris.setText("");
        
        btnGiris.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(tcknDogrula() && sifreDogrula()){
                    twGiris.setText("Giriş Yapılıyor...");
                    girisYap();
                }
            }
        });
    }

    private void girisYap() {
        final DatabaseReference reference2 = FirebaseDatabase.getInstance().getReference("Users");
        reference2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    Kisi kisi = snapshot.getValue(Kisi.class);
                    if(kisi.getTCKN().equalsIgnoreCase(tilTCKN.getEditText().getText().toString()) && kisi.getSifre().equalsIgnoreCase(tilSifre.getEditText().getText().toString())){
                        User.uID=snapshot.getKey();
                        if(kisi.getRol().equalsIgnoreCase("Hasta")){
                            Intent intent = new Intent(LoginActivity.this,HastaHomePage.class);
                            startActivity(intent);
                            finish();
                            break;
                        }else if(kisi.getRol().equalsIgnoreCase("Doktor")){
                            AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                            builder.setTitle("Giriş Seçimi");
                            builder.setMessage("Hangi panelde oturum açmak istiyorsunuz?");
                            builder.setNegativeButton("Hasta Olarak Devam Et", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Intent intent = new Intent(LoginActivity.this,HastaHomePage.class);
                                    startActivity(intent);
                                    finish();
                                }
                            });
                            builder.setPositiveButton("Doktor Olarak Devam Et", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Intent intent = new Intent(LoginActivity.this,DoktorAnasayfa.class);
                                    startActivity(intent);
                                    finish();
                                }
                            });
                            builder.show();
                        }else if(kisi.getRol().equalsIgnoreCase("Admin")){
                            AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                            builder.setTitle("Giriş Seçimi");
                            builder.setMessage("Hangi panelde oturum açmak istiyorsunuz?");
                            builder.setNegativeButton("Hasta Olarak Devam Et", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Intent intent = new Intent(LoginActivity.this,HastaHomePage.class);
                                    startActivity(intent);
                                    finish();
                                }
                            });
                            builder.setPositiveButton("Admin Olarak Devam Et", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Intent intent = new Intent(LoginActivity.this,Anasayfa.class);
                                    startActivity(intent);
                                    finish();
                                }
                            });
                            builder.show();
                        }
                    }
                }
                if(User.uID=="" || User.uID=="0"){
                    twGiris.setText("");
                    Toast.makeText(LoginActivity.this, "TCKN veya Şifre Hatalı!", Toast.LENGTH_SHORT).show();
                }
                reference2.removeEventListener(this);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void ilklendirme() {
        tilTCKN = findViewById(R.id.text_input_tckn);
        tilSifre = findViewById(R.id.text_input_sifre);
        btnGiris = findViewById(R.id.btnGiris);
        twGiris = findViewById(R.id.twGiris);
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

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(LoginActivity.this,MainActivity.class);
        startActivity(intent);
        finish();
    }
}
