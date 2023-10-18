package com.example.stucheck.Service;

import android.content.Context;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.stucheck.Model.ClassItem;
import com.example.stucheck.R;

import java.util.ArrayList;

public class ClassAdapter extends RecyclerView.Adapter<ClassAdapter.ViewHolder> {
    ArrayList<ClassItem> classItems;
    Context context;
    private OnItemClickListener OnclickListener;
    public interface OnItemClickListener{
        void OnClick(int position);
    }
    public void setOnItemClickListener(OnItemClickListener listener){
        this.OnclickListener = listener;
    }
    public ClassAdapter(Context context, ArrayList<ClassItem> classItems) {
        this.classItems = classItems;
        this.context = context;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener {
        TextView className;
        TextView subjectName;

        public ViewHolder(@NonNull View viewitem, OnItemClickListener itemClickListener) {
            super(viewitem);
            className = viewitem.findViewById(R.id.text_class_name);
            subjectName = viewitem.findViewById(R.id.text_supject_name);
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
        View viewitem = LayoutInflater.from(parent.getContext()).inflate(R.layout.class_item, parent, false);
        return new ViewHolder(viewitem, OnclickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.className.setText(classItems.get(position).getClassName());
        holder.subjectName.setText(classItems.get(position).getSubjectName());
    }

    @Override
    public int getItemCount() {
        return classItems.size();
    }
}
