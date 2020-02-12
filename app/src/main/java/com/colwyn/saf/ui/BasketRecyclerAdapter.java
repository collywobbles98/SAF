package com.colwyn.saf.ui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.colwyn.saf.R;
import com.colwyn.saf.model.BasketItem;
import com.squareup.picasso.Picasso;

import java.util.List;

public class BasketRecyclerAdapter extends RecyclerView.Adapter<BasketRecyclerAdapter.ViewHolder> {

    //Add a context
    private Context context;

    private List<BasketItem> BasketItemList;


    public BasketRecyclerAdapter(Context context, List<BasketItem> basketItemList) {
        this.context = context;
        BasketItemList = basketItemList;
    }

    @NonNull
    @Override
    public BasketRecyclerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.basket_row, viewGroup,false);

        return new BasketRecyclerAdapter.ViewHolder(view, context);
    }

    @Override
    public void onBindViewHolder(@NonNull BasketRecyclerAdapter.ViewHolder viewHolder, int position) {

        //Get BasketItem
        BasketItem basketItem = BasketItemList.get(position);
        String imageURL;

        //Display Data
        viewHolder.title.setText(basketItem.getTitle());
        viewHolder.price.setText(basketItem.getPrice());
        viewHolder.delivery_Notes.setText("Delivery: " + basketItem.getDelivery_Notes());

        imageURL = basketItem.getImageURL();
        Picasso.get().load(imageURL).placeholder(R.drawable.ic_add_a_photo_black_24dp).fit().into(viewHolder.image);

        //Get Document ID onclick
        final String documentID = BasketItemList.get(position).docID;

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, documentID, Toast.LENGTH_SHORT).show();

                //Store Document ID in Global Variable
                //userData.catItemClicked_Global = documentID;

                //Move to listing view Acivity
//                Intent intent = new Intent(context, CatListingView.class);
//                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                context.startActivity(intent);



            }
        });
    }

    @Override
    public int getItemCount() {

        return BasketItemList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        //Widgets
        public TextView title,  price, delivery_Notes;
        public ImageView image;
        String UserID;

        public ViewHolder(@NonNull View itemView, Context ctx) {
            super(itemView);
            //Get Context
            context = ctx;

            //Get Widgets
            title = itemView.findViewById(R.id.basketTitleTextView);
            image = itemView.findViewById(R.id.basketImageView);
            price = itemView.findViewById(R.id.basketPriceTextView);
            delivery_Notes = itemView.findViewById(R.id.basketDeliveryTextView);



        }
    }
}
