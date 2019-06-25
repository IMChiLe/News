package news.tencent.charco.android.entity;

/**
 * @CreateDate: 2019/6/23 21:25
 * @ClassName: Recommend
 * @Author: Lunatic Princess
 * @Descriptions: $description
 */
public class Recommend {

    private String id;
    private String url;

    public Recommend() {
    }

    public Recommend(String id, String url) {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
