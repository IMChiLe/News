package news.tencent.charco.android;

public class New {
    /*c
        acount 新闻编号
        videourl 新闻视频路径
        author 新闻作者
        newtype 新闻类型
        title 新闻标题
        dateofpublication 发布日期
        text 新闻内容
        img 图片路径
         */
    private String account;
    private String videourl;
    private String author;
    private int newtype;
    private String title;
    private String dateofpublication;
    private String text;
    private String img;
    public New() {
    }

    public New(String account, String videourl, String author,int newtype,String title,String dateofpublication,String text) {
        this.account = account;
        this.videourl = videourl;
        this.author = author;
        this.newtype = newtype;
        this.title=title;
        this.dateofpublication = dateofpublication;
        this.text=text;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getVideourl() {
        return videourl;
    }

    public void setVideourl(String videourl) {
        this.videourl = videourl;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public int getNewtype() {
        return newtype;
    }

    public void setNewtype(int newtype) {
        this.newtype = newtype;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDateofpublication() {
        return dateofpublication;
    }

    public void setDateofpublication(String dateofpublication) {
        this.dateofpublication = dateofpublication;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }
}
