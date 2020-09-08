package com.example.myapp.Planner;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapp.R;

import java.util.ArrayList;

public class HorizontalDaysAdapter extends RecyclerView.Adapter<HorizontalDaysAdapter.ViewHolder> {

    private ArrayList<DayModel> days;
    private Context context;
    private OnHorizontalItemClickListener onHorizontalItemClickListener;


    public HorizontalDaysAdapter(Context context, ArrayList<DayModel> days,
                                 OnHorizontalItemClickListener onHorizontalItemClickListener) {
        this.context = context;
        this.days = days;
        this.onHorizontalItemClickListener = onHorizontalItemClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.day_item, parent, false);
        return new ViewHolder(v, onHorizontalItemClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        DayModel dayModel = days.get(position);
        holder.day.setText(dayModel.getDay());
    }

    @Override
    public int getItemCount() {
        return days.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView day;
        OnHorizontalItemClickListener onHorizontalItemClickListener;

        public ViewHolder(@NonNull View itemView, OnHorizontalItemClickListener onHorizontalItemClickListener) {
            super(itemView);
            day = itemView.findViewById(R.id.textViewDayItem);
            this.onHorizontalItemClickListener = onHorizontalItemClickListener;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            onHorizontalItemClickListener.dayClicked(getAdapterPosition());
        }
    }

    public interface OnHorizontalItemClickListener {
        void dayClicked (int position);
    }
}
