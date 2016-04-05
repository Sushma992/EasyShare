package com.example.sushma.easyshare;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckedTextView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class DisplayScannedList extends AppCompatActivity {
    ArrayList<String> scannedList = new ArrayList<String>();
    ArrayList<String> selectedScannedItems = new ArrayList<String>();
    ArrayList<String> ItemList = new ArrayList<String>();
    ArrayList<String> AmountList = new ArrayList<String>();
    HashMap<String, String> IaHashMap = new HashMap();
    ArrayAdapter<String> adapter;
    ListView listview;
    int flag = 0;
    String key, value, actualAmount,CategorySelected;
    String[] Amount = new String[]{"Amount", "Total", "Net Amount"};
    Button tag2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       // setContentView(R.layout.activity_display_scanned_list);
        Intent intent1 = getIntent();

        scannedList = intent1.getStringArrayListExtra("list");
        CategorySelected=intent1.getStringExtra("CategorySelected");
        DisplayMessage();
        Iterator<String> ItemIterator = ItemList.iterator();
        Iterator<String> AmountIterator = AmountList.iterator();
        while (ItemIterator.hasNext() && AmountIterator.hasNext()) {
            IaHashMap.put(ItemIterator.next(), AmountIterator.next());
        }
        Set hmset = IaHashMap.entrySet();
        Iterator i = hmset.iterator();
        while (i.hasNext()) {

            Map.Entry me = (Map.Entry) i.next();
            key = (String) me.getKey();
            // Toast.makeText(getApplicationContext(),"inside while",Toast.LENGTH_LONG).show();
            for (String amount : Amount) {

                if (ResultsActivity.containsIgnoreCase(key, amount)) {
                    value = (String) me.getValue();


                }
            }

        }

        Toast.makeText(getApplicationContext(), "value" + value, Toast.LENGTH_LONG).show();
        Intent intent = new Intent(this, ItemAmountEdit.class);
        intent.putStringArrayListExtra("ItemList", ItemList);
        intent.putStringArrayListExtra("AmountList", AmountList);
        intent.putExtra("total", value);
        intent.putExtra("CategorySelected",CategorySelected);
        startActivity(intent);
        finish();
    }

    public void DisplayMessage() {
        Iterator<String> it1 = scannedList.iterator();
        while (it1.hasNext()) {

            ScanItemAmount(it1.next());
        }

    }

    public void ScanItemAmount(String it1) {
        int foundChar = 0;
        int foundDigit = 0;
        String itemScanned = "";
        String amountScanned = "";
        String amount = "";
        for (int i = 0; i < it1.length(); i++) {
            if (Character.isLetter(it1.charAt(i))) {
                itemScanned += it1.charAt(i);
                foundChar = 1;
            } else if (foundChar == 1 && Character.isDigit(it1.charAt(i))) {
                break;
            } else if (foundChar == 1 && it1.charAt(i) == ' ') {
                itemScanned += it1.charAt(i);
            }

        }
        ItemList.add(itemScanned);
        // Toast.makeText(getApplicationContext(),"scanned item"+itemScanned,Toast.LENGTH_LONG).show();
        for (int i = it1.length() - 1; i > 0; i--) {
            if (Character.isDigit(it1.charAt(i))) {
                amountScanned += it1.charAt(i);
                foundDigit = 1;
            } else if (foundDigit == 1 && it1.charAt(i) == '.') {
                amountScanned += it1.charAt(i);
            } else if (foundDigit == 1 && it1.charAt(i) == ' ')
                break;
        }
        for (int k = amountScanned.length() - 1; k >= 0; k--) {
            amount += amountScanned.charAt(k);
        }
        AmountList.add(amount);
        // Toast.makeText(getApplicationContext(),"scanned item"+amount,Toast.LENGTH_LONG).show();

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

        finish();
    }
}

