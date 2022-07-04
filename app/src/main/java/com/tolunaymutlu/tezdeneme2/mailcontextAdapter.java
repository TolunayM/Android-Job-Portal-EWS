package com.tolunaymutlu.tezdeneme2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class mailcontextAdapter extends RecyclerView.Adapter<mailcontextAdapter.MyViewHolder> {

    private ArrayList<User> usersList;

    public mailcontextAdapter(ArrayList<User> usersList){
        this.usersList = usersList;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }

    @NonNull
    @Override
    public mailcontextAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull mailcontextAdapter.MyViewHolder holder, int position) {


    }

    @Override
    public int getItemCount() {
        return 0;
    }
}
