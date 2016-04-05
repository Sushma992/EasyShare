package com.example.sushma.easyshare;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import com.pushbots.push.Pushbots;

public class HomePage extends AppCompatActivity implements AdapterView.OnItemClickListener{
       private ActionBarDrawerToggle actionBarDrawerToggle;
    String msg;

    public static final String LOGIN_PREFS="Login_Name";

    private  MyAdapter myAdapter;
    private ActionBar actionBar;
    private DrawerLayout dl;
    private  ListView navlist;
    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.homepage);


        View header=getLayoutInflater().inflate(R.layout.drawer_header, null);
        ImageView pro=(ImageView)header.findViewById(R.id.profile_image);
        TextView tv1=(TextView)header.findViewById(R.id.name);
       Intent intent = getIntent();
        String name1 = intent.getStringExtra(Login.USER_NAME);
        SharedPreferences prefs=getSharedPreferences(LOGIN_PREFS, MODE_PRIVATE);
        String name=prefs.getString("username", null);
         tv1.setText(name);
      dl=(DrawerLayout)findViewById(R.id.drawer_layout);
       navlist= (ListView)findViewById(R.id.left_drawer);
        myAdapter=new MyAdapter(this);
        navlist.addHeaderView(header);
        navlist.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        navlist.setAdapter(myAdapter);
        navlist.setOnItemClickListener(this);
       // navlist.setSelector(Color.parseColor("#1e90ff"));
        actionBarDrawerToggle= new ActionBarDrawerToggle(this,dl,R.string.opendrawer,R.string.closedrawer);
        dl.setDrawerListener(actionBarDrawerToggle);

        actionBar=getSupportActionBar();
        actionBar.setDisplayShowHomeEnabled(true);
       actionBar.setDisplayHomeAsUpEnabled(true);

        fragmentManager= getSupportFragmentManager();
        Pushbots.sharedInstance().init(this);
        Pushbots.sharedInstance().setCustomHandler(customHandler.class);

        loadselection(0);
    }
      private  void loadselection(int i)
      {
          navlist.setItemChecked(i, true);
          switch (i) {
              case 0: Home home=new Home();
                  fragmentTransaction=fragmentManager.beginTransaction();
                  fragmentTransaction.replace(R.id.content_frame,home);
                  fragmentTransaction.commit();
                  break;
              case 1:
                  Profiles profile=new Profiles();
                  fragmentTransaction=fragmentManager.beginTransaction();
                  fragmentTransaction.replace(R.id.content_frame,profile);
                  fragmentTransaction.addToBackStack("fragback").commit();
                  break;
              case 2:
                  Split split=new Split();
                  fragmentTransaction=fragmentManager.beginTransaction();
                  fragmentTransaction.replace(R.id.content_frame,split);
                  fragmentTransaction.addToBackStack("fragback").commit();
                  break;
              case 3:
                 Notify notify=new Notify();
                  fragmentTransaction=fragmentManager.beginTransaction();
                  fragmentTransaction.replace(R.id.content_frame,notify);
                  fragmentTransaction.addToBackStack("fragback").commit();
                  break;
              case 4:
                  Notification notification=new Notification();
                  fragmentTransaction=fragmentManager.beginTransaction();
                  fragmentTransaction.replace(R.id.content_frame,notification);
                  fragmentTransaction.addToBackStack("fragback").commit();
                  break;
              case 5:
                  History history=new History();
                  fragmentTransaction=fragmentManager.beginTransaction();
                  fragmentTransaction.replace(R.id.content_frame,history);
                  fragmentTransaction.addToBackStack("fragback").commit();
                  break;
              case 6:
                  Help help=new Help();
                  fragmentTransaction=fragmentManager.beginTransaction();
                  fragmentTransaction.replace(R.id.content_frame,help);
                  fragmentTransaction.addToBackStack("fragback").commit();
                  break;
              case 7:
                  About about=new About();
                  fragmentTransaction=fragmentManager.beginTransaction();
                  fragmentTransaction.replace(R.id.content_frame,about);
                  fragmentTransaction.addToBackStack("fragback").commit();
                  break;
          }

      }

    @Override
    public void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        actionBarDrawerToggle.syncState();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;

    }


    public boolean onOptionsItemSelected(MenuItem item){
         int id=item.getItemId();
         if(id==R.id.action_settings)
         {
           Intent i= new Intent(this,Settings.class);
             startActivity(i);
         }
         else if(id==R.id.action_signout)
         {
             SharedPreferences.Editor editor= getSharedPreferences(LOGIN_PREFS,MODE_PRIVATE).edit();
             editor.putString("username",null);
             editor.commit();
             Intent i= new Intent(HomePage.this,MainActivity.class);
             startActivity(i);
             Toast.makeText(getApplicationContext(),"You Have successfully Logged Out",Toast.LENGTH_SHORT).show();

             finish();
         }
         else if (id== android.R.id.home)
         {
             if(dl.isDrawerOpen(navlist))
             {
                 dl.closeDrawer(navlist);
             }else
             {
                 dl.openDrawer(navlist);
             }
         }
        return  super.onOptionsItemSelected(item);
     }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        loadselection(position);
        dl.closeDrawer(navlist);
    }
    @Override
    public void onBackPressed() {
        if (getSupportFragmentManager().findFragmentByTag("fragBack") != null) {
           getFragmentManager().popBackStack();
           // Home home=new Home();
            //fragmentManager.beginTransaction().replace(R.id.content_frame,home).commit();
        }
        else {
           super.onBackPressed();
            return;
        }

        finish();

    }


}
class  MyAdapter extends BaseAdapter{
    private Context context;
      String[] Options;
    int[] images={R.drawable.account,R.drawable.splitbill,R.drawable.notify,R.drawable.bell,R.drawable.history,R.drawable.help,R.drawable.logo};
   public MyAdapter(Context context){
       this.context=context;
         Options=context.getResources().getStringArray(R.array.options);
   }
    @Override
    public int getCount() {
        return Options.length;
    }

    @Override
    public Object getItem(int position) {
        return Options[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
       View row=null;
        if(convertView==null){
            LayoutInflater inflater= (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
            row= inflater.inflate(R.layout.custom_row,parent,false);
        }
        else
        {
            row=convertView;
        }

          TextView titleTextView=(TextView) row.findViewById(R.id.textView2);
          ImageView titleImageView=(ImageView) row.findViewById(R.id.imageView4);

        titleTextView.setText(Options[position]);
        titleImageView.setImageResource(images[position]);
        return row;
    }
}
