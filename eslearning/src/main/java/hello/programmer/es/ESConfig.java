/**
 * @title ESConfig
 * @package hello.programmer.es
 * @description: 简理财
 * @copyright: Copyright (c) 2017
 * @company:北京简便快乐信息技术有限公司
 * @author liwei
 * @date 2018/2/28 15:30
 */
package hello.programmer.es;

import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

//使用javaConfig方式配置
@Configuration
@ComponentScan(basePackageClasses=ESClientSpringFactory.class)
public class ESConfig {

    @Value("${httpHost.host}")
    private String host;

    @Value("${httpHost.port}")
    private int port;

    @Value("${httpHost.schema}")
    private String schema;

    @Value("${esclient.connectNum}")
    private Integer connectNum;

    @Value("${esclient.connectPerRoute}")
    private Integer connectPerRoute;



    @Bean
    public HttpHost httpHost(){
        return new HttpHost(host,port,schema);
    }

    @Bean(initMethod="init",destroyMethod="close")
    public ESClientSpringFactory getFactory(){
        return ESClientSpringFactory.
                build(httpHost(), connectNum, connectPerRoute);
    }

    @Bean
    @Scope("singleton")
    public RestClient getRestClient(){
        return getFactory().getClient();
    }

    @Bean
    @Scope("singleton")
    public RestHighLevelClient getRHLClient(){
        return getFactory().getRhlClient();
    }
}