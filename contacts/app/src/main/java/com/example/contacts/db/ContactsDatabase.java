package com.example.contacts.db;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.contacts.db.model.Contact;

@Database(entities = Contact.class, version = 1)
public abstract class ContactsDatabase extends RoomDatabase {

    private static ContactsDatabase INSTANCE;

    public abstract ContactDao conactsDao();

    public static ContactsDatabase getInstance(Context context) {
        if (INSTANCE == null) {
            INSTANCE = buildDatabase(context);
        }
        return INSTANCE;
    }

    private static ContactsDatabase buildDatabase(Context context) {
        return Room.databaseBuilder(
                context,
                ContactsDatabase.class,
                "Contacts.db"
        )
                .build();
    }

}
