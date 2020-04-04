package com.eroom.erooja.features.auth.kakao.service;

import com.eroom.erooja.features.auth.kakao.json.KakaoIdsJSON;
import com.eroom.erooja.features.auth.kakao.json.KakaoUserJSON;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@Service
public class KakaoUserRESTService {
    private RestTemplate restTemplate = new RestTemplate();

    @Value("${KAKAO_ADMIN_KEY}")
    private String ADMIN_KEY;

    @Value("${KAKAO_REST_API_KEY}")
    private String REST_API_KEY;

    public KakaoUserJSON findUserById(Long targetId) {
        ResponseEntity<KakaoUserJSON> response = this.restTemplate.postForEntity(
                ServiceConstants.BASE_HOST_KAPI + ServiceConstants.END_POINT_USER_ME, buildHttpEntity(targetId), KakaoUserJSON.class);

        return response.getBody();
    }

    public Long logoutById(Long targetId) {
        ResponseEntity<Long> response = this.restTemplate.postForEntity(
                ServiceConstants.BASE_HOST_KAPI + ServiceConstants.END_POINT_USER_LOGOUT, buildHttpEntity(targetId), Long.class);

        return response.getBody();
    }

    private HttpEntity<MultiValueMap<String, String>> buildHttpEntity(Long targetId) {
        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        formData.add("target_id_type", "user_id");
        formData.add("target_id", String.valueOf(targetId));

        return new HttpEntity<>(formData, buildHeader());
    }

    public KakaoIdsJSON findUserIds(Integer limit, Integer fromId, Boolean isAsc) {

        MultiValueMap<String, Object> formData = new LinkedMultiValueMap<>();
        formData.add("limit", limit);
        formData.add("from_id", fromId);
        formData.add("order", isAsc? "asc" : "desc");

        HttpEntity<MultiValueMap<String, Object>> httpEntity = new HttpEntity<>(formData, buildHeader());

        ResponseEntity<KakaoIdsJSON> response = this.restTemplate.postForEntity(
                ServiceConstants.BASE_HOST_KAPI + ServiceConstants.END_POINT_USER_IDS, httpEntity, KakaoIdsJSON.class);

        return response.getBody();
    }

    public KakaoUserJSON findUserByToken(String accessToken) {
        HttpEntity httpEntity = new HttpEntity(buildHeader(accessToken));

        ResponseEntity<KakaoUserJSON> response = this.restTemplate.postForEntity(
                ServiceConstants.BASE_HOST_KAPI + ServiceConstants.END_POINT_USER_ME, httpEntity, KakaoUserJSON.class);

        return response.getBody();
    }

    private HttpHeaders buildHeader() {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        httpHeaders.add(HttpHeaders.AUTHORIZATION, "KakaoAK " + ADMIN_KEY);

        return httpHeaders;
    }

    private HttpHeaders buildHeader(String accessToken) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        httpHeaders.setBearerAuth(accessToken);

        return httpHeaders;
    }
}
