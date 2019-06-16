package news.tencent.charco.android.view.activity;

import com.google.gson.Gson;

import java.io.IOException;

import news.tencent.charco.android.GetSMS;
import news.tencent.charco.android.utils.HttpUtils;
import okhttp3.Call;
import okhttp3.Response;

public class SMSActivity {
    public void getCode(String phone){
        HttpUtils.sendCodeRequest(phone, new okhttp3.Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                //String responseData = response.body().string();
                Gson gson = new Gson();
                GetSMS getSMS = gson.fromJson(response.body().string(),GetSMS.class);
                System.out.println(getSMS.getMessage());
            }
        });
    }

    public void getCodeLogin(String phone,String code){
        HttpUtils.sendCodeLogin(phone,code, new okhttp3.Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Gson gson = new Gson();
                GetSMS getSMS = gson.fromJson(response.body().string(),GetSMS.class);
            }
        });
    }

}
