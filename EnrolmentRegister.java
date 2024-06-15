/*
* Student Enrolment Register Application
* The program contains the following classes:
* EnrolmentRegister, CourseManager, Student, FileIO,
* FullTimeStudent, PartTimeStudent
*/

package enrolmentregister;
import java.io.IOException;
import java.text.ParseException;
import java.util.Scanner;

/*
* This class acts as the user interface
* The class reads Student details from file
* Gives user a menu to choose to generate a report,
* add, delete or search for a student
* The new details are written to the text files
*/

public class EnrolmentRegister {
    // Defining the instance variables
    private final CourseManager course;
    private final FileIO fileIO;

    public EnrolmentRegister(CourseManager course, FileIO fileIO) {
        this.course = course;
        this.fileIO = fileIO;
    }

    public static void main(String[] args) throws ParseException, IOException {
        System.out.println("☆☆☆ Student Enrolment Register ☆☆☆");
        
        // Instantiating self class to use instance variables declared
        // herewith and avoid the need to make more methods in the
        // program static
        
        CourseManager course = new CourseManager();
        FileIO fileIO = new FileIO(course);
        EnrolmentRegister register = new EnrolmentRegister(course, fileIO);
        
        // Read the files
        register.fileIO.readFiles();
        
        // Display the menu and get user's choice
        register.runMenu(); 

        // Write details to files and exit program
        register.fileIO.writeFiles();
        System.out.println("\nGoodbye!");
    }
    
    public void runMenu() {        
        // Control if the program should go to the menu
        boolean goToMenu = true;        
        try (Scanner input = new Scanner(System.in)) {
            do {
                // Get user's choice
                MenuOption choice = getUserChoice(input); 
                switch (choice) {
                    case PRINT_REPORT:
                        System.out.println();
                        this.course.printReport();
                        break;
                    case ADD_STUDENT:
                        System.out.println();
                        this.course.addStudentPrompt();
                        break;
                    case DELETE_STUDENT:
                        System.out.println();
                        this.course.deleteStudentPrompt();
                        break;
                    case SEARCH_STUDENT:
                        System.out.println();
                        this.course.searchStudent();
                        break;
                    case EXIT:
                        goToMenu = false;
                        break;
                }   
                // Check if the program should go to menu or exit
                if (goToMenu) {
                    goToMenu = getUserConfirmation(input);
                }
            } while (goToMenu);
        }
    }
    
    /* 
    * Enum to store the different menu options
    * This is referenced from the Geeks For Geeks website
    * @see <a href="https://www.geeksforgeeks.org/enum-in-java/"></a>
    */
    public enum MenuOption {
        PRINT_REPORT("Print report"),
        ADD_STUDENT("Add student"),
        DELETE_STUDENT("Delete student"),
        SEARCH_STUDENT("Search student"),
        EXIT("Exit");
        
        // Instance variable to store the label for 
        // each menu option
        private final String label;

        MenuOption(String label) {
            this.label = label;
        }

        public String getLabel() {
            return label;
        }
    }
    
    /*
    * Formatting is referenced from Oracle JavaDocs
    * @see <a href=
    * "https://docs.oracle.com/javase/tutorial/java/data/numberformat.html"></a>
    */

    public MenuOption getUserChoice(Scanner input) {
        // Method gets the user's menu choice and returns it
        
        System.out.println("\n☆☆☆ Main Menu ☆☆☆");
        
        while (true) {
            // Display each menu option
            for (MenuOption option : MenuOption.values()) {
            System.out.printf("%d) %s%n", option.ordinal() + 1,
                    option.getLabel());
            // %d and %s are placeholders for int and string 
            // respectively. %n is newline character
            }
            
            System.out.print("\nEnter your choice: ");
            String choiceString = input.nextLine();
          
            try {
                // Parse user input as integer and returns 
                // corresponding menu option
                int choice = Integer.parseInt(choiceString);
                return MenuOption.values()[choice - 1];
                
            } catch (NumberFormatException 
                    | ArrayIndexOutOfBoundsException ex) {
                System.out.println("\nInvalid Input. "
                        + "Please enter a valid menu option.\n");
            }
        }        
    }

    /*
    * Class pattern is referenced from Oracle JavaDocs
    * @see <a href=
     "https://docs.oracle.com/javase/7/docs/api/java/util/regex/Pattern.html">
     </a>
    */
    public boolean getUserConfirmation(Scanner input) {
        // Get user confirmation if they want to go to menu
        while (true) {
            System.out.print("\nDo you want to go back to menu? (Yes/No): ");
            String answer = input.nextLine().toLowerCase();
            
            if (answer.matches("^(yes|no)$")) {
                // Return true if answer is "yes", false if "no"
                return answer.equals("yes");
            } else {
                System.out.println("\nInvalid Input.");
            }
        }
    }
}

//    Darryl Philbin, 18/11/2003, M, FT, 3, 6, 2500
//    Michael Scott, 08/04/2002, M, FT, 2, 6, 5000