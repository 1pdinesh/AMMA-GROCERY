package com.example.amma;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.amma.Info.Info;
import com.example.amma.Model.Users;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import io.paperdb.Paper;

public class MainActivity extends AppCompatActivity {

    private Button jbut, logbt;
    private ProgressDialog load;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        load=new ProgressDialog(this);
        jbut=findViewById(R.id.main_join_now_btn);
        logbt=findViewById(R.id.main_login_btn);
        Paper.init(this);

        logbt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);

            }
        });



        jbut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent pass=new Intent(MainActivity.this, RegisterActivity.class);
                startActivity(pass);
            }
        });


        String phone=Paper.book().read(Info.userPK);
        String password=Paper.book().read(Info.userPW);


        if(phone !="" && password!="")
        {
            if (!TextUtils.isEmpty(phone) && !TextUtils.isEmpty(password))
            {
                AllowAccess(phone,password);

                load.setTitle("Already Logged In");
                load.setMessage("Just A Moment");
                load.setCanceledOnTouchOutside(false);
                load.show();
            }
        }

    }

    private void AllowAccess(final String phonenum, final String pw)
    {
        final DatabaseReference RootRef;
        RootRef = FirebaseDatabase.getInstance().getReference();
        RootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.child("Users").child(phonenum).exists()) {
                    Users users = dataSnapshot.child("Users").child(phonenum).getValue(Users.class);
                    if (users.getPhone().equals(phonenum)) {
                        if (users.getPassword().equals(pw)) {
                            Toast.makeText(MainActivity.this, "Please wait, you are already logged in...", Toast.LENGTH_SHORT).show();
                            load.dismiss();
                            Intent intent = new Intent(MainActivity.this, HomeActivity.class);
                            Info.currentonuser = users;
                            startActivity(intent);
                        } else {
                            load.dismiss();
                            Toast.makeText(MainActivity.this, "Password is incorrect", Toast.LENGTH_SHORT).show();
                        }


                    }

                }

                if(dataSnapshot.child("Admin").child(phonenum).exists()){
                    Users users=dataSnapshot.child("Admin").child(phonenum).getValue(Users.class);
                    if(users.getPhone().equals(phonenum))
                    {
                        if(users.getPassword().equals(pw))
                        {
                            Toast.makeText(MainActivity.this,"Login Sucessful",Toast.LENGTH_SHORT).show();
                            load.dismiss();
                            Intent intent=new Intent(MainActivity.this,AdminCategory.class);

                            startActivity(intent);
                        }
                        else
                        {
                            load.dismiss();
                            Toast.makeText(MainActivity.this,"Password is incorrect",Toast.LENGTH_SHORT).show();
                        }


                    }

                }
                else
                {
                    Toast.makeText(MainActivity.this,"Account with this "+phonenum+ "does not exist",Toast.LENGTH_SHORT).show();
                    load.dismiss();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
