package com.example.contact;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.contact.databinding.ActivityNewContactBinding;

import java.io.FileNotFoundException;
import java.io.InputStream;

public class NewContactActivity extends AppCompatActivity {
    private ActivityNewContactBinding binding;
    private static final int REQ_CODE_IMG = 456;
    private Bitmap avatar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityNewContactBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Create new contact");
        getSupportActionBar().setHomeAsUpIndicator(getResources().getDrawable(R.drawable.ic_baseline_close_24));

        binding.ivChoseAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(Intent.createChooser(intent, "Choose image"), REQ_CODE_IMG);
            }
        });
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.add_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQ_CODE_IMG) {
            try {
                InputStream inputStream = getContentResolver().openInputStream(data.getData());
                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                binding.ivAvatar.setImageBitmap(bitmap);
                avatar = bitmap;
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.save_contact) {
            String name = binding.etName.getText().toString().trim();
            String phone = binding.etPhone.getText().toString().trim();
            String email = binding.etEmail.getText().toString().trim();

            if (name.equals("")) {
                Toast.makeText(this, "Tên không được để trống", Toast.LENGTH_SHORT).show();
            } else if (phone.equals("")) {
                Toast.makeText(this, "Số điện thoại không được để trống", Toast.LENGTH_SHORT).show();
            } else {
                AppDatabase db = AppDatabase.getInstance(this);
                ContactDao contactDao = db.contactDao();
                Contact contact = new Contact(phone, email, name);
                if (avatar != null) {
                    contact.setAvatar(EditContact.convertImage2ByteArray(avatar));
                }
                contactDao.insertAll(contact);
                finish();
            }
        }
        return super.onOptionsItemSelected(item);
    }
}