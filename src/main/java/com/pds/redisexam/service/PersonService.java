package com.pds.redisexam.service;

import com.pds.redisexam.PersonValueDto;
import com.pds.redisexam.entity.Person;
import com.pds.redisexam.repository.PersonRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PersonService {

    private final PersonRepository personRepository;

    public String createPerson(PersonValueDto personDto) {
        Person person = personRepository.save(
                Person.builder()
                        .name(personDto.getName())
                        .age(personDto.getAge())
                        .build());
        return person.getId();
    }

    public PersonValueDto getPersonById(String id) {
        Person person = personRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Not Exists!"));
        return new PersonValueDto(person.getName(), person.getAge());
    }

}
