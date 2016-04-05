package com.example.sushma.easyshare;


import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;



/**
 * A simple {@link Fragment} subclass.
 */
public class Home extends Fragment {
    private TextView welcome;
       TextView tv;
        String username2;
        Button spilt,notify;
    public static final String LOGIN_PREFS="Login_Name";


    public Home() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View rv = inflater.inflate(R.layout.fragment_home, container, false);
        ((HomePage) getActivity()).getSupportActionBar().setTitle("Home");

        welcome= (TextView) rv.findViewById(R.id.display);
     
            spilt=(Button)rv.findViewById(R.id.spilt);
        notify=(Button)rv.findViewById(R.id.notify);
              username2 = getActivity().getIntent().getStringExtra(Login.USER_NAME);

        SharedPreferences prefs=this.getActivity().getSharedPreferences(LOGIN_PREFS, Context.MODE_PRIVATE);
        String data=prefs.getString("username", null);

        welcome.setText("Hi  " + data + ",\n Welcome to Easy Share");

         spilt.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 Intent i= new Intent(getActivity(),snapnsplit.class);
                 startActivity(i);
             }
         });

        notify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent j= new Intent(getActivity(),paybill.class);
                startActivity(j);

            }
        });



        return rv;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

}

