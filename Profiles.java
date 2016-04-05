package com.example.sushma.easyshare;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

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
import java.util.StringTokenizer;

/**
 * Created by sushma on 31/03/2016.
 */
public class Profiles extends Fragment {

    ListView listview;
    Button payto,getfrom,reqmoney;
    Context context;
    String data;
    public  static final String LOGIN_PREFS="Login_Name";
     ArrayList<String> trans = new ArrayList<String>();
    String flag = "uowe"; int flag1=0;
    @Nullable


    public  void  reqmny(String data,String amt,String mobile)
    {
        UserLoginClass1 ulc1= new UserLoginClass1();
        ulc1.execute(data,amt,mobile);

    }

    class  UserLoginClass1 extends AsyncTask<String, Void ,String>{
        ProgressDialog loading;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            loading = ProgressDialog.show(getActivity(), "Please Wait", null, true, true);
        }
        @Override
        protected String doInBackground(String... params) {
            String username = params[0];
            String amt=params[1];
            String mobile=params[2];
            try {
                String reg_url = "http://snapper.esy.es/loan.php";
                URL url = new URL(reg_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                OutputStream OS = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(OS, "UTF-8"));
                String data = URLEncoder.encode("username", "UTF-8") + "=" + URLEncoder.encode(username, "UTF-8") + "&" +
                        URLEncoder.encode("amt", "UTF-8") + "=" + URLEncoder.encode(amt, "UTF-8") + "&" +
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
           if(result.toLowerCase().contains("message sent to ")){
               Toast.makeText(getActivity().getApplicationContext(),"Request sent  successfully",Toast.LENGTH_LONG).show();
           }
           else {
               Toast.makeText(getActivity().getApplicationContext(),result,Toast.LENGTH_LONG).show();

           }

        }

    }
    public void populateListView(String data,String flag) {

        UserLoginClass ulc = new UserLoginClass();
        ulc.execute(data, flag);

    }

    class UserLoginClass extends AsyncTask<String, Void, String> {
        ProgressDialog loading;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            loading = ProgressDialog.show(getActivity(), "Please Wait", null, true, true);
        }

        @Override
        protected String doInBackground(String... params) {
            String username = params[0];
            String flag=params[1];
            try {
                String reg_url = "http://snapper.esy.es/profile.php";
                URL url = new URL(reg_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                OutputStream OS = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(OS, "UTF-8"));
                String data = URLEncoder.encode("username", "UTF-8") + "=" + URLEncoder.encode(username, "UTF-8") + "&" +
                        URLEncoder.encode("flag", "UTF-8") + "=" + URLEncoder.encode(flag, "UTF-8");
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

            StringTokenizer tokens = new StringTokenizer(result, "@");
            String temp;
            trans = new ArrayList<String>();
            while (tokens.hasMoreTokens()) {
                temp = tokens.nextToken();
                trans.add(temp);
            }
            temp2();

        }
    }

    public void temp2(){

        ArrayAdapter<String> adapter=new ArrayAdapter<String>(getActivity(),android.R.layout.simple_list_item_1,trans);
        listview.setAdapter(adapter);

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        final View v = inflater.inflate(R.layout.fragment_profile,
                container, false);
        ((HomePage) getActivity()).getSupportActionBar().setTitle("Profile");


        listview = (ListView) v.findViewById(R.id.listViewprofile);
        payto = (Button) v.findViewById(R.id.buttonpayto);
        getfrom=(Button)v.findViewById(R.id.buttongetfrom);
        reqmoney=(Button)v.findViewById(R.id.buttonreq);
        //   Toast.makeText(getActivity(), "entering trans()", Toast.LENGTH_SHORT).show();
        SharedPreferences prefs=this.getActivity().getSharedPreferences(LOGIN_PREFS, Context.MODE_PRIVATE);
        data=prefs.getString("username", null);
        populateListView(data, flag);
       // temp2();
        payto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              //  payto.setBackgroundColor(Color.GREEN);
                //getfrom.setBackgroundColor(Color.parseColor("#3f51b5"));
                flag = "uowe";
                populateListView(data, flag);

            }
        });

        getfrom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // getfrom.setBackgroundColor(Color.GREEN);
                //payto.setBackgroundColor(Color.parseColor("#3f51b5"));
                flag = "urowed";
                populateListView(data, flag);
            }
        });

        reqmoney.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                LayoutInflater factory = LayoutInflater.from(getActivity());

                final View textEntryView = factory.inflate(R.layout.reqdialog, null);

                final EditText input1 = (EditText) textEntryView.findViewById(R.id.editTexta1);
                final EditText input2 = (EditText) textEntryView.findViewById(R.id.editTexta2);

                final AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
                alert.setTitle("Enter Details").setView(textEntryView).setPositiveButton("Request",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,
                                                int whichButton) {

                                Log.i("AlertDialog", "TextEntry 1 Entered " + input1.getText().toString());
                                Log.i("AlertDialog", "TextEntry 2 Entered " + input2.getText().toString());

                                String amt = input1.getText().toString();
                                String mobile = input2.getText().toString();
                               reqmny(data,amt,mobile);
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

        return v;
    }




}
