package com.yusufalicezik.drvirtual.Login;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.yusufalicezik.drvirtual.AcilisActivity;
import com.yusufalicezik.drvirtual.HastaIslemleri.HastaAnaMenuActivity;
import com.yusufalicezik.drvirtual.R;

public class HastaLoginActivity extends AppCompatActivity {


    private EditText hastaTc, hastaSifre;
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hasta_login);
        hastaTc=findViewById(R.id.edt_hastaGirisTc);
        hastaSifre=findViewById(R.id.edt_hastaGirisSifre);
        mAuth = FirebaseAuth.getInstance();
    }

    public void hastaKayitOlTxtTik(View view){
        Intent intent=new Intent(HastaLoginActivity.this, HastaKayitOlActivity.class);
        startActivity(intent);
    }


    public void hastaGirisYapButonTik(View view){

        if(!hastaSifre.getText().toString().isEmpty() && !hastaTc.getText().toString().isEmpty()){



        String email=hastaTc.getText().toString()+"@gmail.com";
        String sifre=hastaSifre.getText().toString();

        mAuth.signInWithEmailAndPassword(email, sifre)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Intent intent=new Intent(HastaLoginActivity.this, HastaAnaMenuActivity.class);
                            startActivity(intent);
                        } else {
                            Toast.makeText(HastaLoginActivity.this, "TC veya şifre yanlış", Toast.LENGTH_SHORT).show();
                        }


                    }
                });
        }
    }

}
