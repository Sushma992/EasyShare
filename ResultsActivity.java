package com.example.sushma.easyshare;


import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ResultsActivity extends Activity {
    ArrayList<String> scannedList=new ArrayList<String>();
    String[] Header={"Item","Product","Description"};
int flag=0;
    String outputPath;
    TextView tv;
String CategorySelected;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

               String imageUrl = "unknown";

        Bundle extras = getIntent().getExtras();
        if( extras != null) {
            imageUrl = extras.getString("IMAGE_PATH" );
            outputPath = extras.getString( "RESULT_PATH" );
            CategorySelected=extras.getString("CategorySelected");
        }
        new AsyncProcessTask(this).execute(imageUrl, outputPath);
    }

    public void updateResults(Boolean success) {
        if (!success)
            return;
        try {
            StringBuffer contents = new StringBuffer();
            FileInputStream fis = openFileInput(outputPath);
            try {
                Reader reader = new InputStreamReader(fis, "UTF-8");
                BufferedReader bufReader = new BufferedReader(reader);
                String text = null;
                while ((text = bufReader.readLine()) != null) {
                    scannedList.add(text);
                    contents.append(text).append(System.getProperty("line.separator"));
                }
            } finally {
                fis.close();
            }

            Iterator<String> it=scannedList.iterator();

            while(it.hasNext()){
                String haystack=it.next();
                for(String item:Header){
                    String needle=item;
                    if (containsIgnoreCase( haystack, needle) ) {
                        flag=1;
                        it.remove();
                        break;
                    }
                    else {
                        // it.remove();
                    }
                }
                if(flag==0)
                {
                    it.remove();
                }

            }
            Intent intent=new Intent(this,DisplayScannedList.class);
            intent.putStringArrayListExtra("list",scannedList);
            intent.putExtra("CategorySelected",CategorySelected);
            startActivity(intent);
            finish();
           // displayMessage(contents.toString());
        } catch (Exception e) {
            displayMessage("Error: " + e.getMessage());
        }
    }
    public static boolean containsIgnoreCase( String haystack, String needle ) {
        if(needle.equals(""))
            return true;
        if(haystack == null || needle == null || haystack .equals(""))
            return false;
        Pattern p = Pattern.compile(needle,Pattern.CASE_INSENSITIVE+Pattern.LITERAL);
        Matcher m = p.matcher(haystack);
        return m.find();
    }
 public void displayMessage( String text )
    {
        tv.post( new MessagePoster( text ) );
    }
 class MessagePoster implements Runnable {
        public MessagePoster( String message )
        {
            _message = message;
        }
        public void run() {
            tv.append( _message + "\n" );
            setContentView( tv );
        }
        private final String _message;
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

