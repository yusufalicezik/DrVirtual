package com.yusufalicezik.drvirtual.HastaIslemleri;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.yusufalicezik.drvirtual.Model.GenelKullanici;
import com.yusufalicezik.drvirtual.Model.Mesajlar;
import com.yusufalicezik.drvirtual.R;
import com.yusufalicezik.drvirtual.Utils.MessagesAdapter;

import java.util.ArrayList;
import java.util.List;

import static com.yusufalicezik.drvirtual.AcilisActivity.girisYapan;

public class MessagesPersonActivity extends AppCompatActivity {

    private TextView isimSoyisim;
    private EditText mesajText;

    private String mesajGonderilenTC; //bu sonradan uid olacak. bu tc ye göre id yi çekeceğiz.
    private String mesajGonderilenIsimSoyisim;
    private String mesajGonderilenID;



    ///
    private FirebaseDatabase database;
    private DatabaseReference mRefMesajGonderen,mRefMesajAlan,mRef;
    private FirebaseAuth mAuth;
    private RecyclerView liste;

    ///

    String currentIsim,currentuid;

    ////
    private final List<Mesajlar> messagesList=new ArrayList<>();
    private LinearLayoutManager linearLayoutManager;
    private MessagesAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messages_person);

        database=FirebaseDatabase.getInstance();
        mRefMesajGonderen=database.getReference().child("Messages");
        mRefMesajAlan=database.getReference().child("Messages");
        mRef=database.getReference();
        mAuth=FirebaseAuth.getInstance();

        isimSoyisim=findViewById(R.id.personMessagesIsimSoyisimtxt);
        mesajText=findViewById(R.id.personMessagesMesajGonderEdittext);

        mesajGonderilenTC=getIntent().getStringExtra("tc"); //bu tc ye göre uid çekilecek
        mesajGonderilenIsimSoyisim=getIntent().getStringExtra("isim");
        isimSoyisim.setText(mesajGonderilenIsimSoyisim);


        tcyeGoreUidBul();





    }

    public void tcyeGoreUidBul(){
        if(!mesajGonderilenTC.isEmpty() && !mesajGonderilenTC.equals("") || mesajGonderilenTC.equals(" ")){
            if(girisYapan==1) { //current kullanıcı hastadır. doktor tablosuna bak---------------

                ParseQuery<ParseObject> query = ParseQuery.getQuery("Doktorlar");
                query.whereEqualTo("doktor_tc", mesajGonderilenTC);
                query.findInBackground(new FindCallback<ParseObject>() {
                    public void done(List<ParseObject> scoreList, ParseException e) {
                        if (e == null) {
                            if(scoreList.size()>0) {
                                for(ParseObject object:scoreList){

                                    mesajGonderilenID=(object.getString("uid"));
                                    initBaslat();
                                }

                            }else {
                                Toast.makeText(MessagesPersonActivity.this, "mesaj sayfası hata2", Toast.LENGTH_SHORT).show();
                            }
                        }else {
                            Toast.makeText(MessagesPersonActivity.this, "mesaj sayfası hata2", Toast.LENGTH_SHORT).show();
                        }
                    }
                });



            }else{//current kullanıcı doktordur. hasta tablosuna bak------------------------
                ParseQuery<ParseObject> query = ParseQuery.getQuery("Hastalar");
                query.whereEqualTo("tc", mesajGonderilenTC);
                query.findInBackground(new FindCallback<ParseObject>() {
                    public void done(List<ParseObject> scoreList, ParseException e) {
                        if (e == null) {
                            if(scoreList.size()>0) {
                                for(ParseObject object:scoreList){

                                    mesajGonderilenID=(object.getString("uid"));
                                    initBaslat();
                                }

                            }else {
                                Toast.makeText(MessagesPersonActivity.this, "mesaj sayfası hata2.2", Toast.LENGTH_SHORT).show();
                            }
                        }else {
                            Toast.makeText(MessagesPersonActivity.this, "mesaj sayfası hata2.2", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        }
    }

    public void initBaslat(){
        adapter=new MessagesAdapter(messagesList);
        liste=findViewById(R.id.personMessagesListe);
        linearLayoutManager=new LinearLayoutManager(MessagesPersonActivity.this);
        linearLayoutManager.setStackFromEnd(true);
        liste.setHasFixedSize(true);
        liste.setLayoutManager(linearLayoutManager);
        liste.setAdapter(adapter);


        Log.e("mesajlasilanBilgiler","mesajlasilan isim : "+mesajGonderilenIsimSoyisim);
        Log.e("mesajlasilanBilgiler","mesajlasilan tc : "+mesajGonderilenTC);
        Log.e("mesajlasilanBilgiler","mesajlasilan uid : "+mesajGonderilenID);
        Log.e("mesajlasilanBilgiler","gonderen uid : "+mAuth.getCurrentUser().getUid().toString());
        // fetchMessage();
    }
}
