package com.example.sushma.easyshare;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.provider.ContactsContract.CommonDataKinds.Phone;
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

public class Payment extends AppCompatActivity {
    private final  int  CONTACT_PICKER_RESULT=1;
 Button pay,sendiou,contacts;
    EditText name1,phno,username;
    String result;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       setContentView(R.layout.payment);
        ActionBar actionBar =getSupportActionBar();
        actionBar.setDisplayShowHomeEnabled(true);
       actionBar.setDisplayHomeAsUpEnabled(true);
       // actionBar.setIcon(R.mipmap.paybill);

        name1=(EditText)findViewById(R.id.editTextname);
        phno=(EditText)findViewById(R.id.editTextphno);
        pay=(Button)findViewById(R.id.paybutton);
        sendiou=(Button)findViewById(R.id.sendiou);
        contacts=(Button)findViewById(R.id.buttoncontact);


        sendiou.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final    String amt=getIntent().getStringExtra("text");
                final     String mobile3 = phno.getText().toString();

                LayoutInflater factory = LayoutInflater.from(Payment.this);

                final View textEntryView = factory.inflate(R.layout.alertdialog, null);

                final EditText input1 = (EditText) textEntryView.findViewById(R.id.editTextalert1);
                final EditText input2 = (EditText) textEntryView.findViewById(R.id.editTextalert2);

