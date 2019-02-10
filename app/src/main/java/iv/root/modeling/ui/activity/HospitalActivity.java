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
import iv.root.modeling.hospital.Model;

public class HospitalActivity extends AppCompatActivity {
    @BindView(R.id.inputClientMin)
    EditText inputClientMin;
    @BindView(R.id.inputClientMax)
    EditText inputClientMax;
    @BindView(R.id.inputRegMin)
    EditText inputRegMin;
    @BindView(R.id.inputRegMax)
    EditText inputRegMax;
    @BindView(R.id.inputProcessMin)
    EditText inputProcessMin;
    @BindView(R.id.inputProcessMax)
    EditText inputProcessMax;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hospital);
        ButterKnife.bind(this);
    }

    public static void start(Activity activity) {
        Intent intent = new Intent(activity, HospitalActivity.class);
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
                Model hospital = new Model(
                        Integer.valueOf(inputClientMin.getText().toString()),
                        Integer.valueOf(inputClientMax.getText().toString()),
                        Integer.valueOf(inputRegMin.getText().toString()),
                        Integer.valueOf(inputRegMax.getText().toString()),
                        Integer.valueOf(inputProcessMin.getText().toString()),
                        Integer.valueOf(inputProcessMax.getText().toString())
                );
                hospital.modeling();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
