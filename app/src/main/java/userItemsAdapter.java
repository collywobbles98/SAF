import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.colwyn.saf.R;
import com.colwyn.saf.userItems;

import java.util.List;

public class userItemsAdapter extends RecyclerView.Adapter<userItemsAdapter.ViewHolder> {

    public List<userItems> userItemsList;

    public userItemsAdapter(List<userItems> userItemsList){

        this.userItemsList = userItemsList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_item, parent, false);
        return new ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        holder.titleTextView.setText(userItemsList.get(position).getTitle());
    }

    @Override
    public int getItemCount() {

        return userItemsList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{


        View mView;

        public TextView titleTextView;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mView = itemView;

            titleTextView = mView.findViewById(R.id.titleEditText);
        }
    }
}
