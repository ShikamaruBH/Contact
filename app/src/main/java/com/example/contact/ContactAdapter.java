package com.example.contact;

import android.text.SpannableStringBuilder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.ViewHolder>  {
    private List<Contact> contacts;
    private ItemClickListener listener;

    public ContactAdapter(List<Contact> data, ItemClickListener itemClickListener) {
        this.contacts = data;
        this.listener = itemClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.contact_item, parent, false);

        return new ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Contact contact = contacts.get(position);
        String firstChar = contact.getName().substring(0,1);
        holder.tvName.setText(contact.getName());
        if (contact.getAvatar() == null) {
            holder.tvAvatar.setVisibility(View.VISIBLE);
            holder.cvAvatar.setVisibility(View.GONE);
            holder.tvAvatar.setText(firstChar);
        } else {
            holder.tvAvatar.setVisibility(View.GONE);
            holder.cvAvatar.setVisibility(View.VISIBLE);
            holder.ivAvatar.setImageBitmap(EditContact.convertByteArray2Image(contact.getAvatar()));
        }
        if (position == 0 || !firstChar.equals(contacts.get(position-1).getName().substring(0,1))) {
            holder.tvChar.setText(firstChar);
        } else {
            holder.tvChar.setText("");
        }
    }

    @Override
    public int getItemCount() {
        return contacts.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView tvName;
        public TextView tvAvatar;
        public TextView tvChar;
        public ImageView ivAvatar;
        public CardView cvAvatar;
        public ViewHolder(View view) {
            super(view);
            tvName = view.findViewById(R.id.tv_name);
            tvAvatar = view.findViewById(R.id.tv_avatar);
            tvChar = view.findViewById(R.id.tv_char);
            ivAvatar = view.findViewById(R.id.iv_avatar);
            cvAvatar = view.findViewById(R.id.cv_avatar);
            view.setOnClickListener(this);
        }
        @Override
        public void onClick(View v) {
            listener.onClick(v, getAdapterPosition());
        }
    }
    public interface ItemClickListener {
        void onClick(View v, int position);
    }
}
