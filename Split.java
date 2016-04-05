package com.example.sushma.easyshare;


import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
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


/**
 * A simple {@link Fragment} subclass.
 */
public class Split extends Fragment {
    EditText desc,amt;
    Button btn;
    public Split() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View v = inflater.inflate(R.layout.fragment_split,
                container, false);
        ((HomePage) getActivity()).getSupportActionBar().setTitle("Split");
        desc=(EditText)v.findViewById(R.id.event);
        amt=(EditText)v.findViewById(R.id.amount);
           btn=(Button)v.findViewById(R.id.buttonspiltbill);


        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent k = new Intent(getActivity(), TaggedContacts.class);
                k.putExtra("CategorySelected", desc.getText().toString());
                k.putExtra("amt", amt.getText().toString());
                startActivity(k);

            }
        });
        return v;

    }



}
