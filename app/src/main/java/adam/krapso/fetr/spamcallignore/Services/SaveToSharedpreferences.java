package adam.krapso.fetr.spamcallignore.Services;

import android.content.Context;
import android.content.SharedPreferences;

import adam.krapso.fetr.spamcallignore.Models.ReceiverStatus;

/**
 * Created by krapso on 27-Jan-18.
 */

public class SaveToSharedpreferences {

    private Context context;
    private SharedPreferences sharedPreferences;
    private int _defaultValue = 0;
    private static String _preferencesName = "SHARED_PREFERENCES_SPAM_CALL_IGNORE_APP";
    public static String LAST_RECEIVER_STATUS = "LAST_RECEIVER_STATUS";

    public SaveToSharedpreferences(Context context){
        this.context = context;
        sharedPreferences = context.getSharedPreferences(_preferencesName, Context.MODE_PRIVATE);
    }

    public void SetLastReceiverStatus(ReceiverStatus status){
        sharedPreferences.edit().putInt(LAST_RECEIVER_STATUS, status.ordinal()).apply();
    }

    public ReceiverStatus GetLastReceiverStatus(){
        return ReceiverStatus.values()[sharedPreferences.getInt(LAST_RECEIVER_STATUS, _defaultValue)];
    }

}
