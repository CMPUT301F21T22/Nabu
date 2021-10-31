package ca.cmput301f21t22.nabu.ui.my_day;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    //String [] Events = {"Go to work","Walk Dog","Eat Mmmmmmmmmmm mmufins","1","2","3","4","5","6","7","8","9","10","11","12","13","14","15","16","17","18"};
    ListView EventView;
    List<String> data;
    //Button addcity;
    //Button delete;
    int position;
    //int click = 2;

    ArrayList<String> arrayList;
    ArrayAdapter<String> arrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        data = new ArrayList<>();
        EventView = (ListView)findViewById(R.id.Events);
        for(int i = 0; i < 20; i ++){
            data.add("event" + i);
        }

        MyAdapter adapter = new MyAdapter(data);
        EventView.setAdapter(adapter);

        //addcity = (Button)findViewById(R.id.button12);
        //delete = (Button)findViewById(R.id.button11);


        EventView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                View popupView = getLayoutInflater().inflate(R.layout.popwindow,null);
                PopupWindow popupWindow = new PopupWindow(popupView, 300, 300,true);
                Toast.makeText(MainActivity.this,"click event i = " + i + "l = " + l, Toast.LENGTH_SHORT).show();
            }
        });


        //delete.setOnClickListener(new View.OnClickListener() {
        //            public void onClick(View view) {
        //        if (click == 1){
        //        arrayList.remove(position);
        //        click = 2;}
        //        arrayAdapter.notifyDataSetChanged();
        //    }        }            );


    }

    //public void leoClick(View view){
    //    View popupView = getLayoutInflater().inflate(R.layout.popwindow,null);
    //    Button CONFIRM = popupView.findViewById(R.id.button13);
    //    EditText editText = popupView.findViewById(R.id.editTextTextPersonName);

    //    PopupWindow popupWindow = new PopupWindow(popupView, 300, 300,true);

    //    popupWindow.showAsDropDown(view,0,listView.getHeight());

     //   CONFIRM.setOnClickListener(new View.OnClickListener() {
    //        @Override
     //       public void onClick(View view) {
    //            String cy = editText.getText().toString();
     //           arrayList.add(cy);
       //         arrayAdapter.notifyDataSetChanged();
      //          popupWindow.dismiss();
      //      }
      //  });

}
