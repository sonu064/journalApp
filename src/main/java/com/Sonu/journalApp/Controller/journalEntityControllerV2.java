package com.Sonu.journalApp.Controller;

import com.Sonu.journalApp.Entity.JournalEntity;
import com.Sonu.journalApp.Entity.User;
import com.Sonu.journalApp.service.JournalEntryService;
import com.Sonu.journalApp.service.UserService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/journal")
public class journalEntityControllerV2 {

    @Autowired
    private JournalEntryService journalEntryService;

    @Autowired
    private UserService userService;
//       private Map<Long , journalEntity> journalEntries = new HashMap<>();

       @GetMapping
        public ResponseEntity<?> getAllJournalEntriesOfUser()
       {
           Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
           String userName = authentication.getName();
           User user = userService.findByUserName(userName);
           List <JournalEntity> all = user.getJournalEntries();
           if(!all.isEmpty() && all != null)
           {
               return new ResponseEntity<>(all , HttpStatusCode.valueOf(200));
           }
               return  new ResponseEntity<>(HttpStatusCode.valueOf(404));


       }

       @PostMapping
        public ResponseEntity<JournalEntity> CreateEntity (@RequestBody JournalEntity myEntry)
       {
           Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
           String userName = authentication.getName();
           try {
               journalEntryService.saveEntry(myEntry,userName);
               return new ResponseEntity<>(myEntry, HttpStatusCode.valueOf(200));
           }catch (Exception e)
           {
               return new ResponseEntity<>(HttpStatusCode.valueOf(400));
           }


       }

       @GetMapping("id/{myId}")
       public ResponseEntity<Optional<JournalEntity>> GetJournalEntityId (@PathVariable  ObjectId myId )
       {
           Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
           String userName = authentication.getName();
           User user = userService.findByUserName(userName);
           List<JournalEntity> collect  = user.getJournalEntries().stream().filter(x ->x.getId().equals(myId)).collect(Collectors.toList());
           if(!collect.isEmpty())
           {
               Optional<JournalEntity> journalEntity = journalEntryService.findById(myId);
               if(journalEntity.isPresent()) {
                   return new ResponseEntity<>(journalEntryService.findById(myId), HttpStatusCode.valueOf(200));
               }
           }
            return new ResponseEntity<>(HttpStatusCode.valueOf(404));
       }


    @DeleteMapping("id/{myId}")
    public ResponseEntity<?> DeleteJournalEntityId (@PathVariable ObjectId myId )
    {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userName = authentication.getName();
        boolean removed = journalEntryService.deleteById(myId,userName);
        if(removed) {
            return new ResponseEntity<>(HttpStatusCode.valueOf(204));
        }else
        {
            return new ResponseEntity<>(HttpStatusCode.valueOf(404));
        }
    }

    @PutMapping("id/{myId}")

    public ResponseEntity<JournalEntity> EditJournalEntityid (@PathVariable ObjectId myId , @RequestBody JournalEntity newEntry)
    {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userName = authentication.getName();
        User user = userService.findByUserName(userName);
        List<JournalEntity> collect  = user.getJournalEntries().stream().filter(x ->x.getId().equals(myId)).collect(Collectors.toList());

        if(!collect.isEmpty())
        {
            Optional<JournalEntity> journalEntity = journalEntryService.findById(myId);
            if (journalEntity.isPresent())
            {
                JournalEntity old = journalEntity.get();
                old.setTitle(newEntry.getTitle() != null && !newEntry.getTitle().equals("")? newEntry.getTitle() : old.getTitle());
                old.setContent(newEntry.getContent() != null && !newEntry.getContent().equals("")? newEntry.getContent() : old.getContent());
                journalEntryService.saveEntry(old);
                return new ResponseEntity<>(old,HttpStatusCode.valueOf(200));
            }
        }

        return new ResponseEntity<>(HttpStatusCode.valueOf(404));
    }

}

