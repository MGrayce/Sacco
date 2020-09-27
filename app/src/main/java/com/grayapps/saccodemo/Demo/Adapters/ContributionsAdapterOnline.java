package com.grayapps.saccodemo.Demo.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.grayapps.saccodemo.Demo.Utils.Entities;
import com.grayapps.saccodemo.Demo.Utils.EntitiesOnlineObj;
import com.grayapps.saccodemo.R;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ContributionsAdapterOnline extends RecyclerView.Adapter<ContributionsAdapterOnline.MyViewHolder> {

    private List<EntitiesOnlineObj> entitiesOnlineObjList;
    private Context context;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView member, amount, action;

        public MyViewHolder(View view) {
            super(view);
            member = itemView.findViewById(R.id.member);
            amount = itemView.findViewById(R.id.amount);
            action = itemView.findViewById(R.id.action);
        }
    }
    public ContributionsAdapterOnline(List<EntitiesOnlineObj> entitiesOnlineObjList, Context context) {
        this.entitiesOnlineObjList = entitiesOnlineObjList;
        this.context = context;
    }
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.contributions_layout, parent, false);
        return new MyViewHolder(itemView);
    }
    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        EntitiesOnlineObj t = entitiesOnlineObjList.get(position);
        holder.member.setText(t.getMember());
        holder.amount.setText(t.getAmount());
        holder.action.setText(t.getAction());
    }
    @Override
    public int getItemCount() {
        return entitiesOnlineObjList.size();
    }
}
