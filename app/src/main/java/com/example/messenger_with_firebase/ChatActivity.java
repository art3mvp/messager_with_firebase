package com.example.messenger_with_firebase;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.checkerframework.checker.units.qual.A;

import java.util.ArrayList;
import java.util.List;

public class ChatActivity extends AppCompatActivity {

    private EditText editTextNewMessage;
    private ImageView imageViewChatStatus;
    private RecyclerView recyclerViewMessages;
    private ImageView imageViewSend;
    private TextView textViewTitle;
    private MessagesAdapter adapter;
    private String currentUserId;
    private String otherUserId;
    private static final String EXTRA_CURRENT_USER_ID = "current_id";
    private static final String EXTRA_OTHER_USER_ID = "other_id";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_chat);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        initViews();
        Intent intent = getIntent();
        currentUserId = intent.getStringExtra(EXTRA_CURRENT_USER_ID);
        otherUserId = intent.getStringExtra(EXTRA_OTHER_USER_ID);
        adapter = new MessagesAdapter(currentUserId);
        recyclerViewMessages.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
        recyclerViewMessages.setAdapter(adapter);
        List<Message> messageList = new ArrayList<>();

        textViewTitle.setText(otherUserId);

        for (int i = 0; i < 10; i++) {
            if (i % 2 == 0) {
                messageList.add(new Message("message" + i, otherUserId, currentUserId));
            } else {
                messageList.add(new Message("message" + i, currentUserId, otherUserId));
            }
        }
        adapter.setMessages(messageList);
    }

    private void initViews() {
        editTextNewMessage = findViewById(R.id.editTextNewMessage);
        imageViewChatStatus = findViewById(R.id.imageViewChatStatus);
        recyclerViewMessages = findViewById(R.id.recyclerViewMessages);
        imageViewSend = findViewById(R.id.imageViewSend);
        textViewTitle = findViewById(R.id.textViewTitle);

    }

    public static Intent newIntent(Context context, String currentUserId, String otherUserId) {
        Intent intent = new Intent(context, ChatActivity.class);
        intent.putExtra(EXTRA_CURRENT_USER_ID, currentUserId);
        intent.putExtra(EXTRA_OTHER_USER_ID, otherUserId);
        return intent;
    }
}