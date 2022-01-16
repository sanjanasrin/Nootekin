 package com.example.nootekin.Adapter;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.nootekin.AddNewTask;
import com.example.nootekin.MainActivity;
import com.example.nootekin.Model.ToDoModel;
import com.example.nootekin.R;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class ToDoAdapter extends RecyclerView.Adapter<ToDoAdapter.MyViewHolder> {

    private List<ToDoModel> todoList;
    private MainActivity activity;
    private FirebaseFirestore firestore;

    public ToDoAdapter(MainActivity mainActivity , List<ToDoModel> todoList){
        this.todoList = todoList;
        activity=mainActivity;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(activity).inflate(R.layout.each_task, parent ,false);
        firestore=FirebaseFirestore.getInstance();
        return new MyViewHolder(view);
    }
     public void deleteTask(int position){
        ToDoModel toDoModel=todoList.get(position);
        firestore.collection("task").document(toDoModel.TaskId).delete();
        todoList.remove(position);
        notifyItemRemoved(position);
     }

     public Context getContext(){
        return activity;
     }

     private void editTask(int position){
         ToDoModel toDoModel=todoList.get(position);

         Bundle bundle= new Bundle();
         bundle.putString("task",toDoModel.getTask());
         bundle.putString("due",toDoModel.getDue());
         bundle.putString("id",toDoModel.TaskId);

         AddNewTask addNewTask= new AddNewTask();
         addNewTask.setArguments(bundle);
         addNewTask.show(activity.getSupportFragmentManager(), addNewTask.getTag());

     }
    @Override
    public void onBindViewHolder(@NonNull ToDoAdapter.MyViewHolder holder, int position) {
     ToDoModel toDoModel= todoList.get(position);
     holder.mCheckBox.setText(toDoModel.getTask());
     holder.mDueDateTv.setText("Due on" + toDoModel.getDue());

     holder.mCheckBox.setChecked(toBoolean(toDoModel.getStatus()));

     holder.mCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
         @Override
         public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
             if (isChecked) {
                 firestore.collection("task").document(toDoModel.TaskId).update("status", 1);

             } else {
                 firestore.collection("task").document(toDoModel.TaskId).update("status", 0);

             }
         }
     });
     holder.delete_btn.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View v) {
             deleteTask(position);
         }
     });
     holder.edit_btn.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View v) {
             editTask(position);
         }
     });
    }

    private boolean toBoolean(int status){
        return status !=0;

    }

    @Override
    public int getItemCount() {
        return todoList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        TextView mDueDateTv;
        CheckBox mCheckBox;
        ImageButton delete_btn;
        ImageButton edit_btn;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            mDueDateTv=itemView.findViewById(R.id.due_date_tv);
            mCheckBox=itemView.findViewById(R.id.mcheckbox);
            delete_btn=itemView.findViewById(R.id.delete_btn);
            edit_btn=itemView.findViewById(R.id.edit_btn);

        }
    }
}
