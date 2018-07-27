package com.yenti.pencarianjarak;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.yenti.pencarianjarak.helper.Config;
import com.yenti.pencarianjarak.model.Wisata;

public class DetailWisataActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_wisata);

        final Wisata data = getIntent().getParcelableExtra("data");
        if(data != null){
            String path = Config.Base_URL + data.getFoto();
            Picasso.get().load(path).into((ImageView) findViewById(R.id.ImageWisata));
            ((TextView) findViewById(R.id.textViewWisata)).setText(data.getNama_wisata());
            ((TextView) findViewById(R.id.textViewTelp)).setText(data.getNo_telp());
            ((TextView) findViewById(R.id.textViewKet)).setText(data.getKeterangan());
            ((TextView) findViewById(R.id.textViewAlamat)).setText(data.getAlamat());
            (findViewById(R.id.textViewLabelNav)).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent nextPage = new Intent(DetailWisataActivity.this, DirectionListActivity.class);
                    nextPage.putExtra("data", data);
                    startActivity(nextPage);
                }
            });
        }else {
            onBackPressed();
        }
    }
}
