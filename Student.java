package enrolmentregister;

import java.util.Scanner;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Student {
    private String name;
    private Date dob;
    private String gender;
    private String studyMode;
    private int year;
    private int numModules;
    private int fee;
    private final Scanner input = new Scanner(System.in);

    public Student() {
        name = null;
        dob = null;
        gender = null;
        studyMode = null;
        year = 0;
        numModules = 0;
        fee = 0;
    }

    public Student(String name, Date dob, String gender, 
            String studyMode, int year, int modules, int fee) {
        this.name = name;
        this.dob = dob;
        this.gender = gender;
        this.studyMode = studyMode;
        this.year = year;
        this.numModules = modules;
        this.fee = fee;
    }

    public void setName(String name){ 
        this.name = name;
    }
    
    public void promptForName() {
        // Prompt user to enter the name of the student, 
        // validate it before setting it as name
        
        while (true) {            
            System.out.print("Enter Student's Full Name: ");
            String nameInput = input.nextLine().trim();
            // Validate the name
            String validatedName = validateName(nameInput);
            
            // If the validated name is not null set
            // validated name to the variable name
            if (validatedName != null){
                setName(validatedName);
                return;
            }                     
        }
    }

    public String validateName(String userInput){
        // Validates user input for student's name
        
        String[] nameParts = userInput.split(" ");        
        if (nameParts.length != 2) {
            // If it does not contain two parts, returns null
            System.out.println("Invalid Input.");
            return null;
            
        } else if (!nameParts[0].matches("[a-zA-Z]+") ||
                !nameParts[1].matches("[a-zA-Z]+")) {
            // If it does not contain only alphabets, return null 
            System.out.println("Invalid Input.");
            return null;
            
        } else {
            // First letter of each part is capitalised and returned
            String firstName = nameParts[0].substring(0, 1).toUpperCase()
                    + nameParts[0].substring(1);
            String lastName = nameParts[1].substring(0, 1).toUpperCase() 
                    + nameParts[1].substring(1);
            return String.format("%s %s", firstName, lastName);
        }
    }
    
    public String getName() {
        return name;
    }
    
    public void setDOB(Date dob) {
        this.dob = dob;
    }
    
    public void promptForDOB() {
        // Prompt user to enter the date of birth of student,
        // validate it before setting it as dob
        
        while (true) {
            System.out.print("Enter Date of Birth (dd/mm/yyyy): ");
            String dobInput = input.nextLine().trim();
            // Validate the date of birth
            Date validatedDOB = validateDOB(dobInput);
                        
            if (dobInput.equals("")){
                // If user leaves the date of birth blank
                System.out.println("*Left Blank*");
                return;
            } else if (validatedDOB != null){
                // If the validated dob is not null set 
                // validated dob to the variable dob
                setDOB(validatedDOB);
                return;
            }
        }
    }
    
    /* 
    * Calendar class referenced from JavaTpoint website
    * @see <a href="https://www.javatpoint.com/java-calculate-age"></a>
    */ 
    public Date validateDOB(String userInput) {
        // Validate the date of birth entered by the user
        
        try {
            if (userInput.equals("")) {
                // If input is empty, return null
                return null;
            } 
            
            // Parse the input as a date in the format "dd/MM/yyyy"
            SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
            format.setLenient(false);
            Date date = format.parse(userInput);
            
            // Get the year of birth from the parsed date
            Calendar cal = Calendar.getInstance();
            cal.setTime(date);
            int yearOfBirth = cal.get(Calendar.YEAR);
            
            // Calculates the age of the user based on the current year
            int age = Calendar.getInstance().get(Calendar.YEAR) - yearOfBirth;

            // Set the minimum and maximum age values
            final int MINIMUM_AGE = 16;
            final int MAXIMUM_AGE = 60;
            
            // Check if the age is within the realistic range
            if (age < MINIMUM_AGE || age > MAXIMUM_AGE) {
                throw new ParseException("Date is not "
                        + "within realistic range.", 0);
            }
            // Return the parsed date if it is valid
            return date;
            
        } catch (ParseException e) {
            System.out.println("Invalid Input.");
            return null;
        }
    }
    
    public Date getDOB() {
        return dob;
    }

    public String getDOBStr() {
        // If dob is not null, it is parsed and is
        // returned as a string
        if (dob == null) {
            return null;
        }
        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        return df.format(dob);
    }    
    
    public void setGender(String gender){
        this.gender = gender;
    }
    
    public void promptForGender() {
        // Prompt usr to enter the gender of the student, 
        // validate it before setting it as gender
        
        while (true) {
            System.out.print("Enter Gender (M/F/Leave Blank): ");
            String genderInput = input.nextLine().trim();
            // Validate the gender
            String validatedGender = validateGender(genderInput);
                       
            if (genderInput.equals("")) {
                // If user leaves the gender blank
                System.out.println("*Left Blank*");    
                return;
            } else if (validatedGender != null) {
                // If the validated gender is not null set
                // validated gender to the variable gender 
                setGender(validatedGender);
                return;
            }
        }
    }

    public String validateGender(String genderInput) {
        // Validates the gender entered by the user
        
        try {
            if (genderInput.equals("")) {
                // If the user input is empty, return null
                return null;
                
            } else if (genderInput.equalsIgnoreCase("M")
                    || genderInput.equalsIgnoreCase("F")) {
                // If the user input is 'M' or 'F' is
                // capitalised and returned
                return genderInput.toUpperCase();
                
            } else {
                throw new IllegalArgumentException();
            }
        } catch (IllegalArgumentException e) {
            System.out.println("Invalid Input.");
            return null;
        }
    }
    
    public String getGender() {
        return gender;
    }
    
    public void setStudyMode(String studyMode){
        this.studyMode = studyMode;
    }
    
    public void promptForStudyMode() {
        // Prompt user to enter the study mode of student,
        // validate it before setting it as studyMode
        
        while (true) {
            System.out.print("Enter Study Mode (FT/PT/Leave Blank): ");
            String studyModeInput  = input.nextLine().trim();
            // Validate the study mode
            String validatedStudyMode = validateStudyMode(studyModeInput );
            
            if (studyModeInput.equals("")) {
                // If user leaves the study mode blank
                System.out.println("*Left Blank*");
                return;
            } else if (validatedStudyMode != null) {
                // If the validated study mode is not null set
                // validated study mode to the variable study mode 
                setStudyMode(validatedStudyMode);
                return;
            }
        }
    }
    
    public String validateStudyMode(String studyModeInput ) {
        // Validate the study mode entered by the user
        
        try {
            if (studyModeInput .equals("")) {
                // If the user input is empty, return null
                return null;
            } else if (studyModeInput.equalsIgnoreCase("PT") ||
                    studyModeInput.equalsIgnoreCase("FT")) {
                // If the user input is 'FT' or 'PT' is
                // capitalised and returned
                return studyModeInput.toUpperCase();
                
            } else {
                throw new IllegalArgumentException();
            }
        } catch (IllegalArgumentException e) {
            System.out.println("Invalid Input.");
            return null;  
        }
    }
    
    public String getStudyMode() {
        return studyMode;
    }
    
    public void setYear(int year){
        this.year = year;
    }
    
    public void promptForYear() {
        // Prompt user to enter the year student is in, 
        // validate it before setting it as year
        
        while (true) {
            System.out.print("Enter Year (1-4): ");
            String yearInput = input.nextLine().trim();
            // Validate the year
            int validatedYear = validateYear(yearInput);
            
            if (yearInput.isEmpty()) {
                // If user leaves the year blank
                System.out.println("*Left Blank*");
                return;
            } else if (validatedYear != 0) {
                // If the validated year is not 0 set
                // validated year to the variable year
                setYear(validatedYear);
                return;
            }
        }
    }
    
    public int validateYear(String yearInput) {
        // Validate the year entered by the user
        
        if (yearInput.isEmpty()) {
            // If the user input is empty, return 0
            return 0;
        }
        try {
            // Parse user input to convert it into int
            int tempYear  = Integer.parseInt(yearInput);
            // If the year is not between 1 and 4
            if (tempYear  < 1 || tempYear  > 4) {
                throw new IllegalArgumentException();
            }
            return tempYear; // Return the validated year
            
        } catch (IllegalArgumentException e) {
            System.out.println("Invalid Input.");
            return 0;
        }
    }
    
    public int getYear() {
        return year;
    }
    
    public void setNumModules(int numModules){
        this.numModules = numModules;
    }
    
    public void promptForNumModules() {
        // Prompt user to enter the number of modules the student took,
        // validate it before setting it to numModules
        
        while (true) {
            System.out.print("Enter Number of Modules (1-6): ");
            String modulesInput = input.nextLine().trim();
            // Validated the number of modules
            int validatedModules = validateNumModules(modulesInput);
            
            if (modulesInput.isEmpty()) {
                // If user leaves the num of modules blank
                System.out.println("*Left Blank*");
                return;
                
            } else if (validatedModules != 0) {
                // If the validated num of modules is not 0
                // validated num of modules to the variable
                // num of modules
                setNumModules(validatedModules);
                return;
            }
        }
    }
    
    public int validateNumModules(String modulesInput) {
        // Validate num of modules entered by user
        if (modulesInput.isEmpty()) {
            // If user input is empty, return 0
            return 0;
        }
        try {
            // Parse user input to convert it into int
            int tempNumModules  = Integer.parseInt(modulesInput);
            // If the num of modules is not between 1 and 6
            if (tempNumModules  < 1 || tempNumModules  > 6) {
                throw new IllegalArgumentException();
            } 
            return tempNumModules; // Return the validated num of modules
        } catch (IllegalArgumentException e) {
            System.out.println("Invalid Input.");
            return 0;
        }
    }
    
    public int getNumModules() {
        return numModules;
    }
    
    public void setFee() throws NullPointerException {
        // This method sets the tuition fee of the student based on
        // their study mode, year and number of modules
        int feePerModule = 750;
        int tuitionFee = 0;
        
        // If studyMode is null, fee cannot be calculated
        if (studyMode == null) {
            this.fee = 0;
            return;
        }
        
        switch (studyMode) {
            case "FT":
                // If the studyMode is 'FT' and year is 1, 2 or 4
                if (year == 1 || year == 2 || year == 4) {
                    tuitionFee = 5000;
                
                } else if (year == 3) {
                    // If the student is on placement year
                    tuitionFee = 2500;
                }
                break;
            
            case "PT":
                // If the study mode is 'PT' fee is determined
                // by the num of modules
                tuitionFee = feePerModule * numModules;
                break;
            default:
                tuitionFee = 0;
                break;
        }
        // the fee of the student is set to the value
        // of tuitionFee
        this.fee = tuitionFee;
    }

    public int getFee() {
        return fee;
    }
}