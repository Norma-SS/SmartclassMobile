package com.projectbelajar.yuukbelajar.chat.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.mlsdev.animatedrv.AnimatedRecyclerView;
import com.projectbelajar.yuukbelajar.Preferences;
import com.projectbelajar.yuukbelajar.R;
import com.projectbelajar.yuukbelajar.chat.Notifications.Client;
import com.projectbelajar.yuukbelajar.chat.Notifications.Data;
import com.projectbelajar.yuukbelajar.chat.Notifications.MyResponse;
import com.projectbelajar.yuukbelajar.chat.Notifications.Sender;
import com.projectbelajar.yuukbelajar.chat.Notifications.Token;
import com.projectbelajar.yuukbelajar.chat.adapter.MessageAdapter;
import com.projectbelajar.yuukbelajar.chat.fragment.APIService;
import com.projectbelajar.yuukbelajar.chat.model.Chat;
import com.projectbelajar.yuukbelajar.chat.model.Chater;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class ChatRoomActivity extends AppCompatActivity {
    CircleImageView imgProfil, imgOn, imgOff;
    TextView username, online;
    ImageButton btnSend;
    EditText textSend;
    private RecyclerView rvChat;
    private ArrayList<Chat> chats;
    private MessageAdapter adapter;

    ValueEventListener seenListener;

    FirebaseUser firebaseUser;
    DatabaseReference reference, reference_user;
    private String userid, chatType, level, name;
    Intent intent;

    APIService apiService;
    boolean notify = false;

    private Preferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_room);

        preferences = new Preferences(this);

        Toolbar tb = findViewById(R.id.main_tb);
        setSupportActionBar(tb);
//        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Verification");
        LayoutInflater inflater = LayoutInflater.from(this);
        View v = inflater.inflate(R.layout.custom_title_textview, null);
        ((TextView) v.findViewById(R.id.tvTitle)).setText(this.getTitle());
        getSupportActionBar().setCustomView(v);
        intent = getIntent();
        userid = intent.getStringExtra("userid");
        chatType = intent.getStringExtra("chatType");
        level = intent.getStringExtra("level");



        tb.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ChatRoomActivity.this, ChatActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP).putExtra("chatType", preferences.getValues("chatType")));
//                onBackPressed();
            }
        });

        imgProfil = findViewById(R.id.civ_lawan_chat);
