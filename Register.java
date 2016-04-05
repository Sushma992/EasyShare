package com.example.sushma.easyshare;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.ToggleButton;
import android.text.TextUtils;
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
import java.util.Locale;
import android.support.design.widget.TextInputLayout;
import android.view.WindowManager;

import com.pushbots.push.Pushbots;

public class Register extends MainActivity implements View.OnClickListener {
    EditText editTextName;
    EditText editTextUsername;
    EditText editTextPassword;
    EditText editTextEmail, editTextMobileno, editTextAccountno;
    String name, username, pass, email, mobileno, acno;
    Button submit,reset;
    private TextInputLayout inputLayoutName,inputLayoutusername,inputLayoutpassword,inputLayoutemail,inputLayoutmobileno,inputLayoutaccountno;
    String emailpattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);
        ActionBar actionBar =getSupportActionBar();
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        inputLayoutName = (TextInputLayout) findViewById(R.id.input_layout_name);
        inputLayoutusername=(TextInputLayout)findViewById(R.id.input_layout_username);
        inputLayoutpassword = (TextInputLayout) findViewById(R.id.input_layout_password);
        inputLayoutemail=(TextInputLayout)findViewById(R.id.input_layout_email);
        inputLayoutmobileno = (TextInputLayout) findViewById(R.id.input_layout_mobileno);
        inputLayoutaccountno=(TextInputLayout)findViewById(R.id.input_layout_accountno);


        editTextName = (EditText) findViewById(R.id.editTextName);
        editTextUsername = (EditText) findViewById(R.id.editTextUserName);
        editTextPassword = (EditText) findViewById(R.id.editTextPassword);
        editTextEmail = (EditText) findViewById(R.id.editTextEmail);
        editTextAccountno = (EditText) findViewById(R.id.editTextAccountno);
        editTextMobileno = (EditText) findViewById(R.id.editTextMobileno);

         reset=(Button)findViewById(R.id.reset);
        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editTextName.setText("");
                editTextUsername.setText("");
                editTextAccountno.setText("");
                editTextPassword.setText("");
                editTextEmail.setText("");
                editTextMobileno.setText("");
            }
        });



        submit = (Button) findViewById(R.id.buttonSubmit);
       // Toast.makeText(getApplicationContext(), "Entered into register", Toast.LENGTH_SHORT).show();
        submit.setOnClickListener(this);


        editTextAccountno.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (editTextAccountno.getText().length() < 11) {
                    editTextAccountno.setError("Invalid Account number");
                }
            }
        });
        editTextPassword.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (editTextPassword.getText().length() < 8) {
                    editTextPassword.setError("password should be minimum 8 letters");
                }
            }
        });

        editTextEmail.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!(editTextEmail.getText().toString().matches(emailpattern))) {
                    editTextEmail.setError("Invalid Email");
                }
            }
        });

        editTextMobileno.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (editTextMobileno.getText().length() < 9) {
                    editTextMobileno.setError("Invalid Mobile number");
                }
            }
        });

       Pushbots.sharedInstance().init(this);
       Pushbots.sharedInstance().setCustomHandler(customHandler.class);


    }

    public void userRegister1() {
        //Context ctx;
        //AlertDialog alertDialog=new AlertDialog.Builder(ctx).create();
        //     AlertDialog alertDialog=new AlertDialog.Builder(this).create();
        name = editTextName.getText().toString();
        username = editTextUsername.getText().toString();
        pass = editTextPassword.getText().toString();
        email = editTextEmail.getText().toString().toLowerCase();
        mobileno = editTextMobileno.getText().toString();
        acno = editTextAccountno.getText().toString();
        Pushbots.sharedInstance().setAlias(mobileno);
        userRegister(name, username, pass, email, mobileno, acno);
    }

    private void userRegister(String name, String username, String pass, String email, String mobileno, String acno) {
        class backgroundTask
                extends AsyncTask<String, Void, String> {
            //AlertDialog alertDialog;
            ProgressDialog loading;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(Register.this, "Please Wait", null, true, true);

            }
            protected String doInBackground(String... params) {
                String reg_url = "http://snapper.esy.es/123.php";

                String name = params[0];
                String username = params[1];
                String acno = params[2];
                String pass = params[3];
                String email = params[4];
                String mobileno = params[5];
                try {

                    if (name.isEmpty() || username.isEmpty() || acno.isEmpty() || pass.isEmpty() || email.equalsIgnoreCase("") ||
                            mobileno.isEmpty())
                        return "Enter all Fields";
                    URL url = new URL(reg_url);
                    HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                    httpURLConnection.setRequestMethod("POST");
                    httpURLConnection.setDoOutput(true);
                    OutputStream OS = httpURLConnection.getOutputStream();
                    BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(OS, "UTF-8"));
                    String data = URLEncoder.encode("name", "UTF-8") + "=" + URLEncoder.encode(name, "UTF-8") + "&" +
                            URLEncoder.encode("username", "UTF-8") + "=" + URLEncoder.encode(username, "UTF-8") + "&" +
                            URLEncoder.encode("acno", "UTF-8") + "=" + URLEncoder.encode(acno, "UTF-8") + "&" +
                            URLEncoder.encode("pass", "UTF-8") + "=" + URLEncoder.encode(pass, "UTF-8") + "&" +
                            URLEncoder.encode("email", "UTF-8") + "=" + URLEncoder.encode(email, "UTF-8") + "&" +
                            URLEncoder.encode("mobileno", "UTF-8") + "=" + URLEncoder.encode(mobileno, "UTF-8");
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
                if(result.toLowerCase().contains("registration success"))
                {
                    Toast.makeText(Register.this, "Registered Sucessfully", Toast.LENGTH_LONG).show();
                    Intent launchActivity1 = new Intent(Register.this, Login.class);
                    startActivity(launchActivity1);
                    finish();

                }

                else {
                        Toast.makeText(Register.this, result, Toast.LENGTH_LONG).show();
                }
            }
        }
        backgroundTask bg = new backgroundTask();
        bg.execute(name, username, pass, email, mobileno, acno);
    }


    public void onClick(View v) {
        if (v == submit){
        userRegister1();
    }
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