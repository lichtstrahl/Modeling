package iv.root.modeling.ui;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import iv.root.modeling.R;

public class QueueActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_queue);
    }

    public static void start(Activity activity) {
        Intent intent = new Intent(activity, QueueActivity.class);
        activity.startActivity(intent);
    }
}
