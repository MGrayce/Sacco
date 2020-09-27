package com.grayapps.saccodemo.Demo.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.grayapps.saccodemo.Demo.Utils.Entities;
import com.grayapps.saccodemo.R;

import java.util.List;

public class ContibutionsAdapter extends RecyclerView.Adapter<ContibutionsAdapter.TasksViewHolder> {

    private Context mCtx;
    private List<Entities> entitiesList;

    public ContibutionsAdapter(Context mCtx, List<Entities> entitiesList) {
        this.mCtx = mCtx;
        this.entitiesList = entitiesList;
    }

    @Override
    public TasksViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mCtx).inflate(R.layout.contributions_layout, parent, false);
        return new TasksViewHolder(view);
    }

    @Override
    public void onBindViewHolder(TasksViewHolder holder, int position) {
        Entities t = entitiesList.get(position);
        holder.member.setText(t.getMember());
        holder.amount.setText(t.getAmount());
        holder.action.setText(t.getAction());
    }

    @Override
    public int getItemCount() {
        return entitiesList.size();
    }

    class TasksViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView member, amount, action;

        public TasksViewHolder(View itemView) {
            super(itemView);

            member = itemView.findViewById(R.id.member);
            amount = itemView.findViewById(R.id.amount);
            action = itemView.findViewById(R.id.action);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            Entities task = entitiesList.get(getAdapterPosition());
        }
    }
}
