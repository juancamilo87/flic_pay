package com.example.l.flicbank;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.Transformation;
import android.widget.Button;

import android.os.Handler;
import android.widget.ImageView;
import android.widget.TextSwitcher;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Random;

import cz.msebera.android.httpclient.Header;
import io.flic.lib.FlicButton;
import io.flic.lib.FlicButtonCallback;
import io.flic.lib.FlicButtonCallbackFlags;
import io.flic.lib.FlicManager;
import io.flic.lib.FlicManagerInitializedCallback;


public class SetPassword extends Activity {

    private int area;
    private int[] down_password = new int[10];
    private int[] up_password = new int[10];
    private FlicManager manager;
    private ImageView background;
    private View pass_nums;
    private Button btn_start;
    private int counter;
    private TextSwitcher txt_counter;
    private List<FlicButton> buttons;
    private String connectedButton; // mac address of the connected button
    private String account_id;
    private String username;
    private String other_password;
    private boolean recording;
    private FlicButton theButton;
    private String firstPassword;
    private String secondPassword;
    private boolean first_time;
    String TAG ="flicpay";
    private FlicButtonCallback buttonCallback = new FlicButtonCallback() {
        @Override
        public void onButtonUpOrDown(FlicButton button, boolean wasQueued, int timeDiff, boolean isUp, boolean isDown) {
            String text = button + " was " + (isDown ? "pressed" : "released");
            Log.d(TAG, text);
            if(recording) {
                if(isDown)
                {
                    down_password[area]++;
                }
                if(isUp)
                {
                    up_password[area]++;
                }
            }
        }

    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        first_time = true;
        connectedButton = getIntent().getStringExtra("flic_id");
        account_id = getIntent().getStringExtra("account_id");
        username = getIntent().getStringExtra("username");
        other_password = getIntent().getStringExtra("password");

        //reset the layout
        setContentView(R.layout.activity_set_password);
        btn_start = (Button)findViewById(R.id.button);
        btn_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btn_start.setEnabled(false);
                startAnimation();
            }
        });

        counter = 3;
        background = (ImageView) findViewById(R.id.img_background);
        pass_nums = findViewById(R.id.pass_nums);
        txt_counter = (TextSwitcher) findViewById(R.id.counter);
        txt_counter.setInAnimation(this, R.anim.fade_in);
        txt_counter.setOutAnimation(this, R.anim.fade_out);
        recording = false;
        FlicManager.setAppCredentials("ff17a835-a41c-4e27-ac47-4d9d38c334a1", "b955302f-7ad6-4885-a211-52fd96985c91", "FlicBank");

        FlicManager.getInstance(this, new FlicManagerInitializedCallback() {
            @Override
            public void onInitialized(FlicManager manager) {
                SetPassword.this.manager = manager;
                manager.initiateGrabButton(SetPassword.this);// this refers to the current Activity.
            }
        });

    }

    private void setButtonCallback(FlicButton button) {
        button.removeAllFlicButtonCallbacks();
        button.addFlicButtonCallback(buttonCallback);
        button.setFlicButtonCallbackFlags(FlicButtonCallbackFlags.UP_OR_DOWN);;
    }


    @Override
    protected void onDestroy() {
//        FlicManager.destroyInstance();
        super.onDestroy();
    }

    private void startAnimation()
    {

        final ResizeAnimation resizeAnimation = new ResizeAnimation(background, pass_nums.getWidth());
        resizeAnimation.setDuration(11000);
        resizeAnimation.setInterpolator(new LinearInterpolator());
        resizeAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        final Handler handler = new Handler();
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                txt_counter.setOutAnimation(getApplicationContext(), R.anim.fade_out);
                if(counter>0)
                {

                    txt_counter.setText(counter + "");
                    counter--;
                    handler.postDelayed(this,1000);
                }
                else
                {
                    txt_counter.setText("Start");
                    background.startAnimation(resizeAnimation);

                    //Record password
                    Log.d(TAG, "Click input pin button, start handler");

                    area = -1;
                    for(int i = 0; i<down_password.length; i++){
                        down_password[i]=0;
                        up_password[i]=0;
                    }

                    final Handler handler_pass = new Handler();

                    final Runnable r = new Runnable() {
                        public void run() {
                            area++;
                            if(area<10){
                                recording = true;
                                Log.d(TAG, "second:"+ area);
                                handler_pass.postDelayed(this, 1000);}
                            else{
                                recording = false;
//                                Log.d(TAG, "password:"
//                                        + "[0]" + password[0] + ","
//                                        + "[1]" + password[1] + ","
//                                        + "[2]" + password[2] + ","
//                                        + "[3]" + password[3] + ","
//                                        + "[4]" + password[4] + ","
//                                        + "[5]" + password[5] + ","
//                                        + "[6]" + password[6] + ","
//                                        + "[7]" + password[7] + ","
//                                        + "[8]" + password[8] + ","
//                                        + "[9]" + password[9] + ".");
                                if(verifyPassword())
                                {
                                    getPassword();
                                }
                                else
                                {
                                    btn_start.setEnabled(true);
                                    first_time = true;
                                    firstPassword = "";
                                    secondPassword = "";
                                    txt_counter.setText("Password is invalid, please try again!");
                                }
                            }

                        }
                    };

                    handler_pass.postDelayed(r, 1000);

                    counter--;
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            txt_counter.setOutAnimation(getApplicationContext(), R.anim.long_fade_out);
                            txt_counter.setText("");
                            counter = 3;
                        }
                    },800);
                }
            }
        };

        handler.post(runnable);

    }

    public class ResizeAnimation extends Animation {
        final int startWidth;
        final int targetWidth;
        View view;

        public ResizeAnimation(View view, int targetWidth) {
            this.view = view;
            this.targetWidth = targetWidth;
            startWidth = view.getWidth();
        }

        @Override
        protected void applyTransformation(float interpolatedTime, Transformation t) {
            int newWidth = (int) (targetWidth * interpolatedTime);
            view.getLayoutParams().width = newWidth;
            view.requestLayout();
        }

        @Override
        public void initialize(int width, int height, int parentWidth, int parentHeight) {
            super.initialize(width, height, parentWidth, parentHeight);
        }

        @Override
        public boolean willChangeBounds() {
            return true;
        }
    }

    private void getPassword()
    {
        String thisPassword = "";

        for(int i = 0; i< 10; i++)
        {
            thisPassword = thisPassword + (down_password[i]+up_password[i]*10);
            thisPassword+= "-";
        }
        if(first_time)
        {
            first_time = false;
            firstPassword = thisPassword.substring(0, thisPassword.length()-1);
            txt_counter.setText("Enter the password again:");
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    startAnimation();
                }
            },1000);
        }
        else
        {
            secondPassword = thisPassword.substring(0, thisPassword.length()-1);
            if(firstPassword.equals(secondPassword))
            {
                txt_counter.setText("Password set!");
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        uploadFlic();
                    }
                },1000);
            }
            else
            {
                btn_start.setEnabled(true);
                first_time = true;
                firstPassword = "";
                secondPassword = "";
                txt_counter.setText("Passwords don't match, please try again!");
            }
        }
    }

    private void uploadFlic()
    {
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.put("flic_id", connectedButton);
        params.put("password", secondPassword);
        params.put("account_id", account_id);
        params.put("username", username);
        params.put("other_password", other_password);

        client.get("http://pan0166.panoulu.net/flic_pay/addFlic.php", params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                Toast.makeText(getApplicationContext(), "Flic Pay added",Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getApplicationContext(), UserInfo.class);
                intent.putExtra("ACTION","AFTER_ADDING");
                intent.putExtra("username",username);
                intent.putExtra("password",other_password);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Log.d("values",statusCode+"");
                Log.d("values",new String(responseBody));
                Toast.makeText(getApplicationContext(), "Failed to add Flic Pay",Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }

    private boolean verifyPassword()
    {
        int down = 0;
        int up = 0;
        for(int i = 0; i< 10; i++)
        {
            up+=up_password[i];
            down+=down_password[i];
        }
        Log.d("pass", up + " + " + down);
        if(up == 3 && down == 3)
        {
            return true;
        }
        else
        {
            return false;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        FlicButton button = manager.completeGrabButton(requestCode, resultCode, data);
        connectedButton = button.getButtonId();
        setButtonCallback(button);
    }

}

