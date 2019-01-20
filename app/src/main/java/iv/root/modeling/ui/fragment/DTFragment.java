package iv.root.modeling.ui.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import iv.root.modeling.R;

public class DTFragment extends BaseFragment {
    private static final String NAME = "dt";
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_modeling_dt, container, false);
        return view;
    }

    public static DTFragment newInstance() {
        DTFragment fragment = new DTFragment();
        return fragment;
    }

    @Override
    public String getName() {
        return NAME;
    }
}
