package com.practice.taskmaster.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.practice.taskmaster.R;
import com.practice.taskmaster.models.Task;
import com.practice.taskmaster.activities.TaskDetailsActivity;

import java.util.List;

public class TaskAdapter extends RecyclerView.Adapter<TaskViewHolder> {
    private List<Task> taskList;
    private Context context;
    public TaskAdapter(List<Task> taskList, Context context) {
        this.taskList = taskList;
        this.context=context;
    }

    @NonNull
    @Override
    public TaskViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.task_item, parent, false);
        return new TaskViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TaskViewHolder holder, int position) {
        Task task = taskList.get(position);
        holder.bindTask(task);
        holder.itemView.setOnClickListener(view -> {
            Intent sendTitle = new Intent(context, TaskDetailsActivity.class);
            sendTitle.putExtra("taskTitle", task.getTitle());
            sendTitle.putExtra("taskBody", task.getBody());
            sendTitle.putExtra("taskStatus", task.getState().toString());
            context.startActivity(sendTitle);
        });
    }

    @Override
    public int getItemCount() {
        return taskList.size();
    }
}
