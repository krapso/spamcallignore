package adam.krapso.fetr.spamcallignore;

import android.animation.Animator;
import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.content.ComponentName;
import android.content.pm.PackageManager;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import adam.krapso.fetr.spamcallignore.Models.ReceiverStatus;
import adam.krapso.fetr.spamcallignore.Receivers.IncomingCallReceiver;
import adam.krapso.fetr.spamcallignore.Services.SaveToSharedpreferences;

import static adam.krapso.fetr.spamcallignore.Services.PermissionService.checkPermissions;

public class MainActivity extends AppCompatActivity {
    ConstraintLayout _mainActivityLayout;
    Button _start;
    Button _stop;

    View _circleFirst;
    View _circleSecond;
    View _circleThird;

    SaveToSharedpreferences _sharedpreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        _mainActivityLayout = findViewById(R.id.mainActivityLayout);

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
                    Snackbar.make(_mainActivityLayout,getResources().getString(R.string.grant_permissions_text),Snackbar.LENGTH_INDEFINITE).setAction(getResources().getString(R.string.enable_permissions_button),
                            new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    checkPermissions(MainActivity.this);
                                }
                            }).show();
                }
            }
        }
    }

    private void init(){
        _start = findViewById(R.id.runReceiver);
        _stop = findViewById(R.id.stopReceiver);

        _circleFirst = findViewById(R.id.firstCircle);
        _circleSecond = findViewById(R.id.secondCircle);
        _circleThird = findViewById(R.id.thirdCircle);

        _sharedpreferences = new SaveToSharedpreferences(this);
        getWindow().setStatusBarColor(ContextCompat.getColor(this,R.color.mainBackground));

        if(_sharedpreferences.GetLastReceiverStatus()==ReceiverStatus.FIRST_START ||
                _sharedpreferences.GetLastReceiverStatus()==ReceiverStatus.RECEIVER_RUN){
            StopIsVisible();
        }
        else{
            RunIsVisible();
        }
    }

    public void RunCallIdentifier(View view){
        PackageManager pm  = this.getPackageManager();
        ComponentName componentName = new ComponentName(this, IncomingCallReceiver.class);
        pm.setComponentEnabledSetting(componentName,PackageManager.COMPONENT_ENABLED_STATE_ENABLED, PackageManager.DONT_KILL_APP);
        //Toast.makeText(getApplicationContext(), "Started", Toast.LENGTH_LONG).show();

        new SaveToSharedpreferences(this).SetLastReceiverStatus(ReceiverStatus.RECEIVER_RUN);
        AnimateClickSet(1,true,true);
    }

    public void StopCallIdentifier(View view){
        PackageManager pm  = this.getPackageManager();
        ComponentName componentName = new ComponentName(this, IncomingCallReceiver.class);
        pm.setComponentEnabledSetting(componentName,PackageManager.COMPONENT_ENABLED_STATE_DISABLED, PackageManager.DONT_KILL_APP);
        //Toast.makeText(getApplicationContext(), "Stoped", Toast.LENGTH_LONG).show();

        new SaveToSharedpreferences(this).SetLastReceiverStatus(ReceiverStatus.RECEIVER_STOP);
        AnimateClickSet(1,false,true);
    }

    public void StopIsVisible(){
        _start.setVisibility(View.INVISIBLE);
        _stop.setVisibility(View.VISIBLE);
    }

    public void RunIsVisible(){
        _start.setVisibility(View.VISIBLE);
        _stop.setVisibility(View.INVISIBLE);
    }

    public void AnimateClickSet(final int step, final boolean isStart, final boolean isSet){
        int colorFrom = 0;
        switch (step){
            case 1:
                colorFrom = getResources().getColor(R.color.circleMain);
                break;
            case 2:
                colorFrom = getResources().getColor(R.color.circleFirst);
                break;
            case 3:
                colorFrom = getResources().getColor(R.color.circleSecond);
                break;
            case 4:
                colorFrom = getResources().getColor(R.color.circleThird);
                break;
        }
        int colorTo = isStart?getResources().getColor(R.color.circleStar):getResources().getColor(R.color.circleStop);
        final ValueAnimator colorAnimation = isSet?
                ValueAnimator.ofObject(new ArgbEvaluator(), colorFrom, colorTo):
                ValueAnimator.ofObject(new ArgbEvaluator(), colorTo, colorFrom);
        colorAnimation.setDuration(200);
        colorAnimation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

            @Override
            public void onAnimationUpdate(ValueAnimator animator) {

                switch (step){
                    case 1:
                        if(isStart){
                            ((GradientDrawable)_start.getBackground()).setColor((int)animator.getAnimatedValue());
                        }
                        else{
                            ((GradientDrawable)_stop.getBackground()).setColor((int)animator.getAnimatedValue());
                        }
                        break;
                    case 2:
                            ((GradientDrawable)_circleFirst.getBackground()).setColor((int)animator.getAnimatedValue());
                        break;
                    case 3:
                            ((GradientDrawable)_circleSecond.getBackground()).setColor((int)animator.getAnimatedValue());
                        break;
                    case 4:
                            ((GradientDrawable)_circleThird.getBackground()).setColor((int)animator.getAnimatedValue());
                        break;
                }
            }
        });
        colorAnimation.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {

            }

            @Override
            public void onAnimationEnd(Animator animator) {
                if(isSet){
                    AnimateClickSet(step,isStart,false);
                }
                else{
                    if(step<4){
                        AnimateClickSet(step+1,isStart,true);
                    }else{
                        if(isStart){
                            StopIsVisible();
                        }else{
                            RunIsVisible();
                        }
                    }
                }
            }

            @Override
            public void onAnimationCancel(Animator animator) {

            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        });
        colorAnimation.start();
    }
}
