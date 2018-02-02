package com.example.android.earthquakerecorder;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ListView;

import com.example.android.earthquakerecorder.models.Earthquake;
import com.example.android.earthquakerecorder.models.Earthquakes;
import com.example.android.earthquakerecorder.models.QuakeRawData;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {
    private static final String LOG_TAG = MainActivity.class.getSimpleName();

    private List<Earthquake> mQuakeList = new ArrayList<>();

    @BindView(R.id.quake_list_view) ListView quakeListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        ArrayAdapter<Earthquake> quakeAdapter =
                new EarthquakeAdapter( this, mQuakeList);

        quakeListView.setAdapter(quakeAdapter);

        quakeListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Earthquake quake = (Earthquake) parent.getAdapter().getItem(position);

                if (quake != null) {
                    String url = quake.getUrl();

                    Log.d(LOG_TAG, "Opening URL: " + url);

                    Intent intent = new Intent();
                    intent.setAction(Intent.ACTION_VIEW);
                    intent.setData(android.net.Uri.parse(url));
                    parent.getContext().startActivity(intent);
                }
            }
        });

        QuakeAsyncTask quakeFetchTask = new QuakeAsyncTask();
        quakeFetchTask.execute(new QuakeDataFetcher() {
            @Override
            public String getURLString() {
                return Earthquakes.QUAKE_DATA_URL;
            }

            @Override
            public void onQuakeListAvailable(List<Earthquake> quakeList) {
                mQuakeList.clear();
                mQuakeList.addAll(quakeList);
                ((BaseAdapter) quakeListView.getAdapter()).notifyDataSetChanged();
            }
        });
    }
}
