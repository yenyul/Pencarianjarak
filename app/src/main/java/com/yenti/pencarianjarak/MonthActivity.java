package com.yenti.pencarianjarak;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.yenti.pencarianjarak.adapter.RecyclerViewMonthAdapter;
import com.yenti.pencarianjarak.listener.OnRecyclerItemClickListener;

import java.util.ArrayList;

public class MonthActivity extends AppCompatActivity {
    ArrayList<String> dataBulan = new ArrayList<>();
    private OnRecyclerItemClickListener<String> bulanListener = new OnRecyclerItemClickListener<String>() {
        @Override
        public void onRecyclerItemClickListener(String param) {
            Intent a = new Intent(MonthActivity.this, EventActivity.class);
            a.putExtra("bulan", param);
            startActivity(a);
        }
    };


    RecyclerViewMonthAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_month);


        if(getSupportActionBar()!= null)
        {
            getSupportActionBar().setTitle("BALADO");
        }
        dataBulan.add("Januari");
        dataBulan.add("Februari");
        dataBulan.add("Maret");
        dataBulan.add("April");
        dataBulan.add("Mei");
        dataBulan.add("Juni");
        dataBulan.add("Juli");
        dataBulan.add("Agustus");
        dataBulan.add("September");
        dataBulan.add("Oktober");
        dataBulan.add("November");
        dataBulan.add("Desember");

        adapter = new RecyclerViewMonthAdapter(dataBulan, bulanListener);

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.list);
        if(recyclerView != null) {
            recyclerView.setLayoutManager(new GridLayoutManager(this, 1));
            recyclerView.setAdapter(adapter);
        }

        //new MonthActivity.getMonth(this).execute();

    }
}
