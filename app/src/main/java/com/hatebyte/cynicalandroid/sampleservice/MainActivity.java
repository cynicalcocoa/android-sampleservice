package com.hatebyte.cynicalandroid.sampleservice;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;


public class MainActivity extends Activity {

    private Boolean isBound = false;

    // Activity methods
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void onResume() {
        super.onResume();
        doBindService();
    }

    @Override
    protected void onPause() {
        super.onPause();
        doUnbindService();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        doUnbindService();
    }



    // Bind or Unbind the service
    private void doBindService() {
        bindService(new Intent(getApplicationContext(), SampleService.class), serviceConnection, Context.BIND_AUTO_CREATE);
        isBound = true;
    }

    private void doUnbindService() {
        if (isBound) {
            unbindService(serviceConnection);
            isBound = false;
        }
    }



    // ServiceConnection Interface(Delegate) object.
    private SampleService boundService = null;

    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            boundService = ((SampleService.LocalBinder) service).getService();
            Toast.makeText(getApplicationContext(), boundService.connectionStatus,  Toast.LENGTH_SHORT).show();
            // SHOULD SAY 'Local Service Connected'
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            Toast.makeText(getApplicationContext(), boundService.connectionStatus, Toast.LENGTH_SHORT).show();
            // SHOULD SAY 'Local Service Disconnected'
            boundService = null;
        }
   };



    // Menu methods
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(com.hatebyte.cynicalandroid.sampleservice.R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == com.hatebyte.cynicalandroid.sampleservice.R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
