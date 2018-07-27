package com.yenti.pencarianjarak.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.yenti.pencarianjarak.R;
import com.yenti.pencarianjarak.helper.Config;
import com.yenti.pencarianjarak.listener.OnRecyclerItemClickListener;
import com.yenti.pencarianjarak.model.Wisata;

import java.util.ArrayList;
import java.util.Locale;

/**
 * Created by Yenti on 4/22/2018.
 *
 */

public class RecyclerViewWisataAdapter extends RecyclerView.Adapter<RecyclerViewWisataAdapter.ViewHolder>
{
    private final ArrayList<Wisata> listWisata;
    private final ArrayList<Wisata> tempList = new ArrayList<>();
    private final OnRecyclerItemClickListener<Wisata> mListener;




    public RecyclerViewWisataAdapter(ArrayList<Wisata> listWisata, OnRecyclerItemClickListener<Wisata> listener) {
        this.listWisata = listWisata;
        mListener = listener;
    }

    @Override
    public RecyclerViewWisataAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_wisata, parent, false);
        return new RecyclerViewWisataAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final RecyclerViewWisataAdapter.ViewHolder holder, int position) {
        String path = Config.Base_URL + listWisata.get(position).getFoto();
        Picasso.get().load(path).into(holder.mImageView);
        final Wisata tempPosition = listWisata.get(position);
        holder.mTitleView.setText(listWisata.get(position).getNama_wisata());
        holder.mTextViewSeeMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    mListener.onRecyclerItemClickListener(tempPosition);
                }
            }
        });
    }

    public int getItemCount() {
        return listWisata.size();
    }

    public void invalidateSearchDataSet(){
        tempList.addAll(listWisata);
    }

    public void filter(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());
        listWisata.clear();
        if (charText.length() == 0) {
            listWisata.addAll(tempList);
        } else {
            for (Wisata wp : tempList) {
                if (wp.getKeterangan().toLowerCase(Locale.getDefault()).contains(charText)) {
                    listWisata.add(wp);
                }
            }
        }
        notifyDataSetChanged();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final ImageView mImageView;
        public final TextView mTitleView;
        public final TextView mTextViewSeeMore;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mImageView = (ImageView) view.findViewById(R.id.imageView);
            mTitleView = (TextView) view.findViewById(R.id.textWisata);
            mTextViewSeeMore = (TextView) view.findViewById(R.id.textViewSeeMore);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mTitleView.getText() + "'";
        }
    }
}

