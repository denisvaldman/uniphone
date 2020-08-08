package sk.unimo.softphone;

import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.PersistableBundle;
import android.service.carrier.CarrierIdentifier;
import android.service.carrier.CarrierService;
import android.telephony.CarrierConfigManager;
import android.util.Log;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;


public class SampleCarrierConfigService extends CarrierService {

    private static final String TAG = "CarrierService";

    public SampleCarrierConfigService() {
        Log.d(TAG, "Service created");
        //EuiccManager.ACTION_MANAGE_EMBEDDED_SUBSCRIPTIONS



    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        sendBroadcastMessage();
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public PersistableBundle onLoadConfig(CarrierIdentifier id) {
        Log.d(TAG, "Config being fetched");
        PersistableBundle config = new PersistableBundle();
        config.putBoolean(
                CarrierConfigManager.KEY_CARRIER_VOLTE_AVAILABLE_BOOL, true);
        config.putBoolean(
                CarrierConfigManager.KEY_CARRIER_VOLTE_TTY_SUPPORTED_BOOL, false);
        config.putInt(CarrierConfigManager.KEY_VOLTE_REPLACEMENT_RAT_INT, 6);
        // Check CarrierIdentifier and add more config if needed…
        sendBroadcastMessage();
        return config;
    }

    private void sendBroadcastMessage() {

           ;

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent("breadcast");
                LocalBroadcastManager.getInstance(getBaseContext()).sendBroadcast(intent);
            }
        }, 5000);

    }


    @Override
    public IBinder onBind(Intent intent) {
        super.onBind(intent);
        sendBroadcastMessage();
        return null;
    }
}
