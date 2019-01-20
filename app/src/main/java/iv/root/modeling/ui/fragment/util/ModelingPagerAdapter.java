package iv.root.modeling.ui.fragment.util;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class ModelingPagerAdapter extends FragmentPagerAdapter {
    private int count;

    public ModelingPagerAdapter(FragmentManager fm, int c) {
        super(fm);
        count = c;
    }

    @Override
    public Fragment getItem(int i) {
        return FragmentsHolder.getFragment(i);
    }

    @Override
    public int getCount() {
        return count;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return FragmentsHolder.getFragmentName(position);
    }
}
