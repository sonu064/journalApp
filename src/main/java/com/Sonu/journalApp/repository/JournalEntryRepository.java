package com.Sonu.journalApp.repository;

import com.Sonu.journalApp.Entity.JournalEntity;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface JournalEntryRepository extends MongoRepository<JournalEntity, ObjectId>
{

}
