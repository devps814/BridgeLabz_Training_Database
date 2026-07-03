package com.lmsapp.dao;
import com.lmsapp.config.DatabaseConnection;
import java.sql.*;

public class AdminDAO {

    public AdminDAO() {
        createTables();
        seedDefaultAdmin();
    }

    private void createTables() {
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement()) {
            stmt.execute("""
                    CREATE TABLE IF NOT EXISTS admins(
                    
                    id SERIAL PRIMARY KEY,
                    name VARCHAR(100),
                    email VARCHAR(100) UNIQUE,
                    password VARCHAR(100)
                    
                    )
                    """);

            stmt.execute("""
                    CREATE TABLE IF NOT EXISTS jobs(
                    
                    id SERIAL PRIMARY KEY,
                    title VARCHAR(100),
                    department VARCHAR(100)
                    
                    )
                    """);

            stmt.execute("""
                    CREATE TABLE IF NOT EXISTS courses(
                    
                    id SERIAL PRIMARY KEY,
                    title VARCHAR(100),
                    description TEXT
                    
                    )
                    """);

            stmt.execute("""
                    CREATE TABLE IF NOT EXISTS interviewers(
                    
                    id SERIAL PRIMARY KEY,
                    name VARCHAR(100),
                    email VARCHAR(100) UNIQUE
                    
                    )
                    """);

        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void seedDefaultAdmin() {
        String checkSql =
                "SELECT * FROM admins WHERE email=?";

        String insertSql =
                """
                INSERT INTO admins
                (name,email,password)
                VALUES(?,?,?)
                """;

        try (Connection conn = DatabaseConnection.getConnection()){
            PreparedStatement check = conn.prepareStatement(checkSql);

            check.setString(1, "admin@gmail.com");

            ResultSet rs = check.executeQuery();
            if (!rs.next()) {
                PreparedStatement insert = conn.prepareStatement(insertSql);

                insert.setString(1, "User Admin");

                insert.setString(2, "admin@gmail.com");

                insert.setString(3, "Dev@123");

                insert.executeUpdate();

                System.out.println("Default Admin Created");
            }

        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean authenticateAdmin(String email, String password){

        String sql =
                """
                SELECT *
                FROM admins
                WHERE email=?
                AND password=?
                """;

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)){
            pstmt.setString(1,email);
            pstmt.setString(2,password);

            ResultSet rs = pstmt.executeQuery();
            return rs.next();

        }
        catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    public void addJob(String title, String department){

        String sql =
                """
                INSERT INTO jobs
                (title,department)
                VALUES(?,?)
                """;

        try (
                Connection conn = DatabaseConnection.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)
        ) {

            pstmt.setString(1,title);
            pstmt.setString(2,department);

            pstmt.executeUpdate();

            System.out.println("Job Added Successfully");

        }
        catch (Exception e) {

            e.printStackTrace();
        }
    }

    public void addCourse(
            String title,
            String description
    ) {

        String sql =
                """
                INSERT INTO courses
                (title,description)
                VALUES(?,?)
                """;

        try (
                Connection conn = DatabaseConnection.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)
        ) {

            pstmt.setString(1,title);
            pstmt.setString(2,description);

            pstmt.executeUpdate();

            System.out.println("Course Added Successfully");

        }
        catch (Exception e) {

            e.printStackTrace();
        }
    }

    public void addInterviewer(
            String name,
            String email
    ) {

        String sql =
                """
                INSERT INTO interviewers
                (name,email)
                VALUES(?,?)
                """;

        try (
                Connection conn = DatabaseConnection.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)
        ) {

            pstmt.setString(1,name);
            pstmt.setString(2,email);

            pstmt.executeUpdate();

            System.out.println("Interviewer Added Successfully");

        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void viewHiringPipeline() {

        String sql =
                """
                SELECT
                id,
                name,
                email,
                status
                FROM candidates
                """;

        try (
                Connection conn = DatabaseConnection.getConnection();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql)
        ) {

            System.out.println("\n===== HIRING PIPELINE =====");

            while (rs.next()) {
                System.out.println(rs.getInt("id")
                                + " | " +
                                rs.getString("name")
                                + " | " +
                                rs.getString("email")
                                + " | " +
                                rs.getString("status")
                );
            }

        }
        catch (Exception e) {
            System.out.println("Candidates table not created yet.");
        }
    }

    public void viewOnboardingSummary() {

        String sql =
                """
                SELECT
                candidate_id,
                status
                FROM onboardings
                """;

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)){

            System.out.println("\n===== ONBOARDING SUMMARY =====");

            while (rs.next()) {

                System.out.println("Candidate : " + rs.getInt("candidate_id")
                                + " | Status : "
                                + rs.getString("status")
                );
            }

        }
        catch (Exception e) {
            System.out.println("Onboarding table not created yet.");
        }
    }
}