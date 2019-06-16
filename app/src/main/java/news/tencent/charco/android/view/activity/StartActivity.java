package news.tencent.charco.android.view.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import news.tencent.charco.android.R;
import news.tencent.charco.android.view.fragment.PsdLoginActivity;

public class StartActivity extends AppCompatActivity {

    private Handler handler=new Handler();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent intent = new Intent(StartActivity.this,MainActivity.class);
                    StartActivity.this.startActivity(intent);
                    finish();
                }
            },2500);
    }
}
