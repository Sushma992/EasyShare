package com.example.sushma.easyshare;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckedTextView;
import android.widget.ListView;
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
import java.util.ArrayList;

/**
 * Created by sushma on 26/03/2016.
 */
public class TaggedContacts extends AppCompatActivity {
    ListView listview1;
    public static final String LOGIN_PREFS="Login_Name";
    TextView selctnts;
    Button tagged;
    Context context;
    String amt1,desc,count;
    Double share,bill;
    String amt,event,mobile;
    int n;
    ArrayList<String> selectedNames = new ArrayList<String>();
    ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tagcontact);

       ActionBar actionBar =getSupportActionBar();
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        context = this;
        SharedPreferences prefs=getSharedPreferences(LOGIN_PREFS, MODE_PRIVATE);
        final String name1=prefs.getString("username", null);;

        Intent intent=getIntent();
      amt1= intent.getStringExtra("amt");
        desc=intent.getStringExtra("CategorySelected");
             // count=intent.getStringExtra("cnt");
try {
    bill = Double.parseDouble(amt1);
}catch (NumberFormatException e)
{
    bill=0.0;
}
      // n=(selectedNames.size())+1;
  listview1 = (ListView) findViewById(R.id.listViewtagcontact);
        tagged = (Button) findViewById(R.id.buttonTagcontact);
        selctnts=(TextView)findViewById(R.id.selectedtagcontact);

        tagged.setOnClickListener(new View.OnClickListener() {
                                      @Override
                                      public void onClick(View v) {
                                          ArrayList<String> selectedNumbers = new ArrayList<String>();
                                          int listSize = selectedNames.size();
                                          for (int i = 0; i < listSize; i++) {
                                              String selection = ContactsContract.Contacts.IN_VISIBLE_GROUP + " = '"
                                                      + ("1") + "'";
                                              String sortOrder = ContactsContract.Contacts.DISPLAY_NAME
                                                      + " COLLATE LOCALIZED ASC";
                                              Cursor cur = getContentResolver().query(
                                                      ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                                                      null, selection + " AND " + ContactsContract.Contacts.HAS_PHONE_NUMBER
                                                              + "=1" + " AND " + ContactsContract.Contacts.DISPLAY_NAME + " LIKE ?",
                                                      new String[]{selectedNames.get(i)}, sortOrder);
                                              int numberIdx = cur.getColumnIndex(Phone.DATA);
                                              while (cur.moveToNext()) {
                                                  String contactNumber = cur.getString(numberIdx);
                                                  int digits = 10;
                                                  String result = contactNumber.replaceAll("[^0-9]", "");
                                                  if (result.length() > digits) {
                                                      int phno = result.length() - digits;
                                                      result = result.substring(phno);
                                                  }
                                                  selectedNumbers.add(result);
                                              }

                                              cur.close();
                                              share = bill / (listSize + 1);
                                              Toast.makeText(getApplicationContext(), "Splitted amount Rs. " + share + " For the event " + desc, Toast.LENGTH_SHORT).show();

                                              int vall=share.intValue();
                                              amt = String.valueOf(vall);
                                              event = desc.toString();
                                              mobile="";
                                              // payer=splitcontactno.toArray(payer);
                                              for (String s : selectedNumbers) {
                                                  mobile += s + "*";
                                              }
                                            //  Toast.makeText(getApplicationContext(), "values" + name1 + "amt" + amt + "event" + event + "mobile" + mobile, Toast.LENGTH_LONG).show();
                                              sendmsg(name1, amt, event, mobile);

                                              selectedNumbers.clear();

                                          }
                                      }
                                  });

        String selection = ContactsContract.Contacts.IN_VISIBLE_GROUP + " = '"
                + ("1") + "'";
        String sortOrder = ContactsContract.Contacts.DISPLAY_NAME
                + " COLLATE LOCALIZED ASC";
        Cursor cur = context.getContentResolver().query(
                ContactsContract.Contacts.CONTENT_URI, null, selection
                        + " AND " + ContactsContract.Contacts.HAS_PHONE_NUMBER
                        + "=1",null, sortOrder);

        String nameFromContacts[] = new String[cur.getCount()];
        String numberFromContacts[] = new String[cur.getCount()];
        int i = 0;
        int name = cur.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME);
        while (cur.moveToNext()) {

            String contactName = cur.getString(name);
            nameFromContacts[i] = contactName;
            i++;
        }

        cur.close();
        adapter = new ArrayAdapter<String>(this, R.layout.android_row, nameFromContacts);
        listview1.setAdapter(adapter);
        listview1.setItemsCanFocus(false);

        listview1.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        listview1.setOnItemClickListener(new CheckBoxClick());

    }

    public class CheckBoxClick implements AdapterView.OnItemClickListener {
        int listlength;
        @Override
        public void onItemClick(AdapterView<?> arg0, View view, int position, long id) {

            CheckedTextView ctv = (CheckedTextView) view;
            String item = adapter.getItem(position);
            //String number=
            if (ctv.isChecked()) {
                ctv.setChecked(true);
               // Toast.makeText(TaggedContacts.this, " " + item + " checked", Toast.LENGTH_SHORT).show();
                selectedNames.add(ctv.getText().toString());
                selctnts.append(item+", ");


            } else {
                ctv.setChecked(false);
               // Toast.makeText(TaggedContacts.this, " " + item + " unchecked", Toast.LENGTH_SHORT).show();
                selectedNames.remove(ctv.getText().toString());
                listlength=selectedNames.size();
                selctnts.setText("");
                for(int l=0;l<listlength;l++)
                    selctnts.append(selectedNames.get(l)+", ");
            }
        }
    }


     public void sendmsg(String username,String amt,String event,String mobile)
    {

        class backgroundTask
                extends AsyncTask<String, Void, String> {
            AlertDialog alertDialog;
            ProgressDialog loading;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(TaggedContacts.this, "Please Wait", null, true, true);
                alertDialog = new AlertDialog.Builder(TaggedContacts.this).create();
               // alertDialog.setTitle("send you owe me");

            }

            protected String doInBackground(String... params) {
                String reset_url = "http://snapper.esy.es/splitup.php";
                String username=params[0];
                String amt=params[1];
                String event=params[2];
                String mobile=params[3];
                try

                {
                    URL url = new URL(reset_url);
                    HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                    httpURLConnection.setRequestMethod("POST");
                    httpURLConnection.setDoOutput(true);
                    OutputStream OS = httpURLConnection.getOutputStream();
                    BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(OS, "UTF-8"));
                    String data =  URLEncoder.encode("username", "UTF-8") + "=" + URLEncoder.encode(username, "UTF-8")+"&" +
                            URLEncoder.encode("amt", "UTF-8") + "=" + URLEncoder.encode(amt, "UTF-8")+"&" +
                            URLEncoder.encode("event", "UTF-8") + "=" + URLEncoder.encode(event, "UTF-8")+"&" +
                            URLEncoder.encode("mobile", "UTF-8") + "=" + URLEncoder.encode(mobile, "UTF-8");

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
                if(result.toLowerCase().contains("message sent to ")) {
                    alertDialog.setMessage("Message sent successfully");
                    alertDialog.show();
                }
                else {
                    Toast.makeText(getApplicationContext(),result,Toast.LENGTH_LONG).show();
                }
            }
        }
        backgroundTask bg = new backgroundTask();
        bg.execute(username, amt, event, mobile);
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
