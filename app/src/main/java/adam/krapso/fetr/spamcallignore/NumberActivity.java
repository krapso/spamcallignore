package adam.krapso.fetr.spamcallignore;

import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import adam.krapso.fetr.spamcallignore.Adapters.RecyclerViewNumberActivityAdapter;
import adam.krapso.fetr.spamcallignore.Models.CommentModel;
import adam.krapso.fetr.spamcallignore.Receivers.IncomingCallReceiver;
import adam.krapso.fetr.spamcallignore.Services.CommentService;

public class NumberActivity extends AppCompatActivity {

    public static String NUMBER_STRING = "Number";
    private String number;
    private List<CommentModel> commentModelList;
    private RecyclerView recyclerView;
    private CommentService commentService;
    private RecyclerViewNumberActivityAdapter recyclerViewAdapterNumberActivity;
    private Handler mainHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Bundle data = msg.getData();
            boolean isLoaded = Boolean.parseBoolean((data.getString(IncomingCallReceiver.ISLOADED)!=null)?data.getString(IncomingCallReceiver.ISLOADED):"false");
            if(isLoaded){
                getPhoneNumberInfo();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_number);
        number = getIntent().getExtras().getString(NUMBER_STRING);
        commentModelList = new ArrayList<>();
        commentService = new CommentService(number,mainHandler);
        commentService.new InitTask().execute();

        recyclerView = findViewById(R.id.recyclerView);
        recyclerViewAdapterNumberActivity = new RecyclerViewNumberActivityAdapter(this,commentModelList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
        recyclerView.setAdapter(recyclerViewAdapterNumberActivity);
    }

    public void getPhoneNumberInfo(){
        commentModelList = commentService.getAllComments();
        recyclerViewAdapterNumberActivity.setNewData(commentModelList);
        recyclerViewAdapterNumberActivity.notifyDataSetChanged();
        //List<String> mostUsedCommentStatus = commentService.getAllCommentsSortByMostUsedStatuses();
    }
}
