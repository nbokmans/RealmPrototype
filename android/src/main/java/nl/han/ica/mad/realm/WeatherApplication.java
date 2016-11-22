package nl.han.ica.mad.realm;

import android.app.Application;
import android.util.Log;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.log.AndroidLogger;
import io.realm.log.RealmLog;

/**
 * @author Niels Bokmans
 * @version 1.0
 * @since 8-11-2016
 */

public class WeatherApplication extends Application {
    public static final String AUTH_URL = "http://" + BuildConfig.OBJECT_SERVER_IP + ":9080/auth";
    public static final String REALM_URL = "realm://" + BuildConfig.OBJECT_SERVER_IP + ":9080/~/weatherRealm";

    @Override
    public void onCreate() {
        super.onCreate();
        Realm.init(this);
        RealmLog.add(new AndroidLogger(Log.VERBOSE));
    }
}
