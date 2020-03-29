package com.colwyn.saf.ui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.colwyn.saf.R;
import com.colwyn.saf.model.ReviewItem;

import java.util.List;

public class ReviewItemRecyclerAdapter extends RecyclerView.Adapter<ReviewItemRecyclerAdapter.ViewHolder> {

    //Add a context
    private Context context;

    private List<ReviewItem> ReviewItemList;


    public ReviewItemRecyclerAdapter(Context context, List<ReviewItem> reviewItemList) {
        this.context = context;
        ReviewItemList = reviewItemList;
    }

    @NonNull
    @Override
    public ReviewItemRecyclerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.review_row, viewGroup,false);

        return new ReviewItemRecyclerAdapter.ViewHolder(view, context);
    }

    @Override
    public void onBindViewHolder(@NonNull ReviewItemRecyclerAdapter.ViewHolder viewHolder, int position) {

        //Get ReviewItem
        ReviewItem reviewItem = ReviewItemList.get(position);
        String imageURL;

        //Display Data
        //Display Stars
        if (reviewItem.getStars().equals("1")){
            viewHolder.reviewStarsTextView.setText("★");
        }
        else if (reviewItem.getStars().equals("2")){
            viewHolder.reviewStarsTextView.setText("★★");
        }
        else if (reviewItem.getStars().equals("3")){
            viewHolder.reviewStarsTextView.setText("★★★");
        }
        else if (reviewItem.getStars().equals("4")){
            viewHolder.reviewStarsTextView.setText("★★★★");
        }
        else if (reviewItem.getStars().equals("5")){
            viewHolder.reviewStarsTextView.setText("★★★★★");
        }
        else{

        }

        //Display The Rest
        viewHolder.reviewTitleTextView.setText(reviewItem.getTitle());
        viewHolder.reviewDescriptionTextView.setText(reviewItem.getDescription());
        viewHolder.reviewertextView.setText(reviewItem.getReviewer());

//        //Get Document ID onclick
//        final String documentID = ReviewItemList.get(position).docID;
//
//        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Toast.makeText(context, documentID, Toast.LENGTH_SHORT).show();
//
//                //Store Document ID in Global Variable
//                userData.catItemClicked_Global = documentID;
//
//                //Move to listing view Acivity
//                Intent intent = new Intent(context, CatListingView.class);
//                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                context.startActivity(intent);
//
//
//
//            }
//        });
    }

    @Override
    public int getItemCount() {

        return ReviewItemList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        //Widgets
        public TextView reviewStarsTextView, reviewTitleTextView, reviewDescriptionTextView, reviewertextView;

        public ViewHolder(@NonNull View itemView, Context ctx) {
            super(itemView);
            //Get Context
            context = ctx;

            //Get Widgets
            reviewStarsTextView = itemView.findViewById(R.id.reviewStarsTextView);
            reviewTitleTextView = itemView.findViewById(R.id.reviewTitleTextView);
            reviewDescriptionTextView = itemView.findViewById(R.id.reviewDescriptionTextView);
            reviewertextView = itemView.findViewById(R.id.reviewerTextView);

        }
    }
}
