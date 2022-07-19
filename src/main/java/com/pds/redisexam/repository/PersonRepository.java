package com.pds.redisexam.repository;

import com.pds.redisexam.entity.Person;
import org.springframework.data.repository.CrudRepository;

public interface PersonRepository extends CrudRepository<Person, String> {

}