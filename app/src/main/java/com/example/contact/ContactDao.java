package com.example.contact;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface ContactDao {
    @Query("SELECT * from Contact ORDER BY name")
    List<Contact> getAllContacts();

    @Query("SELECT * from contact where name LIKE :s ORDER BY name")
    List<Contact> findByName(String s);

    @Query("SELECT * from contact where id = :id")
    Contact findByID(int id);

    @Insert
    void insertAll(Contact... contact);

    @Update
    void updateAll(Contact... contacts);

    @Delete
    void delete(Contact contact);

}
