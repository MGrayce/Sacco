package com.grayapps.saccodemo.Demo.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.grayapps.saccodemo.Demo.Utils.Entities;
import com.grayapps.saccodemo.R;

import java.util.List;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.TasksViewHolder> {

    private Context mCtx;
    private List<Entities> entitiesList;

    public HistoryAdapter(Context mCtx, List<Entities> entitiesList) {
        this.mCtx = mCtx;
        this.entitiesList = entitiesList;
    }

    @Override
    public TasksViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mCtx).inflate(R.layout.transactions_layout, parent, false);
        return new TasksViewHolder(view);
    }

    @Override
    public void onBindViewHolder(TasksViewHolder holder, int position) {
        Entities t = entitiesList.get(position);
        holder.date.setText(t.getDate());
        holder.amount.setText(t.getAmount());
        int pp = entitiesList.size() - 1;



        if(entitiesList.get(position).getAmount().equals(entitiesList.get(0).getAmount())){
            holder.balance.setText(t.getAmount());
        }
//        else {
//            for(int i = 0; i < entitiesList.size(); i++){
//                int key = entitiesList.indexOf(i);
//                int cm = Integer.parseInt(entitiesList.get(position).getAmount());
//                int pm = Integer.parseInt(entitiesList.get(i).getAmount());
//                int balance = cm + pm;
//                holder.balance.setText(""+balance);
//            }
//        }

    }

    @Override
    public int getItemCount() {
        return entitiesList.size();
    }

    class TasksViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView date, amount, balance;

        public TasksViewHolder(View itemView) {
            super(itemView);

            date = itemView.findViewById(R.id.date);
            amount = itemView.findViewById(R.id.amount);
            balance = itemView.findViewById(R.id.balance);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            Entities task = entitiesList.get(getAdapterPosition());
        }
    }
}
