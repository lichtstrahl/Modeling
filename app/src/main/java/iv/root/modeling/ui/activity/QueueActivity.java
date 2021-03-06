package iv.root.modeling.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.PagerTabStrip;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import butterknife.BindView;
import butterknife.ButterKnife;
import iv.root.modeling.R;
import iv.root.modeling.ui.fragment.util.ModelingPagerAdapter;

public class QueueActivity extends AppCompatActivity {
    private static final int COUNT_PAGE = 2;
    @BindView(R.id.viewPager)
    ViewPager viewPager;
    PagerAdapter pagerAdapter;
    @BindView(R.id.pagerTabs)
    PagerTabStrip pagerTabs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_queue);
        ButterKnife.bind(this);

        pagerAdapter = new ModelingPagerAdapter(getSupportFragmentManager(), COUNT_PAGE);
        viewPager.setAdapter(pagerAdapter);
    }

    public static void start(Activity activity) {
        Intent intent = new Intent(activity, QueueActivity.class);
        activity.startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_modeling, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return false;
    }
}
