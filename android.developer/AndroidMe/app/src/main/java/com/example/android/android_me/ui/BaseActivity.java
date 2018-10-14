package com.example.android.android_me.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.android.android_me.R;
import com.example.android.android_me.data.AndroidImageAssets;

public abstract class BaseActivity extends AppCompatActivity
        implements BodyPartFragment.OnClickListener,
                   MasterListFragment.OnImageClickListener {
    private static final int MAX_IMG_IDX = 12;
    private static final int HEAD_PART_IDX = 0;
    private static final int HEAD_IMG_OFFSET = HEAD_PART_IDX * MAX_IMG_IDX;
    private static final int BODY_PART_IDX = 1;
    private static final int BODY_IMG_OFFSET = BODY_PART_IDX * MAX_IMG_IDX;
    private static final int LEGS_PART_IDX = 2 ;
    private static final int LEGS_IMG_OFFSET = LEGS_PART_IDX * MAX_IMG_IDX;

    private static final String HEAD_IDX_KEY = "head.idx.key";
    private static final String BODY_IDX_KEY = "body.idx.key";
    private static final String LEGS_IDX_KEY = "legs.idx.key";

    private BodyPartFragment mHeadFragment;
    private BodyPartFragment mBodyFragment;
    private BodyPartFragment mLegsFragment;

    private int mHeadIdx = 0;
    private int mBodyIdx = 0;
    private int mLegsIdx = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState != null) {
            mHeadIdx = savedInstanceState.getInt(HEAD_IDX_KEY);
            mBodyIdx = savedInstanceState.getInt(BODY_IDX_KEY);
            mLegsIdx = savedInstanceState.getInt(LEGS_IDX_KEY);
        }

        setContent();

        postSetContent();
    }

    protected abstract void setContent();

    private void postSetContent() {
        ViewGroup bodyPartsContainer = findViewById(R.id.body_parts_container);
        boolean isLandscape = (bodyPartsContainer != null);

        Button nextButton = findViewById(R.id.next_button);

        if (isLandscape) {
            initBody();

            nextButton.setVisibility(View.GONE);
        } else {
            if (nextButton != null) { // main activity -> portrait mode

                nextButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final Intent intent = new Intent(BaseActivity.this, AndroidMeActivity.class);
                        intent.putExtra(HEAD_IDX_KEY, mHeadIdx);
                        intent.putExtra(BODY_IDX_KEY, mBodyIdx);
                        intent.putExtra(LEGS_IDX_KEY, mLegsIdx);
                        startActivity(intent);
                    }
                });
            } else { // android-me activity
                Intent intent = getIntent();
                mHeadIdx = intent.getIntExtra(HEAD_IDX_KEY, 0);
                mBodyIdx = intent.getIntExtra(BODY_IDX_KEY, 0);
                mLegsIdx = intent.getIntExtra(LEGS_IDX_KEY, 0);

                initBody();
            }
        }
    }

    private void initBody() {
        FragmentManager fragmentManager = getSupportFragmentManager();

        mHeadFragment = (BodyPartFragment) fragmentManager.findFragmentById(R.id.head_fragment);
        if (mHeadFragment != null) {
            mHeadFragment.setImageResourceId(AndroidImageAssets.getHeads().get(mHeadIdx));
            mHeadFragment.setOnClickListener(this);
        }


        mBodyFragment = (BodyPartFragment) fragmentManager.findFragmentById(R.id.body_fragment);
        if (mBodyFragment != null) {
            mBodyFragment.setImageResourceId(AndroidImageAssets.getBodies().get(mBodyIdx));
            mBodyFragment.setOnClickListener(this);
        }

        mLegsFragment = (BodyPartFragment) fragmentManager.findFragmentById(R.id.legs_fragment);
        if (mLegsFragment != null) {
            mLegsFragment.setImageResourceId(AndroidImageAssets.getLegs().get(mLegsIdx));
            mLegsFragment.setOnClickListener(this);
        }
    }

    protected void setHeadImgIdx(int imgIdx) {
        validateImgIdx(imgIdx);
        mHeadIdx = imgIdx;
        if (mHeadFragment != null) {
            mHeadFragment.setImageResourceId(AndroidImageAssets.getHeads().get(mHeadIdx));
        }
    }

    protected void setBodyImgIdx(int imgIdx) {
        validateImgIdx(imgIdx);
        mBodyIdx = imgIdx;
        if (mBodyFragment != null) {
            mBodyFragment.setImageResourceId(AndroidImageAssets.getBodies().get(mBodyIdx));
        }
    }

    protected void setLegsImgIdx(int imgIdx) {
        validateImgIdx(imgIdx);
        mLegsIdx = imgIdx;
        if (mLegsFragment != null) {
            mLegsFragment.setImageResourceId(AndroidImageAssets.getLegs().get(mLegsIdx));
        }
    }

    private void validateImgIdx(int idx) {
        if (idx < 0 || idx > MAX_IMG_IDX) {
            throw new IllegalArgumentException("Invalid image index: " + idx);
        }
    }

    private int nextImgIdx(int imgIdx) {
        imgIdx++;
        imgIdx = (imgIdx >= 12) ? 0 : imgIdx;
        return imgIdx;
    }

    private void nextHead() {
        mHeadIdx = nextImgIdx(mHeadIdx);
        mHeadFragment.setImageResourceId(AndroidImageAssets.getHeads().get(mHeadIdx));
    }

    private void nextBody() {
        mBodyIdx = nextImgIdx(mBodyIdx);
        mBodyFragment.setImageResourceId(AndroidImageAssets.getBodies().get(mBodyIdx));
    }

    private void nextLegs() {
        mLegsIdx = nextImgIdx(mLegsIdx);
        mLegsFragment.setImageResourceId(AndroidImageAssets.getLegs().get(mLegsIdx));
    }

    @Override
    public void onClick(int id) {
        switch (id) {
            case R.id.head_fragment: nextHead(); break;
            case R.id.body_fragment: nextBody(); break;
            case R.id.legs_fragment: nextLegs(); break;
        }
    }

    // Define the behavior for onImageSelected
    public void onImageSelected(int position) {
        int bodyPartIdx = position / MAX_IMG_IDX;
        switch (bodyPartIdx) {
            case HEAD_PART_IDX: setHeadImgIdx(position - HEAD_IMG_OFFSET); break;
            case BODY_PART_IDX: setBodyImgIdx(position - BODY_IMG_OFFSET); break;
            case LEGS_PART_IDX: setLegsImgIdx(position - LEGS_IMG_OFFSET); break;
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putInt(HEAD_IDX_KEY, mHeadIdx);
        outState.putInt(BODY_IDX_KEY, mBodyIdx);
        outState.putInt(LEGS_IDX_KEY, mLegsIdx);
    }
}
