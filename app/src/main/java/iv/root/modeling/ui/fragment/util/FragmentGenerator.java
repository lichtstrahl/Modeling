package iv.root.modeling.ui.fragment.util;

import android.support.v4.app.Fragment;

import iv.root.modeling.ui.fragment.ActionFragment;
import iv.root.modeling.ui.fragment.DTFragment;

abstract public class FragmentGenerator {
    private static Fragment[] fragments = new Fragment[] {
            ActionFragment.newInstance(),
            DTFragment.newInstance()
    };

    public static Fragment getFragment(int i) {
        return fragments[i];
    }
}
