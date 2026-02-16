package net.PORC.journalApp.service;

import net.PORC.journalApp.Repository.JournalEntryRepo;
import net.PORC.journalApp.entity.JournalEntry;
import net.PORC.journalApp.entity.User;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class JournalEntryService {
    @Autowired
    private JournalEntryRepo journalEntryRepo;
    @Autowired
    private UserService userService;

    @Transactional
    public void SaveEntry(JournalEntry journalEntry, String username) {
        User user = userService.FindByUsername(username);
        journalEntry.setDate(LocalDateTime.now());
        JournalEntry saved = journalEntryRepo.save(journalEntry);
        user.getJournalEntries().add(saved);
        userService.SaveUser(user);
    }


    public List<JournalEntry> getAll() {
        return journalEntryRepo.findAll();

    }


    public Optional<JournalEntry> Find_byID(ObjectId id) {
        return journalEntryRepo.findById(id);

    }


    public boolean DeleteEntryByID(ObjectId id, String username) {
        User user = userService.FindByUsername(username);
        if( user == null){
            return false;

        }
        boolean removed = user.getJournalEntries().removeIf(x -> x.getId().equals(id));
        if(!removed){
            return false;
        }
        userService.SaveUser(user);
        journalEntryRepo.deleteById(id);
        return true;
    }

    public Optional<JournalEntry> FindByUserID(String username, ObjectId id) {
        User user = userService.FindByUsername(username);
        if (user == null) {
            return Optional.empty();
        }

        return user.getJournalEntries().stream().filter(entry -> entry.getId().equals(id)).findFirst();
    }
    public void SaveEntry(JournalEntry journalEntry){
        journalEntryRepo.save(journalEntry);
    }

}