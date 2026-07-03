package com.lmsapp;
import com.lmsapp.dao.AdminDAO;
import com.lmsapp.dao.CandidateDAO;
import com.lmsapp.dao.OnboardingDAO;

import java.util.Scanner;

public class LMSApplication {

    private static final Scanner scanner =
            new Scanner(System.in);

    private static final AdminDAO adminDAO =
            new AdminDAO();

    private static final CandidateDAO candidateDAO =
            new CandidateDAO();

    private static final OnboardingDAO onboardingDAO =
            new OnboardingDAO();

    public static void main(String[] args) {

        while(true){

            printMainMenu();

            try{

                int choice =
                        Integer.parseInt(
                                scanner.nextLine()
                        );

                switch (choice){

                    case 1:
                        adminPortal();
                        break;

                    case 2:
                        hiringPortal();
                        break;

                    case 3:
                        onboardingPortal();
                        break;

                    case 4:

                        System.out.println(
                                "Exiting..."
                        );

                        return;

                    default:

                        System.out.println(
                                "Invalid Choice"
                        );
                }

            }catch (Exception e){

                System.out.println(
                        "Invalid Input"
                );
            }
        }
    }

    private static void printMainMenu(){

        System.out.println(
                "\n===== LMS SYSTEM ====="
        );

        System.out.println(
                "1. Admin Dashboard"
        );

        System.out.println(
                "2. Candidate Hiring Portal"
        );

        System.out.println(
                "3. Candidate Onboarding Portal"
        );

        System.out.println(
                "4. Exit"
        );
    }

    private static void adminPortal(){

        System.out.print("Email : ");
        String email =
                scanner.nextLine();

        System.out.print("Password : ");
        String password =
                scanner.nextLine();

        boolean valid =
                adminDAO.authenticateAdmin(
                        email,
                        password
                );

        if(!valid){

            System.out.println(
                    "Invalid Login"
            );

            return;
        }

        while(true){

            System.out.println(
                    "\n===== ADMIN ====="
            );

            System.out.println(
                    "1. Add Job"
            );

            System.out.println(
                    "2. Add Course"
            );

            System.out.println(
                    "3. Add Interviewer"
            );

            System.out.println(
                    "4. View Hiring Pipeline"
            );

            System.out.println(
                    "5. View Onboarding Summary"
            );

            System.out.println(
                    "6. Logout"
            );

            int choice =
                    Integer.parseInt(
                            scanner.nextLine()
                    );

            switch(choice){

                case 1:

                    System.out.print(
                            "Job Title : "
                    );

                    String title =
                            scanner.nextLine();

                    System.out.print(
                            "Department : "
                    );

                    String dept =
                            scanner.nextLine();

                    adminDAO.addJob(
                            title,
                            dept
                    );

                    break;

                case 2:

                    System.out.print(
                            "Course Title : "
                    );

                    String course =
                            scanner.nextLine();

                    System.out.print(
                            "Description : "
                    );

                    String desc =
                            scanner.nextLine();

                    adminDAO.addCourse(
                            course,
                            desc
                    );

                    break;

                case 3:

                    System.out.print(
                            "Interviewer Name : "
                    );

                    String name =
                            scanner.nextLine();

                    System.out.print(
                            "Email : "
                    );

                    String interviewerEmail =
                            scanner.nextLine();

                    adminDAO.addInterviewer(
                            name,
                            interviewerEmail
                    );

                    break;

                case 4:

                    adminDAO.viewHiringPipeline();

                    break;

                case 5:

                    adminDAO.viewOnboardingSummary();

                    break;

                case 6:

                    return;
            }
        }
    }

    private static void hiringPortal(){

        while(true){

            System.out.println(
                    "\n===== HIRING ====="
            );

            System.out.println(
                    "1. View Jobs"
            );

            System.out.println(
                    "2. Apply Job"
            );

            System.out.println(
                    "3. List Candidates"
            );

            System.out.println(
                    "4. Candidate Details"
            );

            System.out.println(
                    "5. Interviewers"
            );

            System.out.println(
                    "6. Schedule Interview"
            );

            System.out.println(
                    "7. List Interviews"
            );

            System.out.println(
                    "8. Submit Interview Score"
            );

            System.out.println(
                    "9. Back"
            );

            int choice =
                    Integer.parseInt(
                            scanner.nextLine()
                    );

            switch(choice){

                case 1:

                    candidateDAO.listOpenJobs();

                    break;

                case 2:

                    applyJob();

                    break;

                case 3:

                    candidateDAO.listAllCandidates();

                    break;

                case 4:

                    System.out.print(
                            "Candidate ID : "
                    );

                    int candidateId =
                            Integer.parseInt(
                                    scanner.nextLine()
                            );

                    candidateDAO.viewCandidateDetails(
                            candidateId
                    );

                    break;

                case 5:

                    candidateDAO.listInterviewers();

                    break;

                case 6:

                    scheduleInterview();

                    break;

                case 7:

                    candidateDAO.listInterviews();

                    break;

                case 8:

                    submitInterview();

                    break;

                case 9:

                    return;
            }
        }
    }

