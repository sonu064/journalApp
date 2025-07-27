package com.Sonu.journalApp.repository;

import com.Sonu.journalApp.Entity.User;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserRepository extends MongoRepository<User, ObjectId>
{
    User findByUserName(String username);

    void deleteByUserName(String name);
}
