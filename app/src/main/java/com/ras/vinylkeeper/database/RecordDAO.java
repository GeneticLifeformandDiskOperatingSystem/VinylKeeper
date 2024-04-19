package com.ras.vinylkeeper.database;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.ras.vinylkeeper.database.entities.Record;

import java.util.List;

@Dao
public interface RecordDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Record record);

    @Query("SELECT * FROM " + RecordDatabase.RECORD_TABLE)
    List<Record> getAllRecords();
}
