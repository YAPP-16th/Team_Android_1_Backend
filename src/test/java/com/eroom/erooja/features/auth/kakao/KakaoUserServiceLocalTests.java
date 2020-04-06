package com.eroom.erooja.features.auth.kakao;

import com.eroom.erooja.features.auth.kakao.exception.KakaoRESTException;
import com.eroom.erooja.features.auth.kakao.json.KakaoUserJSON;
import com.eroom.erooja.features.auth.kakao.service.KakaoUserRESTService;
import lombok.RequiredArgsConstructor;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.*;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;

@Disabled
@SpringBootTest
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class KakaoUserServiceLocalTests {
    private static final Logger logger = LoggerFactory.getLogger(KakaoUserServiceLocalTests.class);

    private static final Integer USER_POOL_SIZE = 5;
    private Set<KakaoUserJSON> userPool;
    private KakaoUserJSON randomUser;

    private final KakaoUserRESTService userInfoRESTService;

    @BeforeEach
    public void setUp() throws KakaoRESTException {
        if (this.userPool == null) {
            Set<KakaoUserJSON> set = new HashSet<>();
            for (Long aLong : userInfoRESTService
                    .findUserIds(USER_POOL_SIZE, 0, true)
                    .getElements()) {
                KakaoUserJSON userById = userInfoRESTService.findUserById(aLong);
                set.add(userById);
            }
            this.userPool = set;
        }

        int idx = new Random().nextInt(this.userPool.size());
        Iterator<KakaoUserJSON> iterator = this.userPool.iterator();
        do {
            randomUser = iterator.next();
        } while (--idx > 0);
    }

    @Test
    @DisplayName("유저 아이디로부터 필수 정보를 가져올 수 있다.")
    public void essentialPropertiesShouldBeRetrievedByKakaoId() throws KakaoRESTException {
        Long targetId = randomUser.getId();
        String[] EssentialPropertiesName = new String[] {"nickname", "profile_image", "thumbnail_image"};

        KakaoUserJSON user = userInfoRESTService.findUserById(targetId);
        Map<String, String> properties = user.getProperties();

        assertThat(user.getId(), equalTo(targetId));

        for (String propertyName : EssentialPropertiesName) {
            assertThat(properties.containsKey(propertyName), is(true));
        }

        assertThat(properties.get("profile_image"), CoreMatchers.startsWith("http"));
        assertThat(properties.get("thumbnail_image"), CoreMatchers.startsWith("http"));
    }
}
