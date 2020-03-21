package com.colwyn.saf;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.colwyn.saf.model.ChatItem;
import com.colwyn.saf.ui.ChatItemRecyclerAdapter;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

public class chats extends AppCompatActivity {

    //---Firebase & Firestore---//
    private StorageReference mStorageRef;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

    //Firestore Collection Reference
    private CollectionReference collectionReference = db.collection("chats");

    private List<ChatItem> ChatItemList;
    private RecyclerView chatItemRecyclerView;
    private ChatItemRecyclerAdapter chatItemRecyclerAdapter;
    private TextView noChatsTextView;
    private ImageView noChatsImageView;
    private SwipeRefreshLayout swipeRefreshLayout;

    //---Navigation---//
    //Selling Activity
    public void sellingClicked(View view) {

        startActivity(new Intent(chats.this, Selling.class).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION));
    }

    //Basket Activity
    public void basketClicked(View view) {

        startActivity(new Intent(chats.this, basket.class).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION));
    }

    //profile activity
    public void profileClicked(View view) {
        startActivity(new Intent(chats.this, Profile.class).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION));
    }

    //Home Activity
    public void homeClicked(View view) {
        startActivity(new Intent(chats.this, MainActivity.class).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION));
    }

    //Messages Activity
    public void messagesClicked(View view) {
        startActivity(new Intent(chats.this, chats.class).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chats);
        //get Widgets
        swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout);

        //Recycler View
        ChatItemList = new ArrayList<>();
        chatItemRecyclerView = findViewById(R.id.chatItemRecyclerView);
        chatItemRecyclerView.setHasFixedSize(true);
        chatItemRecyclerView.setLayoutManager(new GridLayoutManager(this,1));


        //Swipe to refresh
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadChats();

                chatItemRecyclerAdapter.notifyDataSetChanged();
                swipeRefreshLayout.setRefreshing(false);
            }
        });



    }

    @Override
    protected void onStart() {
        super.onStart();

    loadChats();

    }

    //---MakeShift OR Query---//
    public void loadChats(){
        ChatItemList.clear();

        //Task 1 (If user is user1)
        Task task1 = collectionReference.whereEqualTo("User1", user.getUid()).orderBy("TimeStamp", Query.Direction.DESCENDING).get();
        //Task 2 (If user is user2)
        Task task2 = collectionReference.whereEqualTo("User2", user.getUid()).orderBy("TimeStamp", Query.Direction.DESCENDING).get();

        Task<List<QuerySnapshot>> allTasks = Tasks.whenAllSuccess(task1, task2);
        allTasks.addOnSuccessListener(new OnSuccessListener<List<QuerySnapshot>>() {
            @Override
            public void onSuccess(List<QuerySnapshot> querySnapshots) {
                String data = "";

                for (QuerySnapshot queryDocumentSnapshots : querySnapshots) {
                    for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                        //Get Document ID

                                    ChatItem chatItem = documentSnapshot.toObject(ChatItem.class).withId(documentSnapshot.getId());
                                    ChatItemList.add(chatItem);
                    }

                    if (ChatItemList.isEmpty()){
                        //Display no Chats textview and imageview
                        noChatsImageView = findViewById(R.id.noChatsImageView);
                        noChatsTextView = findViewById(R.id.noChatsTextView);
                        noChatsImageView.setVisibility(View.VISIBLE);
                        noChatsTextView.setVisibility(View.VISIBLE);

                    }
                    else{
                        //Invoke Recycler View
                        noChatsImageView = findViewById(R.id.noChatsImageView);
                        noChatsTextView = findViewById(R.id.noChatsTextView);
                        noChatsImageView.setVisibility(View.GONE);
                        noChatsTextView.setVisibility(View.GONE);
                        chatItemRecyclerAdapter = new ChatItemRecyclerAdapter(getApplicationContext(), ChatItemList);
                        chatItemRecyclerView.setAdapter(chatItemRecyclerAdapter);
                        chatItemRecyclerAdapter.notifyDataSetChanged();
                    }
                }
            }
        });



    }


}
