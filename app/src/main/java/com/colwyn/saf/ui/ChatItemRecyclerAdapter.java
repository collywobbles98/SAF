package com.colwyn.saf.ui;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.colwyn.saf.R;
import com.colwyn.saf.chats;
import com.colwyn.saf.messages;
import com.colwyn.saf.model.ChatItem;
import com.colwyn.saf.userData;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.StorageReference;

import java.util.List;

public class ChatItemRecyclerAdapter extends RecyclerView.Adapter<ChatItemRecyclerAdapter.ViewHolder> {

    //---Firebase & Firestore---//
    private StorageReference mStorageRef;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

    //Add a context
    private Context context;

    private List<ChatItem> ChatItemList;


    public ChatItemRecyclerAdapter(Context context, List<ChatItem> chatItemList) {
        this.context = context;
        ChatItemList = chatItemList;
    }

    @NonNull
    @Override
    public ChatItemRecyclerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.chat_row, viewGroup,false);

        return new ChatItemRecyclerAdapter.ViewHolder(view, context);
    }

    @Override
    public void onBindViewHolder(@NonNull final ChatItemRecyclerAdapter.ViewHolder viewHolder, int position) {

        //Get ChatItem
        ChatItem chatItem = ChatItemList.get(position);

        //Query if user is user1 or user2
        DocumentReference docRef = db.collection("chats").document(ChatItemList.get(position).docID);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        //Retrieve data and store as string
                        String FSUser1= document.getString("User1");
                        String FSUser1FN= document.getString("User1FirstName");
                        String FSUser1LN= document.getString("User1LastName");
                        String FSUser1Initials= document.getString("User1Initials");

                        String FSUser2= document.getString("User2");
                        String FSUser2FN= document.getString("User2FirstName");
                        String FSUser2LN= document.getString("User2LastName");
                        String FSUser2Initials= document.getString("User2Initials");

                        String FSLastMessage = document.getString(("LastMessage"));

                        //Check which user it is
                        if (user.getUid().equals(FSUser1)){
                            //User1 is messaging user2 (Display user 2 username)
                            viewHolder.initialsTextView.setText(FSUser2Initials);
                            viewHolder.nameTextView.setText(FSUser2FN + " " + FSUser2LN);
                            viewHolder.lastMessageTextView.setText(FSLastMessage);
                            viewHolder.otherUserIDTextView.setText(FSUser2);
                        }
                        else if (user.getUid().equals(FSUser2)){
                            //User2 is messaging user1 (Display user 1 username)
                            viewHolder.initialsTextView.setText(FSUser1Initials);
                            viewHolder.nameTextView.setText(FSUser1FN + " " + FSUser1LN);
                            viewHolder.lastMessageTextView.setText(FSLastMessage);
                            viewHolder.otherUserIDTextView.setText(FSUser2);
                        }
                        else{

                        }

                    }
                    else {

                    }
                } else {
                    //Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });

        //Display Data
        //viewHolder.itemTitleTextView.setText(chatItem.getIte() + " X " + soldItem.getQuantity());
        //viewHolder.statusTextView.setText(soldItem.getStatus());

        //Get Document ID onclick
        final String documentID = ChatItemList.get(position).docID;

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(context, documentID, Toast.LENGTH_SHORT).show();

                //Store Document ID in Global Variable
                userData.chatClicked_Global = documentID;
                userData.chatOtherUser_Global = viewHolder.nameTextView.getText().toString();
                userData.chatOtherUserID_Global = viewHolder.otherUserIDTextView.getText().toString();


                //Move to listing view Acivity
                Intent intent = new Intent(context, messages.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);

            }
        });

        viewHolder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                db.collection("chats").document(documentID)
                        .delete()
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                //Delete Messages Too
                                final CollectionReference messagesRef = db.collection("messages");
                                Query query = messagesRef.whereEqualTo("ChatID", documentID);
                                query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                        if (task.isSuccessful()) {
                                            for (DocumentSnapshot document : task.getResult()) {
                                                messagesRef.document(document.getId()).delete();
                                            }
                                        } else {
                                            //Log.d(TAG, "Error getting documents: ", task.getException());
                                        }
                                    }
                                });


                                //Reload Activity
                                //Move to listing view Acivity
                                Intent intent = new Intent(context, chats.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                context.startActivity(intent);
                                Toast.makeText(context, "Chat Deleted", Toast.LENGTH_SHORT).show();




                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(context, "Chat Can't be removed at this time.", Toast.LENGTH_SHORT).show();
                            }
                        });
                return false;
            }

        });
    }

    @Override
    public int getItemCount() {

        return ChatItemList.size();

    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        //Widgets
        public TextView initialsTextView, nameTextView, lastMessageTextView, otherUserIDTextView;

        public ViewHolder(@NonNull View itemView, Context ctx) {
            super(itemView);
            //Get Context
            context = ctx;

            //Get Widgets
            initialsTextView = itemView.findViewById(R.id.initialsTextView);
            nameTextView = itemView.findViewById(R.id.nameTextView);
            lastMessageTextView = itemView.findViewById(R.id.lastMessageTextView);
            otherUserIDTextView = itemView.findViewById(R.id.otherUserIDTextView);


        }
    }
}