//        imgOn = findViewById(R.id.img_on);
//        imgOff = findViewById(R.id.img_off);
        username = findViewById(R.id.tv_nama_lawan_chat);
        btnSend = findViewById(R.id.btn_send_message);
        textSend = findViewById(R.id.et_message_input);
        online = findViewById(R.id.online);

        apiService = Client.getClient("https://fcm.googleapis.com/").create(APIService.class);

        rvChat = (AnimatedRecyclerView) findViewById(R.id.rv_message);
        rvChat.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setStackFromEnd(true);
        rvChat.setLayoutManager(linearLayoutManager);

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("User").child(userid);
        reference_user = FirebaseDatabase.getInstance().getReference("User").child(firebaseUser.getUid());






        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Chater chater = dataSnapshot.getValue(Chater.class);

                assert chater != null;
                name = chater.getNama();

                username.setText(chater.getNama());
                if (chater.getImgUrl().equals("default")) {
                    imgProfil.setImageResource(R.drawable.ic_user);
                } else {
                    Glide.with(getApplicationContext()).load(chater.getImgUrl()).into(imgProfil);
                }
                if (chater.getStatus() != null) {
                    if (chater.getStatus().equals("online")) {
                        online.setVisibility(View.VISIBLE);
//                        imgOn.setVisibility(View.VISIBLE);
//                        imgOff.setVisibility(View.GONE);
                    } else {
                        online.setVisibility(View.GONE);
//                        imgOn.setVisibility(View.GONE);
//                        imgOff.setVisibility(View.VISIBLE);
                    }
                }

                readMessages(firebaseUser.getUid(), userid);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                notify = true;
                String msg = textSend.getText().toString();
                String time = timestampToDateString(-1 * System.currentTimeMillis());
                if (!msg.equals("")) {
                    sendMessage(firebaseUser.getUid(), userid, msg, time, name);
                } else {
                    Toast.makeText(ChatRoomActivity.this, "Kamu tidak bisa mengirim pesan kosong", Toast.LENGTH_SHORT).show();
                }
                textSend.setText("");
            }
        });
        textSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rvChat.smoothScrollToPosition(rvChat.getAdapter().getItemCount());
            }
        });
        seenMessage(userid);
    }
    public static String timestampToDateString(long timestamp){
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss");
        Date date = new Date(timestamp);
        return dateFormat.format(date);
    }
    private void sendNotifiaction(String receiver, final String username, final String message){
        DatabaseReference tokens = FirebaseDatabase.getInstance().getReference("Tokens");
        Query query = tokens.orderByKey().equalTo(receiver);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    Token token = snapshot.getValue(Token.class);
                    Data data = new Data(firebaseUser.getUid(), R.drawable.yukbelajarlogo, username+": "+message, "New Message",
                            userid);

                    Sender sender = new Sender(data, token.getToken());

                    apiService.sendNotification(sender)
                            .enqueue(new Callback<MyResponse>() {
                                @Override
                                public void onResponse(Call<MyResponse> call, Response<MyResponse> response) {
                                    if (response.code() == 200){
                                        if (response.body().success != 1){
                                            //Toast.makeText(MessageActivity.this, "Failed!", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                }

                                @Override
                                public void onFailure(Call<MyResponse> call, Throwable t) {

                                }
                            });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


    private void seenMessage(final String userid) {
        reference = FirebaseDatabase.getInstance().getReference("Chats");
        seenListener = reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Chat chat = snapshot.getValue(Chat.class);
                    if (chat.getReceiver().equals(firebaseUser.getUid()) && chat.getSender().equals(userid)) {
                        HashMap<String, Object> hashMap = new HashMap<>();
//                        hashMap.put("isseen", true);
                        snapshot.getRef().updateChildren(hashMap);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void sendMessage(String sender, final String receiver, String message, final String time, final String name) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();

        String roomname;

        if (preferences.getValues("level").equals("DOKTER")){
            roomname =  preferences.getValues("nama") +  name ;
        }else{
            roomname =  name + preferences.getValues("nama") ;
        }


        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("sender", sender);
        hashMap.put("receiver", receiver);
        hashMap.put("nickname", preferences.getValues("nama"));
        hashMap.put("message", message);
        hashMap.put("roomname", roomname);
        hashMap.put("isseen", false);
        hashMap.put("date", time);

        reference.child("Chats").push().setValue(hashMap);

        final DatabaseReference chatRef = FirebaseDatabase.getInstance().getReference("Chatlist")
                .child(firebaseUser.getUid())
                .child(userid);
        chatRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (!dataSnapshot.exists()) {
                    chatRef.child("id").setValue(userid);
                    chatRef.child("level").setValue(level);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        final DatabaseReference chatRefReceiver = FirebaseDatabase.getInstance().getReference("Chatlist")
                .child(userid)
                .child(firebaseUser.getUid());
        chatRefReceiver.child("id").setValue(firebaseUser.getUid());
        chatRefReceiver.child("level").setValue(preferences.getValues("level"));

        final DatabaseReference receiverUser = FirebaseDatabase.getInstance().getReference("User").child(userid);
        receiverUser.child("time").setValue(time);

        final String msg = message;

        reference = FirebaseDatabase.getInstance().getReference("User").child(firebaseUser.getUid());
        final DatabaseReference finalReference = reference;
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Chater user = dataSnapshot.getValue(Chater.class);
                if (notify) {
                    finalReference.child("time").setValue(time);
                    sendNotifiaction(receiver, user.getNama(), msg);
                }
                notify = false;
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void readMessages(final String myid, final String userid) {
        chats = new ArrayList<>();
        reference = FirebaseDatabase.getInstance().getReference("Chats");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                chats.clear();


                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Chat chat = snapshot.getValue(Chat.class);
                    if (chat.getReceiver().equals(myid) && chat.getSender().equals(userid) || chat.getReceiver().equals(userid) && chat.getSender().equals(myid)) {
                        chats.add(chat);

                    }
                    adapter = new MessageAdapter(chats);
                    rvChat.setAdapter(adapter);
                    rvChat.smoothScrollToPosition(rvChat.getAdapter().getItemCount());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void status(String status) {
        reference = FirebaseDatabase.getInstance().getReference("User").child(firebaseUser.getUid());
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("status", status);

        reference.updateChildren(hashMap);
    }

    @Override
    protected void onResume() {
        super.onResume();
//        status("online");
    }

    @Override
    protected void onPause() {
        super.onPause();
        reference.removeEventListener(seenListener);
//        status("offline");
    }
}