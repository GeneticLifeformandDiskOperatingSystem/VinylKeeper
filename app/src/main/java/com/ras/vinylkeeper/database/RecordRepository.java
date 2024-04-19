package com.ras.vinylkeeper.database;

import android.app.Application;
import android.util.Log;

import com.ras.vinylkeeper.MainActivity;
import com.ras.vinylkeeper.database.entities.Record;

import java.util.ArrayList;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public class RecordRepository {
    private RecordDAO recordDAO;
    private ArrayList<Record> allRecords;
    private static RecordRepository repository;

    public RecordRepository(Application application) {
        RecordDatabase db = RecordDatabase.getDatabase(application);
        this.recordDAO = db.recordDAO();
        this.allRecords = (ArrayList<Record>) this.recordDAO.getAllRecords();
    }

    public static RecordRepository getRepository(Application application) {
        if(repository != null) {
            return repository;
        }
        Future<RecordRepository> future = RecordDatabase.databaseWriteExecutor.submit(
                new Callable<RecordRepository>() {
            @Override
            public RecordRepository call() throws Exception {
                return new RecordRepository(application);
            }
        });
        try {
            return future.get();
        } catch (InterruptedException | ExecutionException e) {
            Log.d(MainActivity.TAG, "Problem getting RecordRepository, thread error");
        }
        return null;
    }

    public ArrayList<Record> getAllRecords() {
        Future<ArrayList<Record>> future = RecordDatabase.databaseWriteExecutor.submit(
                new Callable<ArrayList<Record>>() {
                    @Override
                    public ArrayList<Record> call() throws Exception {
                        return (ArrayList<Record>) recordDAO.getAllRecords();
                    }
                }
        );
        try {
            return future.get();
        } catch (InterruptedException | ExecutionException e) {
            Log.i(MainActivity.TAG, "Problem getting all Records in the repository");
        }
        return null;
    }

    public void insertRecord(Record record) {
        RecordDatabase.databaseWriteExecutor.execute(() -> {
            recordDAO.insert(record);
        });
    }
}
