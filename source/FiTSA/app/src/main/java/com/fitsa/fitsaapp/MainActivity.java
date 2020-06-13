package com.fitsa.fitsaapp;

import android.*;
import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.CallSuper;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.fitsa.fitsaCM.CMTypeDefs;
import com.fitsa.fitsaCM.FitsaConnectionManager;
import com.fitsa.fitsaCM.FitsaConnectionManagerListener;
import com.google.android.gms.nearby.connection.ConnectionInfo;
import com.google.android.gms.nearby.connection.Payload;
import com.fitsa.fitsaCM.CMTypeDefs.*;
import com.google.android.gms.nearby.connection.Strategy;

import static java.lang.Thread.sleep;


public class MainActivity extends AppCompatActivity implements FitsaConnectionManagerListener {


    private FitsaConnectionManager mFitsaCM = null;

    private final Strategy STRATEGY = Strategy.P2P_STAR;
    private boolean isSender = false;
    private boolean mSentFromClient = false;

    private final String SERVICE_ID = "com.fitsa.fitsaapp.SERVER";
    private static final String[] REQUIRED_PERMISSIONS =
            new String[]{
                    android.Manifest.permission.BLUETOOTH,
                    android.Manifest.permission.BLUETOOTH_ADMIN,
                    android.Manifest.permission.ACCESS_WIFI_STATE,
                    android.Manifest.permission.CHANGE_WIFI_STATE,
                    android.Manifest.permission.ACCESS_COARSE_LOCATION,
                    android.Manifest.permission.ACCESS_FINE_LOCATION,
            };


    private static final int REQUEST_CODE_REQUIRED_PERMISSIONS = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (!hasPermissions(this, getRequiredPermissions())) {
            if (!hasPermissions(this, getRequiredPermissions())) {
                if (Build.VERSION.SDK_INT < 23) {
                    ActivityCompat.requestPermissions(
                            this, getRequiredPermissions(), REQUEST_CODE_REQUIRED_PERMISSIONS);
                } else {
                    requestPermissions(getRequiredPermissions(), REQUEST_CODE_REQUIRED_PERMISSIONS);
                }
            }
        }
        mFitsaCM = new FitsaConnectionManager(this, this);


    }

    @Override
    protected void onStart() {
        super.onStart();
        if (hasPermissions(this, getRequiredPermissions())) {
            mFitsaCM.startDiscovering(STRATEGY, SERVICE_ID);
            mFitsaCM.startAdvertising(STRATEGY, SERVICE_ID);
        }

        Button sendButton = findViewById(R.id.button);
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendSelected();
            }
        });
    }

    private void sendSelected()
    {
        isSender = true;
    }

    public static boolean hasPermissions(Context context, String... permissions) {
        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(context, permission)
                    != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

    @CallSuper
    @Override
    public void onRequestPermissionsResult(
            int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_CODE_REQUIRED_PERMISSIONS) {
            for (int grantResult : grantResults) {
                if (grantResult == PackageManager.PERMISSION_DENIED) {
                    Toast.makeText(this, com.fitsa.fitsaCM.R.string.error_missing_permissions, Toast.LENGTH_LONG).show();
                    finish();
                    return;
                }
            }
            recreate();

        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    protected String[] getRequiredPermissions() {
        return join(
                REQUIRED_PERMISSIONS,
                Manifest.permission.RECORD_AUDIO);
    }

    /**
     * Joins 2 arrays together.
     */
    private static String[] join(String[] a, String... b) {
        String[] join = new String[a.length + b.length];
        System.arraycopy(a, 0, join, 0, a.length);
        System.arraycopy(b, 0, join, a.length, b.length);
        return join;
    }

    @Override
    public void onEndpointDiscovered(final FEndpoint oFEndPoint) {

        if(isSender){
        TextView txtView = findViewById(R.id.textView3);
        txtView.setText("Endpoint Found: " + oFEndPoint.getName());
            Button sendBtn = findViewById(R.id.button2);
            sendBtn.setVisibility(View.VISIBLE);
            sendBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                   mFitsaCM.connectToEndpoint(oFEndPoint);
                }
            });
        }
    }

    @Override
    public void onDiscoveryStarted() {

        TextView txtView = findViewById(R.id.textView);
        txtView.setText("Discovery Started");
    }

    @Override
    public void onDiscoveryFailed() {

    }

    @Override
    public void onAdvertisingStarted() {

        TextView txtView = findViewById(R.id.textView2);
        txtView.setText("Advertising Started");
    }

    @Override
    public void onAdvertisingFailed() {

    }

    @Override
    public void onConnectionFailed(FEndpoint oFEndPoint) {

    }

    @Override
    public void connectedToEndpoint(FEndpoint oFEndPoint) {

        TextView txtView = findViewById(R.id.textView4);
        txtView.setText("Connected to: "+oFEndPoint.getName());

    }

    @Override
    public void disconnectedFromEndpoint(FEndpoint oFEndPoint) {

    }

    @Override
    public void onConnectionInitiated(FEndpoint endpoint, ConnectionInfo connectionInfo) {
        TextView txtView = findViewById(R.id.textView4);
        if (isSender) {
            txtView.setText("onConnectionInitiated Sender A");
            mFitsaCM.acceptConnection(endpoint);
        }
        else
        {
            /**end of sender code*/
            if (mSentFromClient) {
                txtView.setText("onConnectionInitiated Receiver A");
                mFitsaCM.acceptConnection(endpoint);
            } else {
                txtView.setText("onConnectionInitiated Receiver R");
                mFitsaCM.rejectConnection(endpoint);
                mSentFromClient = true;
                try {
                    sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                mFitsaCM.connectToEndpoint(endpoint);
            }
        }
    }

    @Override
    public void onReceive(FEndpoint oFEndpoint, Payload payload) {

    }
}
