import enrolmentregister.Student;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.PrintStream;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.junit.After;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import org.junit.Before;
import org.junit.Test;

public class StudentTest{
    
    private Student student;
    private SimpleDateFormat format;
    
    private final InputStream systemIn = System.in;
    private final PrintStream systemOut = System.out;

    @Before
    public void setUp() {
        student = new Student();
        format = new SimpleDateFormat("dd/MM/yyyy");
    }
    
    @After
    public void tearDown() {
        System.setIn(systemIn);
        System.setOut(systemOut);
    }
    
    @Test
    public void testPromptForName_ValidName() {
        // Set up test user input
        String userInput = "  John Doe  ";
        InputStream in = new ByteArrayInputStream(userInput.getBytes());
        System.setIn(in);
        
        student = new Student();
        // Call the method and check if the name is set correctly
        student.promptForName();
        assertEquals("John Doe", student.getName());
        
        // Set up test user input
        userInput = "john doe";
        in = new ByteArrayInputStream(userInput.getBytes());
        System.setIn(in);
        
        student = new Student();
        // Call the method and check if the name is set correctly
        student.promptForName();
        assertEquals("John Doe", student.getName());
    }
    
    @Test
    public void testSetName() {
        student.setName("John Smith");
        assertEquals("John Smith", student.getName());
    }

    @Test
    public void testValidName() {
        // Test valid inputs for name. First letters of the
        // first and late name are capitalised
        assertEquals("John Doe", student.validateName("john doe"));
        assertEquals("John Doe", student.validateName("John Doe"));
    }
        
    @Test
    public void testEmptyName() {
        // Test if user input is empty, null is returned
        assertNull(student.validateName(""));
    }
    
    @Test
    public void testInvalidName() {
        // Test if null is returned when user input is invalid
        assertNull(student.validateName("John Doe14"));
        assertNull(student.validateName("JohnDoe"));
        assertNull(student.validateName("12345"));
    }
    
    @Test
    public void testPromptForDOB_ValidDOB() throws ParseException {
        // Set up test user input
        String userInput = "1/2/2001\n";
        InputStream in = new ByteArrayInputStream(userInput.getBytes());
        System.setIn(in);
        
        student = new Student();
        // Call the method and check if the date is set correctly
        student.promptForDOB();
        Date expected = format.parse(userInput);
        assertEquals(expected, student.getDOB());
    }
    
    @Test
    public void testSetDateOfBirth() throws ParseException {       
        // Verify input is valid
        String validInput = "10/6/2000";
        Date expectedDate = student.validateDOB(validInput);
        student.setDOB(expectedDate);
        assertEquals(expectedDate, student.getDOB());
    }
    
    @Test
    public void testValidDate() throws ParseException {
        // Verify valid input for date
        String validInput = "10/6/2000";
        Date expectedDate = format.parse(validInput);
        assertEquals(expectedDate, student.validateDOB(validInput));
    }

    @Test
    public void testEmptyDate() throws ParseException {
        // Verify input is empty, null is returned
        assertNull(student.validateDOB(""));
    }

    @Test
    public void testInvalidDate() throws ParseException {
        // Verify input is invalid, null is returned
        assertNull(student.validateDOB("1234"));
        assertNull(student.validateDOB("31/2/2001"));
        assertNull(student.validateDOB("1/13/2001"));
        
        // Verify the input date of birth is unrealistic
        assertNull(student.validateDOB("1/1/2023"));
        assertNull(student.validateDOB("1/1/1922"));
        
        // Verify the input date of birth is not a leap day
        // if the year is not a leap year
        assertNull(student.validateDOB("29/2/2001"));
    }
    
    @Test
    public void testPromptForGender_ValidGender() {
        // Set up test user input
        String userInput = "m";
        InputStream in = new ByteArrayInputStream(userInput.getBytes());
        System.setIn(in);
        
        // Call the method and check if the gender is set correctly
        student = new Student();
        student.promptForGender();
        assertEquals("M", student.getGender());
        
        userInput = "F";
        in = new ByteArrayInputStream(userInput.getBytes());
        System.setIn(in);
        
        // Call the method and check if the gender is set correctly
        student = new Student();
        student.promptForGender();
        assertEquals("F", student.getGender());
    } 
    
