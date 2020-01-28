package com.colwyn.saf.ui;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.colwyn.saf.R;
import com.colwyn.saf.listingView;
import com.colwyn.saf.model.UserItem;
import com.colwyn.saf.userData;
import com.squareup.picasso.Picasso;

import java.util.List;

public class UserItemRecyclerAdapter extends RecyclerView.Adapter<UserItemRecyclerAdapter.ViewHolder> {

    //Add a context
    private Context context;

    private List<UserItem> UserItemList;


    public UserItemRecyclerAdapter(Context context, List<UserItem> userItemList) {
        this.context = context;
        UserItemList = userItemList;
    }

    @NonNull
    @Override
    public UserItemRecyclerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.item_row, viewGroup,false);

        return new ViewHolder(view, context);
    }

    @Override
    public void onBindViewHolder(@NonNull UserItemRecyclerAdapter.ViewHolder viewHolder, int position) {

        //Get UserItem
        UserItem userItem = UserItemList.get(position);
        String imageURL;

        //Display Data
        viewHolder.title.setText(userItem.getTitle());
        viewHolder.category.setText("Category: " + userItem.getCategory());
        viewHolder.brand.setText("Brand: " +userItem.getBrand());
        viewHolder.price.setText("Price: " +userItem.getPrice());

        imageURL = userItem.getImageURL();
        Picasso.get().load(imageURL).placeholder(R.drawable.ic_add_a_photo_black_24dp).fit().into(viewHolder.image);

        //Get Document ID onclick
        final String documentID = UserItemList.get(position).docID;

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, documentID, Toast.LENGTH_SHORT).show();

                //Store Document ID in Global Variable
                userData.userItemClicked_Global = documentID;

                //Move to listing view Acivity
                Intent intent = new Intent(context, listingView.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);



            }
        });
    }

    @Override
    public int getItemCount() {

        return UserItemList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        //Widgets
        public TextView title, category, description, brand, condition, price, delivery_Notes, userID;
        public ImageView image;
        String UserID;

        public ViewHolder(@NonNull View itemView, Context ctx) {
            super(itemView);
            //Get Context
            context = ctx;

            //Get Widgets
            title = itemView.findViewById(R.id.ItemRowTextView);
            image = itemView.findViewById(R.id.ItemRowImageView);
            category = itemView.findViewById(R.id.ItemRowCatTextView);
            brand = itemView.findViewById(R.id.ItemRowBraTextView);
            price = itemView.findViewById(R.id.ItemRowPriTextView);



        }
    }

}
