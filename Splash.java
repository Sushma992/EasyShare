package com.example.sushma.easyshare;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.Toast;


/**
 * Created by sushma on 26/02/2016.
 */
public class Splash extends AppCompatActivity {
public static  final String LOGIN_PREFS="Login_Name";
    private final int SPLASH_DISPLAY_LENGHT = 3000;
    SharedPreferences prefs;
    AlertDialog alertDialog;
    boolean check;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);

      //  final ImageView iv = (ImageView) findViewById(R.id.splash);

         prefs = getSharedPreferences(LOGIN_PREFS, MODE_PRIVATE);
        check = checkInternetConnection();

            new Handler().postDelayed(new Runnable() {
                public void run() {
                    if (check) {
                        String restoredText = prefs.getString("username", null);
                       if (restoredText == null) {
                            Intent i = new Intent(Splash.this, MainActivity.class);
                            startActivity(i);
                            finish();
                        } else {
                           Intent j = new Intent(Splash.this, HomePage.class);
                           startActivity(j);
                            finish();
                        }
                    } else {
                        AlertDialog.Builder alertDialog = new AlertDialog.Builder(Splash.this);
                        // Setting Dialog Title
                        alertDialog.setTitle("Network Error");

                        alertDialog.setIcon(R.drawable.error);
                                // Setting Dialog Message
                        alertDialog.setMessage("Please Check Your Internet Connectivity and Try Again ");
                        alertDialog.setPositiveButton("Ok",new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                                finish();
                            }
                        });

                        alertDialog.show();

                    }
                }
            }, SPLASH_DISPLAY_LENGHT);

    }
    public  boolean checkInternetConnection() {

        ConnectivityManager connec = (ConnectivityManager) getSystemService(getApplicationContext().CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connec.getActiveNetworkInfo();
        if (networkInfo == null || !networkInfo.isConnected()) {
            return false;
        }
        else {

            return true;
        }

    }

}
