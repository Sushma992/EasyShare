package com.example.sushma.easyshare;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.app.Activity;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;


/**
 * A simple {@link Fragment} subclass.
 */
public class Help extends Fragment implements AdapterView.OnItemClickListener {
    ListView list;
    public Help() {
        // Required empty public constructor
    }
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View v = inflater.inflate(R.layout.fragment_help,
                container, false);
        ((HomePage) getActivity()).getSupportActionBar().setTitle("Help");
        String[] options= getResources().getStringArray(R.array.helpOptions);

        ArrayAdapter<String> adapter=new ArrayAdapter<String>(getActivity().getApplicationContext(),R.layout.spinner_item,options);
       // ArrayAdapter adapter=new ArrayAdapter<String>(getActivity().getApplicationContext(),android.R.layout.simple_list_item_1,help);
      list=(ListView) v.findViewById(R.id.usermanual);
     //   for(OptionsHelp optionHelp:OptionsHelp.values()){

//            adapter.add(getString(optionHelp.title));


      //  }

        list.setAdapter(adapter);
             list.setOnItemClickListener(this);


        return v;
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

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

       // OptionsHelp val=OptionsHelp.values()[position];
        switch (position){
            case 0:
                Intent intent = new Intent(getActivity(), UserManual.class);
                startActivity(intent);
                break;
            case 1:
                Intent intent1=new Intent("android.intent.action.VIEW", Uri.parse("http://snapper.esy.es/faqForm.php"));
                startActivity(intent1);
                    break;
            case 2:
                Intent intent2=new Intent("android.intent.action.VIEW", Uri.parse("http://snapper.esy.es/suggestForm.php"));
                startActivity(intent2);
                break;
            case 3:
                Intent intent3=new Intent("android.intent.action.VIEW", Uri.parse("http://snapper.esy.es/tncForm.php"));
                startActivity(intent3);
                break;
        }

    }
}

