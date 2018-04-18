package com.example.android.sunshine;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class ForecastAdapter extends RecyclerView.Adapter<ForecastAdapter.ForecastAdapterViewHolder> {
    private List<String> mWeatherData;

    ForecastAdapter() {}

    void setWeatherData(List<String> weatherData) {
        mWeatherData = weatherData;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ForecastAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View root = inflater.inflate(R.layout.forecast_list_item, parent, false);

        return new ForecastAdapterViewHolder(root);
    }

    @Override
    public void onBindViewHolder(@NonNull ForecastAdapterViewHolder holder, int position) {
       holder.mWeatherTextView.setText(mWeatherData.get(position));
    }

    @Override
    public int getItemCount() {
        if (mWeatherData == null) return 0;

        return mWeatherData.size();
    }

    static final class ForecastAdapterViewHolder extends RecyclerView.ViewHolder {
        final TextView mWeatherTextView;

        ForecastAdapterViewHolder(View itemView) {
            super(itemView);

            mWeatherTextView = itemView.findViewById(R.id.tv_weather_data);
        }
    }
}
