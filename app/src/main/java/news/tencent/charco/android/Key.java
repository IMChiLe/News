package news.tencent.charco.android;

import java.util.HashMap;

public class Key {
    private String sessionid;
    private HashMap<String,Object> map;

    public String getSessionid() {
        return sessionid;
    }

    public void setSessionid(String sessionid) {
        this.sessionid = sessionid;
    }

    public HashMap<String, Object> getMap() {
        return map;
    }

    public void setMap(HashMap<String, Object> map) {
        this.map = map;
    }
}
