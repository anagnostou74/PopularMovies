package gr.mobap.popularmovies.utilities;

import android.app.Application;

public class CheckNetwork extends Application {

    private static CheckNetwork mInstance;

    @Override
    public void onCreate() {
        super.onCreate();

        mInstance = this;
    }

    public static synchronized CheckNetwork getInstance() {
        return mInstance;
    }

    public void setConnectivityListener(ConnectivityReceiver.ConnectivityReceiverListener listener) {
        ConnectivityReceiver.connectivityReceiverListener = listener;
    }
}