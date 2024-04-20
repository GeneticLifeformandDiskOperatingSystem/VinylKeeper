package com.ras.vinylkeeper.database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.ras.vinylkeeper.database.entities.Record;

import java.util.List;

@Dao
public interface RecordDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Record record);

    @Update
    void update(Record... record);

    @Delete
    void delete(Record record);

    @Query("SELECT * FROM " + VinylDatabase.RECORD_TABLE)
    List<Record> getAllRecords();

    @Query("SELECT * FROM " + VinylDatabase.RECORD_TABLE + " WHERE ownerUserId == :loggedInUserId ORDER BY artist DESC")
    List<Record> getRecordsByUserId(int loggedInUserId);
}
