package enrolmentregister;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;

/*
The CourseManager class represents a manager for a 
course and its students
* Creates array of Student objects of length maxStudents
* Sets Course name and Course Details
* Can Add and Delete Students from array
* Can search for student in array
* Creates array of FullTimeStudents and PartTimeStudent objects
* Gets percentage of genders in each study mode
* Generates report of CourseManager details
*/

public class CourseManager {
    
    private String courseName;
    private Student[] studentArray;
    private PartTimeStudent[] partTimeStudents;
    private FullTimeStudent[] fullTimeStudents;
    
    private final Scanner input = new Scanner(System.in);
    
    // Sets the student array for the particular course
    public void setStudentArray(Student[] studentArray) {
        this.studentArray = studentArray;
    }
    
    public void setCourseName(String name) {
        courseName = name;
    }
    
    public void promptForCourseName(Scanner input){
        // If the Course name is not available in text file
        // user is prompted to enter the Course name
        System.out.print("Enter Course Name: ");
        String userInput = input.nextLine().trim();
        setCourseName(userInput);
    }
    
    public Student[] getStudentArray(){
        return studentArray;
    }
    
    public PartTimeStudent[] getPTStudentArray() {
        return partTimeStudents;
    }
    
    public FullTimeStudent[] getFTStudentArray() {
        return fullTimeStudents;
    }
    
    public String getCourseName() {
        return courseName;
    }

    public void groupStudentsByMode(Student[] studentArray) { 
        // Populates getPTStudentArray and getFTStudentArray arrays 
        // with the respective students
        
        // Initializes empty lists to store FT and PT students
        List<FullTimeStudent> ftStudents = new ArrayList<>();
        List<PartTimeStudent> ptStudents = new ArrayList<>();
        
        for (Student student : studentArray) {
            if (student == null){
                continue; // Skip any null values
            }
            
            String studyMode = student.getStudyMode();
            if (studyMode == null) {
                continue; // Skip any null study modes
            }
            
            // Add student to appropriate list based on study mode
            switch (studyMode) {
                case "PT":
                    ptStudents.add(new PartTimeStudent(student));
                    break;
                case "FT":
                    ftStudents.add(new FullTimeStudent(student));
                    break;
                default:
                    break;
            }
        }
        
        // Convert the lists to array and assign to class variables
        partTimeStudents = ptStudents.toArray(new PartTimeStudent[0]);
        fullTimeStudents = ftStudents.toArray(new FullTimeStudent[0]);
    }
    
    public boolean addStudentPrompt() {
        // This method is invoked when user wants to add a student
        // to the array.
        
        // Get the next available index in the array 
        int nextAvailableIndex  = getNumOfStudents(studentArray);
        
        // If index is greater than or equal to the 
        // length of the array, array has no space
        if (nextAvailableIndex >= studentArray.length) {
            System.out.println("Cannot Add Student - Enrolment register "
                    + "has reached full capacity.");
            return false;
        }     
        System.out.println("☆☆☆ Adding New Student ☆☆☆\n");
        // Create a new student object
        Student student = createStudentObject();
        // Add the new student object to the array
        addStudent(student, nextAvailableIndex );
        
        // Print the details of the new student that has been added
        printStudentDetails(studentArray[nextAvailableIndex]);
        
        // Check if the student details are valid
        if (!isStudentObjectValid(nextAvailableIndex )) {
            System.out.println("\nWarning: Student Details are invalid and " 
                    + "will NOT be saved beyond current session.");
        } else {
            System.out.println("\nStudent added successfully.");  
        }
        return true;
    }

    public void addStudent(Student student, int nextAvailableIndex ) {
        // Adds the given student to the student array
        // at the specified index 
        studentArray[nextAvailableIndex] = student;
    }
    
    public int getNumOfStudents(Student[] array) {
        // This method returns the number of non-null
        // students in the array
        int count = 0;
        
        for (Student student : array){
            if (student != null){
                count++; // Increment the count of non-null students
            }
        }
        return count;
    }

    private Student createStudentObject() {
        // Creates and returns a new student object
        Student student = new Student();
        
        student.promptForName();
        student.promptForDOB();
        student.promptForGender();
        student.promptForStudyMode();
        student.promptForYear();
        student.promptForNumModules();
        student.setFee();

        return student;
    }

