package com.example.megam.medispachecliente.fragments;

import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.example.megam.medispachecliente.Adapter.UserAdapter;
import com.example.megam.medispachecliente.Notifications.Token;
import com.example.megam.medispachecliente.R;
import com.example.megam.medispachecliente.control.Conexao;
import com.example.megam.medispachecliente.model.Usuarios;
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class UsersFragment extends Fragment {
    private RecyclerView recyclerView;
    private UserAdapter userAdapter;
    private List<Usuarios> mUsers;
    EditText search_users;
    FirebaseUser user;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_users, container, false);

        recyclerView = view.findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mUsers = new ArrayList<>();
        user = FirebaseAuth.getInstance().getCurrentUser();
        search_users = view.findViewById(R.id.search_user);
        search_users.setVisibility(View.GONE); //View.GONE

        Bundle bundle = this.getArguments();
        String strtext = bundle.getString("edttext").toLowerCase();
        System.out.println("SEI NEM OQ E ISSO"+strtext);

        readUsers(strtext);

        if(user==null){
            search_users.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i1, int i2, int after) {
                    searchUsers(charSequence.toString().toLowerCase());
                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    searchUsers(charSequence.toString().toLowerCase());
                }

                @Override
                public void afterTextChanged(Editable s) {
                    searchUsers(s.toString().toLowerCase());
                }
            });
        }else{
            search_users.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i1, int i2, int after) {
                    searchUsers(charSequence.toString().toLowerCase());
                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    searchUsers(charSequence.toString().toLowerCase());
                }

                @Override
                public void afterTextChanged(Editable s) {
                    searchUsers(s.toString().toLowerCase());
                }
            });
            updateToken(FirebaseInstanceId.getInstance().getToken());
        }
        return view;
    }

    private void updateToken(String token){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Tokens");
        Token token1 = new Token(token);
        reference.child(user.getUid()).setValue(token1);
    }

    private void searchUsers(String s) { // FAZENDO A PESQUISA CERTINHO
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        Query query = FirebaseDatabase.getInstance().getReference("Empresas").child("restaurante").orderByChild("name")
                .startAt(s)
                .endAt(s+"\uf8ff");
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mUsers.clear();
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    Usuarios u = snapshot.getValue(Usuarios.class);
                    mUsers.add(u);
                            /*if(!u.getId().equals(user.getUid())){
                                mUsers.add(u);
                            }*/
                }
                userAdapter = new UserAdapter(getContext(), mUsers, true);
                recyclerView.setAdapter(userAdapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void readUsers(String cidadezinha) { // O OBJETIVO AQUI É PASSAR A LOCALIZAÇÃO ATUAL DA PESSOA NO LUGAR DO TEXTO DA STRING
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        Query query = FirebaseDatabase.getInstance().getReference("Empresas").child("restaurante").orderByChild("search")
                .startAt(cidadezinha)
                .endAt(cidadezinha+"\uf8ff");
        System.out.println("TEM UM FUCKINIG city AQUI PRA OLHAR: "+cidadezinha);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                mUsers.clear();
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    Usuarios u = snapshot.getValue(Usuarios.class);
                    mUsers.add(u);
                            /*if(!u.getId().equals(user.getUid())){
                                mUsers.add(u);
                            }*/

                    System.out.println("ID "+u.getId());
                    System.out.println("NOME "+u.getName());
                    System.out.println("TEMPO ENTREGA "+u.getTempo());
                    System.out.println("CIDADE "+u.getCidade());

                }
                System.out.println("USUARIO: "+mUsers);
                userAdapter = new UserAdapter(getContext(), mUsers, true);
                recyclerView.setAdapter(userAdapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        /*FirebaseAuth firebaseAuth = Conexao.getFirebaseAuth();
        final FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        //comentário dessa maluquice aqui
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("UserEmpresa"); // nao ta passando a imagem de Users empresa para empresa no firebase
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (search_users.getText().toString().equals("")) {
                    mUsers.clear();
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        Usuarios user = snapshot.getValue(Usuarios.class);
                        assert user != null;
                        mUsers.add(user);
                        *//*if (!user.getId().equals(firebaseUser.getUid())) {
                            mUsers.add(user);
                        }*//*
                    }

                    userAdapter = new UserAdapter(getContext(), mUsers, true);
                    recyclerView.setAdapter(userAdapter);
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });*/

    }


}