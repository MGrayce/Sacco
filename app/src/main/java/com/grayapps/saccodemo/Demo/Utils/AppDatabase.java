package com.grayapps.saccodemo.Demo.Utils;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {Entities.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    public abstract EntitiesDao entitiesDao();
}
