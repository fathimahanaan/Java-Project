import enrolmentregister.CourseManager;
import enrolmentregister.FullTimeStudent;
import enrolmentregister.PartTimeStudent;
import enrolmentregister.Student;

import java.io.ByteArrayInputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.List;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.Test;

public class CourseTest {

    private CourseManager course;
    private final SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
    private Student[] studentArray;
            
    @Before
    public void setUp() throws ParseException {
        course = new CourseManager();
        
        studentArray = new Student[5];
        // Populate studentArray with test data
        studentArray[0] = new Student("John Doe", 
                format.parse("24/11/2002"), "M", "FT", 1, 5, 5000);
        studentArray[1] = new Student("Jane Doe",
                format.parse("05/05/2003"), "F", "FT", 3, 6, 2500);
        
        studentArray[2] = new Student("Bob Smith", 
                format.parse("16/09/2001"), "M", "PT", 2, 3, 2250);
        studentArray[3] = new Student("Alice Johnson",
                format.parse("17/11/1999"), "F", "PT", 3, 4, 3000);
        course.setStudentArray(studentArray);

    }

    @Test
    public void testSetCourseName() {
        // Test checks if the course name is set properly
        course.setCourseName("Test Course");
        assertEquals("Test Course", course.getCourseName());
    }
    
    @Test
    public void testGroupStudentsByMode() throws ParseException {
        // Test verifies if the students are grouped correctly
        // depending on their study modes
        
        Student[] studentArray1 = new Student[5];
        // Populate studentArray with test data
        studentArray1[0] = new Student("John Doe", 
                format.parse("24/11/2002"), "M", "FT", 1, 5, 5000);
        studentArray1[1] = new Student("Jane Doe",
                format.parse("05/05/2003"), "F", "FT", 3, 6, 2500);
        
        studentArray1[2] = new Student("Bob Smith", 
                format.parse("16/09/2001"), "M", "PT", 2, 3, 2250);
        studentArray1[3] = new Student("Alice Johnson",
                format.parse("17/11/1999"), "F", "PT", 3, 4, 3000);
        course.setStudentArray(studentArray1);
        
        // Group the students by their study mode
        course.groupStudentsByMode(studentArray1);
        PartTimeStudent[] ptStudents = course.getPTStudentArray();
        FullTimeStudent[] ftStudents = course.getFTStudentArray();
        
        // Verify the correct number of students were grouped
        assertEquals(2, ptStudents.length);
        assertEquals(2, ftStudents.length);

        // Verify the correct students were grouped
        List<String> ptStudentNames = Arrays.asList(ptStudents[0]
                .getName(), ptStudents[1].getName());
        List<String> ftStudentNames = Arrays.asList(ftStudents[0]
                .getName(), ftStudents[1].getName());
        
        assertEquals(true, ftStudentNames.contains("John Doe"));
        assertEquals(true, ftStudentNames.contains("Jane Doe"));
        
        assertEquals(true, ptStudentNames.contains("Alice Johnson"));
        assertEquals(true, ptStudentNames.contains("Bob Smith"));
    }
    
    @Test
    public void testAddStudent() throws ParseException {
        // Test checks whether a student is added to the course when
        // addStudentPrompt() is called
        
        // Set up input stream
        String userInput = "Mary Doe\n" + "10/05/2001\n" + "F\n" 
                + "FT\n" + "1\n" + "5\n";
        System.setIn(new ByteArrayInputStream(userInput.getBytes()));
        
        // Check if the name of the student matches the name entered
        int index = course.getNumOfStudents(course.getStudentArray());
        boolean added = course.addStudentPrompt();
        assertTrue(added);
        assertEquals("Mary Doe", course.getStudentArray()[index].getName());
        
        // Since the test array has now reached full capacity, a new student
        // should not be allowed to be added
        userInput = "Jack Clay\n" + "10/05/2001\n" + "F\n" + "FT\n" 
                + "1\n" + "5\n";
        System.setIn(new ByteArrayInputStream(userInput.getBytes()));
        
        // When attempting to add the student to the array, 
        // addStudentPrompt() should return false as array is full
        added = course.addStudentPrompt();
        assertFalse(added);
    }
    
