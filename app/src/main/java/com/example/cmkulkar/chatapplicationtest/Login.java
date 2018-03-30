package com.example.cmkulkar.chatapplicationtest;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.Map;

public class Login extends AppCompatActivity {

    EditText email;
    EditText password;
    Button login;
    TextView errorMsg;
    FireBaseDatabaseConstants databaseConstants;
    String userID,passwd = null;
    String userEnteredEmail,userEnteredPasswd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        email = (EditText) findViewById(R.id.EmailET);
        password = (EditText) findViewById(R.id.PasswordET);
        login = (Button) findViewById(R.id.btnLogin);
        errorMsg = (TextView) findViewById(R.id.errorMessage);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userEnteredEmail = email.getText().toString();
                userEnteredPasswd = password.getText().toString();

                databaseConstants.setDatabaseReference("Users");
                databaseConstants.getDatabaseReference().addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot data : dataSnapshot.getChildren()) {
                            userID = data.getKey().toString();
                            if (userID.equals(userEnteredEmail)) {
                                passwd = data.child("Password").getValue().toString();
                            }
                        }
                        Toast.makeText(getApplicationContext(),"Inside Cloud Function",Toast.LENGTH_SHORT);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
                if (passwd == null) {
                    errorMsg.setText("User not present");
                }
                else if (passwd.equals(userEnteredPasswd)) {
                    Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                    startActivity(intent);
                }
                else {
                    errorMsg.setText("Invalid Password");
                }
            }
        });


    }
}
