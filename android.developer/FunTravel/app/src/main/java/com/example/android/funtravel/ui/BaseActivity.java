package com.example.android.funtravel.ui;

import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.widget.TextView;

import butterknife.BindAnim;
import butterknife.BindView;

import com.example.android.funtravel.R;
import com.example.android.funtravel.utils.NetworkUtils;
import com.example.android.funtravel.utils.NetworkUtils.ConnectivityStatus;


/**
 * Base class for our activities providing the ability to monitor the
 * connectivity status.
 *
 * User is notified about changes in connectivity status with a small message
 * strip at the top (similar with the way the official YouTube app does it).
 */
public abstract class BaseActivity extends AppCompatActivity {
    private static final String LOG_TAG = BaseActivity.class.getSimpleName();

    private static final long CONNECTIVITY_CHECK_INTERVAL = 1000L;

    private ConnectivityStatus mConnStatus = ConnectivityStatus.UNKNOWN;
    private final Handler mHandler = new Handler();

    @BindView(R.id.conn_status_text_view) TextView mConnStatusTextView;
    @BindAnim(R.anim.slide_up) Animation mSlideUpAnimation;
    @BindAnim(R.anim.slide_down) Animation mSlideDownAnimation;

    // continuously check for changes in connectivity status
    private Runnable mCheckForConnectivity = new Runnable() {
        @Override
        public void run() {
            ConnectivityStatus newStatus = NetworkUtils.checkConnectivity(BaseActivity.this);

            // offline -> online transition detected
            if (newStatus == ConnectivityStatus.ONLINE && mConnStatus == ConnectivityStatus.OFFLINE) {
                showBackOnlineStatus();
            }

            // online -> offline transition detected
            if (newStatus == ConnectivityStatus.OFFLINE &&
                    (mConnStatus == ConnectivityStatus.ONLINE ||
                            mConnStatus == ConnectivityStatus.UNKNOWN)) {
                showOfflineStatus();
            }

            mConnStatus = newStatus;
            mHandler.postDelayed(this, CONNECTIVITY_CHECK_INTERVAL);
        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        mHandler.post(mCheckForConnectivity);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mHandler.removeCallbacks(mCheckForConnectivity);
    }

    /** Callback invoked when transitioning into online mode */
    protected void onConnectivityRestored() {
        Log.d(LOG_TAG, "Network connectivity restored.");
    }

    /** Callback invoked when transitioning into offline mode */
    protected void onConnectivityLost() {
        Log.d(LOG_TAG, "Lost network connectivity");
    }

    /**
     * Helper method that displays a user message (on a green strip) when re-gain connectivity.
     *
     * NOTE: this can be easily tested when de-activating air-plane mode.
     */
    private void showBackOnlineStatus() {
        Log.i(LOG_TAG, "Switching back to online mode.");

        if (mConnStatusTextView.getVisibility() == View.VISIBLE) {
            mConnStatusTextView.startAnimation(mSlideUpAnimation);
        }
        mConnStatusTextView.setText(getText(R.string.back_online_status));
        mConnStatusTextView.setBackgroundColor(ContextCompat.getColor(this, R.color.green));
        mConnStatusTextView.startAnimation(mSlideDownAnimation);

        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                // we want to hide the message strip completely once the slide-up animation completes.
                mSlideUpAnimation.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) { }

                    @Override
                    public void onAnimationRepeat(Animation animation) { }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        mConnStatusTextView.setVisibility(View.INVISIBLE);
                    }
                });

                // invoke the callback
                onConnectivityRestored();

                mConnStatusTextView.startAnimation(mSlideUpAnimation);
            }
        }, 5000L);
    }

    /**
     * Helper method that displays a user message (on a black strip) when lose connectivity.
     * The message strip remains visible until we regain connectivity.
     *
     * NOTE: this can be easily tested when activating air-plane mode.
     */
    private void showOfflineStatus() {
        Log.i(LOG_TAG, "Switching to offline mode.");

        if (mConnStatusTextView.getVisibility() == View.VISIBLE) {
            mConnStatusTextView.startAnimation(mSlideUpAnimation);
        }

        mConnStatusTextView.setText(getText(R.string.offline_status));
        mConnStatusTextView.setBackgroundColor(ContextCompat.getColor(this, R.color.black));
        mConnStatusTextView.setVisibility(View.VISIBLE);
        mConnStatusTextView.startAnimation(mSlideDownAnimation);

        // invoke the callback
        onConnectivityLost();
    }
}

