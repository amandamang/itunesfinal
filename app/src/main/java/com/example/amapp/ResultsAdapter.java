package com.example.amapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.io.Serializable;
import java.util.ArrayList;

import androidx.recyclerview.widget.RecyclerView;

public class ResultsAdapter extends RecyclerView.Adapter<ResultsAdapter.MyViewHolder> implements View.OnClickListener, Serializable {
    Context rContext;
    private ResultsAdapter.ItemClickListener rClickListener;
    private ArrayList<Results> resultsArrayList;
    private ArrayList<Results> arraylist;

    public ResultsAdapter(Context context, ArrayList<Results> resultsArrayList){
        this.rContext = context;
        this.resultsArrayList = resultsArrayList;
        this.arraylist = new ArrayList<>();
        this.arraylist.addAll(resultsArrayList);
    }

    @Override
    public ResultsAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.results_item, parent, false);
        itemView.setOnClickListener(this);
        return new ResultsAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final ResultsAdapter.MyViewHolder myViewHolder, int position) {
        myViewHolder.tvResult.setText(String.valueOf(resultsArrayList.get(position).getArtistName()));
    }


    @Override
    public int getItemCount() {
        return resultsArrayList.size();
    }


    @Override
    public void onClick(View v) {

    }

    public void setClickListener(ResultsAdapter.ItemClickListener itemClickListener) {
    this.rClickListener = itemClickListener;
    }


    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView tvResult;

        MyViewHolder(View itemView){
            super(itemView);
            tvResult = itemView.findViewById(R.id.tvResult);
            rContext = itemView.getContext();
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if(rClickListener != null) rClickListener.onItemClick(view, getAdapterPosition());
        }
    }

    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }
}

