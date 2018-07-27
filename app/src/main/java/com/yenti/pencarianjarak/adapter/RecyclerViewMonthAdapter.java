package com.yenti.pencarianjarak.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.yenti.pencarianjarak.R;
import com.yenti.pencarianjarak.listener.OnRecyclerItemClickListener;

import java.util.ArrayList;


public class RecyclerViewMonthAdapter extends RecyclerView.Adapter<RecyclerViewMonthAdapter.ViewHolder> {
    private final ArrayList<String> mValues;
    private final OnRecyclerItemClickListener<String> mListener;
    //private final int images[];

    public RecyclerViewMonthAdapter(ArrayList<String> items, OnRecyclerItemClickListener<String> listener) {
        mValues = items;
        mListener = listener;
    }

    @Override
    public RecyclerViewMonthAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_bulan, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final RecyclerViewMonthAdapter.ViewHolder holder, final int position) {
        holder.mItem = mValues.get(position);
        holder.mTitleView.setText(mValues.get(position));

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    mListener.onRecyclerItemClickListener(String.valueOf(position + 1));
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
        //public final ImageView mImageView;
        public final TextView mTitleView;
        public String mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            //mImageView = (ImageView) view.findViewById(R.id.image);
            mTitleView = (TextView) view.findViewById(R.id.monthtitle);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mTitleView.getText() + "'";
        }
    }
}
