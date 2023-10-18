package com.example.stucheck.Service;

import android.content.Context;
import android.graphics.Color;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.stucheck.Model.ClassItem;
import com.example.stucheck.Model.StudentItem;
import com.example.stucheck.R;

import java.util.ArrayList;

public class StudentAdapter extends RecyclerView.Adapter<StudentAdapter.ViewHolder> {
    ArrayList<StudentItem> studentItems;
    Context context;
    private OnItemClickListener OnclickListener;
    public interface OnItemClickListener{
        void OnClick(int position);
    }
    public void setOnItemClickListener(OnItemClickListener listener){
        this.OnclickListener = listener;
    }
    public StudentAdapter(Context context, ArrayList<StudentItem> studentItems) {
        this.studentItems = studentItems;
        this.context = context;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener {
        TextView roll;
        TextView name;
        TextView status;
        CardView cardView;

        public ViewHolder(@NonNull View viewitem, OnItemClickListener itemClickListener) {
            super(viewitem);
            roll = viewitem.findViewById(R.id.roll);
            name = viewitem.findViewById(R.id.name);
            status = viewitem.findViewById(R.id.status);
            cardView = viewitem.findViewById(R.id.cardView);
            viewitem.setOnClickListener(v -> itemClickListener.OnClick(getAdapterPosition()));
            viewitem.setOnCreateContextMenuListener(this);
        }

        @Override
        public void onCreateContextMenu(ContextMenu contextMenu, View view, ContextMenu.ContextMenuInfo contextMenuInfo) {
            contextMenu.add(getAdapterPosition(), 0, 0, "Edit");
            contextMenu.add(getAdapterPosition(), 1, 0, "Delete");
        }
    }



    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View viewitem = LayoutInflater.from(parent.getContext()).inflate(R.layout.student_item, parent, false);
        return new ViewHolder(viewitem, OnclickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.roll.setText(studentItems.get(position).getRoll()+"");
        holder.name.setText(studentItems.get(position).getStudentName());
        holder.status.setText(studentItems.get(position).getStatus());
        holder.cardView.setCardBackgroundColor(getColor(position));
    }

    private int getColor(int position) {
        String status = studentItems.get(position).getStatus();
      if(status.equals("P"))
          return Color.parseColor("#"+Integer.toHexString(ContextCompat.getColor(context, R.color.present)));
        else if (status.equals("A"))
            return Color.parseColor("#"+Integer.toHexString(ContextCompat.getColor(context, R.color.absent)));
        return Color.parseColor("#"+Integer.toHexString(ContextCompat.getColor(context, R.color.white)));
    }

    @Override
    public int getItemCount() {
        return studentItems.size();
    }
}
