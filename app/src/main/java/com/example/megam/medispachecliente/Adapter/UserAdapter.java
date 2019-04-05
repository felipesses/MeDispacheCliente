package com.example.megam.medispachecliente.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.megam.medispachecliente.Main3Activity;
import com.example.megam.medispachecliente.R;
import com.example.megam.medispachecliente.model.Chat;
import com.example.megam.medispachecliente.model.Produtos;
import com.example.megam.medispachecliente.model.Usuarios;
import com.example.megam.medispachecliente.view.MessageActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder> {

    private Context mContext;
    private List<Usuarios> mUsers;
    private boolean ischat;
    String theLastMessage;
    public UserAdapter(Context mContext, List<Usuarios> mUsers, boolean ischat){

        this.mUsers = mUsers;
        this.mContext = mContext;
        this.ischat=ischat;

    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.user_item, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final Usuarios user = mUsers.get(position);
        holder.username.setText(user.getName());

        if (user.getImageUrl() == null) {
            holder.profile_image.setImageResource(R.drawable.ic_launcher_background);
        } else {
            Glide.with(mContext).load(user.getImageUrl()).into(holder.profile_image);
        }

        if(ischat){
            lastMessage(user.getId(), holder.last_msg);
        }else{
            holder.last_msg.setVisibility(View.GONE);
        }


if(ischat){
            if(user.getStatus().equals("online")){
                holder.img_on.setVisibility(View.VISIBLE);
                holder.img_off.setVisibility(View.GONE);
            }else{
                holder.img_on.setVisibility(View.GONE);
                holder.img_off.setVisibility(View.VISIBLE);
            }
}else{
    holder.img_on.setVisibility(View.GONE);
    holder.img_off.setVisibility(View.VISIBLE);
}



holder.itemView.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {

        Intent intent = new Intent(mContext, Main3Activity.class);
        intent.putExtra("userID", user.getId());
        mContext.startActivity(intent);
    }
});
        holder.mensagem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, MessageActivity.class);
                intent.putExtra("userID", user.getId());
                mContext.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return mUsers.size();
    }

    public class ViewHolder extends  RecyclerView.ViewHolder {

        public TextView username;
        public ImageView profile_image;
        private ImageView img_on;
        private ImageView img_off;
        public TextView last_msg;
        public ImageButton mensagem;
        public TextView tempoentrega;
        public TextView valorentrega;
        public TextView estadoestabelecimento;
        Usuarios user;
        public ViewHolder(View itemView){
            super(itemView);
            username = itemView.findViewById(R.id.username);
            profile_image = itemView.findViewById(R.id.profile_image);
            img_on = itemView.findViewById(R.id.img_on);
            img_off = itemView.findViewById(R.id.img_off);
            last_msg = itemView.findViewById(R.id.last_msg);
            mensagem = itemView.findViewById(R.id.Mensagem);
            tempoentrega = itemView.findViewById(R.id.tempoE);
            valorentrega = itemView.findViewById(R.id.ValorE);
            estadoestabelecimento = itemView.findViewById(R.id.aberto);

            final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

            if(user==null){
                mensagem.setVisibility(View.GONE);
            }else{
                mensagem.setVisibility(View.VISIBLE);// mudar o botao
            }



        }

    }
private void lastMessage(final String userid, final TextView last_msg){
theLastMessage = "default";
    final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Chats");
    reference.addValueEventListener(new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            if(dataSnapshot.exists()&& user !=null){
            for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                Chat chat = snapshot.getValue(Chat.class);

                if(chat.getReceiver().equals(user.getUid())&& chat.getSender().equals(userid)|| chat.getReceiver().equals(userid) && chat.getSender().equals(user.getUid())){
                    theLastMessage = chat.getMessage();
                }


            }}
            switch (theLastMessage){
                case "default":
                    last_msg.setText("nenhuma mensagem");
                    break;


                default:
                    last_msg.setText(theLastMessage);
                    break;
            }
            theLastMessage = "default";
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    });
 }


}
