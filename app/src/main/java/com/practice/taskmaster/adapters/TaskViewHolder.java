package com.practice.taskmaster.adapters;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.amplifyframework.datastore.generated.model.Task;
import com.practice.taskmaster.R;


public class TaskViewHolder extends RecyclerView.ViewHolder {

    private TextView titleTextView;
    private TextView bodyTextView;
    private TextView stateTextView;

    public TaskViewHolder(@NonNull View itemView) {
        super(itemView);
        titleTextView = itemView.findViewById(R.id.titleTextView);
        bodyTextView = itemView.findViewById(R.id.bodyTextView);
        stateTextView = itemView.findViewById(R.id.stateTextView);
    }

    public void bindTask(Task task) {
        titleTextView.setText(task.getName());
        bodyTextView.setText(task.getBody());
        stateTextView.setText(task.getState().toString());
    }
}
