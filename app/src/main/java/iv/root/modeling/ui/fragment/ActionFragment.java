package iv.root.modeling.ui.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import iv.root.modeling.R;

public class ActionFragment extends BaseFragment {
    private static final String NAME = "Action";
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_modeling_action, container, false);
        return view;
    }

    public static ActionFragment newInstance() {
        ActionFragment fragment = new ActionFragment();
        return fragment;
    }

    @Override
    public String getName() {
        return NAME;
    }
}
