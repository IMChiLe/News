package news.tencent.charco.android.utils;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class HttpUtils {

    public static String sendHttpRequest(String address){
        HttpURLConnection connection = null;
        try {
            URL url = new URL(address);
            connection = (HttpURLConnection) url.openConnection();
            connection.setConnectTimeout(8000);
            connection.setReadTimeout(8000);
            connection.setDoInput(true);
            connection.setDoOutput(true);
            InputStream inputStream = connection.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null){
                response.append(line);
            }
            return response.toString();
        }catch (Exception e){
            e.printStackTrace();
            return e.getMessage();
        }finally {
            if(connection != null){
                connection.disconnect();
            }
        }
    }

    public static void sendCodeRequest(String phone,okhttp3.Callback callback){
        OkHttpClient client = new OkHttpClient().newBuilder().connectTimeout(50000, TimeUnit.MILLISECONDS)
                .readTimeout(50000, TimeUnit.MILLISECONDS)
                .build();;
        RequestBody requestBody = new FormBody.Builder()
                .add("phone",phone)
                .build();
        Request request = new Request.Builder()
                .url("http://47.106.112.159:8080/android/login/getSMS")
                .post(requestBody)
                .build();
        client.newCall(request).enqueue(callback);
    }

    public static void sendCodeLogin(String phone,String code,okhttp3.Callback callback){
        OkHttpClient client = new OkHttpClient().newBuilder().connectTimeout(50000, TimeUnit.MILLISECONDS)
                .readTimeout(50000, TimeUnit.MILLISECONDS)
                .build();;
        RequestBody requestBody = new FormBody.Builder()
                .add("phone",phone)
                .add("code",code)
                .build();
        Request request = new Request.Builder()
                .url("http://47.106.112.159:8080/android/login/login")
                .post(requestBody)
                .build();
        client.newCall(request).enqueue(callback);
    }

}