    private static void onboardingPortal(){

        while(true){

            System.out.println(
                    "\n===== ONBOARDING ====="
            );

            System.out.println(
                    "1. Hire Candidate"
            );

            System.out.println(
                    "2. View Courses"
            );

            System.out.println(
                    "3. Update Progress"
            );

            System.out.println(
                    "4. Back"
            );

            int choice =
                    Integer.parseInt(
                            scanner.nextLine()
                    );

            switch(choice){

                case 1:

                    System.out.print(
                            "Candidate ID : "
                    );

                    int candidateId =
                            Integer.parseInt(
                                    scanner.nextLine()
                            );

                    onboardingDAO.hireCandidate(
                            candidateId
                    );

                    break;

                case 2:

                    System.out.print(
                            "Candidate ID : "
                    );

                    onboardingDAO.viewCandidateCourses(
                            Integer.parseInt(
                                    scanner.nextLine()
                            )
                    );

                    break;

                case 3:

                    updateProgress();

                    break;

                case 4:

                    return;
            }
        }
    }

    private static void applyJob(){

        try{

            System.out.print("Name : ");
            String name =
                    scanner.nextLine();

            System.out.print("Email : ");
            String email =
                    scanner.nextLine();

            System.out.print("Phone : ");
            String phone =
                    scanner.nextLine();

            System.out.print("Job ID : ");
            int jobId =
                    Integer.parseInt(
                            scanner.nextLine()
                    );

            System.out.print(
                    "Skills(comma separated): "
            );

            String[] skills =
                    scanner.nextLine()
                            .split(",");

            System.out.print(
                    "Companies(comma separated): "
            );

            String[] companies =
                    scanner.nextLine()
                            .split(",");

            candidateDAO.applyJob(
                    name,
                    email,
                    phone,
                    jobId,
                    skills,
                    companies
            );

        }catch (Exception e){

            e.printStackTrace();
        }
    }

    private static void scheduleInterview(){

        System.out.print(
                "Candidate ID : "
        );

        int candidateId =
                Integer.parseInt(
                        scanner.nextLine()
                );

        System.out.print(
                "Interviewer ID : "
        );

        int interviewerId =
                Integer.parseInt(
                        scanner.nextLine()
                );

        System.out.print(
                "Date Time : "
        );

        String date =
                scanner.nextLine();

        candidateDAO.scheduleInterview(
                candidateId,
                interviewerId,
                date
        );
    }

    private static void submitInterview(){

        System.out.print(
                "Interview ID : "
        );

        int interviewId =
                Integer.parseInt(
                        scanner.nextLine()
                );

        System.out.print(
                "Score : "
        );

        int score =
                Integer.parseInt(
                        scanner.nextLine()
                );

        System.out.print(
                "Feedback : "
        );

        String feedback =
                scanner.nextLine();

        System.out.print(
                "Status : "
        );

        String status =
                scanner.nextLine();

        candidateDAO.submitInterviewScore(
                interviewId,
                score,
                feedback,
                status
        );
    }

    private static void updateProgress(){

        System.out.print(
                "Candidate ID : "
        );

        int candidateId =
                Integer.parseInt(
                        scanner.nextLine()
                );

        System.out.print(
                "Course ID : "
        );

        int courseId =
                Integer.parseInt(
                        scanner.nextLine()
                );

        System.out.print(
                "Status : "
        );

        String status =
                scanner.nextLine();

        System.out.print(
                "Percentage : "
        );

        int percentage =
                Integer.parseInt(
                        scanner.nextLine()
                );

        onboardingDAO.updateCourseProgress(
                candidateId,
                courseId,
                status,
                percentage
        );
    }
}