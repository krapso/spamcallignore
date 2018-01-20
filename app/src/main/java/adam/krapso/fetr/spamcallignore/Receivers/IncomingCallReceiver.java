package adam.krapso.fetr.spamcallignore.Receivers;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.NotificationCompat;
import android.telephony.TelephonyManager;

import java.util.List;

import adam.krapso.fetr.spamcallignore.Models.CommentModel;
import adam.krapso.fetr.spamcallignore.NumberActivity;
import adam.krapso.fetr.spamcallignore.R;
import adam.krapso.fetr.spamcallignore.Services.CommentService;

public class IncomingCallReceiver extends BroadcastReceiver {

    private Context context;
    private CommentService commentService;
    public String number;
    public static String ISLOADED = "isLoaded";
    public static int NOTIFICATION_CHANNEL_ID = 1;

    private Handler mainHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Bundle data = msg.getData();
            boolean isLoaded = Boolean.parseBoolean((data.getString(ISLOADED)!=null)?data.getString(ISLOADED):"false");
            if(isLoaded){
                getPhoneNumberInfo();
            }
        }
    };

    @Override
    public void onReceive(Context context, Intent intent) {
        String state = intent.getStringExtra(TelephonyManager.EXTRA_STATE);
        this.context = context;
        if(state != null){
            if (state.equals(TelephonyManager.EXTRA_STATE_RINGING)) {
                //Phone is ringing
                String incomingNumber = intent.getExtras().getString(TelephonyManager.EXTRA_INCOMING_NUMBER);
                number = incomingNumber;
                commentService = new CommentService(incomingNumber,mainHandler);
                commentService.new InitTask().execute();
            } else if (state.equals(TelephonyManager.EXTRA_STATE_OFFHOOK)) {
                //Call received
            } else if (state.equals(TelephonyManager.EXTRA_STATE_IDLE)) {
                //Call Dropped or rejected
            }
        }
    }

    public void getPhoneNumberInfo(){
        List<String> mostUsedCommentStatus = commentService.getAllCommentsSortByMostUsedStatuses();
        CommentModel mostRatedComment = commentService.getMostRatedComment();
        createNotification(mostUsedCommentStatus,mostRatedComment);
    }

    public String getNotificationText(List<String> mostUsedCommentStatuses, CommentModel commentModel){
        StringBuilder text = new StringBuilder();
        if(mostUsedCommentStatuses.size()>0){
            for(int i=0;i<mostUsedCommentStatuses.size();i++)
                if(i<3){
                    text.append(mostUsedCommentStatuses.get(i)).append(", ");
                }
        }

        if(commentModel !=null){
            text.append("\n(").append(commentModel.status).append("/*").append(commentModel.rating).append(") ").append(commentModel.comment);
        }
        return text.toString();
    }

    public void createNotification(List<String> mostUsedCommentStatuses, CommentModel mostRatedComment){
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context, String.valueOf(NOTIFICATION_CHANNEL_ID));

        notificationBuilder.setAutoCancel(true)
                .setDefaults(Notification.DEFAULT_ALL)
                .setWhen(System.currentTimeMillis())
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setPriority(Notification.PRIORITY_MAX)
                .setContentTitle(number)
                .setStyle(new NotificationCompat.BigTextStyle())
                .setContentText(getNotificationText(mostUsedCommentStatuses,mostRatedComment))
                .setContentInfo(mostRatedComment.comment);

        Intent resultIntent = new Intent(context, NumberActivity.class).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        resultIntent.putExtra(NumberActivity.NUMBER_STRING, number);

        PendingIntent contentIntent = PendingIntent.getActivity(context, 0,resultIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        notificationBuilder.setContentIntent(contentIntent);
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(NOTIFICATION_CHANNEL_ID, notificationBuilder.build());
    }
}