    @Test 
    public void testAddStudent_MaxCapacity() {
        // This test checks that a new student is not added to the array
        // if it has reached full capacity
        
        // Set up the input stream
        String userInput = "Mary Doe\n" + "10/05/2001\n" + "F\n" 
                + "FT\n" + "1\n" + "5\n";
        System.setIn(new ByteArrayInputStream(userInput.getBytes()));
        
        course = new CourseManager();
        Student[] students = new Student[1];
        students[0] = new Student("John Doe", null, "M", null, 0, 0, 0);
        course.setStudentArray(students);
        
        // When attempting to add the student to the array, 
        // addStudentPrompt() should return false as array is full
        boolean added = course.addStudentPrompt();
        assertFalse(added);
    }
    
    @Test 
    public void testAddStudent_EmptyDetail() {
        // This test checks that a new student with empty details
        
        // Set up the input stream with the input for date of birth
        // left empty
        String userInput = "Mary Doe\n" + "\n" + "F\n" 
                + "FT\n" + "1\n" + "5\n";
        System.setIn(new ByteArrayInputStream(userInput.getBytes()));
        
        // Check if the name of the student matches the name entered
        int index = course.getNumOfStudents(course.getStudentArray());
        boolean added = course.addStudentPrompt();
        assertTrue(added);
        assertEquals("Mary Doe", course.getStudentArray()[index].getName());
        assertEquals(null, course.getStudentArray()[index].getDOB());
    }
    
    @Test 
    public void testAddStudent_InvalidDetail() {
        // This test checks that a new student is not added with invalid 
        // input
        
        // Set up the input stream invalid date of birth and gender
        String userInput = "Mary Doe\n" // valid 
                + "1/1/2001\n" // valid
                + "abc\n" + "F\n" // first input for gender in invalid
                + "FT\n" + "1\n" + "5\n"; // valid
        System.setIn(new ByteArrayInputStream(userInput.getBytes()));
        
        // Check if the corrected seconf input for gender is set
        int index = course.getNumOfStudents(course.getStudentArray());
        boolean added = course.addStudentPrompt();
        assertTrue(added);
        assertEquals("Mary Doe", course.getStudentArray()[index].getName());
        assertEquals("F", course.getStudentArray()[index].getGender());
    }
    
    @Test
    public void testDeleteStudent() throws ParseException {
        // This test checks whether the given student is deleted from the 
        // array when deleteStudentPrompt() is called
        
        // Set up input stream
        String userInput = "John Doe\n";
        System.setIn(new ByteArrayInputStream(userInput.getBytes()));

        course = new CourseManager();
        course.setStudentArray(studentArray);
        int index = course.findStudentIndex("John Doe");

        // Verify that the student has been deleted from the array
        boolean deleted = course.deleteStudentPrompt();
        assertTrue(deleted);
        
        // Verify that student in that index is not the student
        // that was deleted
        assertNotEquals("John Doe",course
                .getStudentArray()[index].getName());
                
        // Verify that the last index in the array is null
        int arrayLength = course.getStudentArray().length;
        assertNull(course.getStudentArray()[arrayLength - 1]); 
    }
    
    @Test
    public void testDeleteStudent_StudentMissing() throws ParseException {
        // This test checks if deleteStudentPrompt() returns 'false'
        // when the student does not exist and is thus, not deleted
        
        // Set up input stream
        String userInput = "Lily Mark\n";
        System.setIn(new ByteArrayInputStream(userInput.getBytes()));

        course = new CourseManager();
        course.setStudentArray(studentArray);
        
        // Verify that no student has been deleted from the array
        boolean deleted = course.deleteStudentPrompt();
        assertFalse(deleted);
    }
    
    @Test
    public void testDeleteStudent_ArrayEmpty() throws ParseException {
        // This test checks if deleteStudentPrompt() returns 'false'
        // when the array is empty and no student is deleted
        
        // Set up the input stream
        String userInput = "John Mark\n";
        System.setIn(new ByteArrayInputStream(userInput.getBytes()));
        
        // Set up the empty test array
        Student[] students = new Student[1];
        
        course = new CourseManager();
        course.setStudentArray(students);

        // Verify that no student has been deleted from the array
        boolean deleted = course.deleteStudentPrompt();
        assertFalse(deleted);
    }
    
