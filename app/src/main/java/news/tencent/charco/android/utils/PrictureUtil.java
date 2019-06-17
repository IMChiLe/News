package news.tencent.charco.android.utils;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class PrictureUtil {
    public static InputStream getImageViewInputStream(String Url) throws IOException {
        InputStream inputStream = null;
        URL url = new URL(Url);                    //服务器地址
        if (url != null) {
            //打开连接
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setConnectTimeout(3000);//设置网络连接超时的时间为3秒
            httpURLConnection.setRequestMethod("GET");        //设置请求方法为GET
            httpURLConnection.setDoInput(true);                //打开输入流
            int responseCode = httpURLConnection.getResponseCode();    // 获取服务器响应值
            if (responseCode == HttpURLConnection.HTTP_OK) {        //正常连接
                inputStream = httpURLConnection.getInputStream();        //获取输入流
            }
        }
        return inputStream;
    }
}
