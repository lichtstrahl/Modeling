package iv.root.modeling.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.Viewport;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import iv.root.modeling.R;
import iv.root.modeling.distribution.FragmentAPI;
import iv.root.modeling.distribution.erlang.ErlangFragment;
import iv.root.modeling.distribution.normal.NormalFragment;
import iv.root.modeling.distribution.puasson.PuassonFragment;
import iv.root.modeling.distribution.uniform.UniformFragment;

public class GraphActivity extends AppCompatActivity {
    private static final int QUALITY = 100;
    private FragmentAPI fragment;
    @BindView(R.id.graph)
    GraphView graph;
    @BindView(R.id.inputX0)
    EditText inputX0;
    @BindView(R.id.inputXk)
    EditText inputXn;
    @OnClick(R.id.buttonUpdate)
    public void clickUpdate() {
        Double x0 = Double.valueOf(inputX0.getText().toString());
        Double xn = Double.valueOf(inputXn.getText().toString());

        setRangeX(x0, xn);
        fragment.updateX0(x0);
        fragment.updateXn(xn);
        LineGraphSeries<DataPoint> seriesF = new LineGraphSeries<>(fragment.getPoints_F(QUALITY));
        seriesF.setColor(getResources().getColor(R.color.colorF));
        seriesF.setThickness(7);

        LineGraphSeries<DataPoint> seriesf = new LineGraphSeries<>(fragment.getPoints_f(QUALITY));
        seriesf.setColor(getResources().getColor(R.color.colorf));
        seriesf.setThickness(7);

        graph.removeAllSeries();
        graph.addSeries(seriesF);
        graph.addSeries(seriesf);
    }

    private void setRangeX(double min, double max) {
        Viewport viewport = graph.getViewport();
        viewport.setXAxisBoundsManual(true);
        viewport.setMaxX(max);
        viewport.setMinX(min);
    }

    private void setRangeY(double min, double max) {
        Viewport viewport = graph.getViewport();
        viewport.setYAxisBoundsManual(true);
        viewport.setMinY(min);
        viewport.setMaxX(max);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graph);
        ButterKnife.bind(this);

        fragment = setupUniformFragment(
                Double.valueOf(inputX0.getText().toString()),
                Double.valueOf(inputXn.getText().toString())
        );

        graph.getViewport().setScalableY(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_layout, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Double x0 = Double.valueOf(inputX0.getText().toString());
        Double xn = Double.valueOf(inputXn.getText().toString());
        switch (item.getItemId()) {
            case R.id.menuItemUniform:
                fragment = setupUniformFragment(x0, xn);
                return true;
            case R.id.menuItemPuasson:
                fragment = setupPuassonFragment(x0, xn);
                return true;
            case R.id.menuItemNormal:
                fragment = setupNormalFragment(x0, xn);
                return true;
            case R.id.menuItemErlang:
                fragment = setupErlangFragment(x0, xn);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public static void start(Activity activity) {
        Intent intent = new Intent(activity, GraphActivity.class);
        activity.startActivity(intent);
    }

    private FragmentAPI setupUniformFragment(Double x0, Double xn) {
        setTitle(R.string.uniformDistribution);
        UniformFragment f = UniformFragment.getInstance(x0, xn);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.headLayout, f)
                .commit();
        return f;
    }

    private FragmentAPI setupPuassonFragment(Double x0, Double xn) {
        setTitle(R.string.puassonDistribution);
        PuassonFragment f = PuassonFragment.getInstance(x0, xn);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.headLayout, f)
                .commit();
        return f;
    }

    private FragmentAPI setupNormalFragment(Double x0, Double xn) {
        setTitle(R.string.normalDistribution);
        NormalFragment f = NormalFragment.getInstance(x0, xn);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.headLayout, f)
                .commit();
        return f;
    }

    private FragmentAPI setupErlangFragment(Double x0, Double xn) {
        setTitle(R.string.erlangDistribution);
        ErlangFragment f = ErlangFragment.getInstance(x0, xn);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.headLayout, f)
                .commit();
        return f;
    }
}
