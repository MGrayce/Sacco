package com.grayapps.saccodemo.Demo.Utils;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface EntitiesDao {

    @Query("SELECT * FROM entities")
    List<Entities> getAll();

//    @Query("SELECT SUM(amount) FROM entities")
//    LiveData<List<Entities>> getSavings();

    @Query("SELECT * FROM entities WHERE `action`=:action")
    LiveData<List<Entities>> getAction(String action);

//    @Query("SELECT COUNT(`action`) FROM entities where `action` LIKE 'd%'")
//    LiveData<Integer> getDeposits();
    @Query("SELECT COUNT(`action`) FROM entities WHERE `action` =:action")
    String loadDeposits(String action);

    @Query("SELECT SUM(amount) from entities")
    LiveData<Integer> getSum();


    @Insert
    void insert(Entities entities);
}
