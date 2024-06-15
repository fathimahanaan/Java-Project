package enrolmentregister;

public class FullTimeStudent extends Student {

    // Constructor that initializes a FullTimeStudent object
    // with details from an existing Student object and study mode is FT
    public FullTimeStudent(Student student) {
        super(student.getName(), 
                student.getDOB(), 
                student.getGender(), 
                "FT",
                student.getYear(), 
                student.getNumModules(), 
                student.getFee());
    }
}
