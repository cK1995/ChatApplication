package com.example.cmkulkar.chatapplicationtest;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ChatScreen extends AppCompatActivity {

    TextView textView;
    TextView messageTextView;
    Button sendButton;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference dbRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_screen);

        textView = (TextView) findViewById(R.id.chatDetails);
        textView.setText(MainActivity.chatPersonName);
        sendButton = (Button) findViewById(R.id.btnSend);
        messageTextView = (TextView) findViewById(R.id.message);
        dbRef = database.getReference("Chats");

        messageTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                messageTextView.setText("");
            }
        });

        
    }
}
