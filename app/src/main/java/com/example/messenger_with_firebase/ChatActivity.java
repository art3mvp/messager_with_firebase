package com.example.messenger_with_firebase;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
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
    private ChatViewModel viewModel;
    private ChatViewModelFactory viewModelFactory;
    private String newText;

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

        viewModelFactory = new ChatViewModelFactory(currentUserId, otherUserId);
        viewModel = new ViewModelProvider(this, viewModelFactory).get(ChatViewModel.class);

        adapter = new MessagesAdapter(currentUserId);
        recyclerViewMessages.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
        recyclerViewMessages.setAdapter(adapter);

        observeViewModel();

        imageViewSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                newText = editTextNewMessage.getText().toString().trim();
                if (!newText.isEmpty()) {
                    Message message = new Message(newText, currentUserId, otherUserId);
                    viewModel.sendMessage(message);
                }
            }
        });
    }

    private void observeViewModel() {
        viewModel.getMessages().observe(this, new Observer<List<Message>>() {
            @Override
            public void onChanged(List<Message> messages) {
                adapter.setMessages(messages);
            }
        });

        viewModel.getError().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String errorMessage) {
                if (errorMessage != null) {
                    Toast.makeText(
                            ChatActivity.this,
                            errorMessage,
                            Toast.LENGTH_SHORT
                    ).show();
                }
            }
        });

        viewModel.getMessageSent().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean sent) {
                if (sent != null && sent) {
                    editTextNewMessage.setText("");
                }
            }
        });

        viewModel.getOtherUser().observe(this, new Observer<User>() {
            @Override
            public void onChanged(User user) {
                String userInfo = String.format("%s %s", user.getName(), user.getSurname());
                textViewTitle.setText(userInfo);
            }
        });
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