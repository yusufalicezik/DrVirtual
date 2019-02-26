package com.yusufalicezik.drvirtual.HastaIslemleri;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.yusufalicezik.drvirtual.R;

import java.util.List;

public class HastaAnaMenuActivity extends AppCompatActivity {

    private  TextView txtHastaAnaMenuHG;
    private FirebaseAuth mAuth;
    private String currentUserID;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hasta_ana_menu);

        txtHastaAnaMenuHG=findViewById(R.id.txt_hastaAnaMenuHG);

        mAuth = FirebaseAuth.getInstance();
        currentUserID=mAuth.getCurrentUser().getUid().toString();

        verileriGetir();

    }

    private void verileriGetir() {

        ParseQuery<ParseObject> query = ParseQuery.getQuery("Hastalar");
        query.whereEqualTo("uid", currentUserID);
        query.findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> scoreList, ParseException e) {
                if (e == null) {
                   txtHastaAnaMenuHG.setText("Hoşgeldiniz, "+scoreList.get(0).getString("isim") + " "+ scoreList.get(0).getString("soyisim"));
                } else {

                }
            }
        });

    }

    public void randevuAlMenuTik(View view){
        Intent intent=new Intent(HastaAnaMenuActivity.this,RandevuAlActivity.class);
        startActivity(intent);
    }
}
