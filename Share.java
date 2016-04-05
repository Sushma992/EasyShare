package com.example.sushma.easyshare;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;

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
import java.util.Iterator;

import android.content.Intent;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CheckedTextView;
import android.widget.ListView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by sushma on 10/03/2016.
 */
public class Share extends AppCompatActivity {
    public static final String LOGIN_PREFS="Login_Name";
    ListView lv;
    TableLayout stk;
    TableRow tablerow;
    ArrayList<String> ItemList = new ArrayList<String>();
    ArrayList<String> amountList = new ArrayList<String>();
    ArrayList<String> list = new ArrayList<String>();
    ArrayList<String> SelectedContactNumbers = new ArrayList<String>();
    ArrayList<String> SplitForContacts=new ArrayList<String> ();
    ArrayList<String> splitcontactno=new ArrayList<String> ();
    int count=0,n=0;
    String ItemSelected="",CategorySelected;
    String amountTextString="";
    String TotalBill = "";
Button unequal,preview;
  static  int rowclicked=0,contactselected=0;
    CheckBox check;
    ArrayAdapter<String> arrayAdapter;
    String amt,event,mobile;
    String[] payer;
    ArrayList<String> previewList=new ArrayList<String>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.share);
        SharedPreferences prefs=getSharedPreferences(LOGIN_PREFS, MODE_PRIVATE);
        final String name=prefs.getString("username", null);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        Intent i = getIntent();
        list = i.getStringArrayListExtra("key");
        ItemList = i.getStringArrayListExtra("item_list");
        amountList = i.getStringArrayListExtra("amount_list");
        SelectedContactNumbers = i.getStringArrayListExtra("number");
        // TotalBill = i.getStringExtra("TotalBill");
        CategorySelected=i.getStringExtra("CategorySelected");
        n=list.size();

        stk = (TableLayout) findViewById(R.id.table_main);
        lv = (ListView) findViewById(R.id.taggedcontacts);
        check=(CheckBox)findViewById(R.id.checkBox1);
        preview=(Button)findViewById(R.id.preview);
        preview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in=new Intent(Share.this,SplitActivity.class);
                in.putStringArrayListExtra("preview",previewList);
                startActivity(in);


            }
        });
        arrayAdapter = new ArrayAdapter<String>(this, R.layout.android_row, list);
        lv.setAdapter(arrayAdapter);
        lv.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        lv.setOnItemClickListener(new Share.CheckBoxClick());

  unequal = (Button) findViewById(R.id.Split);
        unequal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (rowclicked == 0) {
                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(v.getContext());
                    alertDialog.setMessage("Please Select an Item to split ");
                    alertDialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();

                            //  finish();
                        }
                    });

                    alertDialog.show();
                }
             else if(contactselected==0){

                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(v.getContext());
                    alertDialog.setMessage("Please Select contacts to split ");
                    alertDialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();

                            //  finish();
                        }
                    });

                    alertDialog.show();

                }
                else
                 {

                    count = SplitForContacts.size();
                    if (check.isChecked()) {
                        count++;
                    }
                   // Toast.makeText(getApplicationContext(), "count" + count, Toast.LENGTH_SHORT).show();
                    Double amount = Double.parseDouble(amountTextString);
                    Double splitAmount = amount / count;
                    Toast.makeText(getApplicationContext(), "done splitting" + splitAmount+"event"+CategorySelected, Toast.LENGTH_LONG).show();
                     Iterator<String> ContactsIterator=SplitForContacts.iterator();
                     while(ContactsIterator.hasNext())
                     {
                         previewList.add(ContactsIterator.next()+"   "+ItemSelected+"  "+"1/"+count+" "+splitAmount);
                     }

                   //  payer=new String[splitcontactno.size()];
                            int vall=splitAmount.intValue();


                         amt=String.valueOf(vall);
                         event=CategorySelected.toString();
                     mobile="";
                        // payer=splitcontactno.toArray(payer);
                     for(String s:splitcontactno)
                     {
                         mobile+=s+"*";
                     }
               //  Toast.makeText(getApplicationContext(),"values"+name+"amt"+amt+"event"+event+"mobile"+mobile,Toast.LENGTH_LONG).show();
                     sendmsg(name, amt, event, mobile);

                          rowclicked=0;
                     contactselected=0;
                     for(int k=0;k<n;k++)
                     {
                         lv.setItemChecked(k,false);
                     }
                     tablerow.setBackgroundColor(Color.parseColor("#348781"));
                     SplitForContacts.clear();
                     splitcontactno.clear();
                     /*   Toast.makeText(getApplicationContext(), "unequalsplit", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(Share.this, SplitActivity.class);
                    intent.putExtra("item_selected", ItemSelected);
                    intent.putExtra("amount_selected", amountTextString);
                    intent.putStringArrayListExtra("SelectedContactNames", list);
                    intent.putStringArrayListExtra("SelectedContactNumbers", SelectedContactNumbers);
                    startActivity(intent);*/
                }
            }
        });

          Iterator<String> it1 = ItemList.iterator();
        Iterator<String> it2 = amountList.iterator();
        while (it1.hasNext() && it2.hasNext()) {
            init(it1.next(), it2.next());

        }
        for (int k = 0, j = stk.getChildCount(); k < j; k++) {
            View view = stk.getChildAt(k);
            if (view instanceof TableRow) {
                final TableRow row = (TableRow) view;
                row.setClickable(true);
                row.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View view) {

                        tablerow = (TableRow) view;
                        // Toast.makeText(getApplicationContext(), "row clicked" + tablerow.getChildAt(1), Toast.LENGTH_LONG).show();
                        TextView tv = (TextView) tablerow.getChildAt(0);
                        ItemSelected = tv.getText().toString();
                        //  Toast.makeText(getApplicationContext(), "retrieved item"+ItemSelected, Toast.LENGTH_LONG).show();

                        TextView amountText = (TextView) tablerow.getChildAt(1);
                        amountTextString = amountText.getText().toString();
                        Toast.makeText(getApplicationContext(), "retrieved amount" + amountTextString, Toast.LENGTH_LONG).show();

                        row.setBackgroundColor(Color.parseColor("#808080"));
                        // row.setClickable(false);
                        rowclicked = 1;

                    }
                });
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
                loading = ProgressDialog.show(Share.this, "Please Wait", null, true, true);
                alertDialog = new AlertDialog.Builder(Share.this).create();
              //  alertDialog.setTitle("send you owe me");

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
               if (result.toLowerCase().contains("message sent to ")) {

                   alertDialog.setMessage("Message sent successfully");
                   alertDialog.show();
               }
                else
               {
                   Toast.makeText(getApplicationContext(),result,Toast.LENGTH_LONG).show();
               }
            }
        }
        backgroundTask bg = new backgroundTask();
        bg.execute(username,amt,event,mobile);
    }

    public class CheckBoxClick implements AdapterView.OnItemClickListener {
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        CheckedTextView ctv = (CheckedTextView) view;
        String ContactName = arrayAdapter.getItem(position);
        String ContactNo = SelectedContactNumbers.get(position);

            if (ctv.isChecked()) {
                ctv.setChecked(true);
                SplitForContacts.add(ctv.getText().toString());
                splitcontactno.add(ContactNo);

            } else {
                ctv.setChecked(false);
                SplitForContacts.remove(ctv.getText().toString());
                splitcontactno.remove(ContactNo);
            }
            contactselected = 1;
        }


}

  public void init(String it1, String it2) {
        TableRow tbrow = new TableRow(this);

        TextView t2v = new TextView(this);
        t2v.setText("" + it1 + " ");
        t2v.setTextColor(Color.parseColor("#ffffff"));
        t2v.setGravity(Gravity.CENTER);
        tbrow.addView(t2v);

        TextView et=new TextView(this);
        et.setText(" " + it2);
        et.setTextColor(Color.parseColor("#ffffff"));
        tbrow.addView(et);
        stk.addView(tbrow);
        tbrow.setPadding(0, 0, 0, 2);

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
