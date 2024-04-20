package com.ras.vinylkeeper.database;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.LiveData;

import com.ras.vinylkeeper.MainActivity;
import com.ras.vinylkeeper.database.entities.Record;
import com.ras.vinylkeeper.database.entities.User;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public class VinylRepository {
    private final RecordDAO recordDAO;
    private final UserDAO userDAO;
    private ArrayList<Record> allRecords;
    private static VinylRepository repository;

    public VinylRepository(Application application) {
        VinylDatabase db = VinylDatabase.getDatabase(application);
        this.recordDAO = db.recordDAO();
        this.userDAO = db.userDAO();
        this.allRecords = (ArrayList<Record>) this.recordDAO.getAllRecords();
    }

    public static VinylRepository getRepository(Application application) {
        if(repository != null) {
            return repository;
        }
        Future<VinylRepository> future = VinylDatabase.databaseWriteExecutor.submit(
                new Callable<VinylRepository>() {
            @Override
            public VinylRepository call() throws Exception {
                return new VinylRepository(application);
            }
        });
        try {
            return future.get();
        } catch (InterruptedException | ExecutionException e) {
            Log.d(MainActivity.TAG, "Problem getting VinylRepository, thread error");
        }
        return null;
    }

    public ArrayList<Record> getAllRecords() {
        Future<ArrayList<Record>> future = VinylDatabase.databaseWriteExecutor.submit(
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
        VinylDatabase.databaseWriteExecutor.execute(() -> {
            recordDAO.insert(record);
        });
    }

    public void insertUser(User... user) {
        VinylDatabase.databaseWriteExecutor.execute(() -> {
            userDAO.insert(user);
        });
    }

    public LiveData<User> getUserByUserName(String username) {
        return userDAO.getUserByUserName(username);
    }

    public LiveData<List<User>> getAllUserNames() {
        return userDAO.getAllUsers();
    }

    public LiveData<User> getUserByUserId(int userId) {
        return userDAO.getUserByUserId(userId);
    }
}
