package com.yapp.erooja.feature.auth.kakao;

import com.yapp.erooja.feature.auth.kakao.json.KakaoIdsJSON;
import com.yapp.erooja.feature.auth.kakao.json.KakaoUserJSON;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.Map;

@Service
@PropertySource(value = "classpath:third-party-config.properties", ignoreResourceNotFound = true)
public class KakaoUserInfoRESTService {
    private final RestTemplate restTemplate = new RestTemplate();

    @Value("${third-party.kakao.admin-key}")
    private String ADMIN_KEY;

    @Value("third-party.kakao.rest-api-key")
    private String REST_API_KEY;

    private static final String BASE_HOST = "https://kapi.kakao.com";
    private static final String END_POINT_USER_IDS = "/v1/user/ids";
    private static final String END_POINT_USER_ME = "/v2/user/me";
    private static final String END_POINT_USER_UNLINK = "/v1/user/unlink";

    public KakaoIdsJSON getAllIds() {
        ResponseEntity<KakaoIdsJSON> response = this.restTemplate.exchange(BASE_HOST + END_POINT_USER_IDS,
                HttpMethod.GET,
                buildHeaders(Collections.singletonMap(HttpHeaders.AUTHORIZATION, "KakaoAK " + ADMIN_KEY)),
                KakaoIdsJSON.class,
                Collections.emptyMap());

        return response.getBody();
    }

    public KakaoUserJSON getInfo(String accessToken) {
        ResponseEntity<KakaoUserJSON> response = this.restTemplate.exchange(BASE_HOST + END_POINT_USER_ME,
                HttpMethod.POST,
                buildHeaders(Collections.singletonMap(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)),
                KakaoUserJSON.class,
                Collections.emptyMap());

        return response.getBody();
    }


    private HttpEntity buildHeaders(Map<String, String> headers) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(HttpHeaders.CONTENT_TYPE, "application/x-www-form-urlencoded;charset=utf-8");

        for (Map.Entry<String, String> entry: headers.entrySet()) {
            httpHeaders.add(entry.getKey(), entry.getValue());
        }

        return new HttpEntity(httpHeaders);
    }
}