                final AlertDialog.Builder alert = new AlertDialog.Builder(Payment.this);
                alert.setTitle("Security Alert").setView(textEntryView).setPositiveButton("Ok",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,
                                                int whichButton) {

                                Log.i("AlertDialog", "TextEntry 1 Entered " + input1.getText().toString());
                                Log.i("AlertDialog", "TextEntry 2 Entered " + input2.getText().toString());

                                String username3 = input1.getText().toString();
                                String pass = input2.getText().toString();
                                sendioumsg(username3, amt, pass, mobile3);
                            }
                        }).setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,
                                                int whichButton) {

                            }
                        });
                alert.show();

            }
        });

    }
    protected void onActivityResult(int reqCode, int resultCode, Intent data) {
        super.onActivityResult(reqCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (reqCode) {
                case CONTACT_PICKER_RESULT:
                    Cursor cursor = null;
                    String number = "";
                    String name="";
                    try {
                        Uri result = data.getData();
                        // get the id from the uri
                        String id = result.getLastPathSegment();
                        // query
                        cursor = getContentResolver().query(
                                ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                                null,
                                ContactsContract.CommonDataKinds.Phone._ID
                                        + " = ? ", new String[]{id}, null);

                        while (cursor.moveToNext()) {
                            int numberIdx = cursor.getColumnIndex(Phone.DATA);
                            //String nameIdx=cursor.getColumnIndexOrThrow(ContactsContract.Contacts.DISPLAY_NAME);
                            if (cursor.moveToFirst()) {
                                number = cursor.getString(numberIdx);
                                int digits = 10;
                             String result1 = number.replaceAll("[^0-9]","");
                                if(result1.length()>digits)
                                {
                                    int phno = result1.length() - digits;
                                    result1=result1.substring(phno);
                                }
                                phno.setText(result1);
                                name = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                            } else {
                                Toast.makeText(getApplicationContext(), "failed to retrieve info", Toast.LENGTH_SHORT).show();
                            }
                            //result2[cursor.getPosition()] = name + "-" +" "+number;
                        }
                    }
                    catch(Exception e){
                        e.printStackTrace();
                    }finally{
                        if (cursor != null) {
                            cursor.close();
                        } else {
                        }
                    }

                    name1.setText(name);
            }
        }

    }

    public void openContacts(View view){
        // Toast.makeText(getApplicationContext(),"button clicked",Toast.LENGTH_SHORT).show();
        Intent intent=new Intent(Intent.ACTION_PICK, ContactsContract.CommonDataKinds.Phone.CONTENT_URI);
        startActivityForResult(intent, CONTACT_PICKER_RESULT);
    }
    public void pay(View view){
     final    String amt=getIntent().getStringExtra("text");
    final     String mobile3 = phno.getText().toString();

        LayoutInflater factory = LayoutInflater.from(this);

        final View textEntryView = factory.inflate(R.layout.alertdialog, null);

        final EditText input1 = (EditText) textEntryView.findViewById(R.id.editTextalert1);
        final EditText input2 = (EditText) textEntryView.findViewById(R.id.editTextalert2);

        final AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle("Security Alert").setView(textEntryView).setPositiveButton("Ok",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,
                                        int whichButton) {

                        Log.i("AlertDialog", "TextEntry 1 Entered " + input1.getText().toString());
                        Log.i("AlertDialog", "TextEntry 2 Entered " + input2.getText().toString());

                        String username3 = input1.getText().toString();
                        String pass = input2.getText().toString();
                        payBillMethod(amt, mobile3, username3, pass);
                    }
                }).setNegativeButton("Cancel",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,
                                        int whichButton) {
     /*
     * User clicked cancel so do some stuff
     */
                    }
                });
        alert.show();

       // payBillMethod(amt,mobile3,username3,pass);
    }
    private void payBillMethod(final String amount, final String mobile,final String username,final String password) {
        class UserLoginClass extends AsyncTask<String, Void, String> {
            ProgressDialog loading;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(Payment.this, "Please Wait", null, true, true);
            }

            @Override
            protected String doInBackground(String... params) {
                String amount = params[0];
                String mobile = params[1];
                String login_name=params[2];
                String login_password=params[3];
                try {
                    String reg_url="http://snapper.esy.es/paybill.php";
                    URL url = new URL(reg_url);
                    HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                    httpURLConnection.setRequestMethod("POST");
                    httpURLConnection.setDoOutput(true);
                    OutputStream OS = httpURLConnection.getOutputStream();
                    BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(OS, "UTF-8"));
                    String data = URLEncoder.encode("login_name", "UTF-8") + "=" + URLEncoder.encode(login_name, "UTF-8") + "&" +
                            URLEncoder.encode("mobile", "UTF-8") + "=" + URLEncoder.encode(mobile, "UTF-8") + "&" +
                            URLEncoder.encode("amount", "UTF-8") + "=" + URLEncoder.encode(amount, "UTF-8")+"&"+
                            URLEncoder.encode("login_password","UTF-8")+"="+URLEncoder.encode(login_password,"UTF-8");

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
               /* if(result.equalsIgnoreCase("Insufficient funds. Failed to settle up") || result.equalsIgnoreCase("payment failed.please try later")|| result.equalsIgnoreCase("authentication failed")|| result.equalsIgnoreCase("there is no such user.please try with a valid user")){
                    //resultView.setText(result);
                    Toast.makeText(Payment.this,result, Toast.LENGTH_LONG).show(); */

                if(result.toLowerCase().contains("payment success")) {
                    Toast.makeText(Payment.this,"Payment Success. Transaction receipt is sent to your email", Toast.LENGTH_LONG).show();
                }
                else
                {
                    Toast.makeText(Payment.this,result, Toast.LENGTH_LONG).show();

                }

            }
        }
        UserLoginClass ulc = new UserLoginClass();
        ulc.execute(amount, mobile, username, password);
    }

    public void sendioumsg(String name,String amt,String pass,String payer)
    {

        class backgroundTask
                extends AsyncTask<String, Void, String> {
            AlertDialog alertDialog;
            ProgressDialog loading;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(Payment.this, "Please Wait", null, true, true);
                alertDialog = new AlertDialog.Builder(Payment.this).create();
              //  alertDialog.setTitle("send I owe you");

            }

            protected String doInBackground(String... params) {

                String reset_url = "http://snapper.esy.es/ioumsg.php";
                String name = params[0];
                String amt=params[1];
                String pass=params[2];
                String payer=params[3];

                try

                {
                    URL url = new URL(reset_url);
                    HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                    httpURLConnection.setRequestMethod("POST");
                    httpURLConnection.setDoOutput(true);
                    OutputStream OS = httpURLConnection.getOutputStream();
                    BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(OS, "UTF-8"));
                    String data = URLEncoder.encode("name", "UTF-8") + "=" + URLEncoder.encode(name, "UTF-8")+ "&" +
                            URLEncoder.encode("amt", "UTF-8") + "=" + URLEncoder.encode(amt, "UTF-8")+"&" +
                            URLEncoder.encode("pass", "UTF-8") + "=" + URLEncoder.encode(pass, "UTF-8")+"&" +
                            URLEncoder.encode("payer", "UTF-8") + "=" + URLEncoder.encode(payer, "UTF-8");

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
                if(result.toLowerCase().contains("insertion success")) {
                    Toast.makeText(Payment.this,"Message Sent successfully", Toast.LENGTH_LONG).show();
                }
                else
                {
                    Toast.makeText(Payment.this,result, Toast.LENGTH_LONG).show();

                }
              //  alertDialog.setMessage("Message Sent Successfully");
               // alertDialog.show();
            }
        }

        backgroundTask bg = new backgroundTask();
        bg.execute(name, amt, pass, payer);
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
