package news.tencent.charco.android.utils;

public interface HttpCallbackListener {
    void onFinish(String response);
    void onError(Exception e);
}
