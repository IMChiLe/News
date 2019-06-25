package news.tencent.charco.android.entity;

/**
 * @CreateDate: 2019/6/23 21:22
 * @ClassName: focus
 * @Author: Lunatic Princess
 * @Descriptions: 第三模块的关注字段的指示器。
 */
public class Focus {

    private String username;
    private String userinfo;
    private String message;
    private String greate;
    private String comment;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUserinfo() {
        return userinfo;
    }

    public void setUserinfo(String userinfo) {
        this.userinfo = userinfo;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getGreate() {
        return greate;
    }

    public void setGreate(String greate) {
        this.greate = greate;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
