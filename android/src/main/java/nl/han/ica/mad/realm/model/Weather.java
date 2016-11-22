package nl.han.ica.mad.realm.model;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import io.realm.Realm;
import io.realm.RealmObject;
import io.realm.RealmQuery;
import io.realm.RealmResults;
import io.realm.annotations.PrimaryKey;

/**
 * @author Niels Bokmans
 * @version 1.0
 * @since 8-11-2016
 */

public class Weather extends RealmObject {
    @PrimaryKey
    public int id;
    public String date;
    public String forecast;
    public String humidity;
    public Wind wind;

    public static Weather create(Realm realm) {
        final SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy HH:mm:ss", Locale.getDefault());
        final Weather weather = new Weather();
        final Wind wind = new Wind();
        final String formattedDate = df.format(new Date(System.currentTimeMillis()));
        wind.direction = "NE";
        wind.speed = "100";
        weather.id = getNextKey(realm);
        weather.date = formattedDate;
        weather.forecast = "Nice weather";
        weather.humidity = "40%";
        weather.wind = wind;
        return weather;
    }

    private static int getNextKey(Realm realm) {
        RealmResults<Weather> q = realm.where(Weather.class).findAll();
        int size = q.size();
        return size == 0 ? 1 : realm.where(Weather.class).max("id").intValue() + 1;
    }
}
