package com.example.android.mygarden.widget;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.example.android.mygarden.R;
import com.example.android.mygarden.utils.PlantUtils;

import static android.provider.BaseColumns._ID;
import static com.example.android.mygarden.provider.PlantContract.BASE_CONTENT_URI;
import static com.example.android.mygarden.provider.PlantContract.PATH_PLANTS;
import static com.example.android.mygarden.provider.PlantContract.PlantEntry.COLUMN_CREATION_TIME;
import static com.example.android.mygarden.provider.PlantContract.PlantEntry.COLUMN_LAST_WATERED_TIME;
import static com.example.android.mygarden.provider.PlantContract.PlantEntry.COLUMN_PLANT_TYPE;
import static com.example.android.mygarden.ui.PlantDetailActivity.EXTRA_PLANT_ID;

public class GridWidgetService extends RemoteViewsService {
    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new GridRemoteViewsFactory(this.getApplicationContext());
    }
}


class GridRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {
    private static final String LOG_TAG = GridRemoteViewsFactory.class.getSimpleName();

    private final Context mContext;
    private Cursor mCursor;

    GridRemoteViewsFactory(Context context) {
        mContext = context;
    }

    @Override
    public void onCreate() {

    }

    @Override
    public void onDataSetChanged() {
        if (mCursor != null) mCursor.close();

        Uri PLANT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_PLANTS).build();
        mCursor = mContext.getContentResolver().query(PLANT_URI, null, null, null,
                COLUMN_CREATION_TIME);

        Log.d(LOG_TAG, "On dataset changed: " + mCursor.getCount());
    }

    @Override
    public void onDestroy() {
        if (mCursor != null) {
            mCursor.close();
            mCursor = null;
        }
    }

    @Override
    public int getCount() {
        if (mCursor == null) return 0;
        return mCursor.getCount();
    }

    @Override
    public RemoteViews getViewAt(int position) {
        if (mCursor == null || mCursor.getCount() == 0) return null;

        mCursor.moveToPosition(position);

        int idIndex = mCursor.getColumnIndex(_ID);
        int createTimeIndex = mCursor.getColumnIndex(COLUMN_CREATION_TIME);
        int waterTimeIndex = mCursor.getColumnIndex(COLUMN_LAST_WATERED_TIME);
        int plantTypeIndex = mCursor.getColumnIndex(COLUMN_PLANT_TYPE);

        long plantId = mCursor.getLong(idIndex);
        long createTime = mCursor.getLong(createTimeIndex);
        long wateredTime = mCursor.getLong(waterTimeIndex);
        int plantType = mCursor.getInt(plantTypeIndex);

        long now = System.currentTimeMillis();

        int imgResId = PlantUtils.getPlantImageRes(mContext, now - createTime, now - wateredTime, plantType);


        Log.d(LOG_TAG, "get view. Plant id: " + plantId + " on position: " + position);

        RemoteViews views = new RemoteViews(mContext.getPackageName(), R.layout.plant_widget);
        views.setImageViewResource(R.id.widget_plant_image, imgResId);
        views.setTextViewText(R.id.widget_plant_id, String.valueOf(plantId));
        views.setViewVisibility(R.id.widget_water_image, View.GONE);

        Bundle extras = new Bundle();
        extras.putLong(EXTRA_PLANT_ID, plantId);
        Intent fillInIntent = new Intent();
        fillInIntent.putExtras(extras);
        views.setOnClickFillInIntent(R.id.widget_plant_image, fillInIntent);

        Log.d(LOG_TAG, "get view: set fill in intent for " + plantId);

        return views;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }
}
