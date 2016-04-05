package com.example.sushma.easyshare;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
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
import java.util.Arrays;
import java.util.StringTokenizer;

/**
 * A simple {@link Fragment} subclass.
 */
public class History extends Fragment {
    ListView listview;
    Button credit,debit;
    Context context;
    String data;
    public  static final String LOGIN_PREFS="Login_Name";
    ArrayList<String> trans = new ArrayList<String>();
    String flag = "credit";
    @Nullable

    public void populateListView(String data,String flag) {

      /*  if(flag=="credit")
        {
            credit.setBackgroundColor(Color.GREEN);
            debit.setBackgroundColor(Color.parseColor("#3f51b5"));

        }else
        {
            debit.setBackgroundColor(Color.GREEN);
            credit.setBackgroundColor(Color.parseColor("#3f51b5"));

        }*/

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
                String reg_url = "http://snapper.esy.es/transaction.php";
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

        final View v = inflater.inflate(R.layout.fragment_history,
                container, false);
        ((HomePage) getActivity()).getSupportActionBar().setTitle("Transaction History");


        listview = (ListView) v.findViewById(R.id.listViewtrans);
        credit = (Button) v.findViewById(R.id.buttontrans);
          debit=(Button)v.findViewById(R.id.buttontrans2);

     //   Toast.makeText(getActivity(), "entering trans()", Toast.LENGTH_SHORT).show();
        SharedPreferences prefs=this.getActivity().getSharedPreferences(LOGIN_PREFS, Context.MODE_PRIVATE);
       data=prefs.getString("username", null);
                  populateListView(data, flag);
               //  temp2();
        credit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 flag = "credit";
                populateListView(data, flag);
            }
        });

debit.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        flag="debit";
        populateListView(data, flag);
    }
});

        return v;
    }


}