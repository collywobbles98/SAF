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
import com.colwyn.saf.model.OrderItem;
import com.colwyn.saf.orderbreakdown;
import com.colwyn.saf.userData;

import java.util.List;

public class OrderItemRecyclerAdapter extends RecyclerView.Adapter<OrderItemRecyclerAdapter.ViewHolder> {

    //Add a context
    private Context context;

    private List<OrderItem> OrderItemList;


    public OrderItemRecyclerAdapter(Context context, List<OrderItem> orderItemList) {
        this.context = context;
        OrderItemList = orderItemList;
    }

    @NonNull
    @Override
    public OrderItemRecyclerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.order_row, viewGroup,false);

        return new ViewHolder(view, context);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderItemRecyclerAdapter.ViewHolder viewHolder, int position) {

        //Get OrderItem
        OrderItem orderItem = OrderItemList.get(position);

        //Get Subtotal and format to 2 dp
        double paid = Double.parseDouble(orderItem.getOrderTotal());
        String strPaid = String.format("%.2f", paid);

        String PayPalREF = "";
        if (orderItem.getPaymentMethod().equals("paypal")){
            viewHolder.details1.setText("Order Ref: " + OrderItemList.get(position).docID + "\nPlaced on: " + orderItem.getTimeStamp());
            viewHolder.details2.setText("Paid: " + strPaid + " " +orderItem.getCurrencyUsed() + " with " + orderItem.getPaymentMethod() + "\n" + orderItem.getPayPalREF());
            viewHolder.details3.setText("\nItems:\n" +orderItem.getGoods());
        }
        else{
            //For Card Payments (Obsolete)
            //Display Data
            viewHolder.details1.setText("Order Ref: " + OrderItemList.get(position).docID + "\nPlaced on: " + orderItem.getTimeStamp());
            viewHolder.details2.setText("Paid: " + strPaid + " " +orderItem.getCurrencyUsed() + " with " + orderItem.getPaymentMethod());
            viewHolder.details3.setText("\nItems:\n" +orderItem.getGoods());
        }


        //Get Document ID onclick
        final String documentID = OrderItemList.get(position).docID;

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(context, documentID, Toast.LENGTH_SHORT).show();

                //Store Document ID in Global Variable
                userData.orderClicked_Global = documentID;

                //Move to listing view Acivity
                Intent intent = new Intent(context, orderbreakdown.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);



            }
        });
    }

    @Override
    public int getItemCount() {

        return OrderItemList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        //Widgets
        public TextView details1, details2, details3;
        String UserID;

        public ViewHolder(@NonNull View itemView, Context ctx) {
            super(itemView);
            //Get Context
            context = ctx;

            //Get Widgets
            details1 = itemView.findViewById(R.id.order1TextView);
            details2 = itemView.findViewById(R.id.order2TextView);
            details3 = itemView.findViewById(R.id.order3TextView);




        }
    }

}
