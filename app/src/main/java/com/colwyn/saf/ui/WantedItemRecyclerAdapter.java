package com.colwyn.saf.ui;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.colwyn.saf.R;
import com.colwyn.saf.WantedAdView;
import com.colwyn.saf.model.WantedItem;
import com.colwyn.saf.userData;

import java.util.List;

public class WantedItemRecyclerAdapter extends RecyclerView.Adapter<WantedItemRecyclerAdapter.ViewHolder> {

    //Add a context
    private Context context;

    private List<WantedItem> WantedItemList;


    public WantedItemRecyclerAdapter(Context context, List<WantedItem> wantedItemList) {
        this.context = context;
        WantedItemList = wantedItemList;
    }

    @NonNull
    @Override
    public WantedItemRecyclerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.wanted_row, viewGroup,false);

        return new WantedItemRecyclerAdapter.ViewHolder(view, context);
    }

    @Override
    public void onBindViewHolder(@NonNull WantedItemRecyclerAdapter.ViewHolder viewHolder, int position) {

        //Get ReviewItem
        WantedItem wantedItem = WantedItemList.get(position);

        //Display Data
        viewHolder.wantedRowDateTextView.setText(wantedItem.getTimeStamp());
        viewHolder.wantedRowTitleTextView.setText(wantedItem.getTitle());
        viewHolder.wantedRowDescriptionTextView.setText(wantedItem.getDescription());

//        //Get Document ID onclick
        final String documentID = WantedItemList.get(position).docID;

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(context, documentID, Toast.LENGTH_SHORT).show();

                //Store Document ID in Global Variable
                userData.WantedAdClicked_Global = documentID;

                //Move to listing view Acivity
                WantedItemList.clear();
                Intent intent = new Intent(context, WantedAdView.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);

            }
        });
    }

    @Override
    public int getItemCount() {

        return WantedItemList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        //Widgets
        public TextView wantedRowDateTextView, wantedRowTitleTextView, wantedRowDescriptionTextView;

        public ViewHolder(@NonNull View itemView, Context ctx) {
            super(itemView);
            //Get Context
            context = ctx;

            //Get Widgets
            wantedRowDateTextView = itemView.findViewById(R.id.wantedRowDateTextView);
            wantedRowTitleTextView = itemView.findViewById(R.id.wantedRowTitleTextView);
            wantedRowDescriptionTextView = itemView.findViewById(R.id.wantedRowDescriptionTextView);
        }
    }
}
