package com.projectbelajar.yuukbelajar.chat.adapter;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.projectbelajar.yuukbelajar.R;
import com.projectbelajar.yuukbelajar.chat.activity.ChatRoomActivity;
import com.projectbelajar.yuukbelajar.chat.model.Chat;
import com.projectbelajar.yuukbelajar.chat.model.Chater;

import java.util.ArrayList;

public class CustomChaterAdapter extends RecyclerView.Adapter<CustomChaterAdapter.ChaterViewHolder> {
    private ArrayList<Chater> chaters;
    private OnItemClickCallback onItemClickCallback;
    private boolean isChat;
    private String lastMessage, chatType, currentLevel;

    public CustomChaterAdapter(ArrayList<Chater> chater, boolean isChat, String chatType, String currentLevel) {
        this.chatType = chatType;
        this.chaters = chater;
        this.isChat = isChat;
        this.currentLevel = currentLevel;
        notifyDataSetChanged();
    }

    public void setOnItemClickCallback(OnItemClickCallback onItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback;
    }

    @NonNull
    @Override
    public ChaterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_teman_chat, parent, false);
        return new ChaterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ChaterViewHolder holder, int position) {
        holder.bind(chaters.get(position));
    }

    @Override
    public int getItemCount() {
        return chaters.size();
    }

    class ChaterViewHolder extends RecyclerView.ViewHolder {
        TextView tvName;
        TextView tvShortChat;
        TextView tvStatus;
        ImageView imgProfil;
        ImageView imgOn;
        ImageView imgOff;

        ChaterViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tv_chatter);
            tvShortChat = itemView.findViewById(R.id.tv_short_chat);
            tvStatus = itemView.findViewById(R.id.tv_status);
            imgProfil = itemView.findViewById(R.id.img_profil_penjual);
            imgOn = itemView.findViewById(R.id.img_on);
            imgOff = itemView.findViewById(R.id.img_off);
        }

        void bind(final Chater chater) {
            tvName.setText(chater.getNama());
            Log.d("image url nya", " "+chater.getImgUrl());
//            Log.d("username url nya", " "+chater.getUsername());
            if (chater.getImgUrl().equals("default")) {
                imgProfil.setImageResource(R.drawable.profile);
            } else {
                Glide.with(itemView.getContext()).load(chater.getImgUrl()).into(imgProfil);
            }

            if (isChat) {
                theLastMessage(chater.getId(), tvShortChat);
            } else {
//                tvShortChat.setVisibility(View.GONE);
                if (chater.getLevel().equals("DOKTER")) {
                    tvShortChat.setText(chater.getKlinik());
                } else {
                    tvShortChat.setText(chater.getNamaSekolah());
                }

            }

            imgOff.setVisibility(View.GONE);
            imgOn.setVisibility(View.GONE);
            if (chater.getStatus().equals("online")) {
                tvStatus.setText(chater.getStatus());
                tvStatus.setTextColor(Color.parseColor("#007B0C"));
                tvStatus.setCompoundDrawablePadding(8);
                tvStatus.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_on, 0, 0, 0);
            } else {
                tvStatus.setText(chater.getStatus());
                tvStatus.setTextColor(Color.parseColor("#FF5252"));
                tvStatus.setCompoundDrawablePadding(8);
                tvStatus.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_off, 0, 0, 0);
            }

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!currentLevel.equals("DOKTER")){
                        Intent toChatRoom = new Intent(itemView.getContext(), ChatRoomActivity.class);
                        toChatRoom.putExtra("userid", chater.getId());
                        toChatRoom.putExtra("level", chater.getLevel());
                        toChatRoom.putExtra("chatType", chatType);
                        itemView.getContext().startActivity(toChatRoom);
                    }
                }
            });
        }
    }

    public interface OnItemClickCallback {
        void onItemClicked(Chater data);
    }

    private void theLastMessage(final String userid, final TextView theLastMessage) {
        this.lastMessage = "default";
        final FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Chats");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Chat chat = snapshot.getValue(Chat.class);
                    if (chat.getReceiver().equals(firebaseUser.getUid()) && chat.getSender().equals(userid) ||
                            chat.getReceiver().equals(userid) && chat.getSender().equals(firebaseUser.getUid())) {
                        lastMessage = chat.getMessage();
                    }
                }
                switch (lastMessage) {
                    case "default":
                        theLastMessage.setText("Tidak ada pesan");
                        break;
                    default:
                        theLastMessage.setText(lastMessage);
                        break;
                }
                lastMessage = "default";
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
