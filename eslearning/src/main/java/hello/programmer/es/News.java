/**
 * @title News
 * @package hello.programmer.es
 * @description: 简理财
 * @copyright: Copyright (c) 2017
 * @company:北京简便快乐信息技术有限公司
 * @author liwei
 * @date 2018/2/28 15:33
 */
package hello.programmer.es;

import java.util.List;

/**
 * (类描述：)
 * @author liwei
 * @create 2018/2/28 15:33
 */
public class News {

    private String title;
    private String tag;
    private String publishTime;
    private List<String> tags;


    public News() {
        super();
    }
    public News(String title, String tag, String publishTime) {
        super();
        this.title = title;
        this.tag = tag;
        this.publishTime = publishTime;
    }
    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public String getTag() {
        return tag;
    }
    public void setTag(String tag) {
        this.tag = tag;
    }
    public String getPublishTime() {
        return publishTime;
    }
    public void setPublishTime(String publishTime) {
        this.publishTime = publishTime;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }
}