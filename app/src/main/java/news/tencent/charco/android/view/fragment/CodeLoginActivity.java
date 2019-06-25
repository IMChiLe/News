package news.tencent.charco.android.view.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;

import news.tencent.charco.android.GetSMS;
import news.tencent.charco.android.Key;
import news.tencent.charco.android.NewsApplication;
import news.tencent.charco.android.R;
import news.tencent.charco.android.User;
import news.tencent.charco.android.utils.HttpUtil;
import news.tencent.charco.android.view.activity.MainActivity;
import news.tencent.charco.android.view.activity.RegisterActivity;
import news.tencent.charco.android.view.activity.UpdatePsdActivity;

import static news.tencent.charco.android.NewsApplication.getContext;

public class CodeLoginActivity extends AppCompatActivity {

    private TimeCount time;
    private Button btnGetcode;
    private EditText codeName ;
    private EditText codePassword;
    private Button codeLogin;
    private Handler handler=new Handler();
    private EditText psdName;
    private EditText psdPassword;
    private Button login;
    private Button codeRegister;
    private Button loginRegister;
    private TextView login_error;
    private TextView codeLogin_error;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.code_login_activity);

        TextView gotopsd = findViewById(R.id.gotoPsd);
        gotopsd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RelativeLayout codeLayout = findViewById(R.id.code_login_layout);
                RelativeLayout psdLayout = findViewById(R.id.psd_login_layout);
                codeLayout.setVisibility(View.GONE);
                psdLayout.setVisibility(View.VISIBLE);
            }
        });

        login_error = findViewById(R.id.login_error);
        codeLogin_error = findViewById(R.id.codeLogin_error);
        login_error.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CodeLoginActivity.this,UpdatePsdActivity.class);
                startActivity(intent);
            }
        });
        codeLogin_error.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CodeLoginActivity.this,UpdatePsdActivity.class);
                startActivity(intent);
            }
        });

        TextView gotocode = findViewById(R.id.gotoCode);
        gotocode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RelativeLayout codeLayout = findViewById(R.id.code_login_layout);
                RelativeLayout psdLayout = findViewById(R.id.psd_login_layout);
                codeLayout.setVisibility(View.VISIBLE);
                psdLayout.setVisibility(View.GONE);
            }
        });

        codeRegister = findViewById(R.id.codeRegister);
        loginRegister = findViewById(R.id.loginRegister);

        codeRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CodeLoginActivity.this,RegisterActivity.class);
                startActivity(intent);
            }
        });

        loginRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CodeLoginActivity.this,RegisterActivity.class);
                startActivity(intent);
            }
        });

        time = new TimeCount(60000, 1000);
        btnGetcode=(Button) findViewById(R.id.getCode);
        codeName = findViewById(R.id.codeName);
        codeLogin = findViewById(R.id.codeLogin);
        codePassword = findViewById(R.id.codePassword);

        psdName = findViewById(R.id.psdName);
        psdPassword = findViewById(R.id.psdPassword);
        login = findViewById(R.id.login);
        codeRegister = findViewById(R.id.codeRegister);
        loginRegister = findViewById(R.id.loginRegister);


        btnGetcode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    if(codeName.length()==0 || codeName==null){
                        Toast.makeText(CodeLoginActivity.this,"请输入相关信息",Toast.LENGTH_LONG).show();
                        return;
                    }
                    final String s = codeName.getText().toString();
                    if(s.length() != 11){
                        Toast.makeText(getContext(),"手机号码长度错误",Toast.LENGTH_LONG).show();
                        return;
                    }
                    if(!isNetworkAvalible(NewsApplication.getContext())){
                        Toast.makeText(getContext(),"网络异常",Toast.LENGTH_LONG).show();
                        return;
                    }
                    time.start();
                    System.out.println(s);
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            /*SMSActivity smsActivity = new SMSActivity();
                            smsActivity.getCode(s);*/
                            Key key = new Key();
                            HashMap<String,Object> map = new HashMap<>();
                            map.put("phone",s);
                            key.setMap(map);
                            key.setSessionid("123456");
                            GetSMS getSMS = HttpUtil.send("http://47.106.112.159:8080/android/login/getSMS",key);
                            System.out.println("----------------" + getSMS.getMessage());
                            if(getSMS.getMessage().equals("noPhone")){
                                handler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(getContext(),"账号不存在，请注册",Toast.LENGTH_LONG).show();

                                    }
                                });
                            }
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
                                        Toast.makeText(getContext(),"发生异常，请稍后再试",Toast.LENGTH_LONG).show();
                                        return;
                                    }
                                });

                            }

                        }
                    }).start();
                }catch (Exception e){
                    Toast.makeText(CodeLoginActivity.this,"未知错误",Toast.LENGTH_LONG).show();
                    return;
                }
            }
        });

        codeLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    final String phone = codeName.getText().toString();
                    final String code = codePassword.getText().toString();
                    if(phone.length()==0 || code.length() == 0){
                        Toast.makeText(CodeLoginActivity.this,"请输入相关信息",Toast.LENGTH_LONG).show();
                        return;
                    }
                    if(phone.length() != 11){
                        Toast.makeText(getContext(),"手机号码长度错误",Toast.LENGTH_LONG).show();
                        return;
                    }
                    if(code.length() != 6){
                        Toast.makeText(getContext(),"验证码长度错误",Toast.LENGTH_LONG).show();
                        return;
                    }
                    if(!isNetworkAvalible(NewsApplication.getContext())){
                        Toast.makeText(getContext(),"网络异常",Toast.LENGTH_LONG).show();
                        return;
                    }
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            /*SMSActivity smsActivity = new SMSActivity();
                            smsActivity.getCode(s);*/
                            Key key = new Key();
                            HashMap<String,Object> map = new HashMap<>();
                            map.put("phone",phone);
                            map.put("code",code);
                            key.setMap(map);
                            key.setSessionid("123456");
                            final GetSMS getSMS = HttpUtil.send("http://47.106.112.159:8080/android/login/codeLogin",key);
                            System.out.println("----------------" + getSMS.getMessage());
                            if(getSMS.getMessage().equals("noCode")){
                                handler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(getContext(),"该手机号码无效",Toast.LENGTH_LONG).show();
                                    }
                                });
                                return;
                            }
                            if(getSMS.getMessage().equals("erCode")){
                                handler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(CodeLoginActivity.this,"请输入相关信息",Toast.LENGTH_LONG).show();
                                        Toast.makeText(getContext(),"验证码错误",Toast.LENGTH_LONG).show();
                                    }
                                });
                                return;
                            }
                            if(getSMS.getMessage().equals("outCode")){
                                handler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(getContext(),"验证码已过期失效",Toast.LENGTH_LONG).show();
                                    }
                                });
                                return;
                            }
                            if(getSMS.getMessage().equals("success")){
                                User user = getSMS.getUser();

                                System.out.println(user.getName() + " " + user.getPassword() + " " + user.getPhone());
                                handler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(getContext(),"登录成功",Toast.LENGTH_LONG).show();
                                        SharedPreferences.Editor editor = getSharedPreferences("data",MODE_PRIVATE).edit();
                                        User user1 = getSMS.getUser();
                                        editor.putString("phone",user1.getName());
                                        editor.putString("name",user1.getName());
                                        editor.apply();
                                        Intent intent = new Intent();
                                        intent.putExtra("name",user1.getName());
                                        setResult(RESULT_OK,intent);
                                        /*  startActivity(intent);*/
                                        finish();
                                    }
                                });
                            }else {
                                handler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(getContext(),"发生异常，请稍后再试",Toast.LENGTH_LONG).show();
                                        return;
                                    }
                                });
                            }
                        }
                    }).start();
                }catch (Exception e){
                    Toast.makeText(CodeLoginActivity.this,"未知错误",Toast.LENGTH_LONG).show();
                    return;
                }
            }
        });


        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    final String name = psdName.getText().toString();
                    final String password = psdPassword.getText().toString();
                    System.out.println("-----------------------"+name+password);
                    if(psdName.length() == 0 || psdName == null || psdPassword.length() == 0 || psdPassword == null){
                        Toast.makeText(getContext(),"请输入相关信息",Toast.LENGTH_LONG).show();
                        return;
                    }
                    if(psdName.length() != 11){
                        Toast.makeText(getContext(),"手机号码长度有误",Toast.LENGTH_LONG).show();
                        return;
                    }
                    if(!isNetworkAvalible(NewsApplication.getContext())){
                        Toast.makeText(getContext(),"网络异常",Toast.LENGTH_LONG).show();
                        return;
                    }
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            Key key = new Key();
                            HashMap<String,Object> map = new HashMap<>();
                            map.put("phone",name);
                            map.put("password",password);
                            key.setSessionid("11111111");
                            key.setMap(map);
                            final GetSMS getSMS = HttpUtil.send("http://47.106.112.159:8080/android/login/psdLogin",key);
                            System.out.println("--------------"+getSMS.getMessage());
                            if(getSMS.getMessage().equals("noPhone")){
                                handler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(CodeLoginActivity.this,"账号不存在",Toast.LENGTH_LONG).show();
                                    }
                                });
                            }
                            if(getSMS.getMessage().equals("noPassword")){
                                handler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(getContext(),"密码错误",Toast.LENGTH_LONG).show();
                                    }
                                });
                            }
                            if(getSMS.getMessage().equals("success")){
                                User user = getSMS.getUser();
                                System.out.println(user.getName() + " " + user.getPassword() + " " + user.getPhone());
                                handler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(getContext(),"登录成功",Toast.LENGTH_LONG).show();
                                        SharedPreferences.Editor editor = getSharedPreferences("data",MODE_PRIVATE).edit();
                                        User user1 = getSMS.getUser();
                                        editor.putString("phone",user1.getPhone());
                                        editor.putString("name",user1.getName());
                                        editor.apply();

                                        Intent intent = new Intent();
                                        intent.putExtra("name",user1.getName());
                                        setResult(RESULT_OK,intent);
                                        /*  startActivity(intent);*/
                                        finish();
                                    }
                                });
                            }else {
                                handler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(getContext(),"发生异常，请稍后再试",Toast.LENGTH_LONG).show();
                                        return;
                                    }
                                });
                            }
                        }

                    }).start();
                }catch (Exception e){
                    Toast.makeText(CodeLoginActivity.this,"未知错误",Toast.LENGTH_LONG).show();
                    return;
                }
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
}
