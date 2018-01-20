package adam.krapso.fetr.spamcallignore;

import android.content.ComponentName;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;

import adam.krapso.fetr.spamcallignore.Receivers.IncomingCallReceiver;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    public void RunCallIdentifier(View view){
        PackageManager pm  = this.getPackageManager();
        ComponentName componentName = new ComponentName(this, IncomingCallReceiver.class);
        pm.setComponentEnabledSetting(componentName,PackageManager.COMPONENT_ENABLED_STATE_ENABLED, PackageManager.DONT_KILL_APP);
        Toast.makeText(getApplicationContext(), "activated", Toast.LENGTH_LONG).show();
    }

    public void StopCallIdentifier(View view){
        PackageManager pm  = this.getPackageManager();
        ComponentName componentName = new ComponentName(this, IncomingCallReceiver.class);
        pm.setComponentEnabledSetting(componentName,PackageManager.COMPONENT_ENABLED_STATE_DISABLED, PackageManager.DONT_KILL_APP);
        Toast.makeText(getApplicationContext(), "cancelled", Toast.LENGTH_LONG).show();

    }
}
