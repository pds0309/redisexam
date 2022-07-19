package com.pds.redisexam.repository;

import com.pds.redisexam.entity.Person;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.redis.DataRedisTest;
import redis.embedded.RedisServer;

import static org.assertj.core.api.Assertions.assertThat;

@DataRedisTest
public class PersonRepositoryTest {

    @Autowired
    private PersonRepository personRepository;
    private static RedisServer redisServer;

    @BeforeAll
    static void setup() {
        redisServer = RedisServer.builder()
                .port(6378)
                .setting("maxmemory 128M")
                .build();
        try {
            System.out.println("START");
            redisServer.start();
        } catch (Exception e) {

        }
    }

    @Nested
    @DisplayName("Person 삽입 테스트")
    class PersonSave {
        @Test
        @DisplayName("Person 객체를 Value로 하여 정상적으로 삽입된다.")
        void postPersonShouldSuccessSave() {
            Person person = personRepository.save(
                    Person.builder()
                            .name("kim")
                            .age(444)
                            .build());
            assertThat(person)
                    .isNotNull()
                    .isInstanceOf(Person.class);
            assertThat(person.getId()).isNotNull();
        }
    }

    @Nested
    @DisplayName("Person 조회 테스트")
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    class PersonSelection {
        private final Person person = new Person(null, "kim", 25);

        @BeforeAll
        void setup() {
            personRepository.save(person);
            assertThat(person).isNotNull();
            assertThat(person.getId()).isNotNull();
        }

        @Test
        @DisplayName("Redis 에 존재하는 key로 Person을 조회하여 객체로 얻어올 수 있다.")
        void findPersonByIdShouldGetPersonDomainEntity() {
            Person fundPerson = personRepository.findById(person.getId())
                    .orElse(null);
            assertThat(fundPerson).isNotNull()
                    .isInstanceOf(Person.class)
                    .usingRecursiveComparison()
                    .isEqualTo(person);
        }

        @Test
        @DisplayName("Redis 에 존재하지 않는 key로 Person을 조회해 예외 발생한다.")
        void findPersonByIdNotExistsShouldThrowsException() {
            assertThat(personRepository.findById("hello")).isNotPresent();
        }
    }

    @AfterAll
    static void destroy() {
        if (redisServer != null) {
            redisServer.stop();
            System.out.println("STOP");
        }
    }

}
