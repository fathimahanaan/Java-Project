package enrolmentregister;

public class PartTimeStudent extends Student {
    
    // Constructor that initializes a PartTimeStudent object
    // with details from an existing Student object and study mode is PT
    public PartTimeStudent(Student student) {
        super(student.getName(), 
                student.getDOB(), 
                student.getGender(), 
                "PT",
                student.getYear(), 
                student.getNumModules(), 
                student.getFee());
    }
}