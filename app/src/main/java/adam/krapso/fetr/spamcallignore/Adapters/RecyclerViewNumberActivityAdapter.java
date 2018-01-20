package adam.krapso.fetr.spamcallignore.Adapters;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by krapso on 20-Jan-18.
 */

public class RecyclerViewNumberActivityAdapter extends RecyclerView.Adapter<RecyclerViewNumberActivityAdapter.RecyclerViewHolderNumberActivity> {

    @Override
    public RecyclerViewHolderNumberActivity onCreateViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerViewNumberActivityAdapter.RecyclerViewHolderNumberActivity holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class RecyclerViewHolderNumberActivity extends RecyclerView.ViewHolder{

        public RecyclerViewHolderNumberActivity(View itemView) {
            super(itemView);
        }
    }
}
