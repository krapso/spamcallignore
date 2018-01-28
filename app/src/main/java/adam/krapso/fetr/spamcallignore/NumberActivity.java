package adam.krapso.fetr.spamcallignore;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import adam.krapso.fetr.spamcallignore.Adapters.RecyclerViewNumberActivityAdapter;
import adam.krapso.fetr.spamcallignore.Models.CommentModel;
import adam.krapso.fetr.spamcallignore.Receivers.IncomingCallReceiver;
import adam.krapso.fetr.spamcallignore.Services.CommentService;

import static adam.krapso.fetr.spamcallignore.Services.PermissionService.checkPermissions;

public class NumberActivity extends AppCompatActivity {

    private ConstraintLayout _numberAcitivityLayout;
    private RecyclerView _recyclerView;
    private RecyclerViewNumberActivityAdapter _recyclerViewAdapterNumberActivity;
    private TextView _phoneNumber;

    public static String NUMBER_STRING = "Number";
    private CommentService _commentService;
    private String _number;
    private List<CommentModel> _commentModelList;
    private Handler _mainHandler = new Handler(){
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
        _numberAcitivityLayout = findViewById(R.id.numberActivityLayout);

        getWindow().setStatusBarColor(ContextCompat.getColor(this,R.color.mainBackground));

        if(checkPermissions(this)){
            init();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        if(requestCode == Constants.MULTIPLE_PERMISSIONS_REQUEST){
            if (grantResults.length > 0) {
                if(grantResults[0] == PackageManager.PERMISSION_GRANTED &&
                   grantResults[1] == PackageManager.PERMISSION_GRANTED &&
                   grantResults[2] == PackageManager.PERMISSION_GRANTED &&
                   grantResults[3] == PackageManager.PERMISSION_GRANTED){
                    init();
                }else{
                    Snackbar.make(_numberAcitivityLayout,getResources().getString(R.string.grant_permissions_text),Snackbar.LENGTH_INDEFINITE).setAction(getResources().getString(R.string.enable_permissions_button),
                            new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    checkPermissions(NumberActivity.this);
                                }
                            }).show();
                }
            }
        }
    }

    private void init(){
        _number = getIntent().getExtras().getString(NUMBER_STRING);
        _commentModelList = new ArrayList<>();
        _commentService = new CommentService(_number,_mainHandler);
        _commentService.new InitTask().execute();

        _phoneNumber = findViewById(R.id.phoneNumber);
        _recyclerView = findViewById(R.id.recyclerView);

        _recyclerViewAdapterNumberActivity = new RecyclerViewNumberActivityAdapter(this,_commentModelList);
        _recyclerView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
        _recyclerView.setAdapter(_recyclerViewAdapterNumberActivity);

        _phoneNumber.setText(_number);
    }

    public void getPhoneNumberInfo(){
        _commentModelList = _commentService.getAllComments();
        _recyclerViewAdapterNumberActivity.setNewData(_commentModelList);
        _recyclerViewAdapterNumberActivity.notifyDataSetChanged();
    }
}
