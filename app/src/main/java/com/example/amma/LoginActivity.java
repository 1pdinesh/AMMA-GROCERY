package com.example.amma;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.amma.Info.Info;
import com.example.amma.Model.Users;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import io.paperdb.Paper;
import com.rey.material.widget.CheckBox;
import android.widget.TextView;




public class LoginActivity extends AppCompatActivity {

    private Button logbtn;
    private EditText hp, pass;
    private ProgressDialog load;
    private String ParentsDb="Users" ;
    private CheckBox rm;
    private TextView AdminLink, NotAdminLink;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        logbtn = findViewById(R.id.login_btn);
        hp = findViewById(R.id.login_phone_number_input);
        pass = findViewById(R.id.login_password_input);
        load = new ProgressDialog(this);
        rm = findViewById(R.id.remember_me_chkb);
        AdminLink = findViewById(R.id.admin_panel_link);
        NotAdminLink = findViewById(R.id.not_admin_panel_link);

        Paper.init(this);
        logbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginUser();
            }
        });


        AdminLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logbtn.setText("Login Admin");
                AdminLink.setVisibility(View.INVISIBLE);
                NotAdminLink.setVisibility(View.VISIBLE);
                ParentsDb = "Admin";


            }
        });

        NotAdminLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logbtn.setText("Login");
                AdminLink.setVisibility(View.VISIBLE);
                NotAdminLink.setVisibility(View.INVISIBLE);
                ParentsDb = "Users";
            }
        });


    }

    private void LoginUser() {
        String phone= hp.getText().toString();
        String password = pass.getText().toString();

        if (TextUtils.isEmpty( phone)) {
            Toast.makeText(this, "Please enter your phone number", Toast.LENGTH_SHORT).show();

        } else if (TextUtils.isEmpty(password)) {
            Toast.makeText(this, "enter your password", Toast.LENGTH_SHORT).show();
        } else {
            load.setTitle("Login in");
            load.setMessage("Please hold,we are checking your credentials");
            load.setCanceledOnTouchOutside(false);
            load.show();

            AllowAccess(phone, password);
        }

//jjj
    }
    private void AllowAccess(final String phone, final String password)
    {
        if(rm.isChecked())
        {
            Paper.book().write(Info.userPK, phone);
            Paper.book().write(Info.userPW, password);
        }



        final DatabaseReference RootRef;
        RootRef = FirebaseDatabase.getInstance().getReference();


        RootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                if (dataSnapshot.child(ParentsDb).child(phone).exists())
                {
                    Users usersData = dataSnapshot.child(ParentsDb).child(phone).getValue(Users.class);

                    if (usersData.getPhone().equals(phone))
                    {
                        if (usersData.getPassword().equals(password))
                        {
                            if (ParentsDb.equals("Admin"))
                            {
                                Toast.makeText(LoginActivity.this, "Welcome Admin", Toast.LENGTH_SHORT).show();
                                load.dismiss();


                                Intent intent = new Intent(LoginActivity.this, AdminCategory.class);

                                startActivity(intent);
                            }
                            else if (ParentsDb.equals("Users"))
                            {
                                Toast.makeText(LoginActivity.this, "logged in Successfully...", Toast.LENGTH_SHORT).show();
                                load.dismiss();

                                Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                                Info.currentonuser= usersData;
                                startActivity(intent);
                            }
                        }
                        else
                        {
                            load.dismiss();
                            Toast.makeText(LoginActivity.this, "Password is incorrect.", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
                else
                {
                    Toast.makeText(LoginActivity.this, "Account with this " + phone + " number do not exists.", Toast.LENGTH_SHORT).show();
                    load.dismiss();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}