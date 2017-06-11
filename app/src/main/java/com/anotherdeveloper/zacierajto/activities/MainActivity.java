package com.anotherdeveloper.zacierajto.activities;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.anotherdeveloper.zacierajto.R;
import com.anotherdeveloper.zacierajto.models.BrewingConfiguration;
import com.anotherdeveloper.zacierajto.networkclasses.TCPClient;
import com.anotherdeveloper.zacierajto.services.BackgroundService;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, CompoundButton.OnCheckedChangeListener, AdapterView.OnItemSelectedListener {
    private FloatingActionButton fab;
    private Button sendMessageBtn;
    private Switch connectSw;
    private TextView temperatureTextView;
    private LinearLayout stagesDetailsContainer;
    private Spinner stagesSpinner;


    private int onOffRelay=0;
    private BrewingConfiguration brewingConfiguration;
    private TCPClient mTcpClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        temperatureTextView = (TextView)findViewById(R.id.temperature_text_view);
        sendMessageBtn = (Button)findViewById(R.id.send_message_button);
        connectSw = (Switch)findViewById(R.id.connect_disconnect_switch);
        stagesDetailsContainer = (LinearLayout)findViewById(R.id.stages_container);
        stagesSpinner = (Spinner)findViewById(R.id.number_of_stages_spinner);
        fab = (FloatingActionButton) findViewById(R.id.fab);

        sendMessageBtn.setOnClickListener(this);
        fab.setOnClickListener(this);
        connectSw.setOnCheckedChangeListener(this);
        AddValuesToSpinner();
        stagesSpinner.setOnItemSelectedListener(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        startService(new Intent(getBaseContext(), BackgroundService.class));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.action_settings:
                break;
            case R.id.esp_details:
                break;

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View view) {
            switch (view.getId()){
                case R.id.send_message_button:
                    StartDevice();
                    break;
                case R.id.fab:
                    EditText editText = (EditText) stagesDetailsContainer.findViewById(1);
                    Snackbar.make(view, editText.getText(), Snackbar.LENGTH_SHORT)
                            .setAction("Action", null).show();
                    break;
            }
    }

    //TODO: Implement service starting or stopping depending on switch state
    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
        //TODO: Create JSON String with all boiling parameters
        if (isChecked) {
            new ConnectTask().execute("");
            compoundButton.setText("Disconnect");
        } else {

            if (mTcpClient != null) {
                mTcpClient.stopClient();
                compoundButton.setText("Connect");
            }
        }
    }

    private void StartDevice(){
        if (mTcpClient != null) {
            switch (onOffRelay) {
                case 0:
                    mTcpClient.sendMessage("START");
                    onOffRelay=1;
                    break;
                case 1:
                    mTcpClient.sendMessage("STOP");
                    onOffRelay=0;
                    break;
            }
        }
    }

    private void AddValuesToSpinner(){
        Integer[] spinnerValues = new Integer[]{1,2,3,4};
        ArrayAdapter<Integer> spinnerAdapter = new ArrayAdapter<>(this, R.layout.support_simple_spinner_dropdown_item, spinnerValues);
        stagesSpinner.setAdapter(spinnerAdapter);
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {

        stagesDetailsContainer.removeAllViews();
        int textViewsAmount = (Integer)adapterView.getItemAtPosition(position);
        for(int i=1; i<=textViewsAmount;i++){
            ConstraintLayout layout = (ConstraintLayout) getLayoutInflater().inflate(R.layout.stages_container_element,null);
                CreateOneRowView(layout,i);
            stagesDetailsContainer.addView(layout);
        }
    }

    private void CreateOneRowView(ConstraintLayout layout, int id) {


        final EditText stageTemperature = (EditText) layout.findViewById(R.id.stages_container_temperature_edittext);
        final EditText stageTime = (EditText) layout.findViewById(R.id.stages_container_time_edittext);


        stageTemperature.setId(id);
        stageTemperature.setHint("Temperature "+id);
        stageTime.setId(id+4);
        stageTime.setHint("Time "+id);
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    //TODO: Extract AsyncTask to separate service
    public class ConnectTask extends AsyncTask<String,String,String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... message) {

            //we create a TCPClient object
            mTcpClient = new TCPClient(new TCPClient.OnMessageReceived() {
                @Override
                //here the messageReceived method is implemented
                public void messageReceived(String message) {

                    publishProgress(message);
                }
            });
            mTcpClient.run();

            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Intent intent = new Intent();
            PendingIntent pIntent = PendingIntent.getActivity(MainActivity.this,0, intent,0);
            Notification notification = new Notification.Builder(MainActivity.this).setTicker("Wololooo")
                    .setContentTitle("Random notification")
                    .setContentText("Done!")
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setContentIntent(pIntent).getNotification();

            notification.flags = Notification.FLAG_AUTO_CANCEL;
            NotificationManager notificationManager = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
            notificationManager.notify(0,notification);
        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
            //response received from server
            Log.d("test", "response " + values[0]);
            //process server response here....
            temperatureTextView.setText("");
            temperatureTextView.setText(values[0]);
        }
    }


}
