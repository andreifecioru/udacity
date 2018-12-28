package com.example.android.funtravel.ui.widget;

import android.content.Intent;
import android.widget.RemoteViewsService;

public class FunTravelRemoteViewsService extends RemoteViewsService {
    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new FunTravelRemoteViewsFactory(getApplicationContext(), intent);
    }
}
