package com.yusufalicezik.drvirtual;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;
import com.yusufalicezik.drvirtual.HastaIslemleri.HastaAnaMenuActivity;
import com.yusufalicezik.drvirtual.HastaIslemleri.OnMuayeneMesajActivity;
import com.yusufalicezik.drvirtual.Login.HastaLoginActivity;

public class AcilisActivity extends AppCompatActivity {

    FirebaseAuth mAuth;

    public static int girisYapan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_acilis);



        mAuth=FirebaseAuth.getInstance();
        if(mAuth.getCurrentUser()!=null){ //eğer current kullanıcı null ise şuan current olanın hasta mı doktor mu olup olmadığını anlamak için;

            SharedPreferences ayarlar = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
             girisYapan = ayarlar.getInt("girisYapan",0);
            if(girisYapan==1){
            Intent intent=new Intent(AcilisActivity.this,HastaAnaMenuActivity.class);
            startActivity(intent);
            }else if(girisYapan==2){
            Intent intent=new Intent(AcilisActivity.this, OnMuayeneMesajActivity.class);
            startActivity(intent);
            }
        }


    }
    public void hastaButonTik(View view){
        girisYapan=1; //doktor ise 1, hasta ise 2 olacak.
        SharedPreferences ayarlar = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        SharedPreferences.Editor editor = ayarlar.edit();
        editor.putInt("girisYapan",girisYapan);
        editor.commit();
        Intent intent=new Intent(AcilisActivity.this, HastaLoginActivity.class);
        startActivity(intent);
    }
    public void doktorButonTik(View view){

        girisYapan=2; //doktor ise 1, hasta ise 2 olacak.
        SharedPreferences ayarlar = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        SharedPreferences.Editor editor = ayarlar.edit();
        editor.putInt("girisYapan",girisYapan);
        editor.commit();
        Intent intent=new Intent(AcilisActivity.this, HastaLoginActivity.class);
        startActivity(intent);
    }
}
