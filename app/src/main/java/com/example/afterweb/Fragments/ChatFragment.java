package com.example.afterweb.Fragments;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.afterweb.Adapter.UserAdapter;
import com.example.afterweb.Model.Chat;
import com.example.afterweb.Model.Users;
import com.example.afterweb.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ChatFragment extends Fragment {
    private RecyclerView recyclerView;
    private UserAdapter userAdapter;
    private List<Users> mUsers;
    private List<String> userList;
    private String notificationUserID;
    private String lastUserName;
    private String lastMessage;
    
    public  final  String CHANNEL_ID ="001";
    FirebaseUser firebaseUser;
    List<Chat> mChat;
    DatabaseReference databaseReference;
    

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =inflater.inflate(R.layout.fragment_chat, container, false);

        recyclerView=view.findViewById(R.id.recycler_view_chat);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        firebaseUser= FirebaseAuth.getInstance().getCurrentUser();

        userList=new ArrayList<>();
        mUsers=new ArrayList<>();
        databaseReference = FirebaseDatabase.getInstance().getReference("Chats");

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                userList.clear();
                for (DataSnapshot snapshot:dataSnapshot.getChildren()){
                    Chat chat = snapshot.getValue(Chat.class);

                    assert chat != null;
                    if(chat.getSender().equals(firebaseUser.getUid())){
                        userList.add(chat.getReceiver());
                    }
                    if (chat.getReceiver().equals(firebaseUser.getUid())){
                        userList.add(chat.getSender());
                    }
                }
                radChats();



            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        return view;



    }

    private void checkfornotification(List<Users> usersfornot) {

        List<Users>mymUsers=usersfornot;




        for (Users users: mymUsers){
            notificationUserID=users.getId();
            lastUserName=users.getUsername();
            getChatFromUser(notificationUserID,lastUserName);
        }






     /*   String lastChat = notificationList.get(mymUsers.size() - 1);
        Toast.makeText(getActivity(),lastChat, Toast.LENGTH_LONG).show();
        String lastChat1 = mymUsers.get(0).getId();
        Toast.makeText(getActivity(),lastChat1, Toast.LENGTH_LONG).show();*/
                    /*if(!mChat.isEmpty()) {
                        String lastChat = mChat.get(mChat.size() - 1).getMessage();
                        Toast.makeText(getActivity(), lastChat, Toast.LENGTH_LONG).show();
                    }
                    else
                        Toast.makeText(getActivity(), "empty", Toast.LENGTH_LONG).show();*/
            }

    private void getChatFromUser(String idForUser,String usserName) {

        firebaseUser= FirebaseAuth.getInstance().getCurrentUser();
        assert firebaseUser != null;
        final String myId=firebaseUser.getUid();

        mChat= new ArrayList<>();
        databaseReference =  FirebaseDatabase.getInstance().getReference("Chats");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

              //  for (String usersId : notificationList) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        Chat chat = snapshot.getValue(Chat.class);
                        assert chat != null;

                        if (chat.getReceiver().equals(myId) && chat.getSender().equals(idForUser))
                            mChat.add(chat);
                               /* if (mChat.isEmpty())
                                Toast.makeText(getActivity(),"somethingwrong", Toast.LENGTH_LONG).show();
                                else
                                    Toast.makeText(getActivity(),"starting to getbig", Toast.LENGTH_LONG).show();*/

                    }

             //   }
                if (!mChat.isEmpty()){
                    String  lastChat=mChat.get(mChat.size()-1).getMessage();
                    if (!lastChat.equals(lastMessage)) {

                        Toast.makeText(getActivity(), lastChat, Toast.LENGTH_LONG).show();
                        postnotification(usserName, lastChat);

                    }

                }
                //else
                //  Toast.makeText(getActivity(), "empty", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }


    private void postnotification(String user,String content ) {
       /* NotificationCompat.Builder builder=new NotificationCompat.Builder(getActivity()).
                setSmallIcon(R.mipmap.ic_launcher).setContentTitle("Custom Chat App").setContentText(notification);

        Intent notificationInntane= new Intent(getContext(),MainOne.class);
        PendingIntent contentIntent=PendingIntent.getActivity(getActivity(),0,notificationInntane,PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(contentIntent);

        NotificationManager manager=(NotificationManager)getLayoutInflater().getContext().getSystemService(Context.NOTIFICATION_SERVICE);
        manager.notify(0,builder.build());*/



            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                //NotificationManager notification = ( NotificationManager ) getActivity().getSystemService( getActivity().NOTIFICATION_SERVICE );
                NotificationChannel notificationChannel = new NotificationChannel(CHANNEL_ID, "001", NotificationManager.IMPORTANCE_DEFAULT);
                notificationChannel.setDescription("description");

                NotificationManager notificationManager = (NotificationManager) Objects.requireNonNull(getActivity()).getSystemService(Context.NOTIFICATION_SERVICE);
                notificationManager.createNotificationChannel(notificationChannel);

                Notification.Builder builder = new Notification.Builder(getContext(), CHANNEL_ID);
                builder.setSmallIcon(R.mipmap.ic_launcher).setContentText(content).setContentTitle(user).setPriority(Notification.PRIORITY_DEFAULT);

                NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(Objects.requireNonNull(getContext()));
                notificationManagerCompat.notify(001, builder.build());


            }
            else
            {
                Notification.Builder builder = new Notification.Builder(getContext());
                builder.setSmallIcon(R.mipmap.ic_launcher).setContentText(content).setContentTitle(user).setPriority(Notification.PRIORITY_DEFAULT);


                NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(Objects.requireNonNull(getContext()));
                notificationManagerCompat.notify(001, builder.build());

            }




    }


    private void radChats() {


        databaseReference=FirebaseDatabase.getInstance().getReference("Users");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mUsers.clear();

                for (DataSnapshot snapshot:dataSnapshot.getChildren()){
                    Users users=snapshot.getValue(Users.class);
                        for (String id : userList){
                            assert users != null;
                            if (users.getId().equals(id)){

                                if (mUsers.size() !=0){
                                    //for (Users users1 :mUsers){
                                   if (!mUsers.contains(users)){
                                        mUsers.add(users);
                                   }
                                }
                                else {
                                    mUsers.add(users);
                                }
                            }
                        }
                }
               // cleanAllDuplicated(mUsers);
                checkfornotification(mUsers);
                userAdapter= new UserAdapter(getContext(),mUsers);
                recyclerView.setAdapter(userAdapter);
            }



            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



    }


}
