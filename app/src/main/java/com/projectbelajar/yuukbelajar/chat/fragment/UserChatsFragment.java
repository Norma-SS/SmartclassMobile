package com.projectbelajar.yuukbelajar.chat.fragment;


import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.projectbelajar.yuukbelajar.Preferences;
import com.projectbelajar.yuukbelajar.R;
import com.projectbelajar.yuukbelajar.chat.adapter.CustomChaterAdapter;
import com.projectbelajar.yuukbelajar.chat.model.Chater;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

/**
 * A simple {@link Fragment} subclass.
 */
public class UserChatsFragment extends Fragment {

    private RecyclerView rvChater;
    private ArrayList<Chater> chaters;
    private CustomChaterAdapter adapter;
    private String currentUserLevel, currentKlinik, currentId;

    private Preferences preferences;

    private String chatType;

    public UserChatsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_user_chats, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        preferences = new Preferences(getActivity());
        chatType = preferences.getValues("chatType");

        rvChater = view.findViewById(R.id.rv_chat);
        rvChater.setHasFixedSize(true);
        rvChater.setLayoutManager(new LinearLayoutManager(getActivity()));

        chaters = new ArrayList<>();
        readUser();
    }

    private void readUser() {
        currentUserLevel = preferences.getValues("level");
        currentKlinik = preferences.getValues("klinik");
        currentId = preferences.getValues("id");
        final FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        final Query reference = FirebaseDatabase.getInstance().getReference("Users").orderByChild("sort_nama");

//        DatabaseReference kodeSekolahRef = FirebaseDatabase.getInstance().getReference("Users");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                chaters.clear();

                String[] currentSchoolCode = preferences.getValues("kodesekolah").split(",");
                ArrayList<String> listCurrent = new ArrayList<>();
                Collections.addAll(listCurrent, currentSchoolCode);

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Chater chater = snapshot.getValue(Chater.class);
//                    Log.d("current Level ", " " + currentUserLevel);
//                    Log.d("chatter ", " " + chater.getLevel());

                    if (chater.getId() != null) {
//                        Log.d("chater code ", " " + chater.getKodeSekolah());
                        String[] allSchoolCode = chater.getKodeSekolah().split(",", -1);
                        ArrayList<String> listAll = new ArrayList<>();
                        Collections.addAll(listAll, allSchoolCode);
//                        if (chater.getKodeSekolah().equals(preferences.getValues("kodesekolah"))){

                        if (checkKodeSekolah(allSchoolCode, currentSchoolCode) || checkKodeSekolah(currentSchoolCode, allSchoolCode)) {
                            Log.d("ada yg sama ", " " + chater.getLevel());
                            if (chatType.equals("walikelas")) {
                                Log.d("chatType walkel", " " + chatType);
                                Log.d("Current User ", " " + currentUserLevel);
                                if (preferences.getValues("kelas").equals(chater.getKelas())) {
                                    if (currentUserLevel.equals("WALIKELAS")) {
                                        if (chater.getLevel().equals("SISWA") || chater.getLevel().equals("WALI MURID") || chater.getLevel().equals("ORANG TUA")) {
                                            chaters.add(chater);
                                        }
                                    } else if (currentUserLevel.equals("SISWA") || currentUserLevel.equals("WALI MURID") || currentUserLevel.equals("ORANG TUA")) {
                                        if (chater.getLevel().equals("WALIKELAS")) {
                                            chaters.add(chater);
                                        }
                                    }
                                }
                            } else if (chatType.equals("dokter")) {
                                Log.d("chatType dokter", " " + chatType);
                                if (currentUserLevel.equals("DOKTER")) {
                                    Log.d("Current User Must", " Dokter");
//                                    if (chater.getLevel().equals("SISWA") || chater.getLevel().equals("WALI MURID") || chater.getLevel().equals("ORANG TUA")) {
//                                        chaters.add(chater);
//                                    }
                                    if (chater.getLevel().equals("DOKTER")) {
                                        if (chater.getKlinik().equals(currentKlinik)) {
                                            if (!chater.getId().equals(currentId)){
                                                chaters.add(chater);
                                            }

                                        }
                                    }
                                } else if (currentUserLevel.equals("SISWA") || currentUserLevel.equals("WALI MURID") || currentUserLevel.equals("ORANG TUA")) {
                                    Log.d("Current User Must", " Pasien");
                                    if (chater.getLevel().equals("DOKTER")) {
                                        chaters.add(chater);
                                    }
                                }
                            }
                        }

                    }

//                    if (!chater.getId().equals(firebaseUser.getUid())){
//                        chaters.add(chater);
//                    }
                }
//                Log.d("chaters size ", " " + chaters.size());
//
//                for (Chater item : chaters){
//                    Log.d("chatter ", " " + item.getNama());
//                }
                adapter = new CustomChaterAdapter(chaters, false, chatType, currentUserLevel);
                rvChater.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public static boolean checkKodeSekolah(String[] outer, String[] inner) {

        return Arrays.asList(outer).containsAll(Arrays.asList(inner));
    }

    private void orderByDateUser() {

        final FirebaseUser fuser = FirebaseAuth.getInstance().getCurrentUser();
        Query query = FirebaseDatabase.getInstance().getReference("Users").orderByChild("time");

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
//                    User user = snapshot.getValue(User.class);

//                    assert user != null;
                    assert fuser != null;
//                    if (!user.getId().equals(fuser.getUid())){
//                        mUsers.add(user);
//                    }
                }

//                userAdapter = new UserAdapter(getContext(),onItemClick, mUsers, false);
//                recyclerView.setAdapter(userAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
}