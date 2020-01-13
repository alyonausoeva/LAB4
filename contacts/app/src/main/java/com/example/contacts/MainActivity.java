package com.example.contacts;

import android.content.Intent;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.Button;

import com.example.contacts.db.ContactDao;
import com.example.contacts.db.ContactsDatabase;
import com.example.contacts.db.model.Contact;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity implements ClickListener {

    private AdapterForListOfContacts adapter;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button addBtn = findViewById(R.id.button);
        addBtn.setOnClickListener(view -> {
            Intent i = new Intent(MainActivity.this, AddContact.class);
            startActivity(i);
        });

        initAdapter();
        show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        compositeDisposable.dispose();
    }

    private void initAdapter() {
        adapter = new AdapterForListOfContacts(this);
        RecyclerView rv = findViewById(R.id.recycler_view);
        rv.setAdapter(adapter);
        rv.setLayoutManager(new LinearLayoutManager(this));
        rv.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
    }

    private void show() {
        ContactsDatabase db = ContactsDatabase.getInstance(this);
        ContactDao dao = db.conactsDao();

        compositeDisposable.add(dao.observeAll()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map(contacts -> {
                    List<AdapterForContacts> adapterForContacts = new ArrayList<>();
                    for (Contact contact : contacts) {
                        adapterForContacts.add(new AdapterForContacts(
                                contact.id,
                                contact.surname + " " + contact.name + " " + contact.patronymic
                        ));
                    }
                    return adapterForContacts;
                })
                .subscribe(this::addContacts));
    }

    private void addContacts(List<AdapterForContacts> contacts) {
        adapter.setItems(contacts);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onClick(Long contactId) {
        Intent i = new Intent(this, OneContactActivity.class);
        i.putExtra(OneContactActivity.CONTACT_ID, (long) contactId);
        startActivity(i);
    }
}
