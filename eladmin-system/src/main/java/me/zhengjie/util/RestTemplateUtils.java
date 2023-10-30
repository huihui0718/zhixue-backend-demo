package me.zhengjie.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import java.net.URI;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import java.net.InetSocketAddress;
import java.net.Proxy;

public class RestTemplateUtils {
    private static final RestTemplate restTemplate = new RestTemplate();
//    static {
//        SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
//        Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress("localhost", 65211));
//        requestFactory.setProxy(proxy);
//        restTemplate.setRequestFactory(requestFactory);
//
//    }
    public static <T> T get(URI url,HttpEntity<?> request, Class<T> responseType) {
        ResponseEntity<T> response = restTemplate.exchange(url, HttpMethod.GET, request, responseType);
        return response.getBody();
    }

    public static <T> T post(String url, Object request, Class<T> responseType) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Object> entity = new HttpEntity<>(request, headers);

        ResponseEntity<T> response = restTemplate.exchange(url, HttpMethod.POST, entity, responseType);
        return response.getBody();
    }
    private static HttpHeaders getRequestHeaders(HttpServletRequest request) {
        HttpHeaders headers = new HttpHeaders();

        // 获取所有请求头的名称
        Enumeration<String> headerNames = request.getHeaderNames();
        if (headerNames != null) {
            // 遍历请求头名称并获取对应的值
            while (headerNames.hasMoreElements()) {
                String headerName = headerNames.nextElement();
                String headerValue = request.getHeader(headerName);
                headers.set(headerName, headerValue);
            }
        }

        return headers;
    }
    public static <T> T post(String url, Map<String, Object> requestBody, Class<T> responseType, HttpServletRequest request) {
        // 设置请求头
//        HttpHeaders headers = getRequestHeaders(request);
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type","application/json");
        headers.set("Cache-Control","no-cache");
        headers.set("Host","43.134.163.43:18000");
        headers.set("Accept-Encoding","gzip, deflate, br");
        // 创建请求实体
        headers.set("Connection","keep-alive");
//        headers.set("User-Agent","PostmanRuntime/7,26");
        HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(requestBody, headers);
        // 发送 POST 请求
        ResponseEntity<T> responseEntity = restTemplate.exchange(
                url,
                HttpMethod.POST,
                requestEntity,
                responseType
        );
        // 获取响应结果
        return responseEntity.getBody();
    }



    public static <T> T put(String url, Object request, Class<T> responseType) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Object> entity = new HttpEntity<>(request, headers);

        ResponseEntity<T> response = restTemplate.exchange(url, HttpMethod.PUT, entity, responseType);
        return response.getBody();
    }

    public static <T> T delete(String url, Class<T> responseType) {
        ResponseEntity<T> response = restTemplate.exchange(url, HttpMethod.DELETE, null, responseType);
        return response.getBody();
    }
}
