/**
 * @title ESClientFactory
 * @package hello.programmer.es
 * @description: 简理财
 * @copyright: Copyright (c) 2017
 * @company:北京简便快乐信息技术有限公司
 * @author liwei
 * @date 2018/2/28 15:46
 */
package hello.programmer.es;

import org.apache.http.HttpHost;
import org.apache.http.impl.nio.client.HttpAsyncClientBuilder;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.client.RestHighLevelClient;

import java.io.IOException;

/**
 * (类描述：)
 * @author liwei
 * @create 2018/2/28 15:46
 */
public class ESClientFactory {
    private static final String HOST = "10.103.27.63";
    private static final int PORT = 9200;
    private static final String SCHEMA = "http";
    private static final int CONNECT_TIME_OUT = 1000;
    private static final int SOCKET_TIME_OUT = 30000;
    private static final int CONNECTION_REQUEST_TIME_OUT = 500;

    private static final int MAX_CONNECT_NUM = 100;
    private static final int MAX_CONNECT_PER_ROUTE = 100;

    private static HttpHost HTTP_HOST = new HttpHost(HOST,PORT,SCHEMA);
    private static boolean uniqueConnectTimeConfig = false;
    private static boolean uniqueConnectNumConfig = true;
    private static RestClientBuilder builder;
    private static RestClient restClient;
    private static RestHighLevelClient restHighLevelClient;

    static {
        init();
    }

    public static void init(){
        builder = RestClient.builder(HTTP_HOST);
        if(uniqueConnectTimeConfig){
            //setConnectTimeOutConfig();
        }
        if(uniqueConnectNumConfig){
            setMutiConnectConfig();
        }
        restClient = builder.build();
        restHighLevelClient = new RestHighLevelClient(builder);
    }

    // 主要关于异步httpclient的连接延时配置
//    public static void setConnectTimeOutConfig(){
//        builder.setRequestConfigCallback(new RequestConfigCallback() {
//
//            @Override
//            public Builder customizeRequestConfig(Builder requestConfigBuilder) {
//                requestConfigBuilder.setConnectTimeout(CONNECT_TIME_OUT);
//                requestConfigBuilder.setSocketTimeout(SOCKET_TIME_OUT);
//                requestConfigBuilder.setConnectionRequestTimeout(CONNECTION_REQUEST_TIME_OUT);
//                return requestConfigBuilder;
//            }
//        });
//    }
    // 主要关于异步httpclient的连接数配置
    public static void setMutiConnectConfig(){
        builder.setHttpClientConfigCallback(new RestClientBuilder.HttpClientConfigCallback() {

            @Override
            public HttpAsyncClientBuilder customizeHttpClient(HttpAsyncClientBuilder httpClientBuilder) {
                httpClientBuilder.setMaxConnTotal(MAX_CONNECT_NUM);
                httpClientBuilder.setMaxConnPerRoute(MAX_CONNECT_PER_ROUTE);
                return httpClientBuilder;
            }
        });
    }

    public static RestClient getClient(){
        return restClient;
    }

    public static RestHighLevelClient getHighLevelClient(){
        return restHighLevelClient;
    }

    public static void close() {
        if (restClient != null) {
            try {
                restClient.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}