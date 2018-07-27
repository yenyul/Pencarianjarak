package com.yenti.pencarianjarak;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

//import static com.yenti.pencarianjarak.SplashScreen.nama_event;

public class MenuActivity extends AppCompatActivity implements View.OnClickListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

//        System.out.println(nama_event[0]);

        Button btn_wisata = (Button) findViewById(R.id.activityMenuBtnWisata);
        Button btn_about = (Button) findViewById(R.id.activityMenuBtnAbout);
        Button btn_event = (Button) findViewById(R.id.activityMenuBtnEvent);
        Button btn_search = (Button) findViewById(R.id.activityMenuBtnSearch);
        Button btn_kontribusi = (Button) findViewById(R.id.activityMenuBtnKontribusi);
        if (btn_wisata != null) {
            btn_wisata.setOnClickListener(this);
        }
        if (btn_event != null) {
            btn_event.setOnClickListener(this);
        }
        if (btn_about != null) {
            btn_about.setOnClickListener(this);
        }
        if (btn_search != null) {
            btn_search.setOnClickListener(this);
        }
        if (btn_kontribusi != null) {
            btn_kontribusi.setOnClickListener(this);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.activityMenuBtnWisata:
                startActivity(new Intent(MenuActivity.this, CategoryActivity.class));
                break;
            case R.id.activityMenuBtnEvent:
                startActivity(new Intent(MenuActivity.this, MonthActivity.class));
                break;
            case R.id.activityMenuBtnAbout:
                startActivity(new Intent(MenuActivity.this, AboutActivity.class));
                break;
            case R.id.activityMenuBtnSearch:
                startActivity(new Intent(MenuActivity.this, SearchActivity.class));
                break;
            case R.id.activityMenuBtnKontribusi:
                startActivity(new Intent(MenuActivity.this, GoogleLoginActivity.class));
                break;
        }
    }
}
