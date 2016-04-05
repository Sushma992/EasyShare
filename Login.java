package com.example.sushma.easyshare;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.support.v7.app.ActionBar;
import android.view.MenuItem;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.TextView;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import android.text.Html;
import android.net.Uri;
import android.text.method.LinkMovementMethod;

/**
 * Created by Akhil on 2/24/2016.
 */
public class Login extends  MainActivity implements View.OnClickListener {
    public static final String USER_NAME = "USER_NAME";
    Button login, forgetpassword;
    EditText username, password;
    String login_name, login_pass;
  //  AlertDialog.Builder   alertDialog1,  alertDialog2,  alertDialog3;
    public static final  String LOGIN_PREFS="Login_Name";
    //String user_name;
    private TextInputLayout inputLayoutusername1, inputLayoutpassword1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        inputLayoutusername1 = (TextInputLayout) findViewById(R.id.input_layout_username1);
        inputLayoutpassword1 = (TextInputLayout) findViewById(R.id.input_layout_password1);
        username = (EditText) findViewById(R.id.username);
        password = (EditText) findViewById(R.id.password);

        ActionBar actionBar =getSupportActionBar();
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);


        login = (Button) findViewById(R.id.buttonLogin);
        login.setOnClickListener(this);

        forgetpassword = (Button) findViewById(R.id.forgetpswd);
        forgetpassword.setOnClickListener(this);
    }

    public void change() {
        login_name = username.getText().toString();
        changepwd(login_name);
    }

    private void changepwd(String login_name) {
        class backgroundTask
                extends AsyncTask<String, Void, String> {
            AlertDialog alertDialog;
            ProgressDialog loading;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(Login.this, "Please Wait", null, true, true);
                alertDialog = new AlertDialog.Builder(Login.this).create();
                alertDialog.setTitle("password change request");

            }

            protected String doInBackground(String... params) {

                String reset_url = "http://snapper.esy.es/change.php";
                String login_name = params[0];

                try

                {
                    URL url = new URL(reset_url);
                    HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                    httpURLConnection.setRequestMethod("POST");
                    httpURLConnection.setDoOutput(true);
                    OutputStream OS = httpURLConnection.getOutputStream();
                    BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(OS, "UTF-8"));
                    String data = URLEncoder.encode("login_name", "UTF-8") + "=" + URLEncoder.encode(login_name, "UTF-8");

                    bufferedWriter.write(data);

                    bufferedWriter.flush();
                    bufferedWriter.close();
                    OS.close();
                    InputStream IS = httpURLConnection.getInputStream();
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(IS, "iso-8859-1"));
                    String response = "";
                    String line = "";
                    while ((line = bufferedReader.readLine()) != null) {
                        response = line;

                    }
                    bufferedReader.close();
                    IS.close();
                    httpURLConnection.disconnect();
                    return response;

                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                return null;
            }

            @Override
            protected void onPostExecute(String result) {
                super.onPostExecute(result);
                loading.dismiss();
                alertDialog.setMessage(result);
                alertDialog.show();
            }
        }

        backgroundTask bg = new backgroundTask();
        bg.execute(login_name);
    }



    public void userLogin1() {
        //Context ctx;
        AlertDialog alertDialog=new AlertDialog.Builder(this).create();
        login_name = username.getText().toString();
        login_pass = password.getText().toString();
        userLogin(login_name, login_pass);
    }
    private void userLogin(final String login_name, String login_pass)
    {
        class backgroundTask
                extends AsyncTask<String,Void,String> {
            AlertDialog alertDialog;
            ProgressDialog loading;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(Login.this, "Please Wait",null, true, true);
                alertDialog = new AlertDialog.Builder(Login.this).create();
                alertDialog.setTitle("login information");

            }

            protected  String doInBackground(String... params)
            {

                String login_url = "http://snapper.esy.es/login.php";
                String login_name = params[0];
                String login_pass = params[1];

                try

                {
                    URL url = new URL(login_url);
                    HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                    httpURLConnection.setRequestMethod("POST");
                    httpURLConnection.setDoOutput(true);
                    OutputStream OS = httpURLConnection.getOutputStream();
                    BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(OS, "UTF-8"));
                    String data = URLEncoder.encode("login_name", "UTF-8") + "=" + URLEncoder.encode(login_name, "UTF-8") + "&" +
                            URLEncoder.encode("login_pass", "UTF-8") + "=" + URLEncoder.encode(login_pass, "UTF-8");
                    bufferedWriter.write(data);

                    bufferedWriter.flush();
                    bufferedWriter.close();
                    OS.close();
                    InputStream IS = httpURLConnection.getInputStream();
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(IS, "iso-8859-1"));
                    String response = "";
                    String line = "";
                    while ((line = bufferedReader.readLine()) != null) {
                        response = line;

                    }
                    bufferedReader.close();
                    IS.close();
                    httpURLConnection.disconnect();
                    return response;

                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                return null;
            }

            @Override
            protected void onPostExecute(String result) {
                super.onPostExecute(result);
                loading.dismiss();
                if(result.equalsIgnoreCase("Login Success Welcome")){
                     // Toast.makeText(Login.this,result,Toast.LENGTH_LONG).show();
                   alertDialog.setMessage(result);
                   alertDialog.show();
                    SharedPreferences.Editor editor=getSharedPreferences(LOGIN_PREFS,MODE_PRIVATE).edit();
                    editor.putString("username",login_name);
                    editor.commit();
                    Intent i_snap=new Intent(Login.this,HomePage.class);
                    i_snap.putExtra(USER_NAME,login_name);
                    startActivity(i_snap);
                    finish();

                }
                else {
                    alertDialog.setMessage(result);
                    alertDialog.show();
                }
            }
        }

        backgroundTask bg=new backgroundTask();
        bg.execute(login_name, login_pass);
    }

    public void onClick(View v)
    {
        if(v==login){
            /*if(username.getText().toString()==""){
               alertDialog1 = new AlertDialog.Builder(Login.this);
                // Setting Dialog Title
                alertDialog1.setTitle("Authentication Error");

                // Setting Dialog Message
                alertDialog1.setMessage("Please Enter your Username ");
                alertDialog1.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        finish();
                    }
                });
                alertDialog1.show();
            }
            if(password.getText().toString()==""){
                alertDialog2 = new AlertDialog.Builder(Login.this);
                // Setting Dialog Title
                alertDialog2.setTitle("Authentication Error");

                // Setting Dialog Message
                alertDialog2.setMessage("Please Enter your Password ");
                alertDialog2.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        finish();
                    }
                });
                alertDialog2.show();
            }*/
            userLogin1();
        }
        else if(v==forgetpassword)
        {
          /*  if(username.getText().toString()==""){
               alertDialog3 = new AlertDialog.Builder(Login.this);
                // Setting Dialog Title
                alertDialog3.setTitle("Authentication Error");

                // Setting Dialog Message
                alertDialog3.setMessage("Please Enter  your Username ");
                alertDialog3.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        finish();
                    }
                });
                alertDialog3.show();
            }*/
            change();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                                onBackPressed();
                           return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void onBackPressed()
    {
        Intent i= new Intent(this,MainActivity.class);
        startActivity(i);
        finish();
    }
}


