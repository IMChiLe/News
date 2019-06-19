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

public class UpdatePsdActivity extends AppCompatActivity {

    private TimeCount time;
    private Handler handler=new Handler();
    private Button updateGetCode;
    private TextView update_gotoLogin;
    private EditText updatePhone;
    private EditText updatePsd;
    private EditText updatePsds;
    private EditText updateCode;
    private Button update_submit;
    private Button update_btn_cancel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.update_psd_activity);


        update_gotoLogin = findViewById(R.id.update_gotoLogin);
        update_gotoLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(UpdatePsdActivity.this,CodeLoginActivity.class);
                startActivity(intent);
                finish();
            }
        });

        update_btn_cancel = findViewById(R.id.update_btn_cancel);
        update_btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        updateGetCode = findViewById(R.id.updateGetCode);
        updatePhone = findViewById(R.id.updatePhone);
        updatePsd = findViewById(R.id.updatePsd);
        updatePsds = findViewById(R.id.updatePsds);
        updateCode = findViewById(R.id.updateCode);
        update_submit = findViewById(R.id.update_submit);
        time = new TimeCount(60000, 1000);

        updateGetCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String updatePhones = updatePhone.getText().toString();
                System.out.println(updatePhones);
                if(updatePhone.length() == 0 || updatePhone == null){
                    Toast.makeText(UpdatePsdActivity.this,"请输入相关信息",Toast.LENGTH_LONG).show();
                    return;
                }
                if(updatePhone.length() != 11){
                    Toast.makeText(getContext(),"手机号码长度错误",Toast.LENGTH_LONG).show();
                    return;
                }
                if(!isNetworkAvalible(NewsApplication.getContext())){
                    Toast.makeText(getContext(),"网络异常",Toast.LENGTH_LONG).show();
                    return;
                }
                time.start();
                System.out.println(updatePhones);
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Key key = new Key();
                        HashMap<String,Object> map = new HashMap<>();
                        map.put("phone",updatePhones);
                        key.setMap(map);
                        key.setSessionid("123456");
                        GetSMS getSMS = HttpUtil.send("http://47.106.112.159:8080/android/changePsd/updateGetSMS",key);
                        System.out.println("----------------" + getSMS.getMessage());
                        if(getSMS.getMessage().equals("noPhone")){
                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(getContext(),"用户未注册",Toast.LENGTH_LONG).show();
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
                                    Toast.makeText(getContext(),"信息有误",Toast.LENGTH_LONG).show();
                                    return;
                                }
                            });

                        }

                    }
                }).start();
            }
        });

        final String code = updateCode.getText().toString();
        final String psd = updatePsd.getText().toString();
        final String password = updatePsds.getText().toString();
        update_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                System.out.println("000000000000000000   "+code);
                final String phone = updatePhone.getText().toString();
                if(updatePhone.length() == 0 || updatePhone == null){
                    Toast.makeText(UpdatePsdActivity.this,"请输入相关信息",Toast.LENGTH_LONG).show();
                    return;
                }
                if(updatePsd.length() == 0 || updatePsd == null){
                    Toast.makeText(UpdatePsdActivity.this,"请输入相关信息",Toast.LENGTH_LONG).show();
                    return;
                }
                if(updatePsds.length() == 0 || updatePsds == null){
                    Toast.makeText(UpdatePsdActivity.this,"请输入相关信息",Toast.LENGTH_LONG).show();
                    return;
                }
                if(updateCode.length() == 0 || updateCode == null){
                    Toast.makeText(UpdatePsdActivity.this,"请输入相关信息",Toast.LENGTH_LONG).show();
                    return;
                }
                if(!isNetworkAvalible(NewsApplication.getContext())){
                    Toast.makeText(getContext(),"网络异常",Toast.LENGTH_LONG).show();
                    return;
                }
                /*if(!password.equals(psd)){
                    Toast.makeText(UpdatePsdActivity.this,"密码输入不一致",Toast.LENGTH_LONG).show();
                    return;
                }*/


                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        final String code = updateCode.getText().toString();
                        final String password = updatePsds.getText().toString();
                        Key key = new Key();
                        HashMap<String,Object> map = new HashMap<>();
                        map.put("phone",phone);
                        map.put("password",password);
                        map.put("code",code);
                        key.setMap(map);
                        final GetSMS getSMS = HttpUtil.send("http://47.106.112.159:8080/android/changePsd/updatePsd",key);
                        System.out.println("----------------" + getSMS.getMessage());
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
                                    Toast.makeText(getContext(),"密码修改成功",Toast.LENGTH_LONG).show();
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
                                    Intent intent = new Intent(UpdatePsdActivity.this,CodeLoginActivity.class);
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
            updateGetCode.setBackgroundColor(Color.parseColor("#B6B6D8"));
            updateGetCode.setClickable(false);
            updateGetCode.setText("("+millisUntilFinished / 1000 +") 秒后可重新发送");
            updateGetCode.setBackgroundColor(Color.parseColor("#00000000"));
        }

        @Override
        public void onFinish() {
            updateGetCode.setText("重新获取验证码");
            updateGetCode.setClickable(true);

        }
    }
}
