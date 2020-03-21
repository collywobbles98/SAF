package com.colwyn.saf;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.colwyn.saf.model.MessageItem;
import com.colwyn.saf.ui.MessageItemRecyclerAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.StorageReference;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class messages extends AppCompatActivity {

    //---Firebase & Firestore---//
    private StorageReference mStorageRef;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

    //Firestore Collection Reference
    private CollectionReference collectionReference = db.collection("messages");

    private List<MessageItem> MessageItemList;
    private RecyclerView messagesRecyclerView;
    private MessageItemRecyclerAdapter messageItemRecyclerAdapter;
    private TextView noItemsTextView;
    private ImageView noItemsImageView;
    private TextView itemCountTextView;
    String userIDFB = user.getUid();

    //---Declare Widgets---//
    EditText messageEditText;
    ImageButton sendImageButton;
    TextView otherUserTextview;

    //---Back Button---//
    public void backToChatClicked (View view){
        userData.chatOtherUser_Global = null;
        startActivity(new Intent(messages.this, chats.class).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.messages);

        //---Get Widgets---//
        otherUserTextview = findViewById(R.id.otherUserTextView);
        sendImageButton = findViewById(R.id.sendImageButton);
        messageEditText = findViewById(R.id.messageEditText);
        MessageItemList = new ArrayList<>();
        messagesRecyclerView = findViewById(R.id.messagesRecyclerView);
        messagesRecyclerView.setHasFixedSize(true);
        messagesRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        //---Display Info---//
        otherUserTextview.setText(userData.chatOtherUser_Global);

        //---Send Button---//
        sendImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (TextUtils.isEmpty(messageEditText.getText().toString().trim())){
                    //No Message to send
                    //Do Nothing
                }
                else {
                    //Send Message
                    // Add a new document with a generated id.
                    Map<String, Object> messages = new HashMap<>();
                    messages.put("Sender", user.getUid());
                    messages.put("Reciever", userData.chatOtherUserID_Global);
                    messages.put("ChatID", userData.chatClicked_Global);
                    messages.put("Message", messageEditText.getText().toString().trim());
                    SimpleDateFormat formatter= new SimpleDateFormat("HH:mm:ss 'at' yyyy-MM-dd");
                    Date date = new Date(System.currentTimeMillis());
                    messages.put("TimeStamp", date.toString().trim());

                    db.collection("messages")
                            .add(messages)
                            .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                @Override
                                public void onSuccess(DocumentReference documentReference) {
                                    //Log.d(TAG, "DocumentSnapshot written with ID: " + documentReference.getId());
                                    //Save Last Chat Message
                                    DocumentReference currencyRef = db.collection("chats").document(userData.chatClicked_Global);
                                    currencyRef
                                            .update("LastMessage", messageEditText.getText().toString().trim())
                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {
                                                    //Log.d(TAG, "DocumentSnapshot successfully updated!");
                                                }
                                            })
                                            .addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    //Log.w(TAG, "Error updating document", e);
                                                }
                                            });

                                    messageEditText.setText("");
                                    getMessages();
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    //Log.w(TAG, "Error adding document", e);
                                    Toast.makeText(messages.this, "Message not sent.", Toast.LENGTH_SHORT).show();
                                    messageEditText.setText("");
                                }
                            });

                }
            }
        });

        //---Listen for incoming messages---//
        db.collection("messages")
                .whereEqualTo("ChatID", userData.chatClicked_Global)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value,
                                        @Nullable FirebaseFirestoreException e) {
                        if (e != null) {
                            //Log.w(TAG, "Listen failed.", e);
                            return;
                        }

                        for (QueryDocumentSnapshot doc : value) {
                            //if (doc.get("name") != null) {
                                //cities.add(doc.getString("name"));
                            //}
                            getMessages();
                        }
                        //Log.d(TAG, "Current cites in CA: " + cities);
                    }
                });




    }

    @Override
    protected void onStart(){
        super.onStart();

        getMessages();

    }

    private void getMessages(){
        //---Query to get items of current user---//
        collectionReference.orderBy("TimeStamp", Query.Direction.ASCENDING).whereEqualTo("ChatID", userData.chatClicked_Global)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            //Check if the query is not empty
                            if (!task.getResult().isEmpty()) {

                                //Clear List and reload
                                MessageItemList.clear();
                                for (QueryDocumentSnapshot document : task.getResult()) {

                                    //Get Document ID
                                    String docID= document.getId();

                                    MessageItem messageItem = document.toObject(MessageItem.class).withId(docID);
                                    MessageItemList.add(messageItem);
                                }

                                //Sort Array List to order by Timestamp
//                                Collections.sort(MessageItemList, new Comparator<MessageItem>() {
//                                    @Override
//                                    public int compare(MessageItem o1, MessageItem o2) {
//                                        return o1.getTimeStamp().compareTo(o2.getTimeStamp());
//                                    }
//                                    MessageItemRecyclerAdapter();
//                                });

                                //Invoke Recycler View
                                messageItemRecyclerAdapter = new MessageItemRecyclerAdapter(getApplicationContext(), MessageItemList);
                                messagesRecyclerView.setAdapter(messageItemRecyclerAdapter);
                                messageItemRecyclerAdapter.notifyDataSetChanged();
                                messagesRecyclerView.scrollToPosition(MessageItemList.size() - 1);

                            }
                            else {
                                //Display NoItem TextView and ImageView
                                //noItemsTextView.setVisibility(View.VISIBLE);
                                //noItemsImageView.setVisibility(View.VISIBLE);

                            }

                        } else {
                            //Log.d(TAG, "Error getting documents: ", task.getException());
                        }

                    }

                });
    }
}
