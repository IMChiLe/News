package news.tencent.charco.android.view.fragment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Looper;
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
import news.tencent.charco.android.R;
import news.tencent.charco.android.User;
import news.tencent.charco.android.utils.HttpUtil;
import news.tencent.charco.android.view.activity.MainActivity;

import static news.tencent.charco.android.NewsApplication.getContext;

public class PsdLoginActivity extends AppCompatActivity {

    private EditText psdName;
    private EditText psdPassword;
    private Button login;
    private Handler handler=new Handler();

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

        psdName = findViewById(R.id.psdName);
        psdPassword = findViewById(R.id.psdPassword);
        login = findViewById(R.id.login);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    final String name = psdName.getText().toString();
                    final String password = psdPassword.getText().toString();
                    System.out.println("-----------------------"+name+password);
                    if(psdName.length() == 0 || psdName == null || psdPassword.length() == 0 || psdPassword == null){
                        Toast.makeText(getContext(),"请将信息填写完整",Toast.LENGTH_LONG).show();
                        return;
                    }
                    if(psdName.length() != 11){
                        Toast.makeText(getContext(),"手机号码长度有误",Toast.LENGTH_LONG).show();
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
                            final GetSMS getSMS = HttpUtil.send("http://192.168.31.198:8080/login/psdLogin",key);
                            System.out.println("--------------"+getSMS.getMessage());
                            if(getSMS.getMessage().equals("noPhone")){
                                handler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(PsdLoginActivity.this,"账号不存在",Toast.LENGTH_LONG).show();
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
                    Toast.makeText(PsdLoginActivity.this,"未知错误",Toast.LENGTH_LONG).show();
                    return;
                }
            }
        });
    }
}
