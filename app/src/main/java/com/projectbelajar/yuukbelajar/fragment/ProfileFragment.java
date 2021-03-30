package com.projectbelajar.yuukbelajar.fragment;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.projectbelajar.yuukbelajar.Login;
import com.projectbelajar.yuukbelajar.Preferences;
import com.projectbelajar.yuukbelajar.R;
import com.projectbelajar.yuukbelajar.RequestHandler;
import com.projectbelajar.yuukbelajar.SessionManager;
import com.projectbelajar.yuukbelajar.konfigurasi;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.app.Activity.RESULT_OK;
import static com.projectbelajar.yuukbelajar.SessionManager.password;

public class ProfileFragment extends Fragment{

    SharedPreferences.Editor editor;
    SharedPreferences pref;
    int mode = 0;

    private String eml;

    private TextView Nama;
    private TextView Kls;
    private TextView Stts;
    private TextView Skl;
    private TextView edit;
    private TextView logout;
    private EditText Email;
    private EditText Hp;
    private EditText Password;
    private CircleImageView Foto;

    private ImageView Edite;

    private String JSON_STRING;

    private static final String pref_name = "crudpref";
    Context context;
    private Preferences preferences;
    private DatabaseReference reference;
    FirebaseUser fuser;
    SessionManager sessionManager;
    StorageReference storageReference;
    private boolean EDIT = false;
    private static final int IMAGE_REQUEST = 1;
    private Uri imageUri;
    private StorageTask<UploadTask.TaskSnapshot> uploadTask;


    public ProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        sessionManager = new SessionManager(getContext());
        preferences = new Preferences(getActivity());
        pref = getActivity().getSharedPreferences(pref_name, mode);
        editor = pref.edit();

        storageReference = FirebaseStorage.getInstance().getReference("uploads");

        fuser = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("Users").child(preferences.getValues("id"));
        preferences = new Preferences(getActivity());
        sessionManager.getUserDetails();

        Foto =  view.findViewById(R.id.iv_Meet_photo);
        Nama = view.findViewById(R.id.Namax);
        Stts = view.findViewById(R.id.Sttsx);
        Kls = view.findViewById(R.id.Klsx);
        Skl = view.findViewById(R.id.Sklx);
        edit = view.findViewById(R.id.edit_data);
        Email = view.findViewById(R.id.et_email);
        Hp = view.findViewById(R.id.et_hp);
        Password = view.findViewById(R.id.et_pw);
        logout = view.findViewById(R.id.logout);
        Edite = view.findViewById(R.id.edite);

