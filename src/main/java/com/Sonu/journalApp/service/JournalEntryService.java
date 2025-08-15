package com.Sonu.journalApp.service;

import com.Sonu.journalApp.Entity.JournalEntity;
import com.Sonu.journalApp.Entity.User;
import com.Sonu.journalApp.repository.JournalEntryRepository;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.slf4j.spi.LoggingEventBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Component
@Slf4j
public class JournalEntryService {

    @Autowired
    private  UserService userService;
    @Autowired
    private JournalEntryRepository journalEntryRepository;


    @Transactional
    public void saveEntry(JournalEntity journalEntity , String userName)
    {
        try {
            User user = userService.findByUserName(userName);
            journalEntity.setDate(LocalDateTime.now());
            JournalEntity saved = journalEntryRepository.save(journalEntity);
            user.getJournalEntries().add(saved);
            userService.saveEntry(user);
        }catch (Exception e)
        {
            System.out.println(e);
            throw new RuntimeException("Error occur",e);
        }

    }

    public void saveEntry(JournalEntity journalEntity)
    {
        journalEntryRepository.save(journalEntity);
    }

    public List<JournalEntity> getAll()
    {
        return journalEntryRepository.findAll();
    }

    public Optional<JournalEntity> findById(ObjectId id)
    {
        return journalEntryRepository.findById(id);
    }

    @Transactional
    public boolean deleteById(ObjectId id,String userName)
    {
        boolean removed = false;
        try {
            User user = userService.findByUserName(userName);
            removed = user.getJournalEntries().removeIf(x->x.getId().equals(id));

            if(removed)
            {
                userService.saveEntry(user);
                journalEntryRepository.deleteById(id);
            }
        }catch (Exception e )
        {
            log.warn("Worning " ,e);
            throw new RuntimeException("An error occurred while deleting the entry.", e);
        }
        return removed;
    }
}






//CONTROLLER ----> SERVICE ---->REPOSITORY