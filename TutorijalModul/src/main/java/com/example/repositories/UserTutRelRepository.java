package com.example.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import com.example.models.Comment;
import com.example.models.UserTutRel;

@RepositoryRestResource(path="usertutrels", collectionResourceRel="usertutrels")
public interface UserTutRelRepository extends CrudRepository<UserTutRel, Long>{

}
