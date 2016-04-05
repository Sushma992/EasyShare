package com.example.sushma.easyshare;
import android.content.SharedPreferences;
import android.support.v7.app.ActionBar;
import android.view.View;
import android.widget.Button;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.content.Intent;
import android.widget.ImageView;
import android.text.SpannableString;
import android.graphics.Color;
import android.text.style.ForegroundColorSpan;
import android.widget.Toast;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.app.AlertDialog;
import android.app.Activity;

import com.pushbots.push.Pushbots;

public class MainActivity extends AppCompatActivity {
   Button login,register;
    String msg;
    AlertDialog alertDialog;
    AlertDialog alert;
   // public static final String LOGIN_PREFS="Login_Name";
    boolean check;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ActionBar actionBar = getSupportActionBar();
      //  actionBar.setLogo(R.drawable.appicon);
       // actionBar.setDisplayUseLogoEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);

        alertDialog=new AlertDialog.Builder(this).create();
       login = (Button) findViewById(R.id.Loginuser);
      login.setOnClickListener(new View.OnClickListener() {
          public void onClick(View v) {
            //  check = checkInternetConnection();
             // Toast.makeText(getApplicationContext(), "validating connectivity", Toast.LENGTH_SHORT).show();
            //  if (check) {
                  // Toast.makeText(getApplicationContext(), "Login form", Toast.LENGTH_SHORT).show();
                  startActivity(new Intent(MainActivity.this, Login.class));
                  finish();
            //  } else
                  //login.setEnabled(false);
          }
      });
        register = (Button) findViewById(R.id.buttonRegister);
          register.setOnClickListener(new View.OnClickListener() {
              public void onClick(View v) {
                  // check = checkInternetConnection();
                //  Toast.makeText(getApplicationContext(), "validating connectivity", Toast.LENGTH_SHORT).show();
                //  if (check) {
                    //  Toast.makeText(getApplicationContext(), "Registration form", Toast.LENGTH_SHORT).show();
                      startActivity(new Intent(MainActivity.this, Register.class));
                  finish();
                //  }
                 // register.setEnabled(false);
              }
          });

     /*   SharedPreferences prefs=getSharedPreferences(LOGIN_PREFS,MODE_PRIVATE);
        String restoredText=prefs.getString("username",null);
        if(restoredText == null){
            Intent i= new Intent(this,Login.class);
            startActivity(i);
        }
        else {
            Intent i= new Intent(this,HomePage.class);
            startActivity(i);
        }*/
        Pushbots.sharedInstance().init(this);
        Pushbots.sharedInstance().setCustomHandler(customHandler.class);
        Bundle extras = getIntent().getExtras();
        if (null != extras && getIntent().getExtras().containsKey("bigText")) {


            alert= new AlertDialog.Builder(MainActivity.this).create();
            alert.setTitle("Notification From Easyshare");

            msg = extras.getString("bigText");
            alert.setMessage(msg);
            alert.show();

        }

    }
   /* public boolean checkInternetConnection() {

        ConnectivityManager connec = (ConnectivityManager) getSystemService(getApplicationContext().CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connec.getActiveNetworkInfo();
        if (networkInfo == null || !networkInfo.isConnected()) {
           // Toast.makeText(getApplicationContext(), "Please check your network connectivity and try again",
           //         Toast.LENGTH_LONG).show();
            alertDialog.setMessage("Please check your network connectivity and try again");
            alertDialog.show();
            return false;
            // Check for network connections
        }
        else {
           // Toast.makeText(getApplicationContext(), "Connected",
            //     Toast.LENGTH_LONG).show();
            alertDialog.setMessage("connected");
            alertDialog.show();
            return true;
        }

    }*/

    @Override
    public void onBackPressed() {
        finish();
    }



}
