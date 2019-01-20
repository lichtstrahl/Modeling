package iv.root.modeling.ui.fragment.util;

import android.support.v4.app.Fragment;

import iv.root.modeling.ui.fragment.ActionFragment;
import iv.root.modeling.ui.fragment.BaseFragment;
import iv.root.modeling.ui.fragment.DTFragment;

abstract public class FragmentsHolder {
    private static BaseFragment[] fragments = new BaseFragment[] {
            ActionFragment.newInstance(),
            DTFragment.newInstance()
    };

    public static Fragment getFragment(int i) {
        return fragments[i];
    }

    public static String getFragmentName(int i) {
        return fragments[i].getName();
    }
}
