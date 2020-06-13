package com.fitsa.fitsaCM;

import com.fitsa.fitsaCM.CMTypeDefs.*;
import com.google.android.gms.nearby.connection.ConnectionInfo;
import com.google.android.gms.nearby.connection.Payload;

/**
 * Created by sasikanth on 6/13/20.
 */

public interface FitsaConnectionManagerListener {

    void onEndpointDiscovered(FEndpoint oFEndPoint);
    void onDiscoveryStarted();
    void onDiscoveryFailed();
    void onAdvertisingStarted();
    void onAdvertisingFailed();
    void onConnectionFailed(FEndpoint oFEndPoint);
    void connectedToEndpoint(FEndpoint oFEndPoint);
    void disconnectedFromEndpoint(FEndpoint oFEndPoint);
    void onConnectionInitiated(FEndpoint endpoint, ConnectionInfo connectionInfo);
    void onReceive(FEndpoint oFEndpoint,Payload payload);
}
