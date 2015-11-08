package com.example.l.flicbank;

import android.app.ActionBar;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Message;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import android.os.Handler;
import android.widget.EditText;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Array;

import io.flic.lib.FlicButton;
import io.flic.lib.FlicButtonCallback;
import io.flic.lib.FlicButtonCallbackFlags;
import io.flic.lib.FlicManager;
import io.flic.lib.FlicManagerInitializedCallback;


public class MainActivity extends ActionBarActivity {

    private Button Button_Send;
    private EditText login_username;
    private EditText login_password;
    String TAG ="flicbank";

    private void stuffToBeExecuted(){

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        login_username = (EditText)findViewById(R.id.login_username);
        login_password = (EditText)findViewById(R.id.login_password);
        login_password.setTransformationMethod(new PasswordTransformationMethod());

    Button_Send = (Button) findViewById(R.id.submit_btn);
    Button_Send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "Click input pin button, start handler");
                String login_user = login_username.getText().toString();
                String login_pw = login_password.getText().toString();

                if (login_user.length() > 0 && login_pw.length() > 0) {
                    AsyncHttpClient client = new AsyncHttpClient();
                    RequestParams params = new RequestParams();
                    params.put("username", login_user);
                    params.put("password", login_pw);

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

                                String name = jObject.getString("name");
                                String account_id = jObject.getString("account_id");
                                String balance = jObject.getString("balance");

                                if (!jObject.has("flic_id")) {
                                    Intent i = new Intent(getApplicationContext(), AddFlic.class);
                                    i.putExtra("username", login_username.getText().toString());
                                    i.putExtra("password", login_password.getText().toString());
                                    i.putExtra("info_name", name);
                                    i.putExtra("info_account", account_id);
                                    i.putExtra("info_balance", balance);
                                    Log.d("gdsg","no flic_id");
                                    startActivity(i);
                                } else {
                                    String flic_id = jObject.getString("flic_id");
                                    String status = jObject.getString("status");
                                    Log.d("gdsg","has flic_id: "+ flic_id+" "+ status);
                                    Intent info = new Intent(getApplicationContext(), UserInfo.class);

                                    info.putExtra("username", login_username.getText().toString());
                                    info.putExtra("password", login_password.getText().toString());
                                    info.putExtra("info_name", name);
                                    info.putExtra("info_account", account_id);
                                    info.putExtra("info_balance", balance);
                                    info.putExtra("flic_id", flic_id);
                                    info.putExtra("status", status);
                                    startActivity(info);
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
//                Intent intent = new Intent(getApplicationContext(),UserInfo.class);
//                startActivity(intent);
                } else {
                    Toast.makeText(getApplicationContext(), "Please fill all the fields.", Toast.LENGTH_SHORT).show();
                }
            }
        });




    }



//    private void setButtonCallback(FlicButton button) {
//        button.removeAllFlicButtonCallbacks();
//        button.addFlicButtonCallback(buttonCallback);
//        button.setFlicButtonCallbackFlags(FlicButtonCallbackFlags.UP_OR_DOWN);
//    }


    @Override
    protected void onDestroy() {
//        FlicManager.destroyInstance();
        super.onDestroy();
    }
}
