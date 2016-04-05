package com.example.sushma.easyshare;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;
import android.content.Context;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckedTextView;
import android.widget.ListView;
import android.widget.TextView;
import java.util.ArrayList;


/**
 * Created by sushma on 17/02/2016.
 */
public class ContactActivity extends AppCompatActivity {
    ListView listview;
    TextView selctnts;
    Button tag2;
    Context context;
    ArrayList<String> selectedNames = new ArrayList<String>();
    ArrayAdapter<String> adapter;
    ArrayList<String> ItemList=new ArrayList<String> ();
    ArrayList<String> amountList=new ArrayList<String> ();
    String TotalBill=null,CategorySelected;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.contact);
        ActionBar actionBar =getSupportActionBar();
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        context = this;
        listview = (ListView) findViewById(R.id.listView1);
        tag2 = (Button) findViewById(R.id.buttonTag);
        selctnts=(TextView)findViewById(R.id.selectedcontacts);
        Intent intent=getIntent();
        ItemList=intent.getStringArrayListExtra("item_list");
        amountList=intent.getStringArrayListExtra("amount_list");
        CategorySelected=intent.getStringExtra("CategorySelected");
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
        listview.setAdapter(adapter);
        listview.setItemsCanFocus(false);

        listview.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        listview.setOnItemClickListener(new CheckBoxClick());

    }

    public class CheckBoxClick implements AdapterView.OnItemClickListener {
   int listlength;
        @Override
        public void onItemClick(AdapterView<?> arg0, View view, int position, long id) {

            CheckedTextView ctv = (CheckedTextView) view;
            String item = adapter.getItem(position);
            if (ctv.isChecked()) {
                ctv.setChecked(true);
               // Toast.makeText(ContactActivity.this, " " + item + " checked", Toast.LENGTH_SHORT).show();
                selectedNames.add(ctv.getText().toString());
                selctnts.append(item+", ");


            } else {
                ctv.setChecked(false);
               // Toast.makeText(ContactActivity.this, " " + item + " unchecked", Toast.LENGTH_SHORT).show();
                selectedNames.remove(ctv.getText().toString());
                listlength=selectedNames.size();
                selctnts.setText("");
                for(int l=0;l<listlength;l++)
                    selctnts.append(selectedNames.get(l)+", ");
            }
        }
    }

    public void methodTag(View view) {
        ArrayList<String> selectedNumbers = new ArrayList<String>();
        int listSize = selectedNames.size();
        int listSize2;
        String numberFromContacts[];
        for (int i = 0; i < listSize; i++) {
          //  Toast.makeText(ContactActivity.this, "Item name " + i + 1 + ": " + selectedNames.get(i), Toast.LENGTH_SHORT).show();
            String selection = ContactsContract.Contacts.IN_VISIBLE_GROUP + " = '"
                    + ("1") + "'";
            String sortOrder = ContactsContract.Contacts.DISPLAY_NAME
                    + " COLLATE LOCALIZED ASC";
            Cursor cur = getContentResolver().query(
                    ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                    null, selection + " AND " + ContactsContract.Contacts.HAS_PHONE_NUMBER
                            + "=1"+ " AND " + ContactsContract.Contacts.DISPLAY_NAME + " LIKE ?",
                    new String[]{selectedNames.get(i)}, sortOrder);

            numberFromContacts   = new String[cur.getCount()];
            listSize2=cur.getCount();
            int numberIdx = cur.getColumnIndex(Phone.DATA);
            // int name=cur.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME);
            while(cur.moveToNext()) {

                //String contactName = cur.getString(name);
                // nameFromContacts[i] =    contactName ;
                String contactNumber = cur.getString(numberIdx);
                //numberFromContacts[j]=contactNumber;
                int digits = 10;
                String result = contactNumber.replaceAll("[^0-9]","");
                if(result.length()>digits)
                {
                    int phno = result.length() - digits;
                    result=result.substring(phno);
                }
                // Toast.makeText(MainActivity.this, "number = " + result, Toast.LENGTH_LONG).show();
                selectedNumbers.add(result);

                //j++;
            }

            cur.close();
        }


        Intent i=new Intent(ContactActivity.this,Share.class);
        i.putStringArrayListExtra("key",selectedNames);
        i.putStringArrayListExtra("number",selectedNumbers);
        i.putStringArrayListExtra("item_list",ItemList);
        i.putStringArrayListExtra("amount_list",amountList);
        i.putExtra("CategorySelected",CategorySelected);
        startActivity(i);
           finish();

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
