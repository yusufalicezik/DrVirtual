package com.yusufalicezik.drvirtual.HastaIslemleri;

import android.app.DatePickerDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;

import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.yusufalicezik.drvirtual.R;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class RandevuAlActivity extends AppCompatActivity {


    private Spinner hastaneSecSpinner, bolumSecSpinner, doktorSecSpinner;
    private EditText secilenTarih;

    private int anaId=0;

    private ArrayList<String> hastaneler=new ArrayList<>();
    private ArrayList<Integer> hastenelerID=new ArrayList<>();

    private ArrayList<String> bolumler=new ArrayList<>();
    private ArrayList<Integer> bolumID=new ArrayList<>();

    private ArrayList<String> doktorlar=new ArrayList<>();
    private ArrayList<Integer> doktorID=new ArrayList<>();






    private ArrayAdapter<String> adapterDoktorlar;


    private ArrayAdapter<String> adapterBolumler;


    private ArrayAdapter<String> adapterHastane;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_randevu_al);
        hastaneSecSpinner=findViewById(R.id.spn_hastaneSecim);
        doktorSecSpinner=findViewById(R.id.spn_DoktorSecim);
        bolumSecSpinner=findViewById(R.id.spn_BolumSecim);
        secilenTarih=findViewById(R.id.edt_secilenTarih);

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
        int gun = takvim.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog dpd = new DatePickerDialog(RandevuAlActivity.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                // ay değeri 0 dan başladığı için (Ocak=0, Şubat=1,..,Aralık=11)
                // değeri 1 artırarak gösteriyoruz.
                month += 1;
                // year, month ve dayOfMonth değerleri seçilen tarihin değerleridir.
                // Edittextte bu değerleri gösteriyoruz.
                secilenTarih.setText(dayOfMonth + "/" + month + "/" + year);
            }
        }, yil, ay, gun);
      // datepicker açıldığında set edilecek değerleri buraya yazıyoruz.
        // şimdiki zamanı göstermesi için yukarıda tanımladığımız değişkenleri kullanıyoruz.
        // dialog penceresinin button bilgilerini ayarlıyoruz ve ekranda gösteriyoruz.
        dpd.setButton(DatePickerDialog.BUTTON_POSITIVE, "Seç", dpd);
        dpd.setButton(DatePickerDialog.BUTTON_NEGATIVE, "İptal", dpd);
        dpd.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
        dpd.show();

    }
}
