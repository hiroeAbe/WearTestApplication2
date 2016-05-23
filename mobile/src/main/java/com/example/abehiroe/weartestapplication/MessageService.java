package com.example.abehiroe.weartestapplication;

import android.util.Log;
import android.widget.TextView;

import com.google.android.gms.wearable.MessageEvent;
import com.google.android.gms.wearable.WearableListenerService;
/**
 * Created by abehiroe on 2016/05/23.
 */


public class MessageService extends WearableListenerService{

    private TextView textView2;
    private String TAG = "MOBILE";
    //private String MESSAGE_PATH = "message";
    public String msg;
    @Override
    public void onMessageReceived(MessageEvent messageEvent) {
        //String msg;
        //textView2 = (TextView) findViewById(R.id.text);
        Log.v(TAG, "onMessageReceived");
        if (messageEvent.getPath().equals("/post/message")) {
         //  String msg;
            try {
                msg = new String(messageEvent.getData(), "UTF-8");
            } catch (Exception ex) {
                Log.e(TAG, ex.getMessage());
                return;
            }
            Log.i(TAG, "receive: " + msg);
          //textView2.getText(msg);
            // ここで適当に処理
        }
    }
}