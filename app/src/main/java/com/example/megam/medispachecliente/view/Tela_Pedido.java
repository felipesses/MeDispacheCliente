package com.example.megam.medispachecliente.view;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
//import com.example.megam.medispachecliente.Adapter.ConfirmaAdapter;
//import com.example.megam.medispachecliente.Adapter.PedidosAdapter;
import com.example.megam.medispachecliente.MainActivity;
import com.example.megam.medispachecliente.Notifications.Token;
import com.example.megam.medispachecliente.R;
import com.example.megam.medispachecliente.model.Chatlist;
import com.example.megam.medispachecliente.model.Usuarios;
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
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.app.Activity.RESULT_OK;


public class Tela_Pedido extends AppCompatActivity {
    CircleImageView image_profile;
    TextView username;
    DatabaseReference referece;
    FirebaseUser user;

    StorageReference storageReference;
    private static final int IMAGE_REQUEST = 1;
    private Uri imageUrl;
    private StorageTask uploadTask;
    boolean aberto;
    String ultimo;
    private RecyclerView recyclerView;
    //private ConfirmaAdapter confirmaAdapter;
    private List<Usuarios> mUsers;
    DatabaseReference databaseReference;
    private List<Chatlist> userslist;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        aberto = false;
        View view = inflater.inflate(R.layout.informacoes_pedido, container, false);
        return view;
    }
        /*image_profile = view.findViewById(R.id.profile_image);
        username = view.findViewById(R.id.username);
        storageReference = FirebaseStorage.getInstance().getReference("uploads");

        username.setEnabled(false);




        user = FirebaseAuth.getInstance().getCurrentUser();
        referece = FirebaseDatabase.getInstance().getReference("UserEmpresa").child(user.getUid());
        referece.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                *//*Usuarios u = dataSnapshot.getValue(Usuarios.class);
                username.setText(u.getName());

                if (u.getImageUrl() == null || getContext() == null) {
                    image_profile.setImageResource(R.drawable.ic_launcher_background);
                } else {
                    //Glide.with(getContext()).load(u.getImageUrl()).into(image_profile);
                }*//*
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        image_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openImage();
            }
        });

        recyclerView = view.findViewById(R.id.recycler_view);
        //recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        user = FirebaseAuth.getInstance().getCurrentUser();
        userslist = new ArrayList<>();

       *//* databaseReference = FirebaseDatabase.getInstance().getReference("Chatlist").child(user.getUid());
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            *//**//*public void onDataChange(DataSnapshot dataSnapshot) {
                userslist.clear();
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    Chatlist chatlist = snapshot.getValue(Chatlist.class);
                    userslist.add(chatlist);

                }
                chatList();

            }*//**//*

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });*//*
        updateToken(FirebaseInstanceId.getInstance().getToken());



        return view;
    }

    private  void  updateToken(String token){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Tokens");
        Token token1 = new Token(token);
        reference.child(user.getUid()).setValue(token1);

    }



   *//* private void chatList() {
        mUsers = new ArrayList<>();
        databaseReference = FirebaseDatabase.getInstance().getReference("User");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mUsers.clear();
                for(DataSnapshot snapshot : dataSnapshot.getChildren() ){
                    Usuarios u = snapshot.getValue(Usuarios.class);
                    for(Chatlist chatlist : userslist){
                        if(u.getId().equals(chatlist.getId())){
                            mUsers.add(u);
                        }
                    }

                }
                confirmaAdapter = new ConfirmaAdapter(getContext(), mUsers, true);
                recyclerView.setAdapter(confirmaAdapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }*//*

    private void openImage() {

        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, IMAGE_REQUEST);
    }


   *//* private String getFileExtension(Uri uri) {
        ContentResolver contentResolver = getContext().getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }*//*

    private void uploadImage() {
        final ProgressDialog pd = new ProgressDialog(getContext());
        pd.setMessage("Upando");
        pd.show();

        if (imageUrl != null) {
            final StorageReference fileReference = storageReference.child(System.currentTimeMillis()
                    + "." + getFileExtension(imageUrl));

            uploadTask = fileReference.putFile(imageUrl);
            uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    if (!task.isSuccessful()) {
                        throw task.getException();
                    }
                    return fileReference.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if (task.isSuccessful()) {
                        Uri downloadUri = task.getResult();
                        String mUri = downloadUri.toString();
                        referece = FirebaseDatabase.getInstance().getReference("UserEmpresa").child(user.getUid());
                        HashMap<String, Object> map = new HashMap<>();
                        map.put("imageUrl", mUri);
                        referece.updateChildren(map);
                        pd.dismiss();
                    } else {

                        Toast.makeText(getContext(), "Failed", Toast.LENGTH_SHORT).show();
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
            Toast.makeText(getContext(), "Imagem não Selecionada", Toast.LENGTH_SHORT).show();

        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null & data.getData() != null) {
            imageUrl = data.getData();

            if(uploadTask != null && uploadTask.isInProgress()){
                Toast.makeText(getContext(), "Envio em progresso", Toast.LENGTH_SHORT).show();
            }else{
                uploadImage();
            }
        }

    }*/
}