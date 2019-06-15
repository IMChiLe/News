package news.tencent.charco.android.view.fragment;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import news.tencent.charco.android.R;

public class PsdLoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.psd_login_activity);
        TextView gotoCode = findViewById(R.id.gotoCode);
        gotoCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PsdLoginActivity.this,CodeLoginActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}
