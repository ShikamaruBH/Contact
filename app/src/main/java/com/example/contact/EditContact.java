package com.example.contact;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.contact.databinding.ActivityEditContactBinding;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class EditContact extends AppCompatActivity {
    private static final int REQ_CODE_IMG = 456;
    private ActivityEditContactBinding binding;
    private Contact contact;
    private Bitmap avatar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityEditContactBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Edit contact");
        getSupportActionBar().setHomeAsUpIndicator(getResources().getDrawable(R.drawable.ic_baseline_close_24));

        binding.ivChoseAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(Intent.createChooser(intent, "Choose image"), REQ_CODE_IMG);
            }
        });

        Intent intent = getIntent();
        int contactID = intent.getIntExtra("id", 0);
        setupTextView(contactID);
    }

    private void setupTextView(int id) {
        AppDatabase db = AppDatabase.getInstance(this);
        ContactDao contactDao = db.contactDao();
        contact = contactDao.findByID(id);
        binding.etName.setText(contact.getName());
        binding.etPhone.setText(contact.getMobile());
        binding.etEmail.setText(contact.getEmail());
        if (contact.getAvatar() != null) {
            binding.ivAvatar.setImageBitmap(convertByteArray2Image(contact.getAvatar()));
        }
    }
    public static byte[] convertImage2ByteArray(Bitmap bitmap) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
        return outputStream.toByteArray();
    }
    public static Bitmap convertByteArray2Image(byte[] array) {
        return BitmapFactory.decodeByteArray(array, 0, array.length);
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
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.add_menu, menu);
        return super.onCreateOptionsMenu(menu);
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
                contact.setName(name);
                contact.setEmail(email);
                contact.setMobile(phone);
                if (avatar != null) {
                    contact.setAvatar(convertImage2ByteArray(avatar));
                }
                contactDao.updateAll(contact);
//                Intent intent = new Intent();
//                setResult(Activity.RESULT_OK, intent);
                finish();
            }
        }
        return super.onOptionsItemSelected(item);
    }
}