package com.example.android.funtravel.utils.ui;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import androidx.appcompat.widget.AppCompatImageView;
import android.text.TextUtils;
import android.util.AttributeSet;

import com.example.android.funtravel.R;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;


public class DynamicHeightNetworkImageView extends AppCompatImageView {
    private float mAspectRatio = 1.5f;

    private Bitmap mBitmap;
    private Protocol mProtocol;

    private Target mTarget = new Target() {
        @Override
        public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
            setImageBitmap(bitmap);
            mBitmap = bitmap;

            if (mProtocol != null) {
                mProtocol.onImageLoaded();
            }
        }

        @Override
        public void onBitmapFailed(Drawable errorDrawable) {
            setBackground(errorDrawable);
        }

        @Override
        public void onPrepareLoad(Drawable placeHolderDrawable) {
            setBackground(placeHolderDrawable);
        }
    };

    public DynamicHeightNetworkImageView(Context context) {
        super(context);
    }

    public DynamicHeightNetworkImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public DynamicHeightNetworkImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public void setAspectRatio(float aspectRatio) {
        mAspectRatio = aspectRatio;
        requestLayout();
    }

    public void loadImage(String imageUrl) {
        if (TextUtils.isEmpty(imageUrl)) {
            setImageResource(R.drawable.attention);
        } else {
            Picasso.with(getContext())
                    .load(imageUrl)
                    .placeholder(R.color.white)
                    .error(R.color.white)
                    .into(mTarget);
        }
    }

    public Bitmap getBitmap() { return mBitmap; }

    public void setProtocol(Protocol protocol) {
        mProtocol = protocol;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int measuredWidth = getMeasuredWidth();
        setMeasuredDimension(measuredWidth, (int) (measuredWidth / mAspectRatio));
    }

    public interface Protocol {
        void onImageLoaded();
    }
}
