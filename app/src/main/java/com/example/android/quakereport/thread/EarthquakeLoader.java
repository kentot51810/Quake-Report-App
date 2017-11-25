package com.example.android.quakereport.thread;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.AsyncTaskLoader;
import android.util.Log;

import com.example.android.quakereport.model.Earthquake;
import com.example.android.quakereport.parser.QueryUtils;

import java.util.List;

/**
 * Created by ASPIRE on 022, 22, Nov, 2017.
 */

public class EarthquakeLoader extends AsyncTaskLoader<List<Earthquake>> {
    private final String url;

    public EarthquakeLoader(@NonNull Context context, String url) {
        super(context);
        this.url = url;
    }

    @Override
    protected void onStartLoading() {
        Log.i("onStartLoading","onStartLoading method in AsysnctaskLoader was called.");
        forceLoad();
    }

    @Nullable
    @Override
    public List<Earthquake> loadInBackground() {
        Log.i("loadInBackground","loadInBackground method in AsysnctaskLoader was called.");
        if (url == null){
            return null;
        }

        List<Earthquake> earthquakes = QueryUtils.fetchJsonFromUrl(url);
        return earthquakes;
    }
}
