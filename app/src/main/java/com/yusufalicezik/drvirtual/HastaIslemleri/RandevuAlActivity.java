package com.yusufalicezik.drvirtual.HastaIslemleri;

import android.app.DatePickerDialog;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;

import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.yusufalicezik.drvirtual.R;
import com.yusufalicezik.drvirtual.Utils.DoktorCalismaTarihiParcala;

import java.lang.reflect.Array;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;

public class RandevuAlActivity extends AppCompatActivity {


    private Spinner hastaneSecSpinner, bolumSecSpinner, doktorSecSpinner;
    private EditText secilenTarih;
    private LinearLayout randevuTarihLayout;
    private Button r08,r10,r11,r12,r13,r15,r17;

    private Button []randevuButonlari;


    private int anaId=0;


    private int secilenGun=0;



    private ArrayList<String> hastaneler=new ArrayList<>();
    private ArrayList<Integer> hastenelerID=new ArrayList<>();

    private ArrayList<String> bolumler=new ArrayList<>();
    private ArrayList<Integer> bolumID=new ArrayList<>();

    private ArrayList<String> doktorlar=new ArrayList<>();
    private ArrayList<Integer> doktorID=new ArrayList<>();






    private ArrayAdapter<String> adapterDoktorlar;


    private ArrayAdapter<String> adapterBolumler;


    private ArrayAdapter<String> adapterHastane;


