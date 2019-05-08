package com.example.afterweb;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.afterweb.Fragments.ChatFragment;
import com.example.afterweb.Fragments.ProfileFragment;
import com.example.afterweb.Fragments.UsersFragment;
import com.example.afterweb.Model.Users;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainOne extends AppCompatActivity {
    private CircleImageView pro_image;
    private TextView username;

    @Override protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main_one);

        final Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar()!= null) {
            getSupportActionBar().setTitle("");
        }

        pro_image = findViewById(R.id.tool_profile_img);
        username = findViewById(R.id.tool_username);

        final FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        final DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users").child(Objects.requireNonNull(firebaseUser).getUid());

        reference.addValueEventListener(new ValueEventListener() {
            @Override public void onCancelled(@NonNull final DatabaseError databaseError) {}
            @Override public void onDataChange(@NonNull final DataSnapshot dataSnapshot) {
                final Users user = dataSnapshot.getValue(Users.class);
                username.setText(Objects.requireNonNull(user).getUsername());
                if (user.getImageURl() != null && !user.getImageURl().equals("default")){
                    Glide.with(MainOne.this).load(user.getImageURl()).into(pro_image);
                } else {
                    pro_image.setImageResource(R.mipmap.ic_launcher);
                }
            }
        });
        TabLayout tabLayout=findViewById(R.id.tab_layout);
        ViewPager viewPager=findViewById(R.id.view_page);

        ViewPagerAdapter viewPagerAdapter= new ViewPagerAdapter(getSupportFragmentManager());
        ViewPagerAdapter.addFragment(new ChatFragment(),getString(R.string.firstTab))  ;
        ViewPagerAdapter.addFragment(new UsersFragment(),getString(R.string.secondTab))  ;
        ViewPagerAdapter.addFragment(new ProfileFragment(),getString(R.string.thirdTab))  ;


        viewPager.setAdapter(viewPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);


    }

    @Override public boolean onCreateOptionsMenu(final Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override public boolean onOptionsItemSelected(final MenuItem item) {
        switch (item.getItemId()){
            case R.id.menu_logout:
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(MainOne.this,MainActivity.class));
                finish();
                return true;
        }
        return false;
    }

    static class ViewPagerAdapter extends FragmentPagerAdapter {

        private static ArrayList<Fragment> fragments;
        private static ArrayList<String> titles;

        ViewPagerAdapter(FragmentManager fm){
            super(fm);
            fragments = new ArrayList<>();
            titles = new ArrayList<>();

        }

        @Override
        public Fragment getItem(int i) {
            return fragments.get(i);
        }

        @Override
        public int getCount() {
            return fragments.size();
        }

        public static void addFragment(Fragment fragment, String title){
            fragments.add(fragment);
            titles.add(title);

        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            return titles.get(position);

        }
    }
}