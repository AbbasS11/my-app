package com.example.afterweb;

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
import com.example.afterweb.Adapter.MessageAdapter;
import com.example.afterweb.Model.Chat;
import com.example.afterweb.Model.Users;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

public class MessagesActivity extends AppCompatActivity {
    private CircleImageView profile_image;
    private TextView username;

    FirebaseUser firebaseUser;
    DatabaseReference databaseReference;

    MessageAdapter messageAdapter;
    List<Chat> mChat;
    RecyclerView recyclerView;

    ImageButton btn_send;
    EditText edt_message;

    Intent intent;
    ValueEventListener valueEventListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messages);


        btn_send=findViewById(R.id.btn_sendMessage);
        edt_message=findViewById(R.id.edt_sendMessage);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        recyclerView=findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        //need to learn
        LinearLayoutManager linearLayoutManager= new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);


        profile_image = findViewById(R.id.tool_profile_img);
        username = findViewById(R.id.tool_username);

        intent = getIntent();
        final String userid = intent.getStringExtra("userId");
        seenMessage(userid);

        firebaseUser= FirebaseAuth.getInstance().getCurrentUser();
        databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(userid);

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Users users = dataSnapshot.getValue(Users.class);
                assert users != null;
                username.setText(users.getUsername());
                if (users.getImageURl() != null && !users.getImageURl().equals("default")){
                    Glide.with(MessagesActivity.this).load(users.getImageURl()).into(profile_image);
                } else {
                    profile_image.setImageResource(R.mipmap.ic_launcher);
                }

                readMessage(firebaseUser.getUid(),userid, users.getImageURl());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String msg=edt_message.getText().toString();
                if (!msg.equals("")){
                    sendMessage(firebaseUser.getUid(),userid,msg);
                }
                else {
                    Toast.makeText(MessagesActivity.this,"PLease type some thing to send",Toast.LENGTH_SHORT).show();
                }
                edt_message.setText("");
            }
        });
        seenMessage(userid);
    }
    private void seenMessage(String id){
        databaseReference=FirebaseDatabase.getInstance().getReference("chats");
        valueEventListener=databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot dataSnapshot1:dataSnapshot.getChildren()){
                    Chat chat= dataSnapshot1.getValue(Chat.class);
                    assert chat != null;
                    if (chat.getReceiver().equals(firebaseUser.getUid()) && chat.getSender().equals(id)){
                        HashMap<String,Object>hashMap=new HashMap<>();
                        hashMap.put("isseen",true);
                        dataSnapshot1.getRef().updateChildren(hashMap);

                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void sendMessage(String sender,String receiver,String message){
        DatabaseReference reference= FirebaseDatabase.getInstance().getReference();

        HashMap<String, Object>hashMap= new HashMap<>();
        hashMap.put("sender",sender);
        hashMap.put("receiver",receiver);
        hashMap.put("message",message);
        hashMap.put("isseen",false);

        reference.child("Chats").push().setValue(hashMap);




    }

    private void readMessage (final String myId, final String userId, final String imageUrl){
        mChat= new ArrayList<>();
        databaseReference =  FirebaseDatabase.getInstance().getReference("Chats");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mChat.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    Chat chat= snapshot.getValue(Chat.class);
                    assert chat != null;

                    if (chat.getReceiver().equals(myId)&&chat.getSender().equals(userId) || chat.getReceiver().equals(userId) && chat.getSender().equals(myId))
                        mChat.add(chat);

                    messageAdapter =  new MessageAdapter(MessagesActivity.this,mChat,imageUrl);
                    recyclerView.setAdapter(messageAdapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        databaseReference.removeEventListener(valueEventListener);
    }
}
