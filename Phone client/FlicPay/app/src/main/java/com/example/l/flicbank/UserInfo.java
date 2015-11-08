package com.example.l.flicbank;

import android.app.ActionBar;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.*;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

public class UserInfo extends ActionBarActivity {

    private String infostatus;
    private String username;
    private String password;
    private String infoaccount;
    private String infoname;
    private String infobalance;
    private String infoflicid;
    private TextView name;
    private TextView account;
    private TextView balance;
    private TextView flicid;
    private TextView status;
    private Button lockflic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);

        name = (TextView)findViewById(R.id.get_name);
        account = (TextView)findViewById(R.id.get_account);
        balance = (TextView)findViewById(R.id.get_balance);
        flicid = (TextView)findViewById(R.id.get_flic_id);
        status = (TextView)findViewById(R.id.get_status);
        lockflic = (Button)findViewById(R.id.disable_btn);

        Intent intent = getIntent();
        if(intent.getStringExtra("ACTION")!=null)
        {
            username = intent.getStringExtra("username");
            password = intent.getStringExtra("password");
            AsyncHttpClient client = new AsyncHttpClient();
            RequestParams params = new RequestParams();
            params.put("username", username);
            params.put("password", password);

            client.get("http://pan0166.panoulu.net/flic_pay/getAccount.php", params, new AsyncHttpResponseHandler() {

                @Override
                public void onStart() {
                    // called before request is started
                }

                @Override
                public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, byte[] responseBody) {
                    try {

                        String response = (new String(responseBody, "UTF-8"));
                        Log.d("dfgh", response);
                        JSONObject jObject = new JSONObject(response);

                        infoname = jObject.getString("name");
                        infoaccount = jObject.getString("account_id");
                        infobalance = jObject.getString("balance");

                        infoflicid = jObject.getString("flic_id");
                        infostatus = jObject.getString("status");

                        name.setText(infoname);
                        account.setText(infoaccount);
                        balance.setText(infobalance);
                        flicid.setText(infoflicid);

                        if(infostatus.equals("1")){
                            status.setText("ACTIVATED");
                            lockflic.setText("LOCK FLICPAY");
                        }
                        else{
                            status.setText("LOCKED");
                            lockflic.setText("ACTIVATE FLICPAY");
                        }

                    } catch (UnsupportedEncodingException e1) {
                        e1.printStackTrace();

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }

                @Override
                public void onFailure(int statusCode, cz.msebera.android.httpclient.Header[] headers, byte[] responseBody, Throwable error) {
                    Toast.makeText(getApplicationContext(),"Invalid username or password.", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onRetry(int retryNo) {
                    // called when request is retried
                }
            });
        }else
        {
            username = intent.getExtras().getString("username");
            password = intent.getExtras().getString("password");
            infoname = intent.getExtras().getString("info_name");
            infoaccount = intent.getExtras().getString("info_account");
            infobalance = intent.getExtras().getString("info_balance");
            infoflicid= intent.getExtras().getString("flic_id");
            infostatus = intent.getExtras().getString("status");
            if(infoflicid!=null)
            {
                Log.d(" fsd",infoflicid);
                Log.d(" fsd",infostatus);
            }
            else
            {
                Log.d("fds"," no flic");
            }

            name.setText(infoname);
            account.setText(infoaccount);
            balance.setText(infobalance);
            flicid.setText(infoflicid);

            if(infostatus.equals("1")){
                status.setText("ACTIVATED");
                lockflic.setText("LOCK FLICPAY");
            }
            else{
                status.setText("LOCKED");
                lockflic.setText("ACTIVATE FLICPAY");
            }
        }



        lockflic.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                AsyncHttpClient client = new AsyncHttpClient();
                RequestParams params = new RequestParams();
                params.put("flic_id", infoflicid);
                Log.d("TAG", infoflicid);
                params.put("account_id", infoaccount);
                Log.d("TAG", infoaccount);
                if (infostatus.equals("1")) {
                    params.put("status", "0");
                } else {
                    params.put("status", "1");
                }

                params.put("username", username);
                Log.d("TAG", username);
                params.put("other_password", password);
                Log.d("TAG", password);

                client.get("http://pan0166.panoulu.net/flic_pay/changeFlicStatus.php", params, new AsyncHttpResponseHandler() {

                    @Override
                    public void onStart() {
                        // called before request is started
                    }

                    @Override
                    public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, byte[] responseBody) {
                        if (infostatus.equals("1")) {
                            infostatus = "0";
                            status.setText("LOCKED");
                            lockflic.setText("ACTIVATE FLICPAY");
                        } else {
                            infostatus = "1";
                            status.setText("ACTIVATED");
                            lockflic.setText("LOCK FLICPAY");
                        }

                    }

                    @Override
                    public void onFailure(int statusCode, cz.msebera.android.httpclient.Header[] headers, byte[] responseBody, Throwable error) {
                        Log.d("TAG",statusCode+"");
                        Toast.makeText(getApplicationContext(),"Update failed.", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onRetry(int retryNo) {
                        // called when request is retried
                    }
                });
//                Intent intent = new Intent(getApplicationContext(),UserInfo.class);
//                startActivity(intent);
            }
        });


    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
