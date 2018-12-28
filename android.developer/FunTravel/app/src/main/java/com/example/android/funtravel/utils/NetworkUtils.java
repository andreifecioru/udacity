package com.example.android.funtravel.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Utility class for working with networking services.
 */
public final class NetworkUtils {
    public enum ConnectivityStatus { UNKNOWN, ONLINE, OFFLINE }

    private NetworkUtils() {
        throw new IllegalStateException("Utility class");
        // Utility class.
    }

    /**
     * Checks the network connectivity status (whether we are connected or not).
     *
     * @return ONLINE if we have internet access, OFFLINE if not.
     */
    public static ConnectivityStatus checkConnectivity(Context context) {
        ConnectivityManager connMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connMgr != null) {
            NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
            if (networkInfo != null && networkInfo.isConnected())
                return ConnectivityStatus.ONLINE;
        }

        return ConnectivityStatus.OFFLINE;
    }
}
