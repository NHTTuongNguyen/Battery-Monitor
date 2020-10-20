package com.example.batterymonitor.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.batterymonitor.R;
import com.example.batterymonitor.models.ChartsModel;

import java.util.ArrayList;

public class ChartsAdapter extends RecyclerView.Adapter<ChartsAdapter.ViewHolder> {
    Context context;
    ArrayList<ChartsModel> chartsModelArrayList;

    public ChartsAdapter(Context context, ArrayList<ChartsModel> chartsModelArrayList) {
        this.context = context;
        this.chartsModelArrayList = chartsModelArrayList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_charts,parent,false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ChartsModel chartsModel = chartsModelArrayList.get(position);
        holder.txtLevelBattery.setText(chartsModel.getLevelBattery()+"");
        holder.txtHoursBattery.setText(chartsModel.getHours()+"");

    }

    @Override
    public int getItemCount() {
        return chartsModelArrayList.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtLevelBattery,txtHoursBattery;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtLevelBattery = itemView.findViewById(R.id.txtLevelBattery);
            txtHoursBattery = itemView.findViewById(R.id.txtHoursBattery);
        }
    }
}
