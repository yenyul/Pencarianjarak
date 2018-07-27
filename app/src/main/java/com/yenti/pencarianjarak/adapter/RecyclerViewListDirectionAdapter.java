package com.yenti.pencarianjarak.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.maps.model.DirectionsRoute;
import com.yenti.pencarianjarak.R;
import com.yenti.pencarianjarak.listener.OnRecyclerItemClickListener;

import java.util.List;

public class RecyclerViewListDirectionAdapter extends RecyclerView.Adapter<RecyclerViewListDirectionAdapter.ViewHolder>{

    private final List<DirectionsRoute> mValues;
    private final OnRecyclerItemClickListener<DirectionsRoute> mListener;

    public RecyclerViewListDirectionAdapter(List<DirectionsRoute> items, OnRecyclerItemClickListener<DirectionsRoute> listener) {
        mValues = items;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_list, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        /*holder.mTitleView.setText("Jalur " + position);
        holder.mDistanceView.setText(String.valueOf(mValues.get(position).legs[0].distance));
        holder.mTimeView.setText(String.valueOf(mValues.get(position).legs[0].duration.humanReadable));
        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    mListener.onRecyclerItemClickListener(mValues.get(position));
                }
            }
        });*/

        holder.mTitleView.setText("Melalui : " + mValues.get(position).summary);
/*
        long distance =  0;
        for(int i = 0; i < mValues.get(position).legs[0].steps.length; i++){
            distance += mValues.get(position).legs[0].steps[i].distance.inMeters;
        }
        holder.mDistanceView.setText(String.valueOf(mValues.get(position).legs[0].distance) + " - " + String.valueOf(distance));
        */
        holder.mDistanceView.setText(String.valueOf(mValues.get(position).legs[0].distance));
        holder.mTimeView.setText(String.valueOf(mValues.get(position).legs[0].duration.humanReadable));
        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    mListener.onRecyclerItemClickListener(mValues.get(position));
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mTitleView;
        public final TextView mDistanceView;
        public final TextView mTimeView;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mTitleView = (TextView) view.findViewById(R.id.id);
            mDistanceView = (TextView) view.findViewById(R.id.distance);
            mTimeView = (TextView) view.findViewById(R.id.time);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mTitleView.getText() + "'";
        }
    }
}
