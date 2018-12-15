package com.example.megam.medispachecliente.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.megam.medispachecliente.Adapter.MessageAdapter;
import com.example.megam.medispachecliente.MainActivity;
import com.example.megam.medispachecliente.Notifications.Client;
import com.example.megam.medispachecliente.Notifications.Data;
import com.example.megam.medispachecliente.Notifications.MyResponse;
import com.example.megam.medispachecliente.Notifications.Sender;
import com.example.megam.medispachecliente.Notifications.Token;
import com.example.megam.medispachecliente.R;
import com.example.megam.medispachecliente.fragments.APIService;
import com.example.megam.medispachecliente.model.Chat;
import com.example.megam.medispachecliente.model.Usuarios;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MessageActivity extends AppCompatActivity {


    CircleImageView profilie_image;
    TextView nomeusuario;
    FirebaseUser user;
    DatabaseReference reference;
    ImageButton btn;
    EditText textmsg;
    MessageAdapter messageAdapter;
    List<Chat> mChat;
    RecyclerView recyclerView;
    Intent intent;
    String userid;

    APIService apiService;
    boolean notify =false;

    ValueEventListener seenListener;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent( MessageActivity.this, MainActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));            }
        });

        apiService = Client.getClient("https://fcm.googleapis.com/").create(APIService.class);
        recyclerView = findViewById(R.id.recycler_msg);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);

        profilie_image = findViewById(R.id.imagemperfila);
        nomeusuario = findViewById(R.id.nomeuser);
        textmsg = findViewById(R.id.text_send);
        btn = findViewById(R.id.btn_send);

        intent = getIntent();
        userid = intent.getStringExtra("userID");
        user = FirebaseAuth.getInstance().getCurrentUser();
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                notify = true;
                String msg = textmsg.getText().toString().trim();
                if(!msg.equals("")){
                    sendMessage(user.getUid(), userid, msg);                }else{

                    alert("Digite sua mensagem");
                }
                textmsg.setText("");
            }
        });



        reference = FirebaseDatabase.getInstance().getReference("UserEmpresa").child(userid);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Usuarios u = dataSnapshot.getValue(Usuarios.class);
                if(u.getImageUrl()== null){
                    profilie_image.setImageResource(R.drawable.ic_launcher_background);

                }else{
                    Glide.with(getApplicationContext()).load(u.getImageUrl()).into(profilie_image);

                }
                nomeusuario.setText(u.getName());
                readMessages(user.getUid(), userid, u.getImageUrl());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        seenMessage(userid);

    }


    private void seenMessage(final String userid){
        reference = FirebaseDatabase.getInstance().getReference("Chats");
        seenListener = reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    Chat chat = snapshot.getValue(Chat.class);
                    if(chat.getReceiver().equals(user.getUid())&& chat.getSender().equals(userid)){
                        HashMap<String, Object> hashMap = new HashMap<>();
                        hashMap.put("isseen", true);
                        snapshot.getRef().updateChildren(hashMap);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }




    private void sendMessage(String sender,final String receiver, String message){

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("sender", sender);
        hashMap.put("receiver", receiver);
        hashMap.put("message", message);
        hashMap.put("isseen", false);


        reference.child("Chats").push().setValue(hashMap);
        final DatabaseReference chatref = FirebaseDatabase.getInstance().getReference("Chatlist")
                .child(user.getUid())
                .child(userid);
        chatref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(!dataSnapshot.exists()){
                    chatref.child("id").setValue(userid);

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        final DatabaseReference chatref2 = FirebaseDatabase.getInstance().getReference("Chatlist")
                .child(userid)
                .child(user.getUid());
        chatref2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(!dataSnapshot.exists()){
                    chatref2.child("id").setValue(user.getUid());

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });



        final String msg = message;
        reference = FirebaseDatabase.getInstance().getReference("User").child(user.getUid());
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Usuarios u = dataSnapshot.getValue(Usuarios.class);
                if(notify) {
                    sendNotification(receiver, u.getName(), msg);
                }
                notify = false;
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void sendNotification(String receiver, final String username, final String message){
        DatabaseReference tokens = FirebaseDatabase.getInstance().getReference("Tokens");
        Query query = tokens.orderByKey().equalTo(receiver);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    Token token = snapshot.getValue(Token.class);
                    Data data = new Data(user.getUid(),  R.drawable.ic_motivo, username+": "+message, "Nova mensagem",
                            userid);
                    Sender sender = new Sender(data, token.getToken());
                    apiService.sendNotification(sender)
                            .enqueue(new Callback<MyResponse>() {
                                @Override
                                public void onResponse(Call<MyResponse> call, Response<MyResponse> response) {
                                    if(response.code() == 200){
                                        if(response.body().success!=1){

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
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


    private void readMessages (final String myid, final String userid, final String imageurl) {
        mChat = new ArrayList<>();
        reference = FirebaseDatabase.getInstance().getReference("Chats");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mChat.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Chat chat = snapshot.getValue(Chat.class);
                    if (chat.getReceiver().equals(myid) && chat.getSender().equals(userid) ||
                            chat.getReceiver().equals(userid) && chat.getSender().equals(myid)) {
                        mChat.add(chat);
                    }
                    messageAdapter = new MessageAdapter(MessageActivity.this, mChat, imageurl);
                    recyclerView.setAdapter(messageAdapter);
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
    public void status(String status){
        reference = FirebaseDatabase.getInstance().getReference("User").child(user.getUid());
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("status", status);
        reference.updateChildren(hashMap);
    }

    @Override
    protected void onResume() {
        super.onResume();
        status("online");
    }

    @Override
    protected void onPause() {
        super.onPause();
        reference.removeEventListener(seenListener);
        status("offline");
    }

    public void alert(String msg){
        Toast.makeText(this,msg,Toast.LENGTH_SHORT).show();
    }

}
