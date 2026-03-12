package net.PORC.journalApp.service;

import net.PORC.journalApp.Repository.JournalEntryRepo;
import net.PORC.journalApp.Repository.UserRepository;
import net.PORC.journalApp.entity.JournalEntry;
import net.PORC.journalApp.entity.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;



@SpringBootTest
@ExtendWith(MockitoExtension.class)
public class UserserviceTest {
    @Mock
    private UserRepository userRepository;
    @InjectMocks
    private UserService userService;

    @Test
    public void AddTesting(){
        assertEquals(4, 2+2);

    }
    @Test
    public void FindByUserName(){
        User user = new User();
        user.setUsername("abcd");
        when(userRepository.findByUsername("abcd")).thenReturn(Optional.of(user));
        User result = userService.FindByUsername("abcd");
        assertNotNull(result);
        assertEquals("abcd", result.getUsername());
        verify(userRepository).findByUsername("abcd");


    }

    @ParameterizedTest
    @CsvSource({
            "1,2,3",
            "3,4,6",
            "5,6,11"
    })
    public void test(int a, int b, int expected){
        assertEquals(expected, a+b);

    }
    @Test
    public void AllUserTest(){
        List<User> user = userRepository.findAll();
        assertNotNull(user);
    }
    @Test
    public void SaveUserTest(){
        User user = new User();
        user.setUsername("abcd");
        when(userRepository.save(user)).thenReturn(user);
        User result = userService.SaveUser(user);
        assertEquals("abcd", result.getUsername());
        verify(userRepository).save(user);
    }
    @Test
    public void TestUpdateUser(){
        User user = new User();
        user.setUsername("ABCD");
        user.setPassword("ABCD");
        User savedUser = userService.SaveUser(user);
        savedUser.setPassword("123456");
        User updatedUser = userService.SaveUser(user);
        assertEquals("1234567", updatedUser.getPassword());
    }
}
