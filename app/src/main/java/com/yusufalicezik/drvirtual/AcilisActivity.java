package com.yusufalicezik.drvirtual;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;
import com.yusufalicezik.drvirtual.HastaIslemleri.HastaAnaMenuActivity;
import com.yusufalicezik.drvirtual.Login.HastaLoginActivity;

public class AcilisActivity extends AppCompatActivity {

    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_acilis);


        mAuth=FirebaseAuth.getInstance();
        if(mAuth.getCurrentUser()!=null){
            Intent intent=new Intent(AcilisActivity.this,HastaAnaMenuActivity.class);
            startActivity(intent);
        }


    }
    public void hastaButonTik(View view){
        Intent intent=new Intent(AcilisActivity.this, HastaLoginActivity.class);
        startActivity(intent);
    }
    public void doktorButonTik(View view){

    }
}
