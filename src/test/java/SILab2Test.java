import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class SILab2Test {

    SILab2 instance = new SILab2();

    List<String> createList(String... names){
        return Arrays.asList(names);
    }

    @Test
    void everyStatementTest(){
        // 1,2; 14; 15; 16;
        assertFalse(instance.function(null, new ArrayList<>()));

        // 1,2; 3; 13; 14; 15; 16;
        assertFalse(instance.function(new User(null, "whatever", "whatever"),
                createList()));

        // 1,2; 3; 4; 5.1; 5.2; 5.3; 6; 7; 8; 9; 10; 11; 12; 16;
        assertTrue(instance.function(new User("david", "whatever", "david@mail.com"),
                createList()));
    }

    @Test
    void everyBranchTest(){
        // 1,2 - 14; 14 - 15; 15 - 16;
        assertFalse(instance.function(null, new ArrayList<>()));

        // 1,2 - 3; 3 - 13; 13 - 14; 14 - 15; 15 - 16;
        assertFalse(instance.function(new User(null, "whatever", "whatever"),
                createList()));

        // 1,2 - 3; 3 - 4; 4 - 5.1; 5.1 - 5.2; 5.2 - 6; 6 - 7; 7 - 8; 8 - 9; 9 - 10;
        // 10 - 5.3; 5.3 - 5.2; 5.2 - 11; 11 - 12; 12 - 16
        assertTrue(instance.function(new User("david", "whatever", "david@mail.com"),
                createList()));

        // 1,2 - 3; 3 - 4; 4 - 5.1; 5.1 - 5.2; 5.2 - 6; 6 - 8; 8 - 10;
        // 10 - 5.3; 5.3 - 5.2; 5.2 - 11; 11 - 13; 13 - 14; 14 - 15; 15 - 16;
        assertFalse(instance.function(new User("david", "whatever", "david$mail_com"),
                createList()));
    }

    @Test
    void everyPathTest(){
        // 1-2, 14, 15, 16
        assertFalse(instance.function(null, new ArrayList<>()));

        // 1-2, 3, 13, 14, 15, 16
        assertFalse(instance.function(new User(null, "whatever" , "whatever"),
                new ArrayList<>()));

        // 1-2, 3, 4, 5.1, 5.2, 11, 13, 14, 15, 16
        assertFalse(instance.function(new User("david", "whatever" , ""),
                new ArrayList<>()));

        // 1-2, 3, 4, 5.1, 5.2, 11, 12, 16
        // - Not possible because it never reaches 7 and 9 for 11 to be true.

        // 1-2, 3, 4, 5.1, loop(5.2, 6, 8, 10, 5.3, 5.2), 11, 13, 14, 15, 16
        assertFalse(instance.function(new User("david", "whatever" , "david$mail.com"),
                new ArrayList<>()));

        // 1-2, 3, 4, 5.1, loop(5.2, 6, 8, 10, 5.3, 5.2), 11, 12, 16
        // - Not possible because it never reaches 7 and 9 for 11 to be true.

        // 1-2, 3, 4, 5.1, loop(5.2, (6, 8) || (6, 7, 8), 10, 5.3, 5.2), 11, 13, 14, 15, 16
        assertFalse(instance.function(new User("david", "whatever" , "david.mail@com"),
                new ArrayList<>()));

        // 1-2, 3, 4, 5.1, loop(5.2, (6, 8) || (6, 7, 8), 10, 5.3, 5.2), 11, 12, 16
        // - Not possible because it doesn't reach 9 for 11 to be true.

        // 1-2, 3, 4, 5.1, loop(5.2, 6, (8, 10) || (8, 9, 10), 5.3, 5.2), 11, 13, 14, 15, 16
        // - Not Possible because it can't reach 9 without reaching 7.

        // 1-2, 3, 4, 5.1, loop(5.2, 6, (8, 10) || (8, 9, 10), 5.3, 5.2), 11, 12, 16 - Not possible
        // - Not possible because it doesn't reach 7 and 9 for 11 to be true.

        // 1-2, 3, 4, 5.1, loop(5.2, (6, 8) || (6, 7, 8), (8, 10) || (8, 9, 10), 5.3, 5.2), 11, 12, 16
        assertTrue(instance.function(new User("david", "whatever" , "david@mail.com"),
                new ArrayList<>()));

        // 1-2, 3, 4, 5.1, loop(5.2, (6, 8) || (6, 7, 8), (8, 10) || (8, 9, 10), 5.3, 5.2), 11, 13, 14, 15, 16
        // - Not possible because when it reaches 7 and 9, 11 becomes true and it reaches 12
        // before it ever reaches 13, 14 or 15.
    }

    @Test
    void multipleConditionsTest(){
        // if (user.getUsername()!=null && user.getEmail()!=null && !allUsers.contains(user.getUsername())){ //3
        // F && X && X
        assertFalse(instance.function(new User(null, "whatever", "whatever"),
                new ArrayList<>()));
        // T && F && X
        assertFalse(instance.function(new User("david", "whatever", null),
                new ArrayList<>()));
        // T && T && F
        assertFalse(instance.function(new User("david", "whatever", "david@mail.com"),
                createList("david")));
        // T && T && T
        assertTrue(instance.function(new User("david", "whatever", "david@mail.com"),
                new ArrayList<>()));

        // if (atChar && user.getEmail().charAt(i)=='.') //8
        // F && X
        assertFalse(instance.function(new User("david", "whatever", "david$mail.com"),
                new ArrayList<>()));

        // T && F
        assertFalse(instance.function(new User("david", "whatever", "david.mail@com"),
                new ArrayList<>()));

        // T && T
        assertTrue(instance.function(new User("david", "whatever", "david@mail.com"),
                new ArrayList<>()));

        // if (atChar && dotChar) //11
        // F && X
        assertFalse(instance.function(new User("david", "whatever", "david$mail.com"),
                new ArrayList<>()));

        // T && F
        assertFalse(instance.function(new User("david", "whatever", "david.mail@com"),
                new ArrayList<>()));

        // T && T
        assertTrue(instance.function(new User("david", "whatever", "david@mail.com"),
                new ArrayList<>()));
    }
}