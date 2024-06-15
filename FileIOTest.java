import enrolmentregister.CourseManager;
import enrolmentregister.FileIO;
import enrolmentregister.Student;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class FileIOTest {
    private CourseManager course;
    private FileIO fileIO;
    private SimpleDateFormat format;

    @Before
    public void setUp() {
        course = new CourseManager();
        fileIO = new FileIO(course);
        format = new SimpleDateFormat("dd/MM/yyyy");
    }
    
    /*
    * java.nio.file.Files Class is referenced from Digital Ocean website
    * @see <a href=
     "https://www.digitalocean.com/community/tutorials/java-files-nio-files-class">
     </a>
    * Temporary file is referenced from Mkyong.com website
    * @see <a href=
     "https://mkyong.com/java/how-to-create-temporary-file-in-java/"></a>
    */
    
    @Test
    public void testReadStudentFile() throws IOException, ParseException {
        // This test creates a temporary file with student data in it
        // reading the file, and asserting that the data was read correctly
        
        // Create a temporary file to read from
        File tempFile = File.createTempFile("TempFile", ".txt");
        tempFile.deleteOnExit();
        // Write some student data to the file
        String fileContent = "Name, Date of Birth, Gender, Study Mode,"
                + "Num of Modules, Fee\n"
                + "John Doe, 01/01/2000, M, FT, 1, 6, 5000\n"
                + "Jane Smith, 01/01/2001, F, PT, 2, 4, 3000\n";
        java.nio.file.Files.write(tempFile.toPath(), fileContent.getBytes());

        // Call the method to read from the file
        fileIO.readStudentDetails(tempFile.getPath());    
        // Get the array that has been created 
        Student[] students = course.getStudentArray();
        // Verify that there are three students in the array
        int numOfStudents = course.getNumOfStudents(students);
        assertEquals(2, numOfStudents);
        
        // Assert that the data was read correctly
        assertEquals("John Doe", students[0].getName());
        assertEquals("01/01/2000", students[0].getDOBStr());
        assertEquals("M", students[0].getGender());
        assertEquals("FT", students[0].getStudyMode());
        assertEquals(1, students[0].getYear());
        assertEquals(6, students[0].getNumModules());
        assertEquals(5000, students[0].getFee());

        assertEquals("Jane Smith", students[1].getName());
        assertEquals("01/01/2001", students[1].getDOBStr());
        assertEquals("F", students[1].getGender());
        assertEquals("PT", students[1].getStudyMode());
        assertEquals(2, students[1].getYear());
        assertEquals(4, students[1].getNumModules());
        assertEquals(3000, students[1].getFee());
    
    }
    
    @Test
    public void testReadStudentFile_EmptyFile() throws IOException, 
            ParseException {
        // Create an empty temporary file
        File tempEmptyFile = File.createTempFile("TempEmptyFile", ".txt");
        tempEmptyFile.deleteOnExit();

        // Call the method to read from the file
        fileIO.readStudentDetails(tempEmptyFile.getPath());
        
        // Verify the array is empty by checking the number
        // of students present in the array
        Student[] students = course.getStudentArray();
        int numOfStudents = course.getNumOfStudents(students);
        assertEquals(0, numOfStudents); 
    }
    
    @Test
    public void testReadCourseFile_EmptyFile() throws IOException, 
            ParseException {
        // Create an empty temporary file
        File tempEmptyFile = File.createTempFile("TempEmptyFile", ".txt");
        tempEmptyFile.deleteOnExit();
        
        // Set up test user input
        String userInput = "Computing Science  \n";
        InputStream in = new ByteArrayInputStream(userInput.getBytes());
        System.setIn(in);

        // Call the method to read from the file
        fileIO.readCourseDetails(tempEmptyFile.getPath());
        
        // Verify the course name is set properly
        assertEquals("Computing Science", course.getCourseName()); 
    }
    
    @Test
    public void testReadCourseDetails() throws IOException {
        // Create a temporary file to read from
        File tempFile = File.createTempFile("TempFile", ".txt");
        tempFile.deleteOnExit();

        // Write some course data to the file
        String fileContent = "Course Name: Java Programming\n";
        java.nio.file.Files.write(tempFile.toPath(), fileContent.getBytes());

        // Call the method to read from the file
        fileIO.readCourseDetails(tempFile.getPath());

        // Assert that the data was read correctly
        assertEquals("Java Programming", course.getCourseName());
    }
    
    @Test
    public void testWriteStudentDetails() throws IOException, ParseException {
        // This test checks whether the student data is
        // correctly written to the file    
        // Create a CourseManager object with some student data        
        Student[] students = new Student[2];
        students[0] = new Student("John Doe", format.parse("01/01/2000"), 
                "M", "FT", 1, 6, 6000);
        students[1] = new Student("Jane Doe", format.parse("06/06/2001"), 
                "F", "PT", 2, 4, 3000);      
        course= new CourseManager();
        course.setStudentArray(students);
        
        fileIO = new FileIO(course);
        fileIO.writeFiles();        
        // Create a temporary file for student details
        File tempStudentDetails = File
                .createTempFile("TempStudentDetails", ".txt");
        tempStudentDetails.deleteOnExit();       
        // Write the student details to the temporary file
        fileIO.writeStudentDetails(tempStudentDetails.getPath());        
        // Verifying each line in the file is written correctly
        try (BufferedReader reader = new BufferedReader(
                new FileReader(tempStudentDetails))) {
            // Check the header row
            String header = reader.readLine();
            assertEquals("Name, Date of Birth, Gender, Study Mode, Year, "
                    + "Num of Modules, Fee", header);
            // Check the first student's data
            String line;
            line = reader.readLine();
            assertEquals("John Doe, 01/01/2000, M, FT, 1, 6, 6000", line);
            // Check the second student's data
            line = reader.readLine();
            assertEquals("Jane Doe, 06/06/2001, F, PT, 2, 4, 3000", line);            
            // Check that there are no more lines
            assertNull(reader.readLine());
        }
    }
    
    //        students[2] = new Student("Mark Smith", null, 
//                null, "FT", 1, 6, 6000);
//        students[3] = new Student("Kelly Mindy", format.parse("01/10/2004"), 
//                "F", null, 1, 6, 0); 
        
    @Test
    public void testWriteCourseDetails() throws IOException, ParseException {        
        // Create a CourseManager object with some student data        
        Student[] students = new Student[4];
        students[0] = new Student("John Doe", format.parse("01/01/2000"), 
                "M", "FT", 1, 6, 6000);
        students[1] = new Student("Jane Doe", format.parse("06/06/2001"), 
                "F", "PT", 2, 4, 3000);        
        course= new CourseManager();
        course.setStudentArray(students);
        course.setCourseName("Computing Science");
        
        fileIO = new FileIO(course);
        fileIO.writeFiles();
        
        // Create a temporary file for course details
        File tempCourseDetails = File
                .createTempFile("tempCourseFile", ".txt");
        tempCourseDetails.deleteOnExit();
        
        // Write the course details to the temporary file
        fileIO.writeCourseDetails(tempCourseDetails.getPath());
        
        // Verifying each line in the file is written correctly
        try (BufferedReader reader = new BufferedReader(
                new FileReader(tempCourseDetails))) {
            // Check the course name and number of students
            assertEquals("Course Name: Computing Science", reader.readLine());
            assertEquals("Num of Students: 2", reader.readLine());           
            reader.readLine(); // skip empty line
            
            // Check the full-time student percentages
            assertEquals("Number of Full Time Students: 1", reader.readLine());
            assertEquals("Female Percentage: 0.00%", reader.readLine());
            assertEquals("Male Percentage: 100.00%", reader.readLine());
            reader.readLine(); // skip empty line
            
            // Check the part-time student percentages
            assertEquals("Number of Part Time Students: 1", reader.readLine());
            assertEquals("Female Percentage: 100.00%", reader.readLine());
            assertEquals("Male Percentage: 0.00%", reader.readLine());
            // Check that there are no more lines
            assertNull(reader.readLine());
        }
    }
    
    @Test
    public void testWriteStudentDetails_InvalidStudents() throws IOException, 
            ParseException {
        // Create a CourseManager object with some student data        
        Student[] students = new Student[2];
        students[0] = new Student("John Doe", format.parse("01/01/2000"), 
                "M", "FT", 1, 6, 6000);
        students[1] = new Student("Jane Doe", null, "F", "PT", 2, 4, 3000);      
        course= new CourseManager();
        course.setStudentArray(students);
        
        fileIO = new FileIO(course);
        fileIO.writeFiles();        
        // Create a temporary file for student details
        File tempStudentDetails = File
                .createTempFile("TempStudentDetails", ".txt");
        tempStudentDetails.deleteOnExit();       
        // Write the student details to the temporary file
        fileIO.writeStudentDetails(tempStudentDetails.getPath());        
        // Verifying each line in the file is written correctly
        try (BufferedReader reader = new BufferedReader(
                new FileReader(tempStudentDetails))) {
            // Check the header row
            assertEquals("Name, Date of Birth, Gender, Study Mode, Year, "
                    + "Num of Modules, Fee", reader.readLine());
            // Check the first student's data
            assertEquals("John Doe, 01/01/2000, M, FT, 1, 6, 6000",
                    reader.readLine());          
            // Check that there are no more lines
            assertNull(reader.readLine());
        }
    }
}
