package iv.root.modeling.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

import butterknife.BindView;
import butterknife.ButterKnife;
import iv.root.modeling.R;
import iv.root.modeling.app.App;

public class InfoCenterActivity extends AppCompatActivity {
    @BindView(R.id.inputDT)
    EditText inputDT;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_center);
        ButterKnife.bind(this);

    }

    public static void start(Activity activity) {
        Intent intent = new Intent(activity, InfoCenterActivity.class);
        activity.startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_modeling, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menuItemRunModeling:
                App.logI("Запуск моделирования");
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