        SharedPreferences prefs = getActivity().getSharedPreferences(pref_name, Context.MODE_PRIVATE);
        eml = prefs.getString(password, null);
        edit.setText("Edit");
        Password.setText(preferences.getValues( "password"));
        Hp.setText(preferences.getValues( "hp"));
        Nama.setText(preferences.getValues( "nama"));
        if (preferences.getValues("level").equals("KS")){
            Stts.setText("Kepala Sekolah");
        } else {
            Stts.setText(preferences.getValues("level"));
        }
        Email.setText(preferences.getValues( "email"));
        if (preferences.getValues("level").equals("DOKTER")){
            Skl.setText(preferences.getValues( "klinik"));
        } else {
            Skl.setText(preferences.getValues("namaSekolah"));
        }
        Edite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openImage();
            }
        });
        Foto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openImage();
            }
        });
        edit.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        if (!EDIT){
                                            edit.setText("Simpan");
                                            Email.setEnabled(true);
                                            Hp.setEnabled(true);
                                            Password.setEnabled(true);
                                            EDIT = true;
                                        } else  {
                                            fuser.updateEmail(Email.getText().toString())
                                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<Void> task) {
                                                            if (task.isSuccessful()) {
                                                                Log.d("TAG", "User email address updated.");
                                                            }
                                                        }
                                                    });
                                            fuser.updatePassword(Password.getText().toString())
                                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<Void> task) {
                                                            if (task.isSuccessful()) {
                                                                Log.d("TAG", "User password updated.");
                                                            }
                                                        }
                                                    });
                                            reference.child("email").setValue(Email.getText().toString());
                                            reference.child("username").setValue(Hp.getText().toString());
                                            reference.child("password").setValue(Password.getText().toString());
                                            Toast.makeText(getActivity(), "Berhasil Disimpan", Toast.LENGTH_SHORT).show();
                                            edit.setText("Edit");
                                            Email.setEnabled(false);
                                            Hp.setEnabled(false);
                                            Password.setEnabled(false);
                                            EDIT = false;
                                        }
                                    }
                                });
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(isAdded()){
                    String password = (String) dataSnapshot.child("password").getValue();
                    String nohp = (String) dataSnapshot.child("username").getValue();
                    String email = (String) dataSnapshot.child("email").getValue();
                    preferences.setValues("hp", nohp);
                    preferences.setValues("password", password);
                    preferences.setValues("email", email);

                    if (preferences.getValues("foto").equals("default")){
                        Foto.setImageResource(R.drawable.profile);
                    } else {
                        Glide.with(getActivity()).load(preferences.getValues("foto"))
                                .fitCenter() //menyesuaikan ukuran imageview
                                .crossFade() //animasi
                                .diskCacheStrategy(DiskCacheStrategy.ALL)
                                .into(Foto);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        if (preferences.getValues("level").equals("DOKTER") || preferences.getValues("level").equals("GURU") || preferences.getValues("level").equals("KS")){
            Kls.setText(preferences.getValues("nip"));
        } else {
            Kls.setText(preferences.getValues("kelas"));
        }
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                preferences.logout();
//                sessionManager.logout();
                editor.clear();
                editor.apply();
                Intent i = new Intent(getActivity(), Login.class);
                startActivity(i);
                getActivity().finish();

            }
        });
        //=================================================================== getjson

        class GetJSON extends AsyncTask<Void,Void,String> {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                JSON_STRING = s;

                //showEmployee();
                //================================================================ showemployee

                JSONObject jsonObject = null;
                ArrayList<HashMap<String,String>> list = new ArrayList<HashMap<String, String>>();
                try {
                    jsonObject = new JSONObject(JSON_STRING);
                    JSONArray result = jsonObject.getJSONArray(konfigurasi.TAG_JSON_ARRAY);

                    for(int i = 0; i<result.length(); i++){
                        JSONObject jo = result.getJSONObject(i);
                        String foto = jo.getString(konfigurasi.TAG_FOTO);
                        String nm = jo.getString(konfigurasi.TAG_NAMA);
                        String stts = jo.getString(konfigurasi.TAG_STATUS);
                        String kls = jo.getString(konfigurasi.TAG_KLS);
                        String nmskl = jo.getString(konfigurasi.TAG_NMSKL);
                        String email = jo.getString(konfigurasi.TAG_EMAIL);
                        String hp = jo.getString(konfigurasi.TAG_HP);


////                        if (nm.equals("")){
//                            Nama.setText(nm);
////                        }else if (stts.equals("")){
//                            Stts.setText(stts);
////                        } else if (kls.equals("")){
//                            Kls.setText(kls);
////                        } else if (nmskl == null){
//                            Skl.setText(nmskl);
////                        } else if (hp == null){
//                            Hp.setText(hp);
////                        } else {

//                        Email.setText(email);
//                        Hp.setText(hp);
//                        }

                        //Toast.makeText(getApplicationContext(), nm+" - "+foto, Toast.LENGTH_LONG).show();

//                        Glide.with(getActivity()).load(foto)
//                                .fitCenter() //menyesuaikan ukuran imageview
//                                .crossFade() //animasi
//                                .diskCacheStrategy(DiskCacheStrategy.ALL)
//                                .into(Foto); //walikls.jpg


                        //Toast.makeText(getApplicationContext(), foto, Toast.LENGTH_LONG).show();

                        HashMap<String,String> employees = new HashMap<>();
                        //employees.put(konfigurasi.TAG_TGL,tgl);
                        //employees.put(konfigurasi.TAG_KET,ket);
                        //employees.put(konfigurasi.TAG_MPEL,mpel);
                        //employees.put(konfigurasi.TAG_NIL,nil);

                        list.add(employees);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                //=========================================================================
            }

            @Override
            protected String doInBackground(Void... params) {

                RequestHandler rh = new RequestHandler();
                String s = rh.sendGetRequestParam(konfigurasi.URL_GET_PROFIL,eml);
                //String s = rh.sendGetRequestParam(konfigurasi.URL_GET_EMP,eml);
                return s;
            }
        }
        GetJSON gj = new GetJSON();
        gj.execute();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }
    private void openImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, IMAGE_REQUEST);
    }

    private String getFileExtension(Uri uri){
        ContentResolver contentResolver = getContext().getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }

    private void uploadImage(){
        final ProgressDialog pd = new ProgressDialog(getContext());
        pd.setIndeterminateDrawable(getResources().getDrawable(R.drawable.ic_picture));

        pd.setMessage("Uploading...");
        pd.show();

        if (imageUri != null){
            final StorageReference fileReference = storageReference.child(System.currentTimeMillis()
                    +"."+getFileExtension(imageUri));

            uploadTask = fileReference.putFile(imageUri);
            uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    if (!task.isSuccessful()){
                        throw  task.getException();
                    }

                    return  fileReference.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if (task.isSuccessful()){
                        Uri downloadUri = task.getResult();
                        String mUri = downloadUri.toString();

                        reference = FirebaseDatabase.getInstance().getReference("Users").child(fuser.getUid());
                        HashMap<String, Object> map = new HashMap<>();
                        map.put("imgUrl", ""+mUri);
                        reference.updateChildren(map);

                        pd.dismiss();
                        reference.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                String foto =(String) dataSnapshot.child("imgUrl").getValue();
                                preferences.setValues("foto", foto );

                                if(isAdded()){
                                    if (reference.child("imgUrl").equals("default")){
                                        Foto.setImageResource(R.drawable.profile);
                                    } else {
                                        Glide.with(getActivity()).load(preferences.getValues("foto"))
                                                .fitCenter() //menyesuaikan ukuran imageview
                                                .crossFade() //animasi
                                                .diskCacheStrategy(DiskCacheStrategy.ALL)
                                                .into(Foto);
                                    }
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                    } else {
                        Toast.makeText(getContext(), "Failed!", Toast.LENGTH_SHORT).show();
                        pd.dismiss();
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                    pd.dismiss();
                }
            });
        } else {
            Toast.makeText(getContext(), "No image selected", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null) {
            imageUri = data.getData();

            if (uploadTask != null && uploadTask.isInProgress()) {
                Toast.makeText(getContext(), "Upload in progress", Toast.LENGTH_SHORT).show();
            } else {
                uploadImage();
            }
        }
    }

}