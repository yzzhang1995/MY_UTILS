package com.zyz;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpHost;
import org.apache.http.util.EntityUtils;
import org.elasticsearch.client.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * 使用Rest低级API操作ES，客户端通过http来连接Elasticsearch集群。用户在
 * 使用该客户端时需要将请求数据手动拼接成Elasticsearch所需JSON格式进行发送，
 * 收到响应时同样也需要将返回的JSON数据手动封装成对象。适用于所有版本。
 * --- 也可以直接使用http发送请求操作ES
 *
 * @author yzzhang
 * @date 2020/9/14 16:41
 */
public class TesRestApi {
    private static final ObjectMapper MAPPER = new ObjectMapper();
    private RestClient restClient;
    private static final String IP = "192.168.25.130";
    private static final int PORT = 9200;

    @Before
    public void init() {
        RestClientBuilder restClientBuilder = RestClient.builder(
                new HttpHost(IP, PORT, "http"));
        restClientBuilder.setFailureListener(new RestClient.FailureListener() {
            @Override
            public void onFailure(Node node) {
                System.out.println("出错了 -> " + node);
            }
        });
        this.restClient = restClientBuilder.build();
    }

    @After
    public void after() throws IOException {
        restClient.close();
    }

    // 查询集群状态
    @Test
    public void testGetInfo() throws IOException {
        Request request = new Request("GET", "/_cluster/state");
        request.addParameter("pretty", "true");
        Response response = this.restClient.performRequest(request);
        System.out.println(response.getStatusLine());
        System.out.println(EntityUtils.toString(response.getEntity()));
    }

    // 新增数据
    @Test
    public void testCreateData() throws IOException {
        Request request = new Request("POST", "/haoke/house");
        Map<String, Object> data = new HashMap<>();
        data.put("id", "2001");
        data.put("title", "张江高科");
        data.put("price", "3500");
        request.setJsonEntity(MAPPER.writeValueAsString(data));
        Response response = this.restClient.performRequest(request);
        System.out.println(response.getStatusLine());
        System.out.println(EntityUtils.toString(response.getEntity()));
    }

    // 根据id查询数据
    @Test
    public void testQueryData() throws IOException {
        Request request = new Request("GET", "/haoke/house/TBvFi3QBNRKYXAr5jey_");
        Response response = this.restClient.performRequest(request);
        System.out.println(response.getStatusLine());
        System.out.println(EntityUtils.toString(response.getEntity()));
    }

    // 搜索数据
    @Test
    public void testSearchData() throws IOException {
        Request request = new Request("POST", "/haoke/house/_search");
        String searchJson = "{\"query\": {\"match\": {\"title\": \"张江高科\"}}}";
        request.setJsonEntity(searchJson);
        request.addParameter("pretty", "true");
        Response response = this.restClient.performRequest(request);
        System.out.println(response.getStatusLine());
        System.out.println(EntityUtils.toString(response.getEntity()));
    }
}