    private boolean isStudentObjectValid(int nextAvailableIndex ) {
        // Checks if the student object at the specified 
        // index only has valid details (not null or 0)
        
        Student student = studentArray[nextAvailableIndex ];
        return student.getDOB() != null 
                && student.getGender() != null 
                && student.getStudyMode() != null 
                && student.getYear() != 0 
                && student.getNumModules() != 0;
    }

    public boolean deleteStudentPrompt() {
        // This method is invoked when user wants to delete
        // a student from the array. It gets the name of the
        // student to be deleted.
        
        // Checks if the array of students is not empty
        if (checkArrayNotEmpty(studentArray)) {
            System.out.println("☆☆☆ Deleting Student ☆☆☆");
            String name;
            
            // Prompt user to enter the name of the student to delete
            do {
                System.out.print("Enter name of student to delete: ");
                name = input.nextLine().trim();
            } while (isNameInvalid(name));
            
            // Find the index of the student to delete
            int indexOfStudent = findStudentIndex(name);
            
            // If the student exists, delete the student
            if (indexOfStudent != -1) {
                deleteStudent(indexOfStudent);
                
                System.out.println("\nStudent deleted successfully.");
                return true;
                
            } else {
                System.out.println("\nStudent not found.");
                return false;
            }
        } else {
            System.out.println("Cannot Delete Student - Enrolment"
                    + " Register is empty.");
            return false;
        }
    }
    
    public void deleteStudent(int indexOfStudent) {
        // Deletes the student at the specified index in the array
        
        for (int i = indexOfStudent; i < studentArray.length - 1; i++){
            // Moves all the students right of the deleted students
            // to the left in the array
            studentArray[i] = studentArray[i + 1];
        }
        // Sets the last student in array as null to
        // avoid storing duplicate values
        studentArray[studentArray.length - 1] = null; 
    }
    
    public int findStudentIndex(String name) {
        // Finds the index of the student with the given name
        int indexOfStudent = -1;
        
        for (int i = 0; i < studentArray.length; i++) {
            if (studentArray[i] != null) {
                
                // If student is not null, get name and 
                // check if both names are equal
                String studentName = studentArray[i].getName();
                if (name.equalsIgnoreCase(studentName)) {
                    indexOfStudent = i;
                    break;
                }
            }
        }
        return indexOfStudent;
    }

    private boolean checkArrayNotEmpty(Student[] studentArray) {
        // Checks if the array is not empty         
        for (Student student : studentArray) {
            if (student != null) {
                // if any student is not null, array
                // is not empty
                return true;
            }
        }
        return false;
    }

    private boolean isNameInvalid(String name) {
        // Split the name into two parts and checks if each part
        // only has alphabets in them.
        String [] parts = name.split(" ");
        if (parts.length != 2 || !parts[0].matches("[a-zA-Z]+")
                || !parts[1].matches("[a-zA-Z]+")) {
            System.out.println("\nInvalid Input.\n");
            return true;
        }
        return false;
    }

    public boolean searchStudent() {
        // This method is invoked when user wants to search for a student
        
        boolean found = false;
        String name;
        
        // Check if the array is not empty
        if (checkArrayNotEmpty(studentArray)) { 
            System.out.println("☆☆☆ Searching for Student ☆☆☆");
            
            do {
                System.out.print("Enter full name of student: ");
                // Reads user input and trims trailing spaces
                name = input.nextLine().trim();
            } while (isNameInvalid(name));
            // Keeps asking until a valid name is entered
            
            // Get the index of student from the array using the name
            int indexOfStudent = findStudentIndex(name);

            if (indexOfStudent != -1) { 
                // if the index of the student is found print details
                printStudentDetails(studentArray[indexOfStudent]);
                found = true;
            } else {
                System.out.println("\nStudent not found.");
            }          
        } else {
            System.out.println("Cannot Search - Enrolment Register is empty.");
        }
        return found;
    }

