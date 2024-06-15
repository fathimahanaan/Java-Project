import enrolmentregister.CourseManager;
import enrolmentregister.EnrolmentRegister;
import enrolmentregister.EnrolmentRegister.MenuOption;
import enrolmentregister.FileIO;
import enrolmentregister.Student;

import org.junit.Test;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.text.ParseException;
import java.util.Scanner;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import org.junit.Before;

public class EnrolmentRegisterTest {
    private EnrolmentRegister register;
    
    @Before
    public void setUp() throws ParseException {       
        // Set up test instances
        Student[] studentArray = new Student[5];
        CourseManager course = new CourseManager();
        course.setStudentArray(studentArray);
        course.setCourseName("Java Programming");
        FileIO fileIO = new FileIO(course);
        register = new EnrolmentRegister(course, fileIO);
    }
    
    @Test
    public void testInitialiseProgram() throws NoSuchFieldException,
            IllegalArgumentException, IllegalAccessException {
        
        // Set up test instances
        CourseManager course = new CourseManager();
        FileIO fileIO = new FileIO(course);
        EnrolmentRegister newRegister = new EnrolmentRegister(course, fileIO);

        // Accessing private instance variables with reflection
        Field courseField = EnrolmentRegister.class
                .getDeclaredField("course");
        courseField.setAccessible(true);
        CourseManager privateCourse = (CourseManager) courseField
                .get(newRegister);
        
        Field fileIOField = EnrolmentRegister.class
                .getDeclaredField("fileIO");
        fileIOField.setAccessible(true);
        FileIO privateFileIO = (FileIO) fileIOField.get(newRegister);

        // Testing if private instance variables are set correctly
        assertNotNull(newRegister);
        assertNotNull(privateCourse);
        assertNotNull(privateFileIO);
        assertEquals(course, privateCourse);
        assertEquals(fileIO, privateFileIO);       
    }   
    
    @Test
    public void testGetUserChoice() {
        // The test checks if the options in the main menu 
        // are called properly corresponding to the user's input
        
        // Set the user input to 1 
        String userInput = "1\n";
        Scanner input = new Scanner(userInput);
        // Menu option 1 corresponds to 'PRINT_REPORT'
        MenuOption expected = MenuOption.PRINT_REPORT;
        MenuOption actual = register.getUserChoice(input);
        
        // Verify if the correct menu option is called
        assertEquals(expected, actual);
        
        // Set the user input to 3
        userInput = "3\n";
        input = new Scanner(userInput);  
        // Menu option 3 corresponds to 'DELETE_STUDENT'
        expected = MenuOption.DELETE_STUDENT;
        actual = register.getUserChoice(input);
        
        // Verify if the correct menu option is called
        assertEquals(expected, actual);
        
        // Set the user input to 5
        userInput = "5\n";
        input = new Scanner(userInput);
        // Menu option 5 corresponds to 'EXIT'
        expected = MenuOption.EXIT;
        actual = register.getUserChoice(input);
        
        // Verify if the correct menu option is called
        assertEquals(expected, actual);
    }
    
    @Test
    public void testGetUserConfirmation() {
        // This test verifies if true is returned when user
        // enters yes and false is returned when user enters 
        // false regardless of the case.
        
        // Set the user input to 'yes'
        Scanner input = new Scanner(
                new ByteArrayInputStream("yes\n".getBytes()));
        // Verify that 'true' is returned
        assertTrue(register.getUserConfirmation(input));
        
        // Set the user input to 'NO'
        input = new Scanner(new ByteArrayInputStream("NO\n".getBytes()));
        // Verify that 'false' is returned
        assertFalse(register.getUserConfirmation(input));
        
        // The user first enters an invalid input and when prompted
        // to enter again, enters 'Yes'
        input = new Scanner(new ByteArrayInputStream("abc\nYes".getBytes()));
        // Verify that 'false' is returned
        assertTrue(register.getUserConfirmation(input));
    }
}