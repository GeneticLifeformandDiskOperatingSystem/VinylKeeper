package com.ras.vinylkeeper.database;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.ras.vinylkeeper.MainActivity;
import com.ras.vinylkeeper.database.entities.Record;
import com.ras.vinylkeeper.database.entities.User;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {Record.class, User.class}, version = 1, exportSchema = false)
public abstract class RecordDatabase extends RoomDatabase {
    public static final String RECORD_TABLE = "recordTable";
    public static final String USER_TABLE = "userTable";
    private static final String DATABASE_NAME = "RecordDatabase";
    private static volatile RecordDatabase INSTANCE;
    private static final int NUMBER_OF_THREADS = 4;
    static final ExecutorService databaseWriteExecutor = Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    static RecordDatabase getDatabase(final Context context){
        if(INSTANCE == null) {
            synchronized (Record.class) {
                if(INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(
                                    context.getApplicationContext(),
                                    RecordDatabase.class, DATABASE_NAME
                            )
                            .fallbackToDestructiveMigration()
                            .addCallback(addDefaultValues)
                            .build();

                }
            }
        }
        return INSTANCE;
    }

    private static final RoomDatabase.Callback addDefaultValues = new RoomDatabase.Callback(){
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            Log.i(MainActivity.TAG, "DB CREATED!");
            databaseWriteExecutor.execute(() -> {
                UserDAO dao = INSTANCE.userDAO();
                dao.deleteAll();
                User admin = new User("admin1", "admin1");
                admin.setAdmin(true);
                dao.insert(admin);

                User testUser1 = new User("testuser1", "testuser1");
                dao.insert(testUser1);
            });
        }
    };

    public abstract RecordDAO recordDAO();

    public abstract UserDAO userDAO();
}
