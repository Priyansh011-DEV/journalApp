package net.PORC.journalApp.Controller;


import net.PORC.journalApp.Repository.JournalEntryRepo;
import net.PORC.journalApp.entity.JournalEntry;
import net.PORC.journalApp.entity.User;
import net.PORC.journalApp.service.JournalEntryService;
import net.PORC.journalApp.service.UserService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.List;

@RestController
@RequestMapping("/JournalV2")
public class controller_V2 {

    @Autowired
    JournalEntryService journalEntryService;
    @Autowired
    private JournalEntryRepo journalEntryRepo;
    @Autowired
    UserService userService;

    @GetMapping("/{username}")
    public ResponseEntity<?> getAlljournalentriesOfUser(@PathVariable String username) {
        User user = userService.FindByUsername(username);
        List<JournalEntry> ALL = user.getJournalEntries();
        if (ALL != null && !ALL.isEmpty()) {
            return ResponseEntity.ok(ALL);
        }
        return ResponseEntity.notFound().build();

    }

    @PostMapping("/{username}")
    public ResponseEntity<JournalEntry> CreateEntry(@RequestBody JournalEntry MyEntry, @PathVariable String username) {
        try {
            journalEntryService.SaveEntry(MyEntry, username);
            return new ResponseEntity<>(MyEntry, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/getByEntryID/{username}/{id}")
    public ResponseEntity<JournalEntry> GetUserEntry(@PathVariable String id, @PathVariable String username) {
        if (!ObjectId.isValid(id)) {
            return ResponseEntity.badRequest().build();
        }
        ObjectId objID = new ObjectId(id);
        return journalEntryService.FindByUserID(username, objID).map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/DeleteByID/{username}/{id}")
    public ResponseEntity<?> DeleteById(@PathVariable String id, @PathVariable String username) {
        if (!ObjectId.isValid(id)) {
            return ResponseEntity.badRequest().build();
        }

        boolean deleted = journalEntryService.DeleteEntryByID(new ObjectId(id), username);

        return deleted
                ? ResponseEntity.noContent().build()
                : ResponseEntity.notFound().build();
    }
    // @PutMapping("/updatejournal/{username}/{id}")
    //  public ResponseEntity<?> UpdateJournalEntries(@PathVariable ObjectId id, @RequestBody JournalEntry NewEntry, @PathVariable String username){
    //     JournalEntry OldEntry = journalEntryService.Find_byID(id).orElse(null);
    //  if(OldEntry != null){
    //     OldEntry.setTitle(NewEntry.getTitle() != null && !NewEntry.getTitle().equals(" ") ? NewEntry.getTitle() : OldEntry.getTitle());
    //     OldEntry.setContent(NewEntry.getContent() != null && !NewEntry.getContent().equals(" ") ? NewEntry.getContent() : OldEntry.getContent());
    //    journalEntryService.SaveEntry(OldEntry);
    //   return ResponseEntity.ok().body(OldEntry);
    //   }
    //  return ResponseEntity.notFound().build();
    //}


    @PutMapping("/updatejournal/{username}/{id}")
    public ResponseEntity<?> UpdateJournalByID(
            @PathVariable String id,
            @PathVariable String username,
            @RequestBody JournalEntry newEntry) {

        if (!ObjectId.isValid(id)) {
            return ResponseEntity.badRequest().build();
        }

        ObjectId objId = new ObjectId(id);
        return journalEntryService.FindByUserID(username , objId).map(oldEntry ->{
            if (newEntry.getTitle() != null && !newEntry.getTitle().isBlank()) {
                oldEntry.setTitle(newEntry.getTitle());
            }
            if (newEntry.getContent() != null && !newEntry.getContent().isBlank()) {
                oldEntry.setContent(newEntry.getContent());
            }
            journalEntryService.SaveEntry(oldEntry);
            return ResponseEntity.ok(oldEntry);
        }).orElseGet(() -> ResponseEntity.notFound().build());


    }
}



