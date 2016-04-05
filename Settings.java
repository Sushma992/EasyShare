package com.example.sushma.easyshare;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.support.v7.app.AppCompatActivity;
import android.widget.EditText;

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

/**
 * Created by sushma on 28/02/2016.
 */
public class Settings extends AppCompatActivity{
    String name;
    Button change;
    EditText cp, np, cop,mail;
    Button delete;
    String email,password,newpd,conpd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings);

        ActionBar actionBar =getSupportActionBar();
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        change=(Button)findViewById(R.id.buttonChange);
        delete=(Button)findViewById(R.id.buttonDelete);

        mail=(EditText)findViewById(R.id.editTextEmail);
        cp=(EditText)findViewById(R.id.editTextPassword);
        np=(EditText)findViewById(R.id.editTextNew);
        cop=(EditText)findViewById(R.id.editTextConfirm);
        change.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                change();
            }
        });
        delete.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                delete();
            }
        });

    }
    public  void delete()
    {
        email=mail.getText().toString();
        password=cp.getText().toString();
        deleteAcc(email, password);
    }
    public void deleteAcc(String email,String password)
    {
        class backgroundTask
                extends AsyncTask<String, Void, String> {
            AlertDialog alertDialog;
            ProgressDialog loading;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(Settings.this, "Please Wait", null, true, true);
                alertDialog = new AlertDialog.Builder(Settings.this).create();
                alertDialog.setTitle("Delete account request");

            }

            protected String doInBackground(String... params) {

                String reset_url = "http://snapper.esy.es/deleteaccount.php";
                String email = params[0];
                String password=params[1];


                try

                {

                    URL url = new URL(reset_url);
                    HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                    httpURLConnection.setRequestMethod("POST");
                    httpURLConnection.setDoOutput(true);
                    OutputStream OS = httpURLConnection.getOutputStream();
                    BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(OS, "UTF-8"));
                    String data = URLEncoder.encode("email", "UTF-8") + "=" + URLEncoder.encode(email, "UTF-8") + "&" +
                            URLEncoder.encode("password", "UTF-8") + "=" + URLEncoder.encode(password, "UTF-8");


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
        bg.execute(email, password);
    }
    public void change()
    {
        email=mail.getText().toString();
        password=cp.getText().toString();
        newpd=np.getText().toString();
        conpd=cop.getText().toString();
        changepwd(email, password, newpd, conpd);
    }

    public void changepwd(String email,String password,String newpassword,String confirmpassword)
    {
        class backgroundTask
                extends AsyncTask<String, Void, String> {
            AlertDialog alertDialog;
            ProgressDialog loading;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(Settings.this, "Please Wait", null, true, true);
                alertDialog = new AlertDialog.Builder(Settings.this).create();
                alertDialog.setTitle("password change request");

            }

            protected String doInBackground(String... params) {

                String reset_url = "http://snapper.esy.es/changepwd.php";
                String email = params[0];
                String password=params[1];
                String newpassword=params[2];
                String confirmpassword=params[3];

                try

                {
                    URL url = new URL(reset_url);
                    HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                    httpURLConnection.setRequestMethod("POST");
                    httpURLConnection.setDoOutput(true);
                    OutputStream OS = httpURLConnection.getOutputStream();
                    BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(OS, "UTF-8"));
                    String data = URLEncoder.encode("email", "UTF-8") + "=" + URLEncoder.encode(email, "UTF-8") + "&" +
                            URLEncoder.encode("password", "UTF-8") + "=" + URLEncoder.encode(password, "UTF-8")+ "&" +
                            URLEncoder.encode("newpassword", "UTF-8") + "=" + URLEncoder.encode(newpassword, "UTF-8")+ "&" +
                            URLEncoder.encode("confirmpassword", "UTF-8") + "=" + URLEncoder.encode(confirmpassword, "UTF-8");

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
        bg.execute(email, password, newpassword, confirmpassword);

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

        if (getFragmentManager().getBackStackEntryCount() == 0) {
            this.finish();
        } else {
            getFragmentManager().popBackStack();
        }
    }
     }

