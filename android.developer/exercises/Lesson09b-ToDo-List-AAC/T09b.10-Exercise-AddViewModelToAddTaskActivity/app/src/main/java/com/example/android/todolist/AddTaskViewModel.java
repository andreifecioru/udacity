package com.example.android.todolist;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import com.example.android.todolist.database.AppDatabase;
import com.example.android.todolist.database.TaskEntry;

public class AddTaskViewModel extends ViewModel {

    private final LiveData<TaskEntry> mTask;

    public AddTaskViewModel(AppDatabase appDb, int taskId) {
        mTask = appDb.taskDao().loadTaskById(taskId);
    }

    public LiveData<TaskEntry> getTask() { return mTask; }
}
