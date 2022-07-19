package com.pds.redisexam.controller;

import com.pds.redisexam.PersonValueDto;
import com.pds.redisexam.service.PersonService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class PersonController {

    private final PersonService personService;

    @GetMapping("/api/person/{id}")
    public ResponseEntity<PersonValueDto> getPerson(@PathVariable String id) {
        return ResponseEntity.ok(personService.getPersonById(id));
    }

    @PostMapping("/api/person")
    public ResponseEntity<String> postPerson(@RequestBody PersonValueDto personValueDto) {
        return ResponseEntity.ok(personService.createPerson(personValueDto));
    }

}
