package com.example.todolist;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;

public class MainActivity extends AppCompatActivity {

    EditText etTaskName;
    Button btnPickDate, btnAddTask;
    RecyclerView recyclerView;

    long selectedDeadline = 0;
    ArrayList<Task> taskList;
    TaskAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etTaskName = findViewById(R.id.etTaskName);
        btnPickDate = findViewById(R.id.btnPickDate);
        btnAddTask = findViewById(R.id.btnAddTask);
        recyclerView = findViewById(R.id.recyclerView);

        taskList = new ArrayList<>();
        adapter = new TaskAdapter(this, taskList);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        btnPickDate.setOnClickListener(v -> openDatePicker());

        btnAddTask.setOnClickListener(v -> addTask());
    }

    private void openDatePicker() {
        Calendar calendar = Calendar.getInstance();

        DatePickerDialog dialog = new DatePickerDialog(this,
                (view, year, month, day) -> {
                    Calendar selected = Calendar.getInstance();
                    selected.set(year, month, day);
                    selectedDeadline = selected.getTimeInMillis();
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH));

        dialog.show();
    }

    private void addTask() {
        String taskName = etTaskName.getText().toString().trim();

        if (taskName.isEmpty() || selectedDeadline == 0) return;

        taskList.add(new Task(taskName, selectedDeadline));

        // SORT BY SHORTEST DEADLINE (HIGHEST PRIORITY)
        Collections.sort(taskList, Comparator.comparingLong(Task::getDeadline));

        adapter.notifyDataSetChanged();

        etTaskName.setText("");
        selectedDeadline = 0;
    }
}
