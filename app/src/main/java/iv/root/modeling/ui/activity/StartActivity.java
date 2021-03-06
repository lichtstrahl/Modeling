package iv.root.modeling.ui.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import butterknife.ButterKnife;
import butterknife.OnClick;
import iv.root.modeling.R;

public class StartActivity extends AppCompatActivity {

    @OnClick(R.id.buttonLab1)
    public void clickStartLab1() {
        GraphActivity.start(this);
    }

    @OnClick(R.id.buttonLab2)
    public void clickStartLab2() {
        RandomActivity.start(this);
    }

    @OnClick(R.id.buttonLab4)
    public void clickStartLab4() {
        QueueActivity.start(this);
    }

    @OnClick(R.id.buttonLab5)
    public void clickStartLab5() {
        InfoCenterActivity.start(this);
    }

    @OnClick(R.id.buttonLab6)
    public void clickStartLab6() {
        HospitalActivity.start(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        ButterKnife.bind(this);
    }
}