    @Test
    public void testSetGender() {
        // Verify the gender is set correctly
        student.setGender("M");
        assertEquals("M", student.getGender());
        
        student.setGender("F");
        assertEquals("F", student.getGender());
    }
    
    @Test
    public void testValidGender() {
        // Verify the gender is set correctly
        // regardless of the case of user input
        assertEquals("M", student.validateGender("M"));
        assertEquals("F", student.validateGender("F"));
        assertEquals("M", student.validateGender("m"));
        assertEquals("F", student.validateGender("f"));
    }
    
    @Test
    public void testEmptyGender() {
        // Verify gender is null if user input is empty
        assertNull(student.validateGender(""));
    }
    
    @Test
    public void testInvalidGender() {
        // Verify input is invalid, null is returned
        assertNull(student.validateGender("X"));
        assertNull(student.validateGender("12"));
    }
    
    @Test
    public void testPromptForStudyMode_ValidStudyMode() {
        // Set up test user input
        String userInput = "ft";
        InputStream in = new ByteArrayInputStream(userInput.getBytes());
        System.setIn(in);
        
        // Call the method and check if the studyMode is set correctly
        student = new Student();
        student.promptForStudyMode();
        assertEquals("FT", student.getStudyMode());
        
        // Set up test user input
        userInput = "PT";
        in = new ByteArrayInputStream(userInput.getBytes());
        System.setIn(in);
        
        // Call the method and check if the studyMode is set correctly
        student = new Student();
        student.promptForStudyMode();
        assertEquals("PT", student.getStudyMode());
    } 
    
    @Test
    public void testSetStudyMode() {
        // Verify the studyMode is set correctly
        student.setStudyMode("FT");
        assertEquals("FT", student.getStudyMode());
        
        student.setStudyMode("PT");
        assertEquals("PT", student.getStudyMode());
    }
    
    @Test
    public void testValidStudyMode() {
        // Verify the studyMode is set correctly
        // regardless of the case of user input
        assertEquals("FT", student.validateStudyMode("FT"));
        assertEquals("PT", student.validateStudyMode("PT"));
        assertEquals("FT", student.validateStudyMode("ft"));
        assertEquals("PT", student.validateStudyMode("pt"));
    }

    @Test
    public void testEmptyStudyMode() {
        // Verify studyMode is null if user input is empty
        assertNull(student.validateStudyMode(""));
    }

    @Test
    public void testInvalidStudyMode() {
        // Verify input is invalid, null is returned
        assertNull(student.validateStudyMode("X"));
        assertNull(student.validateStudyMode("12"));
    }
    
    @Test
    public void testPromptForYear_ValidYear() {
        // Set up test user input
        String userInput = "1";
        InputStream in = new ByteArrayInputStream(userInput.getBytes());
        System.setIn(in);
        
        // Call the method and check if the year is set correctly
        student = new Student();
        student.promptForYear();
        assertEquals(1, student.getYear());
        
        // Set up test user input
        userInput = "2";
        in = new ByteArrayInputStream(userInput.getBytes());
        System.setIn(in);
        
        // Call the method and check if the year is set correctly
        student = new Student();
        student.promptForYear();
        assertEquals(2, student.getYear());
        
        // Set up test user input
        userInput = "4";
        in = new ByteArrayInputStream(userInput.getBytes());
        System.setIn(in);
        
        // Call the method and check if the year is set correctly
        student = new Student();
        student.promptForYear();
        assertEquals(4, student.getYear());       
    }
    
    @Test
    public void testSetYear() {
        // Verify the year is set correctly
        student.setYear(1);
        assertEquals(1, student.getYear());
        
        student.setYear(4);
        assertEquals(4, student.getYear());
        
        student.setYear(0);
        assertEquals(0, student.getYear());
    }
    
