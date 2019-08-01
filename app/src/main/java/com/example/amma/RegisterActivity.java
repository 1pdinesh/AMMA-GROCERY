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
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class RegisterActivity extends AppCompatActivity {

    private Button regbtn;
    private EditText name, hpnum, pass;
    private ProgressDialog load;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        regbtn = (Button) findViewById(R.id.register_btn);
        name = (EditText) findViewById(R.id.register_username_input);
        hpnum = (EditText) findViewById(R.id.register_phone_number_input);
        pass = (EditText) findViewById(R.id.register_password_input);
        load = new ProgressDialog(this);

        regbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CreateAccount();
            }
        });



    }

    private void CreateAccount() {
        String n = name.getText().toString();
        String hp = hpnum.getText().toString();
        String pw = pass.getText().toString();

        if (TextUtils.isEmpty(n)) {
            Toast.makeText(this, "Please enter your name", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(hp)) {
            Toast.makeText(this, "Please enter your phone number", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(pw)) {
            Toast.makeText(this, "Please enter your password", Toast.LENGTH_SHORT).show();
        } else {
            load.setTitle("Create Account");
            load.setMessage("Please hold,we are checking your credentials");
            load.setCanceledOnTouchOutside(false);
            load.show();

            Validate(n, hp, pw);

        }


    }

    private void Validate(final String n, final String hp, final String pw) {
        final DatabaseReference RootRef;
        RootRef = FirebaseDatabase.getInstance().getReference();
        RootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (!(dataSnapshot.child("Users").child(hp).exists())) {
                    HashMap<String, Object> userdataMap = new HashMap<>();
                    userdataMap.put("phone", hp);
                    userdataMap.put("password", pw);
                    userdataMap.put("name", n);

                    RootRef.child("Users").child(hp).updateChildren(userdataMap)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(RegisterActivity.this, "Congratulations, your account has been created.", Toast.LENGTH_SHORT).show();
                                        load.dismiss();

                                        Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                                        startActivity(intent);
                                    } else {
                                        load.dismiss();
                                        Toast.makeText(RegisterActivity.this, "Network Error: Please try again after some time...", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                } else {
                    Toast.makeText(RegisterActivity.this, "This " + hp + " already exists.", Toast.LENGTH_SHORT).show();
                    load.dismiss();
                    Toast.makeText(RegisterActivity.this, "Please try again using another phone number.", Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                    startActivity(intent);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
}
