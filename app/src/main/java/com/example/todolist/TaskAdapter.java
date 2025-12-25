package com.example.todolist;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.TaskViewHolder> {

    Context context;
    List<Task> taskList;

    public TaskAdapter(Context context, List<Task> taskList) {
        this.context = context;
        this.taskList = taskList;
    }

    @NonNull
    @Override
    public TaskViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context)
                .inflate(R.layout.item_task, parent, false);
        return new TaskViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TaskViewHolder holder, int position) {

        Task task = taskList.get(position);

        holder.tvTask.setText(task.getTaskName());

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        holder.tvDeadline.setText("Deadline: " + sdf.format(task.getDeadline()));

        // DELETE
        holder.btnDelete.setOnClickListener(v -> {
            taskList.remove(position);
            notifyDataSetChanged();
        });

        // EDIT (NAME + DEADLINE)
        holder.btnEdit.setOnClickListener(v -> openEditDialog(task));
    }

    private void openEditDialog(Task task) {

        Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.dialog_edit_task);
        dialog.setCancelable(true);

        EditText etTaskName = dialog.findViewById(R.id.etEditTaskName);
        Button btnPickDate = dialog.findViewById(R.id.btnEditDate);
        Button btnSave = dialog.findViewById(R.id.btnSaveTask);

        etTaskName.setText(task.getTaskName());

        final long[] selectedDeadline = {task.getDeadline()};

        btnPickDate.setOnClickListener(v -> {
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(selectedDeadline[0]);

            DatePickerDialog datePickerDialog = new DatePickerDialog(
                    context,
                    (view, year, month, day) -> {
                        Calendar selected = Calendar.getInstance();
                        selected.set(year, month, day);
                        selectedDeadline[0] = selected.getTimeInMillis();
                    },
                    calendar.get(Calendar.YEAR),
                    calendar.get(Calendar.MONTH),
                    calendar.get(Calendar.DAY_OF_MONTH)
            );

            datePickerDialog.show();
        });

        btnSave.setOnClickListener(v -> {

            String updatedName = etTaskName.getText().toString().trim();
            if (updatedName.isEmpty()) return;

            task.setTaskName(updatedName);
            task.setDeadline(selectedDeadline[0]);

            // SORT AGAIN BY SHORTEST DEADLINE
            Collections.sort(taskList, Comparator.comparingLong(Task::getDeadline));

            notifyDataSetChanged();
            dialog.dismiss();
        });

        dialog.show();
    }

    @Override
    public int getItemCount() {
        return taskList.size();
    }

    static class TaskViewHolder extends RecyclerView.ViewHolder {

        TextView tvTask, tvDeadline;
        Button btnEdit, btnDelete;

        public TaskViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTask = itemView.findViewById(R.id.tvTask);
            tvDeadline = itemView.findViewById(R.id.tvDeadline);
            btnEdit = itemView.findViewById(R.id.btnEdit);
            btnDelete = itemView.findViewById(R.id.btnDelete);
        }
    }
}
