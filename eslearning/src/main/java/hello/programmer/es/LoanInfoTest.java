/**
 * @title LoanInfoTest
 * @package hello.programmer.es
 * @description: 简理财
 * @copyright: Copyright (c) 2017
 * @company:北京简便快乐信息技术有限公司
 * @author liwei
 * @date 2018/3/12 15:35
 */
package hello.programmer.es;

import org.apache.http.HttpEntity;
import org.apache.http.entity.ContentType;
import org.apache.http.nio.entity.NStringEntity;
import org.elasticsearch.action.admin.indices.create.CreateIndexRequest;
import org.elasticsearch.action.admin.indices.create.CreateIndexResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.Response;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.json.JsonXContent;
import org.elasticsearch.index.query.*;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.junit.Test;

import java.io.IOException;
import java.util.Collections;

/**
 * (类描述：)
 * @author liwei
 * @create 2018/3/12 15:35
 */
public class LoanInfoTest {
    @Test
    public void queryTest(){
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
        sourceBuilder.from(0);
        sourceBuilder.size(10);
        sourceBuilder.fetchSource(new String[]{"repayDay"}, new String[]{});//返回哪些字段
        MatchQueryBuilder matchQueryBuilder = QueryBuilders.matchQuery("projectName", "*JC优房宝010106-071659128418*");
        TermQueryBuilder termQueryBuilder = QueryBuilders.termQuery("projectName", "*优房宝*");
        TermQueryBuilder termQueryBuilder2 = QueryBuilders.termQuery("companyName", "北");
        TermQueryBuilder termQueryBuilder3 = QueryBuilders.termQuery("repayDay", "19");
        RangeQueryBuilder rangeQueryBuilder = QueryBuilders.rangeQuery("publishTime");
        WildcardQueryBuilder wildcardQueryBuilder = QueryBuilders.wildcardQuery("projectName","*优房*");
        rangeQueryBuilder.gte("2018-01-26T08:00:00Z");
        rangeQueryBuilder.lte("2018-01-26T20:00:00Z");
        BoolQueryBuilder boolBuilder = QueryBuilders.boolQuery();
        //boolBuilder.must(matchQueryBuilder);
        boolBuilder.must(termQueryBuilder);
        //boolBuilder.must(termQueryBuilder3);
        //boolBuilder.must(rangeQueryBuilder);
        //boolBuilder.must(wildcardQueryBuilder);
        sourceBuilder.query(boolBuilder);
        SearchRequest searchRequest = new SearchRequest("loan_info");
        searchRequest.types("loan_info");
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
    public void ngram() throws Exception{
        CreateIndexRequest request = new CreateIndexRequest("demo4");
        request.settings(Settings.builder()
                                .put("analysis.analyzer.ngram.tokenizer","my_tokenizer")
                .put("analysis.tokenizer.my_tokenizer.type","ngram").build());
        XContentBuilder builder = JsonXContent.contentBuilder()
                .startObject()
                .startObject("mappings")
                .startObject("demo4")
                .startObject("properties")
                .startObject("title")
                .field("type","text")
                .field("analyzer","ik_max_word")
                .endObject()
                .startObject("tag")
                .field("type","text")
                //.field("index","true")
                .field("analyzer","ngram")
                .endObject()
                .startObject("content")
                .field("type","text")
                .field("index","true")
                .field("analyzer","ik_max_word")
                .endObject()
//                                    .startObject("uniqueId")
//                                        .field("type","keyword")
//                                        .field("index","not_analyzed")
//                                    .endObject()
                .startObject("publishTime")
                .field("type","date")
                .field("format","strict_date_optional_time||epoch_millis")
                .endObject()
                .endObject()
                .endObject()
                .endObject()
                .startObject("settings")
                .field("number_of_shards",3)
                .field("number_of_replicas",1)
                .endObject()
                .endObject();
        request.source(builder);
        CreateIndexResponse createIndexResponse = ESClientFactory.getHighLevelClient().indices().create(request);
        System.out.println(createIndexResponse);
    }



    @Test
    public void indexTest() {
        try {
            // 借助indexRequest的json拼接工具
            IndexRequest indexRequest = new IndexRequest();

            XContentBuilder builder = JsonXContent.contentBuilder()
                    .startObject()
                        .startObject("mappings")
                            .startObject("demo4")
                                .startObject("properties")
                                    .startObject("title")
                                        .field("type","text")
                                        .field("analyzer","ik_max_word")
                                    .endObject()
                                    .startObject("tag")
                                        .field("type","text")
                                        //.field("index","true")
                                        .field("indexAnalyzer","ngram")
                                    .endObject()
                                    .startObject("content")
                                        .field("type","text")
                                        .field("index","true")
                                        .field("analyzer","ik_max_word")
                                    .endObject()
//                                    .startObject("uniqueId")
//                                        .field("type","keyword")
//                                        .field("index","not_analyzed")
//                                    .endObject()
                                    .startObject("publishTime")
                                        .field("type","date")
                                        .field("format","strict_date_optional_time||epoch_millis")
                                    .endObject()
                                .endObject()
                            .endObject()
                        .endObject()
                        .startObject("settings")
                            .field("number_of_shards",3)
                            .field("number_of_replicas",1)
                        .endObject()
                    .endObject();
            indexRequest.source(builder);
            // 生成json字符串
            String source = indexRequest.source().utf8ToString();
            HttpEntity entity = new NStringEntity(source, ContentType.APPLICATION_JSON);
            // 使用RestClient进行操作 而非rhlClient
            Response response = ESClientFactory.getClient().performRequest("put", "/demo4", Collections.<String, String> emptyMap(),
                    entity);
            System.out.println(response);
        } catch (IOException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }

    }

}