/**
 * @title EsTest
 * @package hello.programmer.es
 * @description: 简理财
 * @copyright: Copyright (c) 2017
 * @company:北京简便快乐信息技术有限公司
 * @author liwei
 * @date 2018/2/5 14:39
 */
package hello.programmer.es;

import org.apache.http.HttpHost;
import org.elasticsearch.Build;
import org.elasticsearch.Version;
import org.elasticsearch.action.DocWriteResponse;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.main.MainResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.support.replication.ReplicationResponse;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.cluster.ClusterName;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.sort.FieldSortBuilder;
import org.elasticsearch.search.sort.SortOrder;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * (类描述：)
 * @author liwei
 * @create 2018/2/5 14:39
 */
public class EsTest {
    private static RestHighLevelClient client = new RestHighLevelClient(
            RestClient.builder(
                    new HttpHost("10.103.27.63", 9200, "http")));

    public static void main(String[] args) throws Exception{

//        RestHighLevelClient client = new RestHighLevelClient(
//                RestClient.builder(
//                        new HttpHost("localhost", 9200, "http"),
//                        new HttpHost("localhost", 9201, "http")));

        RestHighLevelClient client2 = new RestHighLevelClient(
                RestClient.builder(
                        new HttpHost("10.103.27.63", 9200, "http")));

        MainResponse response = client2.info();
        ClusterName clusterName = response.getClusterName();
        String clusterUuid = response.getClusterUuid();
        String nodeName = response.getNodeName();
        Version version = response.getVersion();
        Build build = response.getBuild();


        System.out.println(response.toString()+" "+clusterName);

        //put();
        bulk();
        query();
        search();

        client.close();
    }

    public static void put() throws Exception{
        Map<String, Object> jsonMap = new HashMap<>();
        jsonMap.put("user", "kimchy");
        jsonMap.put("postDate", new Date());
        jsonMap.put("message", "trying out Elasticsearch");
        IndexRequest indexRequest = new IndexRequest("posts", "doc")
                .source(jsonMap);

        IndexResponse indexResponse = client.index(indexRequest);
        String index = indexResponse.getIndex();
        String type = indexResponse.getType();
        String id = indexResponse.getId();
        long version = indexResponse.getVersion();

    }

    public static void bulk() throws Exception{
        BulkRequest request = new BulkRequest();
        request.add(new IndexRequest("posts", "doc", "1")
                .source(XContentType.JSON,"field", "foo")
                .source(XContentType.JSON,"field2", "foo2")
                .source(XContentType.JSON,"field3", "foo3")
        );
        request.add(new IndexRequest("posts", "doc2", "2")
                .source(XContentType.JSON,"field", "bar"));
        request.add(new IndexRequest("posts", "doc", "3")
                .source(XContentType.JSON,"field", "baz"));


        Map<String, Object> jsonMap = new HashMap<>();
        jsonMap.put("user", "kimchy");
        jsonMap.put("postDate", new Date());
        jsonMap.put("message", "trying out Elasticsearch");

        request.add(new IndexRequest("posts", "doc", "4")
                .source(jsonMap));

        BulkResponse bulkResponse = client.bulk(request);


    }

    public static void query() throws Exception{
        GetRequest getRequest = new GetRequest(
                "posts",
                "doc",
                "4");

        GetResponse getResponse = client.get(getRequest);
        String index = getResponse.getIndex();
        String type = getResponse.getType();
        String id = getResponse.getId();
        if (getResponse.isExists()) {
            long version = getResponse.getVersion();
            String sourceAsString = getResponse.getSourceAsString();
            System.out.println(sourceAsString);
            Map<String, Object> sourceAsMap = getResponse.getSourceAsMap();
            byte[] sourceAsBytes = getResponse.getSourceAsBytes();

            System.out.println(sourceAsMap.get("postDate"));
        } else {

        }
    }

    public static void search() throws Exception{
        SearchRequest searchRequest = new SearchRequest("posts");
        searchRequest.types("doc");

        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
        sourceBuilder.query(QueryBuilders.matchAllQuery());
        sourceBuilder.sort(new FieldSortBuilder("_id").order(SortOrder.ASC));

        searchRequest.source(sourceBuilder);

//        sourceBuilder.query(QueryBuilders.termQuery("user", "kimchy"));
//        sourceBuilder.from(0);
//        sourceBuilder.size(5);
//        sourceBuilder.timeout(new TimeValue(60, TimeUnit.SECONDS));


        SearchResponse searchResponse = client.search(searchRequest);

        SearchHits hits = searchResponse.getHits();

        SearchHit[] searchHits = hits.getHits();
        for (SearchHit hit : searchHits) {
           System.out.println("search->"+hit.getSourceAsMap());
        }
    }
}