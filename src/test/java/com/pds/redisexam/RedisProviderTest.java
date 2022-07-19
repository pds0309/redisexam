package com.pds.redisexam;

import com.pds.redisexam.service.RedisProvider;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.Duration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.*;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class RedisProviderTest {

    @Autowired
    private RedisProvider redisProvider;

    private final String objectKey = "key";
    private final SampleObj objectValue = new SampleObj("choiburngae", 25);

    @BeforeAll
    void setup() {
        redisProvider.setDataWithExpiration(objectKey, objectValue, 60000);
    }

    @Test
    @DisplayName("없는 객체 데이터에 대한 조회 요청에 대해 null 을 반환한다.")
    void findObjectValueByKeyAndTypeNotExistsShouldReturnNull() {
        SampleObj result = redisProvider.getData("hello", SampleObj.class);
        assertThat(result).isNull();
    }

    @Test
    @DisplayName("저장된 객체 key-value 데이터를 조회할 수 있다.")
    void findObjectValueByKeyAndTypeShouldReturnObjectResult() {
        SampleObj result = redisProvider.getData(objectKey, SampleObj.class);
        assertThat(result)
                .isNotNull()
                .usingRecursiveComparison()
                .isEqualTo(objectValue);
    }

    @Test
    @DisplayName("객체 key로 조회 시 부적절한 객체 타입일 경우 null을 반환한다")
    void findObjectValueByKeyAndWrongTypeShouldReturnNull() {
        Object result = redisProvider.getData(objectKey, SampleWrongObj.class);
        assertThat(result).isNull();
    }

    @AfterAll
    void destroy() {
        redisProvider.deleteData(objectKey);
    }

    static class SampleObj {

        private final String name;
        private final int age;

        public SampleObj(String name, int age) {
            this.name = name;
            this.age = age;
        }

        public String getName() {
            return name;
        }

        public int getAge() {
            return age;
        }

    }

    static class SampleWrongObj {

        private final String name;

        public SampleWrongObj(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }

    }

}
