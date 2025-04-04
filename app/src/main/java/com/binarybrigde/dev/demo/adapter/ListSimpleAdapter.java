package com.binarybrigde.dev.demo.adapter;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mia.module.R;

import java.util.ArrayList;
import java.util.List;

public class ListSimpleAdapter extends RecyclerView.Adapter<ListSimpleAdapter.ViewHolder> {

    private List<String> sampleData = new ArrayList<>();
    private Listener listener;

    public ListSimpleAdapter(Listener listener) {
        this.listener = listener;
    }

    public ListSimpleAdapter() {
    }

    public void setSampleData(List<String> sampleData) {
        this.sampleData = sampleData;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate( R.layout.item_simple_list, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
       TextView title =  holder.itemView.findViewById(R.id.txtTile);
       title.setText(sampleData.get(position));
       holder.itemView.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               sampleData.remove(position);
               if (listener!=null)
                   listener.onRemoveItem(position);
           }
       });
    }

    @Override
    public int getItemCount() {
        return sampleData.size();
    }


    class ViewHolder extends RecyclerView.ViewHolder {
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }

    public  interface Listener{
        void onRemoveItem(int pos);
    }
}
