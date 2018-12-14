/*
package com.example.huy.myfirstapp;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;

import java.util.List;

public class TaskViewModel extends AndroidViewModel {
    private TaskRepository mTaskRepository;
    private LiveData<List<Task>> mAllTasks;
    public TaskViewModel(Application application) {
        super(application);
        mTaskRepository = new TaskRepository(application);
        mAllTasks = mTaskRepository.getAllTasksByTime();
    }

    public LiveData<List<Task>> getmAllTasks() {
        return mAllTasks;
    }

    public void insert(Task task) {
        mTaskRepository.insert(task);
    }
}
*/
