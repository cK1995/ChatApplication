package com.example.cmkulkar.chatapplicationtest;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ChatScreen extends AppCompatActivity {

    TextView textView;
    TextView messageTextView;
    Button sendButton;
    ListView messageList;
    DatabaseReference dbRef;
    FireBaseDatabaseConstants databaseConstants;
    ArrayAdapter<String> messagesReceived;
    private String messageId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_screen);

        textView = (TextView) findViewById(R.id.chatDetails);
        textView.setText(MainActivity.chatPersonName);
        sendButton = (Button) findViewById(R.id.btnSend);
        messageTextView = (TextView) findViewById(R.id.message);
        messageList = (ListView) findViewById(R.id.messageListView);
        databaseConstants = new FireBaseDatabaseConstants();
        databaseConstants.setDatabaseReference();
        dbRef = databaseConstants.getDatabaseReference().child("Users").child(Login.loggedInAs).child("Chats").child(MainActivity.chatPersonName);
        messagesReceived = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1);
        messageList.setAdapter(messagesReceived);

        messageTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                messageTextView.setText("");
            }
        });

        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                messagesReceived.clear();
                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    if (data.getKey().toString().equals("messageId")) {
                        messageId = data.getValue().toString();
                    }
                    else {
                        String temp = data.getValue().toString();
                        String[] s = temp.split(":");
                        messagesReceived.add(s[1]);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(getApplicationContext(),"Database connection rejected",Toast.LENGTH_SHORT);
            }
        });

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dbRef.child(messageId).setValue("To:" + messageTextView.getText().toString());
                int i = Integer.parseInt(messageId);
                i--;
                dbRef.child("messageId").setValue(Integer.toString(i));
            }
        });
    }
}
