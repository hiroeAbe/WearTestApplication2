package com.example.abehiroe.weartestapplication;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.wearable.activity.WearableActivity;
import android.support.wearable.view.BoxInsetLayout;
import android.util.Log;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.wearable.MessageApi;
import com.google.android.gms.wearable.NodeApi;
import com.google.android.gms.wearable.Wearable;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.CountDownLatch;

public class MainActivity extends WearableActivity implements SensorEventListener {

    private static final SimpleDateFormat AMBIENT_DATE_FORMAT =
            new SimpleDateFormat("HH:mm", Locale.US);

    private GoogleApiClient mGoogleApiClient;
    private BoxInsetLayout mContainerView;
    private TextView mTextView;
    private TextView hateTextView;
    private TextView mClockView;
    private SensorManager sensorManager;
    float heartRate;
    private String TAG_WEAR = "WEAR";
    public static int cntTouch;
    private static final int SENSOR_TYPE_HEARTRATE = 65538;
    private Sensor mHeartRateSensor;

    private CountDownLatch latch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setAmbientEnabled();
        setupGoogleApiClient();
        sendMessage("massage");

        mContainerView = (BoxInsetLayout) findViewById(R.id.container);
        // mTextView = (TextView) findViewById(R.id.text);
        hateTextView = (TextView) findViewById(R.id.hateTextView);
        //mClockView = (TextView) findViewById(R.id.clock);

        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        mHeartRateSensor = sensorManager.getDefaultSensor(65538);
        //mHeartRateSensor = sensorManager.getDefaultSensor(Sensor.TYPE_HEART_RATE);
        sensorManager.registerListener(this, mHeartRateSensor, SensorManager.SENSOR_DELAY_NORMAL);
        Log.d(TAG_WEAR, "heat rate: " + heartRate);

        if (mHeartRateSensor == null)
            Log.d(TAG_WEAR, "heart rate sensor is null");

    }

    private void setupGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(new GoogleApiClient.ConnectionCallbacks() {
                    @Override
                    public void onConnected(Bundle connectionHint) {
                        //接続が完了した時
                    }

                    @Override
                    public void onConnectionSuspended(int cause) {
                        //一時的に切断された時
                    }
                })
                .addOnConnectionFailedListener(new GoogleApiClient.OnConnectionFailedListener() {
                    @Override
                    public void onConnectionFailed(ConnectionResult result) {

                    }
                })
                .addApi(Wearable.API)
                .build();
        mGoogleApiClient.connect();
    }

    private void sendMessage(String message) {
        if (message == null) return;
        new AsyncTask<String, Void, String>() {
            @Override
            protected String doInBackground(String... params) {
                String message = params[0];
                byte[] bytes;
                try {
                    bytes = message.getBytes("UTF-8");
                } catch (Exception e) {
                    return null;
                }
                NodeApi.GetConnectedNodesResult nodes = Wearable.NodeApi.getConnectedNodes(mGoogleApiClient).await();
                for (com.google.android.gms.wearable.Node node : nodes.getNodes()) {
                    Wearable.MessageApi.sendMessage(mGoogleApiClient, node.getId(), "/post/message", bytes)
                            .setResultCallback(new ResultCallback<MessageApi.SendMessageResult>() {
                                @Override
                                public void onResult(MessageApi.SendMessageResult sendMessageResult) {
                                    //sendMessageResult.toString();
                                }
                            });
                }
                return null;
            }
        }.execute(message);
        Log.d(TAG_WEAR, "massage");
    }

    @Override
    protected void onResume() {
        super.onResume();
        List<Sensor> sensors = sensorManager.getSensorList(65538);
        if (sensors.size() > 0) {
            Sensor s = sensors.get(0);
            sensorManager.registerListener(this, s, SensorManager.SENSOR_DELAY_UI);
        }
       /* List<Sensor> sensors = sensorManager.getSensorList(Sensor.TYPE_HEART_RATE);
        if (sensors.size() > 0) {
            Sensor s = sensors.get(0);
            sensorManager.registerListener(this, s, SensorManager.SENSOR_DELAY_UI);
        }*/
        Log.d(TAG_WEAR, "heat rate1: " + heartRate);
        sendMessage("aa");
    }

    @Override
    protected void onStop() {
        super.onStop();
        sensorManager.unregisterListener(this);
    }



    @Override
    public void onSensorChanged(SensorEvent event) {
      if (event.sensor.getType() == 65538) {
            heartRate = event.values[0];
            Log.d(TAG_WEAR, "heat rate2: " + heartRate);

        }

        /*if (event.sensor.getType() == Sensor.TYPE_HEART_RATE) {
            heartRate = event.values[0];
            Log.d(TAG_WEAR, "heat rate2: " + heartRate);

        }*/

     }



    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    @Override
    public void onEnterAmbient(Bundle ambientDetails) {
        super.onEnterAmbient(ambientDetails);
        updateDisplay();
    }

    @Override
    public void onUpdateAmbient() {
        super.onUpdateAmbient();
        updateDisplay();
    }

    @Override
    public void onExitAmbient() {
        updateDisplay();
        super.onExitAmbient();
    }

    private void updateDisplay() {
        if (isAmbient()) {
            mContainerView.setBackgroundColor(getResources().getColor(android.R.color.black));
           // mTextView.setTextColor(getResources().getColor(android.R.color.white));
           // mClockView.setVisibility(View.VISIBLE);

          //  mClockView.setText(AMBIENT_DATE_FORMAT.format(new Date()));
        } else {
            mContainerView.setBackground(null);
           // mTextView.setTextColor(getResources().getColor(android.R.color.black));
          //  mClockView.setVisibility(View.GONE);
        }
        //hateTextView.setText((int) heartRate);
        Log.d(TAG_WEAR, "heat rate3: " + heartRate);
    }
}
