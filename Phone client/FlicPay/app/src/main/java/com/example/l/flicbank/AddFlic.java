package com.example.l.flicbank;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import io.flic.lib.FlicButton;
import io.flic.lib.FlicButtonCallback;
import io.flic.lib.FlicManager;
import io.flic.lib.FlicManagerInitializedCallback;

public class AddFlic extends AppCompatActivity {

    private FlicManager manager;


    private FlicButtonCallback buttonCallback = new FlicButtonCallback() {
        @Override
        public void onButtonUpOrDown(FlicButton button, boolean wasQueued, int timeDiff, boolean isUp, boolean isDown) {
            final String text = button + " was " + (isDown ? "pressed" : "released");
            Log.d("ghj", text);
        }
    };
    private String addname;
    private String addaccount;
    private String addbalance;
    private String addusername;
    private String addpassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_flic);

        TextView name = (TextView)findViewById(R.id.add_name);
        TextView account = (TextView)findViewById(R.id.add_account);
        TextView balance = (TextView)findViewById(R.id.add_balance);
        Button add_flic = (Button)findViewById(R.id.set_flic_btn);
        add_flic.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
//            setButtonCallback(button);
                Intent EditFlict = new Intent(getApplicationContext(), Instruction.class);
                EditFlict.putExtra("account_id",addaccount);
                EditFlict.putExtra("username",addusername);
                EditFlict.putExtra("password",addpassword);
                startActivity(EditFlict);
            }
        });

        Intent intent = getIntent();
        addname = intent.getExtras().getString("info_name");
        addaccount = intent.getExtras().getString("info_account");
        addbalance = intent.getExtras().getString("info_balance");
        addusername = intent.getExtras().getString("username");
        addpassword = intent.getExtras().getString("password");

        name.setText(addname);
        account.setText(addaccount);
        balance.setText(addbalance);

    }

}
