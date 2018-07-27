package com.yenti.pencarianjarak;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.yenti.pencarianjarak.helper.Config;
import com.yenti.pencarianjarak.model.Event;
import com.yenti.pencarianjarak.model.Wisata;

public class DetailEventActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_event);

        final Event data = getIntent().getParcelableExtra("data");
        if(data != null){
            String path = Config.Base_URL;
            //Picasso.get().load(path).into((ImageView) findViewById(R.id.ImageWisata));
            ((TextView) findViewById(R.id.nama_eventText)).setText(data.getNama_event());
            ((TextView) findViewById(R.id.waktuText)).setText(data.getWaktu());
            ((TextView) findViewById(R.id.lokasiText)).setText(data.getTempat());

        }else {
            onBackPressed();
        }
    }
}
