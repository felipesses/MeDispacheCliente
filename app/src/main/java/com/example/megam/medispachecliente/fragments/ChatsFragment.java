package com.example.megam.medispachecliente.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.megam.medispachecliente.Adapter.UserAdapter;
import com.example.megam.medispachecliente.Notifications.Token;
import com.example.megam.medispachecliente.R;
import com.example.megam.medispachecliente.model.Chatlist;
import com.example.megam.medispachecliente.model.Usuarios;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.ArrayList;
import java.util.List;


public class ChatsFragment extends Fragment {


private RecyclerView recyclerView;
private UserAdapter userAdapter;
private List<Usuarios> mUsers;
FirebaseUser user;
DatabaseReference databaseReference;
private List<Chatlist> userslist;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        View view = inflater.inflate(R.layout.fragment_chats, container, false);
        recyclerView = view.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        user = FirebaseAuth.getInstance().getCurrentUser();
        userslist = new ArrayList<>();

databaseReference = FirebaseDatabase.getInstance().getReference("Chatlist").child(user.getUid());
databaseReference.addValueEventListener(new ValueEventListener() {
    @Override
    public void onDataChange(DataSnapshot dataSnapshot) {
        userslist.clear();
     for(DataSnapshot snapshot : dataSnapshot.getChildren()){
        Chatlist chatlist = snapshot.getValue(Chatlist.class);
        userslist.add(chatlist);

     }
        chatList();

    }

    @Override
    public void onCancelled(DatabaseError databaseError) {

    }
});
        updateToken(FirebaseInstanceId.getInstance().getToken());

        return view;
    }



    private  void  updateToken(String token){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Tokens");
        Token token1 = new Token(token);
        reference.child(user.getUid()).setValue(token1);

    }



    private void chatList() {
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
                 userAdapter = new UserAdapter(getContext(), mUsers, true);
                recyclerView.setAdapter(userAdapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


}
