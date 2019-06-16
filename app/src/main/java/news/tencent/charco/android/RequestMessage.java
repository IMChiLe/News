package news.tencent.charco.android;

import java.util.Map;

/**
 * Created by mnilsy on 19-4-19 上午12:52.
 * 用于接收客户端传来的信息包
 * sessionid 为客户端登录时的sessionid
 * data 为客户端传过来的数据包,以map存放
 */public class RequestMessage {
    private String sessionid;
    private Map<String,Object> data;

    public RequestMessage() {
    }

    public RequestMessage(String sessionid, Map<String, Object> data) {
        this.sessionid = sessionid;
        this.data = data;
    }

    public RequestMessage(Map<String, Object> data) {
        this.data = data;
    }

    public String getSessionid() {
        return sessionid;
    }

    public void setSessionid(String sessionid) {
        this.sessionid = sessionid;
    }

    public Map<String, Object> getData() {
        return data;
    }

    public void setData(Map<String, Object> data) {
        this.data = data;
    }
}

