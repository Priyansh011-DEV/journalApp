package net.PORC.journalApp.Controller;


import jakarta.validation.Valid;
import net.PORC.journalApp.Repository.JournalEntryRepo;
import net.PORC.journalApp.entity.JournalEntry;
import net.PORC.journalApp.entity.User;
import net.PORC.journalApp.service.JournalEntryService;
import net.PORC.journalApp.service.UserService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/journal")
public class Journalcontroller {

    @Autowired
    JournalEntryService journalEntryService;
    @Autowired
    private JournalEntryRepo journalEntryRepo;
    @Autowired
    UserService userService;

    @GetMapping()
    public ResponseEntity<?> getAlljournalentriesOfUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        User user = userService.FindByUsername(username);
        List<JournalEntry> ALL = user.getJournalEntries();
        if (ALL != null && !ALL.isEmpty()) {
            return ResponseEntity.ok(ALL);
        }
        return ResponseEntity.notFound().build();

    }

    @PostMapping()
    public ResponseEntity<JournalEntry> CreateEntry(@Valid @RequestBody JournalEntry MyEntry) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        journalEntryService.SaveEntry(MyEntry, username);
        return ResponseEntity.status(HttpStatus.CREATED).body(MyEntry);

    }

    @GetMapping("/getByEntryID/{id}")
    public ResponseEntity<?> GetUserEntry(@PathVariable String id) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        User user = userService.FindByUsername(username);
        List<JournalEntry> all = user.getJournalEntries();

        return ResponseEntity.ok(all != null ? all : Collections.emptyList());
    }

    @DeleteMapping("/DeleteByID/{id}")
    public ResponseEntity<?> DeleteById(@PathVariable String id) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        boolean deleted = journalEntryService.DeleteEntryByID(id, username);

        if (!deleted) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.noContent().build();
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


    @PutMapping("update/{id}")
    public ResponseEntity<?> UpdateJournalByID(
            @PathVariable String id,
            @RequestBody JournalEntry newEntry) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        return journalEntryService.FindByUserID(username, id).map(oldEntry -> {

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



