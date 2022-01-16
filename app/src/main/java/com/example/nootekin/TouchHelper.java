package com.example.nootekin;

import android.app.AlertDialog;
import android.content.DialogInterface;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.example.nootekin.Adapter.ToDoAdapter;

public class TouchHelper extends ItemTouchHelper.SimpleCallback {
    private ToDoAdapter adapter;
    public TouchHelper(ToDoAdapter adapter) {
        super(0,ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT);
        this.adapter= adapter;
    }

    @Override
    public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull  RecyclerView.ViewHolder target) {
        return false;
    }

    @Override
    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
     final int position= viewHolder.getAdapterPosition();
     if(direction==ItemTouchHelper.RIGHT){
         AlertDialog.Builder builder= new AlertDialog.Builder(adapter.getContext());
         builder.setMessage("Are you Sure?")
             .setTitle("Delete Task")
                 .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                     @Override
                     public void onClick(DialogInterface dialog, int which) {
                        adapter.deleteTask(position);
                     }
                 }).setNegativeButton("No", new DialogInterface.OnClickListener() {
             @Override
             public void onClick(DialogInterface dialog, int which) {
            adapter.notifyItemChanged(position);
             }
         });
     }

    }

}
