package com.pds.redisexam.service;

import com.pds.redisexam.PersonValueDto;
import com.pds.redisexam.entity.Person;
import com.pds.redisexam.repository.PersonRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;


@SpringBootTest(classes = PersonService.class)
class PersonServiceTest {

    @MockBean
    private PersonRepository personRepository;

    @Autowired
    private PersonService personService;

    @Nested
    @DisplayName("Person 조회 테스트")
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    class PersonSelection {
        private final Person person = new Person(null, "kim", 25);

        @Test
        @DisplayName("Redis 에 존재하는 key로 Person을 조회하여 객체로 얻어올 수 있다.")
        void findPersonByIdShouldGetPersonDomainEntity() {
            given(personRepository.findById(person.getId()))
                    .willReturn(Optional.of(person));
            PersonValueDto fundPerson = personService.getPersonById(person.getId());
            assertThat(fundPerson).isNotNull()
                    .isInstanceOf(PersonValueDto.class)
                    .usingRecursiveComparison()
                    .isEqualTo(new PersonValueDto(person.getName(), person.getAge()));
        }

        @Test
        @DisplayName("Redis 에 존재하지 않는 key로 Person을 조회해 예외 발생한다.")
        void findPersonByIdNotExistsShouldThrowsException() {
            assertThatThrownBy(() -> personService.getPersonById("hello"))
                    .isInstanceOf(RuntimeException.class);
        }

    }

}
