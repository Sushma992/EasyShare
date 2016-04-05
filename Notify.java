package com.example.sushma.easyshare;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.support.v4.app.Fragment;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.content.Intent;
import android.provider.MediaStore;
import android.net.Uri;
import android.database.Cursor;
import android.os.Environment;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import java.io.File;

public class Notify extends Fragment {
    EditText amt,event;
    Button share;
    RadioButton radioButtonMain;
    RadioGroup radio;
    public Notify() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
       final   View rootView = inflater.inflate(R.layout.fragment_notify,
                container, false);
        ((HomePage) getActivity()).getSupportActionBar().setTitle("Notify");
        amt=(EditText)rootView.findViewById(R.id.payableamount);
        event=(EditText)rootView.findViewById(R.id.event);
        share=(Button)rootView.findViewById(R.id.send);
        radio=(RadioGroup)rootView.findViewById(R.id.radioButton);



    share.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int selectedId = radio.getCheckedRadioButtonId();
            radioButtonMain = (RadioButton)rootView.findViewById(selectedId);
            methodShare(v);
        }
    });

        return rootView;
    }

 public void methodShare(View view) {

     // Toast.makeText(Notify.this, radioButtonMain.getText(), Toast.LENGTH_SHORT).show();
     String text = "Hey Hi...";
     if (radioButtonMain.getText().equals("You Owe")) {
         text = "Message from EasyShare:\n" +
                 "Hey Hi,\n" +
                 "I have to pay  you Rs." + amt.getText().toString() + " for the event '" + event.getText().toString() + "'.I’ll repay you at the earliest possible time. \n" +
                 "Thanks.\n " + "Use EasyShare to split expenses and money transfer." +
                 "Its Simple, Safe and Secure.";
     } else {
         text = "Message from EasyShare:\n" +
                 "Hey Hi,\n" +
                 "I just wanted to note you that you have to pay me Rs." + amt.getText().toString() + " for the event ‘" + event.getText().toString() + "‘. Let’s Settle up soon.\n" +
                 "Thanks,\n" +
                 "\n" +
                 "Use EasyShare to split expenses and money transfer.\n" +
                 "Its Simple, Safe and Secure.";
     }
     Intent sendIntent = new Intent();
     sendIntent.setAction(Intent.ACTION_SEND);
     sendIntent.putExtra(Intent.EXTRA_TEXT, text);
     sendIntent.setType("text/plain");
     //startActivity(sendIntent);
     startActivity(Intent.createChooser(sendIntent, "Send via"));
 }  @Override
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
