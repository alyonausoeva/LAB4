package com.example.contacts.db;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.contacts.db.model.Contact;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Maybe;
import io.reactivex.Observable;
import io.reactivex.Single;

@Dao
public interface ContactDao {
    String TABLE_NAME = "contacts";

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    Completable insert(Contact contact);

    @Update
    Completable update(Contact contact);

    @Query("DELETE FROM " + TABLE_NAME + " WHERE id = :id")
    Maybe<Integer> deleteContactById(Long id);

    @Query("SELECT * FROM " + TABLE_NAME + " WHERE id = :id")
    Single<Contact> getContactById(Long id);

    @Query("SELECT * FROM " + TABLE_NAME)
    Observable<List<Contact>> observeAll();

}
