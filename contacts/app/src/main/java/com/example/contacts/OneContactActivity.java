package com.example.contacts;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import com.example.contacts.db.ContactDao;
import com.example.contacts.db.ContactsDatabase;
import com.example.contacts.db.model.Contact;

import androidx.appcompat.app.AppCompatActivity;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class OneContactActivity extends AppCompatActivity {

    public final static String CONTACT_ID = "contact_id";

    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);

        Long contactId = getIntent().getLongExtra(CONTACT_ID, 0L);
        getContactFromDb(contactId);

        Button confirmBtn = findViewById(R.id.confirm);
        confirmBtn.setOnClickListener(v -> updateContact(contactId));

        Button deleteBtn = findViewById(R.id.delete);
        deleteBtn.setOnClickListener(v -> deleteContact(contactId));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        compositeDisposable.dispose();
    }

    private void getContactFromDb(Long contactId) {
        ContactsDatabase db = ContactsDatabase.getInstance(this);
        ContactDao dao = db.conactsDao();

        compositeDisposable.add(dao.getContactById(contactId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::setContactInfo)
        );
    }

    private void setContactInfo(Contact contact) {
        ((EditText) findViewById(R.id.editText2)).setText(contact.surname);
        ((EditText) findViewById(R.id.editText3)).setText(contact.name);
        ((EditText) findViewById(R.id.editText4)).setText(contact.patronymic);
        ((EditText) findViewById(R.id.editText5)).setText(contact.phone);
        ((EditText) findViewById(R.id.editText6)).setText(contact.email);
    }

    private void updateContact(Long contactId) {
        ContactsDatabase db = ContactsDatabase.getInstance(this);
        ContactDao dao = db.conactsDao();

        String surname = ((EditText) findViewById(R.id.editText2)).getText().toString();
        String name = ((EditText) findViewById(R.id.editText3)).getText().toString();
        String otchestvo = ((EditText) findViewById(R.id.editText4)).getText().toString();
        String tel = ((EditText) findViewById(R.id.editText5)).getText().toString();
        String email = ((EditText) findViewById(R.id.editText6)).getText().toString();

        compositeDisposable.add(dao.update(
                new Contact(
                        contactId,
                        name,
                        surname,
                        otchestvo,
                        tel,
                        email
                ))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::finish)
        );
    }

    private void deleteContact(Long contactId) {
        ContactsDatabase db = ContactsDatabase.getInstance(this);
        ContactDao dao = db.conactsDao();

        compositeDisposable.add(dao.deleteContactById(contactId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        id -> finish()
                )
        );
    }
}