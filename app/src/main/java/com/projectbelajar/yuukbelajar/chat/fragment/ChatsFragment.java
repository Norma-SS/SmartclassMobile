package com.projectbelajar.yuukbelajar.chat.fragment;


import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.mlsdev.animatedrv.AnimatedRecyclerView;
import com.projectbelajar.yuukbelajar.Preferences;
import com.projectbelajar.yuukbelajar.R;
import com.projectbelajar.yuukbelajar.chat.Notifications.Token;
import com.projectbelajar.yuukbelajar.chat.adapter.CustomChaterAdapter;
import com.projectbelajar.yuukbelajar.chat.model.Chater;
import com.projectbelajar.yuukbelajar.chat.model.Chatlist;

import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class ChatsFragment extends Fragment {

    private RecyclerView rvChater;
    private ArrayList<Chater> chatersDokter, chatersGuru;
    private ArrayList<Chatlist> dokterList, waliKelasList;
    private CustomChaterAdapter adapter;

    private ProgressBar pbCurrentChat;
    private FrameLayout obrolanKosong;

    //firebase
    private FirebaseAuth auth;
    private FirebaseUser firebaseUser;
    private Query reference;

    private String chatType, currentUserLevel;
    private boolean isWalikelas;

    private Preferences preferences;

    public ChatsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_chats, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        preferences = new Preferences(getActivity());
        currentUserLevel = preferences.getValues("level");
        chatType = preferences.getValues("chatType");

        auth = FirebaseAuth.getInstance();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();


        obrolanKosong = view.findViewById(R.id.obrolan_kosong);
        pbCurrentChat = view.findViewById(R.id.pb_current_chat);
        rvChater = (AnimatedRecyclerView) view.findViewById(R.id.rv_chat);
        rvChater.setHasFixedSize(true);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
//        mLayoutManager.setReverseLayout(true);
//        mLayoutManager.setStackFromEnd(true);
        rvChater.setLayoutManager(mLayoutManager);

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        dokterList = new ArrayList<>();
        waliKelasList = new ArrayList<>();

        reference = FirebaseDatabase.getInstance().getReference("Chatlist").child(firebaseUser.getUid());
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                dokterList.clear();
                waliKelasList.clear();

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
//                    Log.d("snapshot atas", " masuk" + snapshot.getValue(Chatlist.class));
                    Chatlist chatList = snapshot.getValue(Chatlist.class);
                    waliKelasList.add(chatList);
//                    Log.d("getLevel", " "+chatList.getLevel());
//                    Log.d("getId", " "+chatList.getId());
//                    Log.d("chatType", " "+chatType);
//                    if (chatType.equals("walikelas")){
//                        isWalikelas = true;
//                        if (chatList.getLevel().equals("GURU") || chatList.getLevel().equals("WALIKELAS") || chatList.getLevel().equals("WALI MURID") || chatList.getLevel().equals("SISWA"))
//                            waliKelasList.add(chatList);
//                    }else{
//                        isWalikelas = false;
//                        if (chatList.getLevel().equals("DOKTER") || chatList.getLevel().equals("WALI MURID") || chatList.getLevel().equals("SISWA"))
//                            dokterList.add(chatList);
//                    }

                }
//                if (isWalikelas)
//                    chatList(waliKelasList);
//                else
                chatList(waliKelasList);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        updateToken(FirebaseInstanceId.getInstance().getToken());
    }

    private void updateToken(String token) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Tokens");
        Token token1 = new Token(token);
        reference.child(firebaseUser.getUid()).setValue(token1);
    }

    private void chatList(final ArrayList<Chatlist> list) {
        chatersDokter = new ArrayList<>();
        chatersGuru = new ArrayList<>();
        reference = FirebaseDatabase.getInstance().getReference("dtUsers").orderByChild("time");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                chatersDokter.clear();
                chatersGuru.clear();
//                if (dokterList.size() != 0) {
//                    for (Chatlist chatlist : dokterList) {
//                        Log.d("userlist isi", " " + chatlist.getId());
//                    }
//                } else {
//                    Log.d("userlist isi", " kosong");
//                }
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
//                    Log.d("chattersnapshot", " masuk");
                    Chater chater = snapshot.getValue(Chater.class);
                    if (list.size() != 0) {
                        obrolanKosong.setVisibility(View.GONE);
                        for (Chatlist chatList : list) {
//                            Log.d("chatlist level ", " " + chatList.getLevel());
//                            if (chater.getId().equals(chatList.getId()) ) {
////                                Log.d("chatter3", " masuk");
//                                chaters.add(chater);
//                            }
                            if (chater != null && chater.getId() != null && chatList != null && chatList.getId() != null &&
                                    chater.getId().equals(chatList.getId())) {

                                if (chatType.equals("walikelas")) {
                                    isWalikelas = true;
                                    if (chatList.getLevel().equals("WALIKELAS") || chatList.getLevel().equals("WALI MURID") || chatList.getLevel().equals("SISWA") || chatList.getLevel().equals("ORANG TUA"))
                                        chatersGuru.add(chater);
                                } else {
                                    isWalikelas = false;
                                    if (chatList.getLevel().equals("DOKTER") || chatList.getLevel().equals("WALI MURID") || chatList.getLevel().equals("SISWA") || chatList.getLevel().equals("ORANG TUA"))
                                        chatersDokter.add(chater);
                                }
                            }
                        }
                    } else {
//                        Log.d("userlist isi", " kosong");
                        obrolanKosong.setVisibility(View.VISIBLE);
                    }

                }

                pbCurrentChat.setVisibility(View.GONE);
                if (isWalikelas)
                    adapter = new CustomChaterAdapter(chatersGuru, true, chatType,currentUserLevel);
                else
                    adapter = new CustomChaterAdapter(chatersDokter, true, chatType, currentUserLevel);

                adapter.notifyDataSetChanged();
                rvChater.setAdapter(adapter);
//                Log.d("chatter4", " masuk");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void loginFireBase(String email, String password) {
        auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
//                            circularProgressBar.setVisibility(View.GONE);
//                            bgLoad.setVisibility(View.GONE);
//                            Intent intent = new Intent(getActivity(), ChatActivity.class);
//                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
//                            startActivity(intent);
                        } else {
//                            circularProgressBar.setVisibility(View.GONE);
//                            bgLoad.setVisibility(View.GONE);
//                            FancyToast.makeText(getActivity(), "Autentikasi Gagal !", FancyToast.LENGTH_LONG, FancyToast.ERROR, true).show();
                        }
                    }
                });
    }

}
