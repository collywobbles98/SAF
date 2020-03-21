package com.colwyn.saf.ui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.colwyn.saf.R;
import com.colwyn.saf.model.SoldItem;

import java.util.List;

public class SoldItemRecyclerAdapter extends RecyclerView.Adapter<SoldItemRecyclerAdapter.ViewHolder> {

    //Add a context
    private Context context;

    private List<SoldItem> SoldItemList;


    public SoldItemRecyclerAdapter(Context context, List<SoldItem> soldItemList) {
        this.context = context;
        SoldItemList = soldItemList;
    }

    @NonNull
    @Override
    public SoldItemRecyclerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.sold_row, viewGroup,false);

        return new SoldItemRecyclerAdapter.ViewHolder(view, context);
    }

    @Override
    public void onBindViewHolder(@NonNull SoldItemRecyclerAdapter.ViewHolder viewHolder, int position) {

        //Get UserItem
        SoldItem soldItem = SoldItemList.get(position);

        //Display Data
        viewHolder.itemTitleTextView.setText(soldItem.getItemTitle() + " X " + soldItem.getQuantity());
        viewHolder.statusTextView.setText(soldItem.getStatus());

        //Get Document ID onclick
        final String documentID = SoldItemList.get(position).docID;

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, documentID, Toast.LENGTH_SHORT).show();

                //Store Document ID in Global Variable
                //userData.userItemClicked_Global = documentID;

                //Move to listing view Acivity
                //Intent intent = new Intent(context, listingView.class);
                //intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                //context.startActivity(intent);



            }
        });
    }

    @Override
    public int getItemCount() {

        return SoldItemList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        //Widgets
        public TextView itemTitleTextView, statusTextView;

        public ViewHolder(@NonNull View itemView, Context ctx) {
            super(itemView);
            //Get Context
            context = ctx;

            //Get Widgets
            itemTitleTextView = itemView.findViewById(R.id.itemTitleTextView);
            statusTextView = itemView.findViewById(R.id.statusTextView);



        }
    }

}
