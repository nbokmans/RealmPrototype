package nl.han.ica.mad.realm;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.TextView;

import io.realm.OrderedRealmCollection;
import io.realm.RealmBaseAdapter;
import nl.han.ica.mad.realm.model.Weather;

/**
 * Created by rob on 7-11-16.
 */
public class RealmListAdapter extends RealmBaseAdapter<Weather> implements ListAdapter {

    private static class ViewHolder {
        TextView date;
        TextView forecast;
        TextView humidity;
        TextView windDirection;
        TextView windSpeed;
    }

    public RealmListAdapter(OrderedRealmCollection<Weather> realmResults, Context context) {
        super(context, realmResults);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.weather_item,
                    parent, false);

            viewHolder = new ViewHolder();
            viewHolder.date = (TextView) convertView.findViewById(R.id.date);
            viewHolder.forecast = (TextView) convertView.findViewById(R.id.forecast);
            viewHolder.humidity = (TextView) convertView.findViewById(R.id.humidity);
            viewHolder.windDirection = (TextView) convertView.findViewById(R.id.wind_direction);
            viewHolder.windSpeed = (TextView) convertView.findViewById(R.id.wind_speed);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        if (adapterData != null) {
            Weather item = adapterData.get(position);
            viewHolder.date.setText(item.date);
            viewHolder.humidity.setText(item.humidity);
            viewHolder.windSpeed.setText(item.wind.speed);
            viewHolder.windDirection.setText(item.wind.direction);
            viewHolder.forecast.setText(item.forecast);
        }
        return convertView;
    }
}