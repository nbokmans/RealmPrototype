package nl.han.ica.mad.realm;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import io.realm.ObjectServerError;
import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmResults;
import io.realm.SyncCredentials;
import io.realm.SyncUser;
import nl.han.ica.mad.realm.model.Weather;

public class MainActivity extends AppCompatActivity implements SyncUser.Callback{

    private Realm realm;
    private RealmListAdapter adapter;
    private ListView listView;
    private Button writeDataButton;
    private RealmResults<Weather> weatherItems;

    public void attemptLogin(boolean create){
        SyncCredentials credentials = SyncCredentials.usernamePassword("u","p",create);
        SyncUser.loginAsync(credentials,WeatherApplication.AUTH_URL, this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        attemptLogin(false);

        listView = (ListView) findViewById(R.id.list);
        writeDataButton = (Button) findViewById(R.id.writeDataBtn);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        realm.close();
        UserManager.logoutActiveUser();
    }

    // Login success
    @Override
    public void onSuccess(SyncUser user) {
        Log.v("hoi", "ik ben ingelogd");
        UserManager.setActiveUser(user);

        writeDataButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                realm.executeTransactionAsync(new Realm.Transaction() {
                    @Override
                    public void execute(Realm realm) {
                        final Weather newWeather = Weather.create(realm);
                        realm.copyToRealm(newWeather);
                    }
                }, new Realm.Transaction.OnSuccess() {
                    @Override
                    public void onSuccess() {
                        adapter.notifyDataSetChanged();
                    }
                });
            }
        });
        final Button clearDataButton = (Button) findViewById(R.id.clearDataBtn);
        clearDataButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                realm.executeTransactionAsync(new Realm.Transaction() {
                    @Override
                    public void execute(Realm realm) {
                        final RealmResults<Weather> results = realm.where(Weather.class).findAll();
                        results.deleteAllFromRealm();
                    }
                }, new Realm.Transaction.OnSuccess() {
                    @Override
                    public void onSuccess() {
                        adapter.notifyDataSetChanged();
                    }
                });
            }
        });
        realm = Realm.getDefaultInstance();
        weatherItems = realm.where(Weather.class).findAll();
        weatherItems.sort("id");
        adapter = new RealmListAdapter(weatherItems, this);
        listView.setAdapter(adapter);

        weatherItems.addChangeListener(new RealmChangeListener<RealmResults<Weather>>() {
            @Override
            public void onChange(RealmResults<Weather> results) {
                adapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    public void onError(ObjectServerError error) {
        Log.e("hoi", "inloggen mislukt");
        String errorMsg;
        switch (error.getErrorCode()) {
            case UNKNOWN_ACCOUNT:
                errorMsg = "Account does not exists. Attempting to create account";
                attemptLogin(true);
                break;
            case INVALID_CREDENTIALS:
                errorMsg = "The provided credentials are invalid!"; // This message covers also expired account token
                break;
            default:
                errorMsg = error.toString();
        }
        Toast.makeText(this, errorMsg, Toast.LENGTH_LONG).show();
    }
}
