package enrolmentregister;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.io.IOException;
import java.util.stream.Collectors;

/*
* This class reads student and course details
* to populate the register array for manipulation.
* The class writes student and course details to file.
*/

public class FileIO {
    public final CourseManager course;
    final int MAX_STUDENTS;

    // Constructor initialises FileIO class that takes CourseManager
    // object as parameter and assigns it to class variable
    public FileIO(CourseManager course) {
        this.MAX_STUDENTS = 20;
        this.course = course;
    }

    public void readFiles() throws IOException, ParseException {
        // This method reads files and populates the student array
        readCourseDetails("CourseDetails.txt");
        readStudentDetails("StudentDetails.txt");
    }

    public void readStudentDetails(String fileName)
            throws ParseException, NumberFormatException, IOException {
        // This method reads student data from file and 
        // store it in the student array 

        File file = new File(fileName);
        // Create a temporary Student array of length MAX_STUDENTS
        Student[] tempArray = new Student[MAX_STUDENTS];

        // Check if the file exists. If it does 
        // not exist, create a new file.
        if (!file.exists()){
            file.createNewFile();
        }  
        
        // Read each line in the file
        try (Scanner scanner = new Scanner(file)) {
            // If the file is empty, the temp array is set
            // as the student array in CourseManager object
            if (!scanner.hasNextLine()) {
                course.setStudentArray(tempArray);
                return;
            }
            int index = 0;
            scanner.nextLine(); // Skip the first line
            
            while (scanner.hasNextLine()) {
                // Read student details from each line of text
                String line = scanner.nextLine();              
                // Create a new Student object with these attributes
                Student student = createStudentFromLine(line);
                // Add the new Student to the temporary array
                tempArray[index++] = student;
            }
        } catch (RuntimeException e){
            System.out.println("Error reading student details file");
            throw e;
        }
        // Set the temporary student array as the new student 
        // array in CourseManager object 
        course.setStudentArray(tempArray);
    }
    
    private Student createStudentFromLine(String line) 
            throws ParseException, NumberFormatException {
        // This method creates a Student object from a line of text
        
        // Split each line into parts and store them in an array
            String[] parts = line.split(", ");

            // Store each part in each index as each student attribute
            String name = parts[0];
            Date dob = new SimpleDateFormat("dd/MM/yyyy")
                    .parse(parts[1]);
            String gender = parts[2];
            String studyMode = parts[3];
            int year = Integer.parseInt(parts[4]);
            int modules = Integer.parseInt(parts[5]);
            int fee = Integer.parseInt(parts[6]);

            // Create a new Student object with these attributes
            Student student = new Student(name, dob, gender,
                    studyMode, year, modules, fee);
            
            return student;      
    }

    public void readCourseDetails(String fileName)throws IOException{
        // This method reads course details from a file and sets
        // them in the CourseManager object.

        // Create a new file object
        File file = new File(fileName);

        // If the file does not exist, create a new file
        if (!file.exists()){
            file.createNewFile();
        }

        try (BufferedReader bufferedReader = new BufferedReader
                (new FileReader(file))) {
            // Read the first line of the file 
            String line = bufferedReader.readLine();

            if (line != null && line.startsWith("Course Name: ")) {
                // Set the course name in the CourseManager object
                this.course.setCourseName(line.substring(13).trim());

            } else {
                Scanner input = new Scanner(System.in);
                // Prompt the user to enter the course name
                this.course.promptForCourseName(input);
            }
        } catch (IOException e) {
            System.out.println("Error reading course details file");
            throw e;
        }
    }

    public void writeFiles() throws IOException {
        // This method writes student and course details to text files
        writeStudentDetails("StudentDetails.txt");
        writeCourseDetails("CourseDetails.txt");
    }
    
    /*
    * Stream referenced from Jenkov.com website
    * @see <a href=
     "https://jenkov.com/tutorials/java-functional-programming/streams.html">
     </a>
    */
    public Student[] validateStudents(Student[] students) {
        // This method validates enrolled students and returns 
        // an array of valid students

        // Use streams to filter out null students and 
        // students with invalid details
        List<Student> filteredStudents  = Arrays.stream(students)
                .filter(Objects::nonNull)
                .filter(student -> student.getDOB() != null
                        && student.getGender() != null
                        && student.getStudyMode() != null
                        && student.getYear() != 0
                        && student.getNumModules() != 0)
                .collect(Collectors.toList());

        // Convert the list of valid students to an array
        return filteredStudents.toArray(new Student[0]);
    }

    public void writeStudentDetails(String fileName) throws IOException{
        // Writes valid student data to Student Details file

        // Get the student array from the CourseManager object
        // and filter it for valid students 
        Student[] studentArray = course.getStudentArray();
        Student[] validStudents = validateStudents(studentArray);

        try (BufferedWriter bufferedWriter = new BufferedWriter
                (new FileWriter(fileName))) {

            // Write the header row
            String header = "Name, Date of Birth, Gender, Study Mode, "
                    + "Year, Num of Modules, Fee\n";
            bufferedWriter.write(header);

            // Write each student to the file
            for (Student student : validStudents) {
                if (student != null) {

                    // Construct the student details as a string
                    String line = String.format("%s, %s, %s, %s, %d, %s, %s\n",
                            student.getName(),
                            student.getDOBStr(),
                            student.getGender(),
                            student.getStudyMode(),
                            student.getYear(),
                            student.getNumModules(),
                            student.getFee()
                    );
                    // Write the student details to the file
                    bufferedWriter.write(line);
                }
            }
        } catch (IOException e){
            throw new IOException("Error writing student details to file");
        }
    }

    public void writeCourseDetails(String fileName) throws IOException{
        // Get the student array object and filter it for valid students
        Student[] studentArray = course.getStudentArray();
        Student[] validStudents = validateStudents(studentArray);

        // Group valid students by study mode
        course.groupStudentsByMode(validStudents);

        // Get the necessary data from the CourseManager object
        Student[] ftStudents = course.getFTStudentArray();
        Student[] ptStudents = course.getPTStudentArray();
        String courseName = course.getCourseName();
        int numStudents = course.getNumOfStudents(validStudents);

        // Calculate percentage of genders 
        double ftFemalePercentage = course
                .calcGenderPercent(ftStudents, "F");
        double ftMalePercentage = course
                .calcGenderPercent(ftStudents, "M");
        double ptFemalePercentage = course
                .calcGenderPercent(ptStudents, "F");
        double ptMalePercentage = course
                .calcGenderPercent(ptStudents, "M");

        // Build the string of course details using string formatting
        String courseDetails = String
                .format("Course Name: %s\nNum of Students: %d\n\n"
                + "Number of Full Time Students: %d\n"
                + "Female Percentage: %.2f%%\n"
                + "Male Percentage: %.2f%%\n\n"
                + "Number of Part Time Students: %d\n"
                + "Female Percentage: %.2f%%\n"
                + "Male Percentage: %.2f%%",
                courseName, numStudents, ftStudents.length,
                ftFemalePercentage, ftMalePercentage, ptStudents.length,
                ptFemalePercentage, ptMalePercentage);

        // Write the string of course details to the file   
        try (BufferedWriter bufferedWriter = new BufferedWriter
                (new FileWriter(fileName))) {
            bufferedWriter.write(courseDetails);
        } catch (IOException e){
            throw new IOException("Error writing course details to file");
        }
    }
}
