package com.example.myapp.Planner;

import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapp.R;

import java.util.ArrayList;

public class HorizontalDaysAdapter extends RecyclerView.Adapter<HorizontalDaysAdapter.ViewHolder> {

    private ArrayList<DayModel> days;
    private Context context;
    private OnHorizontalItemClickListener onHorizontalItemClickListener;
    private int selectedPosition = -1;


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

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        DayModel dayModel = days.get(position);
        holder.day.setText(dayModel.getDay());

        if (selectedPosition == position) {
            holder.cardView.getBackground().setTint(Color.parseColor("#3FB16B"));
            holder.day.setTextSize(TypedValue.COMPLEX_UNIT_SP, 25f);
        } else {
            holder.cardView.getBackground().setTint(Color.parseColor("#2E8650"));
            holder.day.setTextSize(TypedValue.COMPLEX_UNIT_SP, 23f);
        }
    }

    @Override
    public int getItemCount() {
        return days.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView day;
        CardView cardView;
        OnHorizontalItemClickListener onHorizontalItemClickListener;

        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
        public ViewHolder(@NonNull View itemView, final OnHorizontalItemClickListener onHorizontalItemClickListener) {
            super(itemView);
            day = itemView.findViewById(R.id.textViewDayItem);
            cardView = itemView.findViewById(R.id.cardViewDayItem);
            this.onHorizontalItemClickListener = onHorizontalItemClickListener;
            itemView.setOnClickListener(this);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(onHorizontalItemClickListener != null){
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION){
                            onHorizontalItemClickListener.dayClicked(position);
                            selectedPosition = position;
                            notifyDataSetChanged();
                        }
                    }
                }
            });
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
