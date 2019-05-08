package com.example.afterweb.Fragments;

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

import com.example.afterweb.Adapter.UserAdapter;
import com.example.afterweb.Model.Users;
import com.example.afterweb.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class UsersFragment extends Fragment {

        private RecyclerView recyclerView;
        private UserAdapter userAdapter;
        private List<Users> mUsers;
        private EditText edt_search;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_users, container, false);
        recyclerView=view.findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        edt_search=view.findViewById(R.id.edt_search);
        edt_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            searchUsers(s.toString());

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });



        mUsers = new ArrayList<>();
        readUsers();
        return view;
    }

    private void searchUsers(String toString) {
        FirebaseUser firebaseUser=FirebaseAuth.getInstance().getCurrentUser();
        Query query = FirebaseDatabase.getInstance().getReference("Users").orderByChild("username").startAt(toString).endAt(toString+"\uf8ff");
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mUsers.clear();
                for (DataSnapshot dataSnapshot1:dataSnapshot.getChildren()){
                    Users users=dataSnapshot1.getValue(Users.class);

                    assert users != null;
                    assert firebaseUser != null;
                    if(!users.getId().equals(firebaseUser.getUid())){
                        mUsers.add(users);
                    }

                }
                userAdapter= new UserAdapter(getContext(),mUsers);
                recyclerView.setAdapter(userAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

    private void readUsers() {
        final FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users");
        if (edt_search.getText().toString().equals("")) {

            reference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (edt_search.getText().toString().equals("")) {

                        mUsers.clear();
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            Users users = snapshot.getValue(Users.class);

                            assert users != null;
                            assert firebaseUser != null;
                            if (!users.getId().equals(firebaseUser.getUid())) {
                                mUsers.add(users);
                            }

                        }

                        userAdapter = new UserAdapter(getContext(), mUsers);
                        recyclerView.setAdapter(userAdapter);

                    }
                }


                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }


    }}
