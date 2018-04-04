package com.example.cmkulkar.chatapplicationtest;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
import com.google.firebase.database.FirebaseDatabase;
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
    public static boolean isCloudExecuted = false;
    //public static String loggedInAs;
    SharedPreferences sp;
    SharedPreferences.Editor spEditor;

    final FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference dbRef = database.getReference().child("Users");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        email = (EditText) findViewById(R.id.EmailET);
        password = (EditText) findViewById(R.id.PasswordET);
        login = (Button) findViewById(R.id.btnLogin);
        errorMsg = (TextView) findViewById(R.id.errorMessage);
        databaseConstants = new FireBaseDatabaseConstants();
        sp = this.getSharedPreferences("MyPref", Context.MODE_PRIVATE);
        spEditor = sp.edit();

        Boolean islogged = sp.getBoolean("isUserLoggedIn",false);

        errorMsg.setText("The Logged In value is " + islogged);

        if (islogged) {
            Intent intent = new Intent(this,MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        }

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userEnteredEmail = email.getText().toString();
                userEnteredPasswd = password.getText().toString();

                dbRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        isCloudExecuted = true;
                        for (DataSnapshot data : dataSnapshot.getChildren()) {
                            userID = data.getKey().toString();
                            if (userID.equals(userEnteredEmail)) {
                                passwd = data.child("AccountInfo").child("Password").getValue().toString();
                            }
                        }
                        Toast.makeText(getApplicationContext(), "Inside Cloud Function", Toast.LENGTH_SHORT);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        errorMsg.setText("Database Connection cancelled");
                    }
                });
                if (isCloudExecuted == false) {
                    errorMsg.setText("Press Login button again");
                }
                else if (passwd == null) {
                    errorMsg.setText("User not present");
                }
                else if (passwd.equals(userEnteredPasswd)) {
                    //loggedInAs = userEnteredEmail;
                    spEditor.putString("loggedInAs",userEnteredEmail);
                    spEditor.commit();
                    spEditor.putBoolean("isUserLoggedIn",true);
                    spEditor.commit();
                    Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    finish();
                }
                else {
                    errorMsg.setText("Invalid Password");
                }
            }
        });


    }
}