    public void printStudentDetails(Student student) {
        // Prints the details of the student
        
        String dob = student.getDOBStr();
        // get the date of birth. If it null convert it 
        // into '--' to display in the report
        if (dob == null){dob = "--";}

        String gender = student.getGender(); // get the gender
        if (gender == null) {gender = "--";}

        String studyMode = student.getStudyMode(); // get the study mode
        if (studyMode == null) {studyMode = "--";}

        String year = String.valueOf(student.getYear()); 
        // get the value of year and convert it into a string
        if (year.equals("0")){year = "--";}

        String modules = String.valueOf(student.getNumModules()); 
        // get the value of number of modules and convert into string 
        if (modules.equals("0")){modules = "--";}

        String fee = String.valueOf(student.getFee());
        // get the value of the fee and convert into string
        if (fee.equals("0")){fee = "--";}

        System.out.println();
        //Print the header for the student details
        System.out.printf("%-20s%-16s%-9s%-13s%-7s%-10s%-13s\n"
                ,"Name", "Date of Birth"
                ,"Gender", "Study Mode", "Year", "Modules"
                ,"Tuition Fee(£)");
        // Print the separator line
        System.out.printf("%-20s%-16s%-9s%-13s%-7s%-10s%-13s\n"
                ,"------------------", "-------------"
                ,"------", "----------", "----", "-------"
                ,"-----------");
        // Print the student details
        System.out.printf("%-20s%-16s%-9s%-13s%-7s%-10s%-13s\n"
                ,student.getName(), dob, gender
                ,studyMode, year, modules, fee);
    }
    
    public double calcGenderPercent(Student[] array, String gender){
        // This method calculates the percentage of  
        // students of the given gender in the array
        
        // Check if the array is not empty
        if (checkArrayNotEmpty(array)) {
            // num of valid students 
            double totalStudents = countValidGenders(array); 
            double numGenderMatch = 0.0; // num of students that match genders
            
            
            // Iterate through each student and get the gender
            for (Student student : array) {
                String studentGender = null;
                if (student != null) {
                    studentGender = student.getGender();
                }
                
                // If the gender matches the count of students of
                // the given gender is incremented
                if (Objects.equals(studentGender, gender)) {
                    numGenderMatch++;
                }
            }
                            
            // Calculates the percentage of students of the given gender
            double percentage = (numGenderMatch / totalStudents) * 100.0;
            // Return the percentage rounded to one decimal places
            return Math.round(percentage * 10.0) / 10.0;
        }
        // Return 0.0 if array is empty or there are no valid 
        return 0.0;
    }
    
    public void printReport() {       
        // Groups the students by study mode in respective arrays
        groupStudentsByMode(studentArray);        
        // Calculate the percentage of female and male students
        double ftFemalePercentage = calcGenderPercent(fullTimeStudents, "F");
        double ftMalePercentage = calcGenderPercent(fullTimeStudents, "M");
        double ptFemalePercentage = calcGenderPercent(partTimeStudents, "F");
        double ptMalePercentage = calcGenderPercent(partTimeStudents, "M");
        
        // Get the number of students with non-null 
        // gender in each study mode
        int numOfFT = countValidGenders(fullTimeStudents);
        int numOfPT = countValidGenders(partTimeStudents);
        
        // Print the course details report
        System.out.println("☆☆☆ Course Details Report ☆☆☆");
        System.out.println("\nCourse Name: "+ getCourseName());
        System.out.println("Total Num of Students: " 
                + getNumOfStudents(studentArray));
        
        // Print num of valid students in each study mode
        System.out.println("\nTotal Num of Full-Time Students: "
                + fullTimeStudents.length);        
        System.out.println("Total Num of Part-Time Students: "
                + partTimeStudents.length);        

        System.out.printf("\n%-11s  %-16s  %-10s  %-8s\n"
                ,"Study Mode", "Num of Students", "Female", "Male");        

        System.out.printf("%-11s  %-16s  %-10s  %-8s\n"
                ,"----------", "---------------", "---------", "-------");
        // Print the report for full-time students
        System.out.printf("%-11s  %-16s  %-10s  %-8s\n", "Full-Time"
                , numOfFT, ftFemalePercentage+"%", ftMalePercentage+"%");       
        // Print the report for part-time students
        System.out.printf("%-11s  %-16s  %-10s  %-8s\n", "Part-Time"
                , numOfPT, ptFemalePercentage+"%", ptMalePercentage+"%");       
        
        // let user know percentage is calculated
        // based only on valid students
        if (fullTimeStudents.length != numOfFT 
                || partTimeStudents.length != numOfPT) {
            System.out.println("\nPercentage of genders in each study mode"
                    + " is calculated based only on valid students.");
        }
    }   
    
    public int countValidGenders(Student[] array) {
        // This method counts the number of students that have 
        // non-null(valid) genders 
        int numOfStudents = 0;
        for (Student student : array) {
            if (student != null && student.getGender() != null) {
                numOfStudents++;
            }
        }
        return numOfStudents;
    }
}