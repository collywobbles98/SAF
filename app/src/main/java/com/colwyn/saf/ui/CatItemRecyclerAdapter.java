package com.colwyn.saf.ui;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.colwyn.saf.CatListingView;
import com.colwyn.saf.R;
import com.colwyn.saf.model.CatItem;
import com.colwyn.saf.userData;
import com.squareup.picasso.Picasso;

import java.util.List;

public class CatItemRecyclerAdapter extends RecyclerView.Adapter<CatItemRecyclerAdapter.ViewHolder> {

    //Add a context
    private Context context;

    private List<CatItem> CatItemList;


    public CatItemRecyclerAdapter(Context context, List<CatItem> catItemList) {
        this.context = context;
        CatItemList = catItemList;
    }

    @NonNull
    @Override
    public CatItemRecyclerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.cat_item_row, viewGroup,false);

        return new ViewHolder(view, context);
    }

    @Override
    public void onBindViewHolder(@NonNull CatItemRecyclerAdapter.ViewHolder viewHolder, int position) {

        //Get CatItem
        CatItem catItem = CatItemList.get(position);
        String imageURL;

        //Display Data
        viewHolder.title.setText(catItem.getTitle());
        viewHolder.condition.setText(catItem.getCondition());
        viewHolder.brand.setText(catItem.getBrand());
        viewHolder.price.setText("Price: " + catItem.getSymbol() + catItem.getPrice() + " (" + catItem.getCurrency() + ")");
        viewHolder.delivery_Notes.setText("Delivery: " + catItem.getDelivery_Notes());

        imageURL = catItem.getImageURL();
        Picasso.get().load(imageURL).placeholder(R.drawable.ic_add_a_photo_black_24dp).fit().into(viewHolder.image);

        //Get Document ID onclick
        final String documentID = CatItemList.get(position).docID;

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(context, documentID, Toast.LENGTH_SHORT).show();

                //Store Document ID in Global Variable
                userData.catItemClicked_Global = documentID;

                //Move to listing view Acivity
                Intent intent = new Intent(context, CatListingView.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);



            }
        });
    }

    @Override
    public int getItemCount() {

        return CatItemList.size();
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
            title = itemView.findViewById(R.id.catItemRowtitleTextView);
            image = itemView.findViewById(R.id.catItemRowImageView);
            condition = itemView.findViewById(R.id.catItemRowconditionTextView);
            brand = itemView.findViewById(R.id.catItemRowbrandTextView);
            price = itemView.findViewById(R.id.catItemRowpriceTextView);
            delivery_Notes = itemView.findViewById(R.id.catItemRowdeliveryTextView);



        }
    }
}
