package com.example.abehiroe.weartestapplication;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

public class MainActivity extends Activity {


    public TextView textView2;
    MessageService ms = new MessageService();
    //MessageEvent messagedata;
    String a = ms.msg;
    //private String MESSAGE_PATH = "message";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textView2 = (TextView) findViewById(R.id.textView2);
        /*if(a == null){
            textView2.setText("測定されていません");
            //textView2.setText(a);
        }/*else{
            textView2.setText(a);
        }*/
        textView2.setText(a);
    }


}
