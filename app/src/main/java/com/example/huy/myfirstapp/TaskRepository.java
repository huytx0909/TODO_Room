package com.example.huy.myfirstapp;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;

import java.util.List;

public class TaskRepository {
    private TaskDao taskDao;
    private List<Task> allTasksByTime;

    public TaskRepository(Application application, String time) {
        TaskDatabase taskDatabase = TaskDatabase.getDatabaseInstance(application);
        taskDao = taskDatabase.taskDao();
        allTasksByTime = taskDao.loadAllByTime(time);
    }

    List<Task> getAllTasksByTime() {
        return allTasksByTime;
    }

    public void insert (Task task) {
        new insertAsyncTask(taskDao).execute(task);
    }

    private static class insertAsyncTask extends AsyncTask<Task, Void, Void> {
        private TaskDao mAsyncTaskDao;

        insertAsyncTask(TaskDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final Task... params) {
            mAsyncTaskDao.insert(params[0]);
            return null;
        }
    }
}
