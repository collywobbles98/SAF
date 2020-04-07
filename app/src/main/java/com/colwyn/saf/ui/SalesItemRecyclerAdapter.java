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
import com.colwyn.saf.model.SalesItem;
import com.colwyn.saf.status;
import com.colwyn.saf.userData;

import java.util.List;

public class SalesItemRecyclerAdapter extends RecyclerView.Adapter<SalesItemRecyclerAdapter.ViewHolder> {

    //Add a context
    private Context context;

    private List<SalesItem> SalesItemList;

    public SalesItemRecyclerAdapter(Context context, List<SalesItem> salesItemList) {
        this.context = context;
        SalesItemList = salesItemList;
    }

    @NonNull
    @Override
    public SalesItemRecyclerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.sales_row, viewGroup,false);

        return new SalesItemRecyclerAdapter.ViewHolder(view, context);
    }

    @Override
    public void onBindViewHolder(@NonNull SalesItemRecyclerAdapter.ViewHolder viewHolder, int position) {

        //Get UserItem
        SalesItem salesItem = SalesItemList.get(position);

        //Display Data
        viewHolder.titleTextView.setText(salesItem.getItemTitle());
        viewHolder.quantityTextView.setText("Quantity: " + salesItem.getQuantity());
        viewHolder.orderIDTextView.setText("Order Ref: " + salesItem.getOrderID());
        viewHolder.buyerNameTextView.setText(salesItem.getBuyerName());
        viewHolder.buyerAddressTextView.setText(salesItem.getBuyerAddress());
        viewHolder.statusTextView.setText(salesItem.getStatus());

        //Get Document ID onclick
        final String documentID = SalesItemList.get(position).docID;

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(context, documentID, Toast.LENGTH_SHORT).show();

                //Store Document ID in Global Variable
                userData.SellingOrderClicked_Global = documentID;

                //Move to listing view Acivity
                Intent intent = new Intent(context, status.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);

            }
        });
    }

    @Override
    public int getItemCount() {

        return SalesItemList.size();

    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        //Widgets
        public TextView titleTextView, quantityTextView, orderIDTextView, buyerNameTextView, buyerAddressTextView, statusTextView;

        public ViewHolder(@NonNull View itemView, Context ctx) {
            super(itemView);
            //Get Context
            context = ctx;

            //Get Widgets
            titleTextView = itemView.findViewById(R.id.titleTextView);
            quantityTextView = itemView.findViewById(R.id.quantityTextView);
            orderIDTextView = itemView.findViewById(R.id.orderIDTextView);
            buyerNameTextView = itemView.findViewById(R.id.buyerNameTextView);
            buyerAddressTextView = itemView.findViewById(R.id.buyerAddressTextView);
            statusTextView = itemView.findViewById(R.id.statusTextView);

        }
    }

}
