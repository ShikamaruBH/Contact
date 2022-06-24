package com.example.contact;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.contact.databinding.ActivityDetailBinding;

public class DetailActivity extends AppCompatActivity {
    private ActivityDetailBinding binding;
    private AppDatabase db;
    private ContactDao contactDao;
    private Contact contact;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDetailBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setHomeAsUpIndicator(getResources().getDrawable(R.drawable.ic_baseline_close_24));
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#4385f6")));

        Intent intent = getIntent();
        db = AppDatabase.getInstance(this);
        contactDao = db.contactDao();
        contact = contactDao.findByID(intent.getIntExtra("id", 0));
        setupUI();
    }

    private void setupUI() {
        if (contact.getAvatar() != null) {
            binding.ivAvatar.setImageBitmap(EditContact.convertByteArray2Image(contact.getAvatar()));
        }
        binding.tvName.setText(contact.getName());
        binding.tvPhone.setText(contact.getMobile());
        if (!contact.getEmail().isEmpty()) {
            binding.tvEmail.setText(contact.getEmail());
        } else {
            binding.tvEmail.setText("No email found");
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.detail_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.edit_contact) {
            Intent intent = new Intent(DetailActivity.this, EditContact.class);
            intent.putExtra("id", contact.getId());
            startActivity(intent);
            setResult(1);
            finish();
        } else if (id == R.id.delete_contact) {
            contactDao.delete(contact);
            setResult(2);
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}