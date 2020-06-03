package com.example.weather;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.AsyncTaskLoader;
import androidx.recyclerview.widget.RecyclerView;

import com.example.weather.Utilities.WeatherDateUtils;

import static com.example.weather.Utilities.WeatherWeatherUtils.formatHighLows;
import static com.example.weather.Utilities.WeatherWeatherUtils.getStringForWeatherCondition;

public class ForecastAdapter extends RecyclerView.Adapter<ForecastAdapter.ForecastAdapterViewHolder> {
//    private String[] mWeatherData;
    ForecastAdapterOnClickHandler mClickHandler;
    private  Context mContext;
    private Cursor mCursor;

    public ForecastAdapter(@NonNull Context context, ForecastAdapterOnClickHandler clickHandler) {
        mContext = context;
        mClickHandler = clickHandler;
    }


    public ForecastAdapter() {

    }

    public interface ForecastAdapterOnClickHandler {
        @SuppressLint("StaticFieldLeak")
        AsyncTaskLoader<String[]> onCreateLoader(int id, Bundle loaderArgs, LoaderManager.LoaderCallbacks<String> callback);

        void onClick(String weatherForDay);

        void onLoadFinished(Loader<String[]> loader, String[] data);

        void onLoaderReset(Loader<String[]> loader);
    }
    public ForecastAdapter(ForecastAdapterOnClickHandler clickHandler) {
        mClickHandler = clickHandler;
    }
    public class ForecastAdapterViewHolder extends RecyclerView.ViewHolder implements OnClickListener {
        final TextView weatherSummary;

        ForecastAdapterViewHolder(View view) {
            super(view);

            weatherSummary = (TextView) view.findViewById(R.id.tv_weather_data);

            view.setOnClickListener(this);
        }

        /**
         * This gets called by the child views during a click. We fetch the date that has been
         * selected, and then call the onClick handler registered with this adapter, passing that
         * date.
         *
         * @param v the View that was clicked
         */
        @Override
        public void onClick(View v) {
            String weatherForDay = weatherSummary.getText().toString();
            mClickHandler.onClick(weatherForDay);
        }
    }


    @Override
    public void onBindViewHolder(ForecastAdapterViewHolder forecastAdapterViewHolder, int position) {
        mCursor.moveToPosition(position);
        /* Read date from the cursor */
        long dateInMillis = mCursor.getLong(MainActivity.INDEX_WEATHER_DATE);
        /* Get human readable string using our utility method */
        String dateString = WeatherDateUtils.getFriendlyDateString(mContext, dateInMillis, false);
        /* Use the weatherId to obtain the proper description */
        int weatherId = mCursor.getInt(MainActivity.INDEX_WEATHER_CONDITION_ID);
        String description = getStringForWeatherCondition(mContext, weatherId);
        /* Read high temperature from the cursor (in degrees celsius) */
        double highInCelsius = mCursor.getDouble(MainActivity.INDEX_WEATHER_MAX_TEMP);
        /* Read low temperature from the cursor (in degrees celsius) */
        double lowInCelsius = mCursor.getDouble(MainActivity.INDEX_WEATHER_MIN_TEMP);

        String highAndLowTemperature =
                formatHighLows(mContext, highInCelsius, lowInCelsius);

        String weatherSummary = dateString + " - " + description + " - " + highAndLowTemperature;
        forecastAdapterViewHolder.weatherSummary.setText(weatherSummary);
    }

    /**
     * Returns the total number of items in the data set held by the adapter.
     *
     * @return The total number of items in this adapter.
     */
    @Override
    public int getItemCount() {
        if (null == mCursor) return 0;
        return mCursor.getCount();
    }

    void swapCursor(Cursor newCursor) {
        mCursor = newCursor;
        notifyDataSetChanged();
    }

    /**
     *
     */



    @Override
    public ForecastAdapterViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        Context context = viewGroup.getContext();
        int layoutIdForListItem = R.layout.activity_forecast_list;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdForListItem, viewGroup, shouldAttachToParentImmediately);
        return new ForecastAdapterViewHolder(view);
    }

//    public void setWeatherData(String[] weatherData) {
//        mWeatherData = weatherData;
//        notifyDataSetChanged();
//    }

}