    @Test
    public void testValidYear() {
        // Verify only valid values of year are accepted (1-4)
        assertEquals(1, student.validateYear("1"));
        assertEquals(2, student.validateYear("2"));
        assertEquals(4, student.validateYear("4"));
    }

    @Test
    public void testInvalidYear() {
        // Verify that invalid values are returned as 0  
        assertEquals(0, student.validateYear("5"));
        assertEquals(0, student.validateYear("0"));
        assertEquals(0, student.validateYear(""));
        assertEquals(0, student.validateYear("ABC"));
    }
    
    @Test
    public void testPromptForModules_ValidModules() {
        // Set up test user input
        String userInput = "1";
        InputStream in = new ByteArrayInputStream(userInput.getBytes());
        System.setIn(in);
        
        // Call the method and check if the numModules is set correctly
        student = new Student();
        student.promptForNumModules();
        assertEquals(1, student.getNumModules());
        
        // Set up test user input
        userInput = "3";
        in = new ByteArrayInputStream(userInput.getBytes());
        System.setIn(in);
        
        // Call the method and check if the numModules is set correctly
        student = new Student();
        student.promptForNumModules();
        assertEquals(3, student.getNumModules());
        
        // Set up test user input
        userInput = "6";
        in = new ByteArrayInputStream(userInput.getBytes());
        System.setIn(in);
        
        // Call the method and check if the numModules is set correctly
        student = new Student();
        student.promptForNumModules();
        assertEquals(6, student.getNumModules());
    }
    
    @Test
    public void testSetModules() {
        // Verify the num of modules is set correctly
        student.setNumModules(1);
        assertEquals(1, student.getNumModules());
        
        student.setNumModules(6);
        assertEquals(6, student.getNumModules());
        
        student.setNumModules(0);
        assertEquals(0, student.getNumModules());
    }
    
    @Test
    public void testValidModule() {
        // Verify only valid values of num of modules are accepted (1-6)
        assertEquals(1, student.validateNumModules("1"));
        assertEquals(3, student.validateNumModules("3"));
        assertEquals(6, student.validateNumModules("6"));
    }

    @Test
    public void testInvalidModule() {
        // Verify that invalid values are returned as 0  
        assertEquals(0, student.validateNumModules("7"));
        assertEquals(0, student.validateNumModules("0"));
        assertEquals(0, student.validateNumModules(""));
        assertEquals(0, student.validateNumModules("ABC"));
    }
    
    @Test
    public void testSetFee_FullTime() {
        // Test full-time year 1
        student.setStudyMode("FT");
        student.setYear(1);
        student.setNumModules(0);
        student.setFee();
        assertEquals(5000, student.getFee());
        
        // Test full-time year 3
        student.setYear(3);
        student.setFee();
        assertEquals(2500, student.getFee());
        
        // Test full-time year 4
        student.setYear(4);
        student.setFee();
        assertEquals(5000, student.getFee());
        
        // Test invalid year
        student.setYear(0); // invalid
        student.setNumModules(6);
        student.setFee();
        assertEquals(0, student.getFee());
    }    
     
    @Test
    public void testSetFee_PartTime() {
        // Test part-time with modules
        student.setStudyMode("PT");
        student.setNumModules(1);
        student.setFee();
        assertEquals(750, student.getFee());
        
        student.setNumModules(3);
        student.setFee();
        assertEquals(2250, student.getFee());

        student.setNumModules(6);
        student.setFee();
        assertEquals(4500, student.getFee());
        
        // Test part-time with invalid numModules
        student.setNumModules(0); // invalid
        student.setFee();
        assertEquals(0, student.getFee());
    }
    
    @Test
    public void testSetFee_InvalidStudyMode() {
        // Test invalid study mode
        student.setStudyMode("abc"); // invalid
        student.setYear(1);
        student.setNumModules(5);
        student.setFee();
        assertEquals(0, student.getFee());
        
        student.setStudyMode(null); // invalid
        student.setYear(3);
        student.setNumModules(2);
        student.setFee();
        assertEquals(0, student.getFee());
    }
}