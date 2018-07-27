package com.yenti.pencarianjarak.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.yenti.pencarianjarak.R;
import com.yenti.pencarianjarak.listener.OnRecyclerItemClickListener;
import com.yenti.pencarianjarak.model.Event;

import java.util.ArrayList;

/**
 * Created by Yenti on 5/7/2018.
 */

public class RecyclerViewEventAdapter extends RecyclerView.Adapter<RecyclerViewEventAdapter.ViewHolder>
{

    private final ArrayList<Event> listEvent;
    private final OnRecyclerItemClickListener<Event> mListener;

    public RecyclerViewEventAdapter(ArrayList<Event> listEvent, OnRecyclerItemClickListener<Event> listener) {
        this.listEvent = listEvent;
        mListener = listener;
    }

    @Override
    public RecyclerViewEventAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_event, parent, false);
        return new RecyclerViewEventAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerViewEventAdapter.ViewHolder holder, int position) {

        final Event tempPosition = listEvent.get(position);
        holder.mTitleView.setText(listEvent.get(position).getNama_event());
        holder.mTitleView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    mListener.onRecyclerItemClickListener(tempPosition);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return listEvent.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mTitleView;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mTitleView = (TextView) view.findViewById(R.id.EventText);
        }
        @Override
        public String toString() {
            return super.toString() + " '" + mTitleView.getText() + "'";
        }
    }

}
