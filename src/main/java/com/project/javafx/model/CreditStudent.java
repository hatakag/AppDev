package com.project.javafx.model;

import com.project.javafx.repository.CourseRepository;

import java.time.LocalDate;
import java.util.Set;

public class CreditStudent extends Student implements Registerable{

    private final int DEFAULT_MAX_CURRENT_CREDITS = 24;

    private CreditMajor creditMajor;
    private int passedMajorCredits;
    private int passedMinorCredits;
    private int currentCredits;
    private int maxCurrentCredits;
    private double GPA;
    private double CPA;
    protected Set<String> passedCourses;

    public CreditStudent(long studentID, String firstName, String lastName, String gender, LocalDate birthday, String phone, String email, String address) {
        super(studentID, firstName, lastName, gender, birthday, phone, email, address, EduSystem.CREDIT);
    }

    public CreditStudent(long studentID, String firstName, String lastName, String gender, LocalDate birthday, String phone, String email, String address, CreditMajor creditMajor) {
        super(studentID, firstName, lastName, gender, birthday, phone, email, address, EduSystem.CREDIT);
        this.creditMajor = creditMajor;
        this.currentCredits = 0;
        this.passedMajorCredits = 0;
        this.passedMinorCredits = 0;
        this.maxCurrentCredits = DEFAULT_MAX_CURRENT_CREDITS;
    }

    // Behavior
    @Override
    public boolean registerCourse(Course course) {
        if (course instanceof CreditCourse) {                                                               //check course
            if (!takenCourses.contains(course)) {                                                           //check takenCourse
                if (currentCredits + ((CreditCourse) course).getCreditHours() <= maxCurrentCredits) {       //check maxCurrentCredits
                    if (((CreditCourse) course).getPrerequisiteCourse().isEmpty()) {                        //check prerequisiteCourse
                        takenCourses.add(course.getCourseCode());                                           //add takenCourse
                        updateCreditTaken(course);                                                          //update currentCredit
                        return true;
                    } else {
                        if (passedCourses.containsAll(((CreditCourse) course).getPrerequisiteCourse())) {   //passed prerequisiteCourse
                            takenCourses.add(course.getCourseCode());                                       //add takenCourse
                            updateCreditTaken(course);                                                      //update currentCredit
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    public void dropCourse(Course course) {
        if (takenCourses.contains(course)) {
            takenCourses.remove(course);
            currentCredits -= ((CreditCourse) course).getCreditHours();
        }
    }

    @Override
    protected StudentResult getGradeResult(Course course) {
        return course.getResult(this.getStudentID());
    }

    @Override
    protected boolean checkAbleToGraduated() {
        if (passedMajorCredits>=creditMajor.getMajorCreditsRequired() && passedMinorCredits>=creditMajor.getMinorCreditsRequired())
            return true;
        else
            return false;
    }

    public void updateCreditTaken(Course course) {
        if (course instanceof CreditCourse) {
            currentCredits += ((CreditCourse) course).getCreditHours();
        }
    }

    public void updatePassedMajorCredits(Course course) {
        if (course instanceof CreditCourse) {
            if (creditMajor.getMajorCatalog().getCourse(course.getCourseCode())!=null) {    //check if course is in majorCatalog
                if (checkPassedCourse(course)) {
                    passedMajorCredits += ((CreditCourse) course).getCreditHours();
                }
            }
        }
    }

    public void updatePassedMinorCredits(Course course) {
        if (course instanceof CreditCourse) {
            if (creditMajor.getMinorCatalog().getCourse(course.getCourseCode())!=null) {    //check if course is in minorCatalog
                if (checkPassedCourse(course)) {
                    passedMinorCredits += ((CreditCourse) course).getCreditHours();
                }
            }
        }
    }

    public void updatePassedCredits(Course course) {
        updatePassedMajorCredits(course);                           //update passedMajorCredits
        updatePassedMinorCredits(course);                           //update passedMinorCredits
    }

    public boolean checkPassedCourse(Course course) {
        StudentResult studentResult=this.getGradeResult(course);        //get studentResult
        if (studentResult.getMidtermPoint()>3.0) {                      //mid term<=3 ->fail
            if (studentResult.getFinalPoint()>3.0) {                    //final term<=3 ->fail
                if (studentResult.getScore()>3.0) return true;          //score <= 3 ->fail
            }
        }
        return false;
    }

    public void updatePassedCourse(Course course) {
        if (checkPassedCourse(course)) {                                //if passed course
            passedCourses.add(course.getCourseCode());                  //add to list passedCourse
            updatePassedCredits(course);                                 //update passedCredits
        }
    }

    public void updatePassedCourseAll() {
        for (String courseCode : takenCourses) {                                            //for each takenCourses
            Course course = CourseRepository.getInstance().findById(courseCode);            //get object Course
            updatePassedCourse(course);                                                     //update passed course
        }
    }

    public void calculateGPA() {
        double sum = 0;
        for (String courseCode : takenCourses) {                                            //for each course in takenCourses
            Course course = CourseRepository.getInstance().findById(courseCode);            //get object Course
            sum += getGradeResult(course).getScore();                                       //sum all scores
        }
        GPA=sum/currentCredits;                                                             //divide by currentCredits
    }

    public void calculateCPA() {
        double sum = 0;
        updatePassedCourseAll();                                                            //update takenCourses passed
        for (String courseCode : passedCourses) {                                           //for each courses in passedCourses
            Course course = CourseRepository.getInstance().findById(courseCode);            //get object Course
            sum += getGradeResult(course).getScore();                                       //sum all scores
        }
        CPA=sum/(passedMinorCredits+passedMajorCredits);                                    //divide by total passed credits
    }

    public void reset() {
        takenCourses.clear();
        currentCredits=0;
    }

    // getter and setter
    public CreditMajor getCreditMajor() {
        return creditMajor;
    }

    public int getCurrentCredits() {
        return currentCredits;
    }

    public void setCreditMajor(CreditMajor creditMajor) {
        this.creditMajor = creditMajor;
    }

    public double getGPA() {
        return GPA;
    }

    public double getCPA() {
        return CPA;
    }

    public int getPassedMajorCredits() {
        return passedMajorCredits;
    }

    public int getPassedMinorCredits() {
        return passedMinorCredits;
    }

    public int getPassedCredits() {
        return passedMajorCredits+passedMinorCredits;
    }

    public int getMaxCurrentCredits() {
        return maxCurrentCredits;
    }

    public Set<String> getPassedCourses() {
        return passedCourses;
    }

    public void registerMajor(CreditMajor creditMajor) {
        this.creditMajor = creditMajor;
        this.currentCredits = 0;
    }
}
