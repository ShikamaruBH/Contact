package com.example.contact;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.widget.SearchView;


import com.example.contact.databinding.ActivityMainBinding;

import java.util.List;

public class    MainActivity extends AppCompatActivity {
    private static final int REQ_CODE_INS = 123;
    private static final int REQ_CODE_DETAIL = 321;
    private ActivityMainBinding binding;
    private List<Contact> contacts;
    private ContactAdapter contactAdapter;
    private ContactDao contactDao;
    private ContactAdapter.ItemClickListener listener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        AppDatabase db = AppDatabase.getInstance(this);
        contactDao = db.contactDao();

        contacts = contactDao.getAllContacts();
//        for(int i=contacts.size()-1; i>-1; i--) {
//            contactDao.delete(contacts.get(i));
//        }

        setOnClickListener();

        contactAdapter = new ContactAdapter(contacts, listener);
        binding.rvContacts.setAdapter(contactAdapter);
        binding.rvContacts.setLayoutManager(new LinearLayoutManager(this));

        binding.fbtAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, NewContactActivity.class);
                startActivityForResult(intent, REQ_CODE_INS);
            }
        });
    }

    private void setOnClickListener() {
        listener = new ContactAdapter.ItemClickListener() {
            @Override
            public void onClick(View v, int position) {
                Intent intent = new Intent(getApplicationContext(), DetailActivity.class);
                intent.putExtra("id", contacts.get(position).getId());
                startActivityForResult(intent, REQ_CODE_DETAIL);
            }
        };
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);

        MenuItem menuItem = menu.findItem(R.id.search_contact);
        SearchView searchView = (SearchView) menuItem.getActionView();
        searchView.setQueryHint("Search contact");

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                contacts.clear();
                contacts.addAll(contactDao.findByName("%" + s + "%"));
                contactAdapter.notifyDataSetChanged();
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQ_CODE_INS) {
            Toast.makeText(this, "New contact added !", Toast.LENGTH_SHORT).show();
        } else if (requestCode == REQ_CODE_DETAIL) {
            if (resultCode == 1) {
                Toast.makeText(this, "Contact updated !", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Contact deleted !", Toast.LENGTH_SHORT).show();
            }
        }
        contacts.clear();
        contacts.addAll(contactDao.getAllContacts());
        contactAdapter.notifyDataSetChanged();
    }

}