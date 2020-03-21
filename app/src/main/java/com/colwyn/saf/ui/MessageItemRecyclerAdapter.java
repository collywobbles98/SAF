package com.colwyn.saf.ui;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.colwyn.saf.R;
import com.colwyn.saf.model.MessageItem;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.StorageReference;

import java.util.List;

import static android.view.Gravity.RIGHT;

public class MessageItemRecyclerAdapter extends RecyclerView.Adapter<MessageItemRecyclerAdapter.ViewHolder> {

    //---Firebase & Firestore---//
    private StorageReference mStorageRef;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

    //Add a context
    private Context context;

    private List<MessageItem> MessageItemList;


    public MessageItemRecyclerAdapter(Context context, List<MessageItem> messageItemList) {
        this.context = context;
        MessageItemList = messageItemList;
    }

    @NonNull
    @Override
    public MessageItemRecyclerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.message_row, viewGroup,false);

        return new MessageItemRecyclerAdapter.ViewHolder(view, context);
    }

    @Override
    public void onBindViewHolder(@NonNull final MessageItemRecyclerAdapter.ViewHolder viewHolder, int position) {

        //Get UserItem
        MessageItem messageItem = MessageItemList.get(position);

        //Display Data
        viewHolder.messageTextView.setText(messageItem.getMessage());
        viewHolder.timeStampTextView.setText((messageItem.getTimeStamp()));

        //Span to right or left
        if (messageItem.getSender().equals(user.getUid())){
            //Snap Text to the right
            viewHolder.messageTextView.setTextColor(Color.parseColor("#FFFFFF"));
            viewHolder.messageTextView.setTextColor(Color.parseColor("#FFFFFF"));
            viewHolder.linearLayoutMessage.setGravity(RIGHT);
            viewHolder.messageCardView.setCardBackgroundColor(Color.parseColor("#26C485"));

        }
        else {
            //Keep to the left
        }

        //Get Document ID onclick
        //final String documentID = SalesItemList.get(position).docID;

//        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Toast.makeText(context, documentID, Toast.LENGTH_SHORT).show();
//
//                //Store Document ID in Global Variable
//                userData.SellingOrderClicked_Global = documentID;
//
//                //Move to listing view Acivity
//                Intent intent = new Intent(context, status.class);
//                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                context.startActivity(intent);

            //}
       // });
    }

    @Override
    public int getItemCount() {

        return MessageItemList.size();

    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        //Widgets
        public TextView messageTextView, timeStampTextView;
        public CardView messageCardView;
        public LinearLayout linearLayoutMessage;

        public ViewHolder(@NonNull View itemView, Context ctx) {
            super(itemView);
            //Get Context
            context = ctx;

            //Get Widgets
            messageTextView = itemView.findViewById(R.id.messageTextView);
            timeStampTextView = itemView.findViewById(R.id.timeStampTextView);
            messageCardView = itemView.findViewById(R.id.messageCardView);
            linearLayoutMessage = itemView.findViewById(R.id.linearLayoutMessage);



        }
    }
}