    @Test 
    public void testSearchStudent() throws ParseException { 
        // This test checks if searchStudent() prints the
        // details of the student 
        
        // Set up the input stream
        String userInput = "John Doe\n";
        System.setIn(new ByteArrayInputStream(userInput.getBytes()));
        
        Student[] students = new Student[2];
        // Populate studentArray with test data
        students[0] = new Student("John Doe", 
                format.parse("24/11/2002"), "M", "FT", 1, 5, 5000);
        
        course = new CourseManager();
        course.setStudentArray(students);
        
        // Verify that the student has been found and the 
        // details have been successfully printed
        boolean found = course.searchStudent();
        assertTrue(found);
    }
    
    @Test
    public void testSearchStudent_StudentMissing() throws ParseException {
        // This test checks if searchStudent() returns 'false'
        // when the student is not in the array
        
        // Set up input stream
        String userInput = "Ryan Wayne\n";
        System.setIn(new ByteArrayInputStream(userInput.getBytes()));
      
        // Set up the empty test array
        Student[] studentArray1 = new Student[2];
        studentArray1[0] = new Student("Bob Smith", 
                format.parse("16/09/2001"), "M", "PT", 2, 3, 2250);        
        studentArray1[1] = new Student("Jane Kelly", 
                format.parse("16/09/2001"), "M", "PT", 2, 3, 2250);
      
        course = new CourseManager();
        course.setStudentArray(studentArray1);
        
        // Verify that the student has not been found and
        // no details are printed
        boolean found = course.searchStudent();
        assertFalse(found);
    }
        
    @Test
    public void testSearchStudent_ArrayEmpty() throws ParseException {
        // This test checks if searchStudent() returns 'false'
        
        // Set up input stream
        String userInput = "Ryan Wayne\n";
        System.setIn(new ByteArrayInputStream(userInput.getBytes()));
        
        // Set up the empty test array
        Student[] studentArray1 = new Student[1];
      
        course = new CourseManager();
        course.setStudentArray(studentArray1);
        
        // Verify that the student has not been found and
        // no details are printed
        boolean found = course.searchStudent();
        assertFalse(found);
    }
    
    @Test
    public void testCalcGenderPercent_ArrayEmpty() {
        // This test checks if the percentage of a gender in
        // the array is 0% when array is empty
        
        // Set up the test data
        Student[] students = new Student[0];
        
        // Verify that percentage of male or female students 
        // in the array are 0%
        double percentage = course.calcGenderPercent(students, "M");
        assertEquals(0.0, percentage, 0.01);
        
        percentage = course.calcGenderPercent(students, "F");
        assertEquals(0.0, percentage, 0.01);
    }

    @Test
    public void testCalcGenderPercent_AllMale() {
        // This test checks if the percentage of male in
        // the array is 100% when array only has male students
        
        // Set up the test data
        Student[] students = new Student[3];
        students[0] = new Student("John Doe", null, "M", null, 0, 0, 0);
        students[1] = new Student("Jane Smith", null, "M", null, 0, 0, 0);
        students[2] = new Student("Bob Johnson", null, "M", null, 0, 0, 0);
        
        // Verify that percentage of male students 
        // in the array is 100%
        double percentage = course.calcGenderPercent(students, "M");
        assertEquals(100.0, percentage, 0.01);
        
        // Verify that percentage of female students 
        // in the array is 0% 
        percentage = course.calcGenderPercent(students, "F");
        assertEquals(0.0, percentage, 0.01);
    }
    
    @Test
    public void testGetNumOfStudents() {
        // This test checks if the number of students in
        // the array is calculated correctly
        
        // Set up the test data
        Student[] students = new Student[5];
        students[0] = new Student("Mary Doe", null, "F", null, 0, 0, 0);
        students[1] = new Student("Jane Smith", null, "F", null, 0, 0, 0);
        students[2] = new Student("Kelly Johnson", null, "F", null, 0, 0, 0);
        
        // Verify that if the number of students is 3 
        int numOfStudents = course.getNumOfStudents(students);
        assertEquals(3, numOfStudents);
    }

