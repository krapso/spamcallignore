package adam.krapso.fetr.spamcallignore.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import adam.krapso.fetr.spamcallignore.Models.CommentModel;
import adam.krapso.fetr.spamcallignore.R;

/**
 * Created by krapso on 20-Jan-18.
 */

public class RecyclerViewNumberActivityAdapter extends RecyclerView.Adapter<RecyclerViewNumberActivityAdapter.RecyclerViewHolderNumberActivity> {

    List<CommentModel> listOfAllComments;
    Context context;

    public class RecyclerViewHolderNumberActivity extends RecyclerView.ViewHolder{

        TextView status;
        TextView rating;
        TextView text;
        public RecyclerViewHolderNumberActivity(View itemView) {
            super(itemView);
            status = itemView.findViewById(R.id.status);
            rating = itemView.findViewById(R.id.rating);
            text = itemView.findViewById(R.id.comment);
        }
    }

    public RecyclerViewNumberActivityAdapter(Context context, List<CommentModel> listOfAllComments){
        this.listOfAllComments = listOfAllComments;
        this.context = context;
    }

    @Override
    public RecyclerViewHolderNumberActivity onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.recycler_view_number_acitivity_item,parent,false);
        return new RecyclerViewHolderNumberActivity(view);
    }

    @Override
    public void onBindViewHolder(RecyclerViewNumberActivityAdapter.RecyclerViewHolderNumberActivity holder, int position) {
        holder.status.setText(listOfAllComments.get(position).status);
        holder.rating.setText(String.valueOf(listOfAllComments.get(position).rating));
        holder.text.setText(listOfAllComments.get(position).comment);
    }

    @Override
    public int getItemCount() {
        return listOfAllComments.size();
    }

    public void setNewData(List<CommentModel> listOfAllComments){
        this.listOfAllComments = listOfAllComments;
    }

}
