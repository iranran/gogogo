/**
 * @title FreeClientTest
 * @package hello.programmer.es
 * @description: 简理财
 * @copyright: Copyright (c) 2017
 * @company:北京简便快乐信息技术有限公司
 * @author liwei
 * @date 2018/2/28 15:33
 */
package hello.programmer.es;

import com.alibaba.fastjson.JSON;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.*;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * (类描述：)
 * @author liwei
 * @create 2018/2/28 15:33
 */
@RunWith(SpringJUnit4ClassRunner.class)

@ContextConfiguration("classpath:SpringContext.xml")
public class FreeClientTest {

    private String index;

    private String type;


    @Before
    public void prepare() {
        index = "my_index";
        type = "demo2";
    }

    @Test
    public void addTest() {
        IndexRequest indexRequest = new IndexRequest(index, type);
        News news = new News();
        news.setTitle("中国产小型无人机的“对手”来了，俄微型拦截导弹便宜量又多");
        news.setTag("军事");
        news.setPublishTime("2018-03-01T23:59:30Z");
        String source = JSON.toJSONString(news);
        indexRequest.source(source, XContentType.JSON);
        try {
            ESClientFactory.getHighLevelClient().index(indexRequest);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    @Test
    public void addListTest() {
        IndexRequest indexRequest = new IndexRequest(index, type);
        News news = new News();
        news.setTitle("纳达尔来了，你准备好了吗？");
        news.setTag("房宝010106-071659128418");

        List<String> tags = new ArrayList<>();
        tags.add("体育");
        tags.add("网球");

        news.setTags(tags);

        news.setPublishTime("2018-03-01T23:59:30Z");
        String source = JSON.toJSONString(news);
        indexRequest.source(source, XContentType.JSON);

        try {
            ESClientFactory.getHighLevelClient().index(indexRequest);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

//bulk test

    @Test
    public void batchAddTest() {
        BulkRequest bulkRequest = new BulkRequest();
        List<IndexRequest> requests = generateRequests();
        for (IndexRequest indexRequest : requests) {
            bulkRequest.add(indexRequest);
        }
        try {
            ESClientFactory.getHighLevelClient().bulk(bulkRequest);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public List<IndexRequest> generateRequests(){
        List<IndexRequest> requests = new ArrayList<>();
        requests.add(generateNewsRequest("中印边防军于拉达克举行会晤 强调维护边境和平", "军事", "2018-01-27T08:34:00Z"));
        requests.add(generateNewsRequest("费德勒收郑泫退赛礼 进决赛战西里奇", "sports", "2018-01-26T14:34:00Z"));
        requests.add(generateNewsRequest("欧文否认拿动手术威胁骑士 兴奋全明星联手詹皇", "sports", "2018-01-26T08:34:00Z"));
        requests.add(generateNewsRequest("皇马官方通告拉莫斯伊斯科伤情 将缺阵西甲关键战", "sports", "2018-01-26T20:34:00Z"));
        return requests;
    }

    public IndexRequest generateNewsRequest(String title, String tag, String publishTime){
        IndexRequest indexRequest = new IndexRequest(index, type);
        News news = new News();
        news.setTitle(title);
        news.setTag(tag);
        news.setPublishTime(publishTime);
        String source = JSON.toJSONString(news);
        indexRequest.source(source, XContentType.JSON);
        return indexRequest;
    }


    //2018年1月26日早八点到晚八点关于费德勒的前十条体育新闻的标题
    @Test
    public void queryTest(){
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
        sourceBuilder.from(0);
        sourceBuilder.size(10);
        sourceBuilder.fetchSource(new String[]{"title","tag"}, new String[]{});
        MatchQueryBuilder matchQueryBuilder = QueryBuilders.matchQuery("tag", "JC优房宝010106");
        TermQueryBuilder termQueryBuilder = QueryBuilders.termQuery("tag", "优房宝");
        MatchPhraseQueryBuilder matchPhraseQueryBuilder = QueryBuilders.matchPhraseQuery("tag", "JC优房宝");
        RangeQueryBuilder rangeQueryBuilder = QueryBuilders.rangeQuery("publishTime");
        rangeQueryBuilder.gte("2018-01-26T08:00:00Z");
        rangeQueryBuilder.lte("2018-01-26T20:00:00Z");
        BoolQueryBuilder boolBuilder = QueryBuilders.boolQuery();
        //boolBuilder.must(matchQueryBuilder);
        //boolBuilder.must(matchPhraseQueryBuilder);
        boolBuilder.must(termQueryBuilder);
        //boolBuilder.must(rangeQueryBuilder);
        sourceBuilder.query(boolBuilder);
        SearchRequest searchRequest = new SearchRequest(index);
        searchRequest.types(type);
        searchRequest.source(sourceBuilder);
        try {
            SearchResponse response = ESClientFactory.getHighLevelClient().search(searchRequest);
            System.out.println(response);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }


    @Test
    public void updateTest(){
        UpdateRequest updateRequest = new UpdateRequest(index, type, "Jphk22EB0dKC4dHRZe9L");
        Map<String, String> map = new HashMap<>();
        map.put("tag", "网球");
        updateRequest.doc(map);
        try {
            ESClientFactory.getHighLevelClient().update(updateRequest);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }



}