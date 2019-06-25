package news.tencent.charco.android.view.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;

import news.tencent.charco.android.GetSMS;
import news.tencent.charco.android.Key;
import news.tencent.charco.android.NewsApplication;
import news.tencent.charco.android.R;
import news.tencent.charco.android.utils.HttpUtil;
import news.tencent.charco.android.view.fragment.CodeLoginActivity;

import static news.tencent.charco.android.NewsApplication.getContext;

public class RegisterActivity extends AppCompatActivity {

    private TimeCount time;
    private Handler handler=new Handler();
    private TextView register_gotoLogin;
    private Button register_btn_cancel;
    private EditText regName;
    private EditText regPsd;
    private EditText regPhone;
    private Button reg_submit;
    private RadioButton man;
    private RadioButton men;
    private EditText regCode;
    private Button regGetCode;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_activity_layout);

        register_gotoLogin = findViewById(R.id.register_gotoLogin);
        register_gotoLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RegisterActivity.this,CodeLoginActivity.class);
                startActivity(intent);
                finish();
            }
        });

        register_btn_cancel = findViewById(R.id.register_btn_cancel);
        register_btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        regName = findViewById(R.id.regName);
        regPsd = findViewById(R.id.regPsd);
        regPhone = findViewById(R.id.regPhone);
        man = findViewById(R.id.man);
        men = findViewById(R.id.men);
        regCode = findViewById(R.id.regCode);
        regGetCode = findViewById(R.id.regGetCode);
        time = new TimeCount(60000, 1000);

        regGetCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String regPhones = regPhone.getText().toString();
                System.out.println(regPhones);
                if(regPhone.length() == 0 || regPhone == null){
                    Toast.makeText(RegisterActivity.this,"请输入相关信息",Toast.LENGTH_LONG).show();
                    return;
                }
                if(regPhone.length() != 11){
                    Toast.makeText(getContext(),"手机号码长度错误",Toast.LENGTH_LONG).show();
                    return;
                }
                if(!isNetworkAvalible(NewsApplication.getContext())){
                    Toast.makeText(getContext(),"网络异常",Toast.LENGTH_LONG).show();
                    return;
                }
                time.start();
                System.out.println(regPhones);
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Key key = new Key();
                        HashMap<String,Object> map = new HashMap<>();
                        map.put("phone",regPhones);
                        key.setMap(map);
                        key.setSessionid("123456");
                        GetSMS getSMS = HttpUtil.send("http://47.106.112.159:8080/android/register/registerGetSMS",key);
                        System.out.println("----------------" + getSMS.getMessage());
                        if(getSMS.getMessage().equals("wrong")){
                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(getContext(),"信息发送失败，请稍后再试",Toast.LENGTH_LONG).show();
                                }
                            });
                        }
                        if(getSMS.getMessage().equals("error")){
                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(getContext(),"验证码生成失败，请稍后再试",Toast.LENGTH_LONG).show();
                                }
                            });
                        }
                        if(getSMS.getMessage().equals("success")){
                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(getContext(),"发送成功,请注意接收",Toast.LENGTH_LONG).show();
                                }
                            });
                        }else {
                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(getContext(),"信息有误",Toast.LENGTH_LONG).show();
                                    return;
                                }
                            });

                        }

                    }
                }).start();
            }
        });


        reg_submit = findViewById(R.id.reg_submit);
        reg_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String code = regCode.getText().toString();
                System.out.println("000000000000000000   "+code);
                final String phone = regPhone.getText().toString();
                if(regName.length() == 0 || regName == null){
                    Toast.makeText(RegisterActivity.this,"请输入相关信息",Toast.LENGTH_LONG).show();
                    return;
                }
                if(regPsd.length() == 0 || regPsd == null){
                    Toast.makeText(RegisterActivity.this,"请输入相关信息",Toast.LENGTH_LONG).show();
                    return;
                }
                if(regPhone.length() == 0 || regPhone == null){
                    Toast.makeText(RegisterActivity.this,"请输入相关信息",Toast.LENGTH_LONG).show();
                    return;
                }
                if(!man.isChecked() && !men.isChecked()){
                    Toast.makeText(RegisterActivity.this,"请输入相关信息",Toast.LENGTH_LONG).show();
                    return;
                }
                if(regCode.length() == 0 || regCode == null){
                    Toast.makeText(RegisterActivity.this,"请输入相关信息",Toast.LENGTH_LONG).show();
                    return;
                }
                if(!isNetworkAvalible(NewsApplication.getContext())){
                    Toast.makeText(getContext(),"网络异常",Toast.LENGTH_LONG).show();
                    return;
                }
                final String name = regName.getText().toString();
                final String password = regPsd.getText().toString();

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Key key = new Key();
                        HashMap<String,Object> map = new HashMap<>();
                        map.put("name",name);
                        map.put("password",password);
                        map.put("phone",phone);
                        map.put("code",code);
                        if(man.isChecked()){
                            map.put("sex",0);
                            key.setMap(map);
                        }else {
                            map.put("sex",1);
                            key.setMap(map);
                        }
                        final GetSMS getSMS = HttpUtil.send("http://47.106.112.159:8080/android/register/register",key);
                        System.out.println("----------------" + getSMS.getMessage());
                        if(getSMS.getMessage().equals("phoneGets")){
                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(getContext(),"手机号码已被注册",Toast.LENGTH_LONG).show();
                                }
                            });
                            return;
                        }
                        if(getSMS.getMessage().equals("erCode")){
                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(getContext(),"验证码错误",Toast.LENGTH_LONG).show();
                                }
                            });
                            return;
                        }
                        if(getSMS.getMessage().equals("outTime")){
                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(getContext(),"验证码已失效",Toast.LENGTH_LONG).show();
                                }
                            });
                            return;
                        }
                        if(getSMS.getMessage().equals("success")){
                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(getContext(),"注册成功",Toast.LENGTH_LONG).show();
                                    /*Intent intent = new Intent(RegisterActivity.this,CodeLoginActivity.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    startActivity(intent);*/
                                    finish();
                                }
                            });
                            return;
                        }else {
                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(getContext(),"信息有误,请修改信息",Toast.LENGTH_LONG).show();
                                    Intent intent = new Intent(RegisterActivity.this,CodeLoginActivity.class);
                                    startActivity(intent);
                                    finish();
                                }
                            });
                        }
                    }
                }).start();
            }
        });
    }

    static boolean isNetworkAvalible(Context context) {
        // 获得网络状态管理器
        ConnectivityManager connectivityManager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);

        if (connectivityManager == null) {
            return false;
        } else {
            // 建立网络数组
            NetworkInfo[] net_info = connectivityManager.getAllNetworkInfo();

            if (net_info != null) {
                for (int i = 0; i < net_info.length; i++) {
                    // 判断获得的网络状态是否是处于连接状态
                    if (net_info[i].getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    class TimeCount extends CountDownTimer {

        public TimeCount(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onTick(long millisUntilFinished) {
            regGetCode.setBackgroundColor(Color.parseColor("#B6B6D8"));
            regGetCode.setClickable(false);
            regGetCode.setText("("+millisUntilFinished / 1000 +") 秒后可重新发送");
            regGetCode.setBackgroundColor(Color.parseColor("#00000000"));
        }

        @Override
        public void onFinish() {
            regGetCode.setText("重新获取验证码");
            regGetCode.setClickable(true);

        }
    }
}
