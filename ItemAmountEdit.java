package com.example.sushma.easyshare;

/**
 * Created by sushma on 27/03/2016.
 */
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Iterator;

public class ItemAmountEdit extends AppCompatActivity {
    ArrayList<String> scannedList = new ArrayList<String>();
    ArrayList<String> ItemList = new ArrayList<String>();
    ArrayList<String> AmountList = new ArrayList<String>();
    ArrayList<String> itemal = new ArrayList<String>();
    ArrayList<String> amountal = new ArrayList<String>();
    TableLayout stk,ItemAmount;
    Button equal, unequal;
    String total,CategorySelected;
    int k = 0,flag=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_amount_edit);
        Intent intent = getIntent();
        ItemList = intent.getStringArrayListExtra("ItemList");
        AmountList = intent.getStringArrayListExtra("AmountList");
        total=intent.getStringExtra("total");
        CategorySelected=intent.getStringExtra("CategorySelected");
     //   Toast.makeText(getApplicationContext(),"desc"+CategorySelected,Toast.LENGTH_LONG).show();
       // Toast.makeText(getApplicationContext(),"total"+total,Toast.LENGTH_SHORT).show();
        Iterator<String> ItemIterator = ItemList.iterator();
        Iterator<String> AmountIterator = AmountList.iterator();
        unequal = (Button) findViewById(R.id.unequalsplit);
        equal = (Button) findViewById(R.id.equalsplit);
        equal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tagctnts(v);
            }
        });

        unequal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DoTagContacts(v);
            }
        });
        stk = (TableLayout) findViewById(R.id.table_main);
        TableRow tbrow0 = new TableRow(this);
        TextView tv0 = new TextView(this);
        tv0.setText(" Sl.No ");
        tv0.setTextColor(Color.WHITE);
        tbrow0.addView(tv0);
        TextView tv1 = new TextView(this);
        tv1.setText(" Product ");
        tv1.setTextColor(Color.WHITE);
        tbrow0.addView(tv1);
        EditText tv2 = new EditText(this);
        tv2.setText(" Unit Price ");
        tv2.setTextColor(Color.WHITE);
        tbrow0.addView(tv2);
        stk.addView(tbrow0);

        while (ItemIterator.hasNext() && AmountIterator.hasNext())
        {

                init(ItemIterator.next(), AmountIterator.next());

        }
    }

    public void DoTagContacts(View v) {
        //save changes

       // Toast.makeText(getApplicationContext(), "inside method" + stk.getChildCount(), Toast.LENGTH_LONG).show();
        itemal = new ArrayList<String>();
        amountal = new ArrayList<String>();

        for (int i = 0, j = stk.getChildCount(); i < j; i++) {
            View view = stk.getChildAt(i);
             TableRow row = (TableRow)  view;

            TextView itemText = (TextView) row.getChildAt(1);
            String itemTextString = itemText.getText().toString();
            itemal.add(itemTextString);

            EditText amountText = (EditText) row.getChildAt(2);
            String amountTextString = amountText.getText().toString();
            amountal.add(amountTextString);

        }
        //til here
                    Intent intent = new Intent(ItemAmountEdit.this, ContactActivity.class);
                    intent.putStringArrayListExtra("item_list", itemal);
                    intent.putStringArrayListExtra("amount_list",amountal);
        intent.putExtra("CategorySelected",CategorySelected);
        startActivity(intent);
                    finish();

    }

    public void tagctnts(View v)
    {
       // Toast.makeText(getApplicationContext(),"amount "+total,Toast.LENGTH_SHORT).show();
        LayoutInflater factory = LayoutInflater.from(ItemAmountEdit.this);

        final View textEntryView = factory.inflate(R.layout.amountedit, null);

        final EditText input1 = (EditText) textEntryView.findViewById(R.id.editTextamount);
        input1.setText(total, TextView.BufferType.EDITABLE);
        final AlertDialog.Builder alert = new AlertDialog.Builder(ItemAmountEdit.this);
        alert.setTitle("Are You Sure Amount is correct?").setView(textEntryView).setPositiveButton("yes",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,
                                        int whichButton) {

                        Log.i("AlertDialog", "TextEntry 1 Entered " + input1.getText().toString());

                        String amt = input1.getText().toString();
                        Intent intent = new Intent(ItemAmountEdit.this, TaggedContacts.class);
                        intent.putExtra("amt",amt);
                        intent.putExtra("CategorySelected",CategorySelected);
                        startActivity(intent);
                        finish();
                        //  Toast.makeText(getApplicationContext(), "after button click", Toast.LENGTH_LONG).show();


                    }
                }).setNegativeButton("No",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,
                                        int whichButton) {

                    }
                });
        alert.show();

    }


    public void init(String it1, String it3) {

        TableRow tbrow = new TableRow(this);
        TextView t1v = new TextView(this);
        t1v.setText("" + k);
        t1v.setTextColor(Color.parseColor("#ffffff"));
        t1v.setGravity(Gravity.CENTER);
        tbrow.addView(t1v);
        TextView t2v = new TextView(this);
        t2v.setText("" + it1 + " ");
        t2v.setTextColor(Color.parseColor("#ffffff"));
        t2v.setGravity(Gravity.CENTER);
        tbrow.addView(t2v);
        EditText et = new EditText(this);
        et.setText(" " + it3);
        tbrow.addView(et);
        stk.addView(tbrow);
        k++;


    }
}
