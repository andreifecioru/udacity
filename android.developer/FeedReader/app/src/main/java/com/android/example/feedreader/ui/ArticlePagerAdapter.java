package com.android.example.feedreader.ui;

import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.ViewGroup;

import java.lang.ref.WeakReference;
import java.util.Hashtable;


public class ArticlePagerAdapter extends FragmentStatePagerAdapter {
    private long mArticleCount;

    private Hashtable<Integer, WeakReference<Fragment>> mFragmentReferences = new Hashtable<>();

    public ArticlePagerAdapter(FragmentManager fragmentManager) {
        super(fragmentManager);
    }

    @Override
    public Fragment getItem(int position) {
        return ArticleDetailsFragment.newInstance(position);
    }

    /**
     * Getting to the currently displayed fragment in a {@link FragmentStatePagerAdapter} is a real pain.
     *
     * See: https://stackoverflow.com/questions/14035090/how-to-get-existing-fragments-when-using-fragmentpageradapter
     */
    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        Fragment createdFragment = (Fragment) super.instantiateItem(container, position);
        mFragmentReferences.put(position, new WeakReference<>(createdFragment));
        return createdFragment;
    }

    @Override
    public int getCount() {
        return (int) mArticleCount;
    }

    Fragment getItemAtPos(int position) {
        WeakReference<Fragment> fragmentRef =  mFragmentReferences.get(position);
        return (fragmentRef != null) ? fragmentRef.get() : null;
    }

    /**
     * Forces an update of the adapter view.
     *
     * @param articleCount The number of articles to be displayed by this adapter.
     */
    void notifyChanged(long articleCount) {
        mArticleCount = articleCount;
        notifyDataSetChanged();
    }
}