    ///Static
    public static HashMap<Integer, String> mapGunler=new HashMap<>();
    public static HashMap<Integer, String> mapSaatler=new HashMap<>();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_randevu_al);
        hastaneSecSpinner=findViewById(R.id.spn_hastaneSecim);
        doktorSecSpinner=findViewById(R.id.spn_DoktorSecim);
        bolumSecSpinner=findViewById(R.id.spn_BolumSecim);
        secilenTarih=findViewById(R.id.edt_secilenTarih);
        randevuTarihLayout=findViewById(R.id.randevuTarihGenelLayout);

        randevuTarihLayout.setVisibility(View.INVISIBLE);

        r08=findViewById(R.id.button_randevu08);
        r10=findViewById(R.id.button_randevu10);
        r11=findViewById(R.id.button_randevu11);
        r12=findViewById(R.id.button_randevu12);
        r13=findViewById(R.id.button_randevu13);
        r15=findViewById(R.id.button_randevu15);
        r17=findViewById(R.id.button_randevu17);

        randevuButonlari= new Button[]{r08, r10,r11,r12,r13,r15,r17};



        mapGunler.put(1,"Pazartesi");
        mapGunler.put(2,"Salı");
        mapGunler.put(3,"Çarşamba");
        mapGunler.put(4,"Perşembe");
        mapGunler.put(5,"Cuma");
        mapGunler.put(6,"Cumartesi");
        mapGunler.put(7,"Cumartesi");

        mapSaatler.put(1,"08:00");
        mapSaatler.put(2,"10:00");
        mapSaatler.put(3,"11:00");
        mapSaatler.put(4,"12:00");
        mapSaatler.put(5,"13:00");
        mapSaatler.put(6,"15:00");
        mapSaatler.put(7,"17:00");



        adapterHastane= new ArrayAdapter<String>(getApplicationContext(),
                android.R.layout.simple_spinner_item, hastaneler);
        adapterHastane.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        hastaneSecSpinner.setAdapter(adapterHastane);



        adapterDoktorlar = new ArrayAdapter<String>(getApplicationContext(),
                android.R.layout.simple_spinner_item, doktorlar);
        adapterDoktorlar.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        doktorSecSpinner.setAdapter(adapterDoktorlar);


        adapterBolumler = new ArrayAdapter<String>(getApplicationContext(),
                android.R.layout.simple_spinner_item, bolumler);
        adapterBolumler.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        bolumSecSpinner.setAdapter(adapterBolumler);




        hastaneVerileriniCek();


        hastaneSecSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                doktorlar.clear();
                doktorID.clear();
                adapterDoktorlar.notifyDataSetChanged();


                anaId=hastenelerID.get(position);
                ParseQuery<ParseObject> query = ParseQuery.getQuery("Bolumler");
                query.whereEqualTo("hastane_id", hastenelerID.get(position));
                query.orderByAscending("hastane_id");
                query.findInBackground(new FindCallback<ParseObject>() {
                    public void done(List<ParseObject> scoreList, ParseException e) {
                        if (e == null) {
                           final String bolum_idleri=scoreList.get(0).getString("bolum_idleri");
                           String dizi[]=bolum_idleri.split(",");

                           bolumler.clear();
                           bolumID.clear();
                            adapterBolumler.notifyDataSetChanged();



                           for(int i=0;i<dizi.length;i++) {
                               ParseQuery<ParseObject> query = ParseQuery.getQuery("Branslar");
                               query.whereEqualTo("brans_id", Integer.valueOf(dizi[i]));
                               query.orderByAscending("brans_id");
                               query.findInBackground(new FindCallback<ParseObject>() {
                                   public void done(List<ParseObject> scoreList, ParseException e) {
                                       if (e == null) {
                                          bolumler.add(scoreList.get(0).getString("brans_adi"));
                                          bolumID.add(scoreList.get(0).getInt("brans_id"));
                                          adapterBolumler.notifyDataSetChanged();
                                          adapterDoktorlar.notifyDataSetChanged();
                                       } else {

                                       }
                                   }
                               });
                           }













                        } else {

                        }
                    }
                });

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });



        bolumSecSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, final int position, long id) {
                doktorlar.clear();
                doktorID.clear();
                adapterDoktorlar.notifyDataSetChanged();

                ParseQuery<ParseObject> query = ParseQuery.getQuery("Doktorlar");

                query.whereEqualTo("hastane_id", String.valueOf(anaId));
                query.whereEqualTo("brans_id", String.valueOf(bolumID.get(position)));

                query.findInBackground(new FindCallback<ParseObject>() {
                    public void done(List<ParseObject> scoreList, ParseException e) {
                        if (e == null) {
                            for(ParseObject object:scoreList){

                                Log.d("hata Hastane id", String.valueOf(hastenelerID.get(position)));
                                Log.d("hata brans id", String.valueOf(bolumID.get(position)));

                                doktorlar.add(object.getString("doktor_adi_soyadi"));
                                doktorID.add(object.getInt("doktor_tc"));

                            }

                            adapterDoktorlar.notifyDataSetChanged();


                        } else {

                        }
                    }
                });

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


            }

    private void hastaneVerileriniCek() {
        ParseQuery<ParseObject> query=ParseQuery.getQuery("Hastaneler");
        query.orderByAscending("hastane_id");
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {

                if(objects.size()>0){

                    for(ParseObject object:objects){
                        hastaneler.add(object.getString("hastane_adi"));
                        hastenelerID.add(object.getInt("hastane_id"));
                        Log.e("ddd", String.valueOf(object.getInt("hastane_id")));
                    }

                    adapterHastane.notifyDataSetChanged();

                }
            }
        });
    }

    public void tarihSecButonTik(View view){
        // Şimdiki zaman bilgilerini alıyoruz. güncel yıl, güncel ay, güncel gün.
        final Calendar takvim = Calendar.getInstance();
        int yil = takvim.get(Calendar.YEAR);
        int ay = takvim.get(Calendar.MONTH);
        int gun = takvim.get(Calendar.DAY_OF_MONTH)+1;





        DatePickerDialog dpd = new DatePickerDialog(RandevuAlActivity.this, new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                // ay değeri 0 dan başladığı için (Ocak=0, Şubat=1,..,Aralık=11)
                // değeri 1 artırarak gösteriyoruz.
                month += 1;
                // year, month ve dayOfMonth değerleri seçilen tarihin değerleridir.
                // Edittextte bu değerleri gösteriyoruz.
                secilenTarih.setText(dayOfMonth + "/" + month + "/" + year);




                GregorianCalendar GregorianCalendar = new GregorianCalendar(year, month-1, dayOfMonth-1);

                int dayOfWeek=GregorianCalendar.get(takvim.DAY_OF_WEEK);

                secilenGun=dayOfWeek;

            }
        }, yil, ay, gun);
      // datepicker açıldığında set edilecek değerleri buraya yazıyoruz.
        // şimdiki zamanı göstermesi için yukarıda tanımladığımız değişkenleri kullanıyoruz.
        // dialog penceresinin button bilgilerini ayarlıyoruz ve ekranda gösteriyoruz.
        dpd.setButton(DatePickerDialog.BUTTON_POSITIVE, "Seç", dpd);
        dpd.setButton(DatePickerDialog.BUTTON_NEGATIVE, "İptal", dpd);
        dpd.getDatePicker().setMinDate(System.currentTimeMillis()+86400000);
        dpd.getDatePicker().setMaxDate(System.currentTimeMillis()+604800000);




        dpd.show();

    }
    public void randevuAlButonTik(View view){

        String denemeVeri="1-3,1-5,1-1,2-1,1-4,2-2,2-4,2-5"; //dolu olanlar bunlar. Bunlar bir doktorun(seçilen) doktorun yani
        //daha önce alınan randevuları.. günler ve saatlere göre..

        DoktorCalismaTarihiParcala doktorCalismaTarihiParcala=new DoktorCalismaTarihiParcala();

        ArrayList<ArrayList>gelenListe;

        gelenListe=doktorCalismaTarihiParcala.parcala(denemeVeri);

        ArrayList<String>doluGunler=gelenListe.get(0);
        ArrayList<String>doluSaatler=gelenListe.get(1);

        for(int i=0;i<doluGunler.size();i++){

            Log.e("kontrol", "Dolu Gün : "+mapGunler.get(Integer.valueOf(doluGunler.get(i))) + " ve saati : "+
                    mapSaatler.get(Integer.valueOf(doluSaatler.get(i))));

        }

        Log.e("secilenGun",String.valueOf(secilenGun));


        ArrayList<String>secilenGunDoluSaatler=new ArrayList<>();
        for(int i=0;i<doluGunler.size();i++){
            if(doluGunler.get(i).equals(String.valueOf(secilenGun))){
                secilenGunDoluSaatler.add(doluSaatler.get(i));

            }
        }



        ////
        randevuTarihLayout.setVisibility(View.VISIBLE);




        /////T
        for(int i=0;i<randevuButonlari.length;i++){
            randevuButonlari[i].setEnabled(true);
            randevuButonlari[i].setBackgroundColor(Color.GREEN);

            final int finalI = i;
            randevuButonlari[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(RandevuAlActivity.this, randevuButonlari[finalI].getText().toString()+
                            " saatini seçtiniz..", Toast.LENGTH_SHORT).show();
                }
            });
        }
        /////T

        for(int i=0;i<secilenGunDoluSaatler.size();i++){
            Log.e("dols",secilenGunDoluSaatler.get(i));
      // Log.e("doluu",mapSaatler.get(Integer.valueOf(secilenGunDoluSaatler.get(i))));
           String saat=mapSaatler.get(Integer.valueOf(secilenGunDoluSaatler.get(i)));
           switch (saat){
               case "08:00":{
                   randevuButonlari[0].setEnabled(false);
                   randevuButonlari[0].setBackgroundColor(Color.RED);
               }break;
               case "10:00":{
                   randevuButonlari[1].setEnabled(false);
                   randevuButonlari[1].setBackgroundColor(Color.RED);
               }break;
               case "11:00":{
                   randevuButonlari[2].setEnabled(false);
                   randevuButonlari[2].setBackgroundColor(Color.RED);
               }break;
               case "12:00":{
                   randevuButonlari[3].setEnabled(false);
                   randevuButonlari[3].setBackgroundColor(Color.RED);
               }break;
               case "13:00":{
                   randevuButonlari[4].setEnabled(false);
                   randevuButonlari[4].setBackgroundColor(Color.RED);
               }break;
               case "15:00":{
                   randevuButonlari[5].setEnabled(false);
                   randevuButonlari[5].setBackgroundColor(Color.RED);
               }break;
               case "17:00":{
                   randevuButonlari[6].setEnabled(false);
                   randevuButonlari[6].setBackgroundColor(Color.RED);
               }break;
           }
        }



        //BU KISIMLAR FARKLI METHODTA YAPILABİLİR.


    }
}