    @Test
    public void testCalcGenderPercent_NoMatch() {
        // This test checks if the percentage of male in
        // the array is 0% when array only has female students 
        
        // Set up the test data
        Student[] students = new Student[3];
        students[0] = new Student("Mary Doe", null, "F", null, 0, 0, 0);
        students[1] = new Student("Jane Smith", null, "F", null, 0, 0, 0);
        students[2] = new Student("Kelly Johnson", null, "F", null, 0, 0, 0);
        
        // Verify that percentage of male students 
        // in the array is 0%
        double percentage = course.calcGenderPercent(students, "M");
        assertEquals(0.0, percentage, 0.01);
    }

    @Test
    public void testCalcGenderPercent() {
        // This test checks if the percentage of genders in
        // the array are calculated correctly 
        
        // Verify that percentage of male students 
        // in the array is 50%
        double percentage = course.calcGenderPercent(studentArray, "M");
        assertEquals(50.0, percentage, 0.01);
        
        // Verify that percentage of female students 
        // in the array is 50%
        percentage = course.calcGenderPercent(studentArray, "F");
        assertEquals(50.0, percentage, 0.01);
    }
    
    @Test
    public void testNumOfStudents_InvalidStudyMode() throws ParseException {
        // This test verfies that invalid students are not taken into
        // consideration when calculating
      
        // Set up the empty test array
        Student[] studentArray1 = new Student[4];
        studentArray1[0] = new Student("Bob Smith", 
                format.parse("16/09/2001"), "F", "PT", 2, 3, 2250);        
        studentArray1[1] = new Student("Jane Kelly", 
                format.parse("16/09/2001"), "M", "FT", 2, 3, 2250);
        studentArray1[2] = new Student("Jane Kelly", 
                format.parse("16/09/2001"), "M", null, 2, 3, 2250);
      
        course = new CourseManager();
        course.setStudentArray(studentArray1);
        course.groupStudentsByMode(studentArray1);
        
        FullTimeStudent[] ftStudents = course.getFTStudentArray();
        PartTimeStudent[] ptStudents = course.getPTStudentArray();
        
        // Verify that the student with invalid study mode has
        // been ignored
        assertEquals(1, course.getNumOfStudents(ftStudents));
        assertEquals(1, course.getNumOfStudents(ptStudents)); 
        
        double percentage = course.calcGenderPercent(ftStudents, "M");
        assertEquals(100.0, percentage, 0.01);
        percentage = course.calcGenderPercent(ptStudents, "M");
        assertEquals(0.0, percentage, 0.01);
    }
    
    @Test
    public void testNumOfStudents_InvalidGender() throws ParseException {
        // This test verfies that invalid students are not taken into
        // consideration when calculating
      
        // Set up the empty test array
        Student[] studentArray1 = new Student[5];
        studentArray1[0] = new Student("Bob Smith", 
                format.parse("16/09/2001"), null, "PT", 2, 3, 2250);        
        studentArray1[1] = new Student("Jane Kelly", 
                format.parse("16/09/2001"), "M", "FT", 2, 3, 2250);
        studentArray1[2] = new Student("Jane Kelly", 
                format.parse("16/09/2001"), "F", "PT", 2, 3, 2250);
        studentArray1[3] = new Student("Kelly May", 
                format.parse("16/09/2001"), null, "FT", 2, 3, 2250);
      
        course = new CourseManager();
        course.setStudentArray(studentArray1);
        course.groupStudentsByMode(studentArray1);
        
        FullTimeStudent[] ftStudents = course.getFTStudentArray();
        PartTimeStudent[] ptStudents = course.getPTStudentArray();
        
        // Verify that the student with invalid study mode has
        // been ignored
        assertEquals(1, course.countValidGenders(ftStudents));
        assertEquals(1, course.countValidGenders(ptStudents)); 
        
        double percentage = course.calcGenderPercent(ftStudents, "M");
        assertEquals(100.0, percentage, 0.01);
        percentage = course.calcGenderPercent(ptStudents, "M");
        assertEquals(0.0, percentage, 0.01);
    }
}   

