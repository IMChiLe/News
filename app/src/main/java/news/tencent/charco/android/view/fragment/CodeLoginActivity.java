package news.tencent.charco.android.view.fragment;

import android.content.Intent;
import android.graphics.Color;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import news.tencent.charco.android.R;
import news.tencent.charco.android.view.activity.SMSActivity;

import static news.tencent.charco.android.NewsApplication.getContext;

public class CodeLoginActivity extends AppCompatActivity {

    private TimeCount time;
    private Button btnGetcode;
    private EditText codeName;
    private EditText codePassword;
    private Button codeLogin;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.code_login_activity);

        TextView gotopsd = findViewById(R.id.gotoPsd);
        gotopsd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CodeLoginActivity.this,PsdLoginActivity.class);
                startActivity(intent);
                finish();
            }
        });

        time = new TimeCount(60000, 1000);
        btnGetcode=(Button) findViewById(R.id.getCode);
        codeName = findViewById(R.id.codeName);
        codeLogin = findViewById(R.id.codeLogin);
        codePassword = findViewById(R.id.codePassword);
        btnGetcode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(codeName.length()==0 || codeName==null){
                    Toast.makeText(CodeLoginActivity.this,"请输入相关信息",Toast.LENGTH_LONG).show();
                }else {
                    time.start();
                    final String s = codeName.getText().toString();
                    System.out.println(s);
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            SMSActivity smsActivity = new SMSActivity();
                            smsActivity.getCode(s);
                            Looper.prepare();
                            new Handler().post(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(getContext(),"发送成功,请注意接收",Toast.LENGTH_LONG).show();
                                }
                            });
                            Looper.loop();
                        }
                    }).start();
                }
            }
        });

        codeLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String phone = codeName.getText().toString();
                String code = codePassword.getText().toString();
            }
        });
    }

    class TimeCount extends CountDownTimer {

        public TimeCount(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onTick(long millisUntilFinished) {
            btnGetcode.setBackgroundColor(Color.parseColor("#B6B6D8"));
            btnGetcode.setClickable(false);
            btnGetcode.setText("("+millisUntilFinished / 1000 +") 秒后可重新发送");
            btnGetcode.setBackgroundColor(Color.parseColor("#00000000"));
        }

        @Override
        public void onFinish() {
            btnGetcode.setText("重新获取验证码");
            btnGetcode.setClickable(true);

        }
    }
}
