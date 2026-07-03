package com.lmsapp.dao;
import com.lmsapp.config.DatabaseConnection;

import java.sql.*;

public class OnboardingDAO {

    public OnboardingDAO() {

        createTables();
        seedCourses();
    }

    private void createTables() {

        try(
                Connection conn =
                        DatabaseConnection.getConnection();

                Statement stmt =
                        conn.createStatement()
        ){

            stmt.execute("""
                    CREATE TABLE IF NOT EXISTS onboardings(

                    id SERIAL PRIMARY KEY,

                    candidate_id INT
                    REFERENCES candidates(id),

                    status VARCHAR(50)

                    )
                    """);

            stmt.execute("""
                    CREATE TABLE IF NOT EXISTS candidate_courses(

                    candidate_id INT
                    REFERENCES candidates(id),

                    course_id INT
                    REFERENCES courses(id),

                    course_status VARCHAR(50),

                    percentage INT

                    )
                    """);

        }catch (Exception e){

            e.printStackTrace();
        }
    }
    private void seedCourses() {

        try(
                Connection conn =
                        DatabaseConnection.getConnection();

                Statement stmt =
                        conn.createStatement()
        ){

            ResultSet rs =
                    stmt.executeQuery(
                            "SELECT COUNT(*) FROM courses"
                    );

            rs.next();

            if(rs.getInt(1)==0){

                stmt.executeUpdate("""
                    INSERT INTO courses
                    (title,description)
                    VALUES
                    ('Java Advanced JDBC Programming','JDBC Training'),
                    ('Enterprise Git & Version Control','Git Training'),
                    ('Information Security & Compliance','Security Training')
                    """);
            }

        }catch (Exception e){

            e.printStackTrace();
        }
    }
    public boolean hireCandidate(
            int candidateId
    ) {

        Connection conn = null;

        try {

            conn =
                    DatabaseConnection.getConnection();

            conn.setAutoCommit(false);

            PreparedStatement updateCandidate =
                    conn.prepareStatement(
                            """
                            UPDATE candidates
                            SET status='Hired'
                            WHERE id=?
                            """
                    );

            updateCandidate.setInt(
                    1,
                    candidateId
            );

            updateCandidate.executeUpdate();

            PreparedStatement onboarding =
                    conn.prepareStatement(
                            """
                            INSERT INTO onboardings
                            (candidate_id,status)
                            VALUES(?,?)
                            """
                    );

            onboarding.setInt(
                    1,
                    candidateId
            );

            onboarding.setString(
                    2,
                    "In Progress"
            );

            onboarding.executeUpdate();

            Statement stmt =
                    conn.createStatement();

            ResultSet rs =
                    stmt.executeQuery(
                            "SELECT id FROM courses"
                    );

            PreparedStatement mapCourse =
                    conn.prepareStatement(
                            """
                            INSERT INTO candidate_courses
                            (candidate_id,course_id,
                            course_status,percentage)
                            VALUES(?,?,?,?)
                            """
                    );

            while(rs.next()) {

                mapCourse.setInt(
                        1,
                        candidateId
                );

                mapCourse.setInt(
                        2,
                        rs.getInt("id")
                );

                mapCourse.setString(
                        3,
                        "Pending"
                );

                mapCourse.setInt(
                        4,
                        0
                );

                mapCourse.addBatch();
            }

            mapCourse.executeBatch();

            conn.commit();

            return true;

        }
        catch (Exception e){

            try {

                if(conn!=null){

                    conn.rollback();
                }

            }catch (Exception ex){

                ex.printStackTrace();
            }

            e.printStackTrace();
        }

        return false;
    }
    public void updateCourseProgress(
            int candidateId,
            int courseId,
            String status,
            int percentage
    ) {

        Connection conn = null;

        try {

            conn =
                    DatabaseConnection.getConnection();

            conn.setAutoCommit(false);

            PreparedStatement update =
                    conn.prepareStatement(
                            """
                            UPDATE candidate_courses
                            SET course_status=?,
                                percentage=?
                            WHERE candidate_id=?
                            AND course_id=?
                            """
                    );

            update.setString(1,status);
            update.setInt(2,percentage);
            update.setInt(3,candidateId);
            update.setInt(4,courseId);

            update.executeUpdate();

            PreparedStatement check =
                    conn.prepareStatement(
                            """
                            SELECT COUNT(*)
                            FROM candidate_courses
                            WHERE candidate_id=?
                            AND percentage<100
                            """
                    );

            check.setInt(
                    1,
                    candidateId
            );

            ResultSet rs =
                    check.executeQuery();

            rs.next();

            if(rs.getInt(1)==0){

                PreparedStatement complete =
                        conn.prepareStatement(
                                """
                                UPDATE onboardings
                                SET status='Completed'
                                WHERE candidate_id=?
                                """
                        );

                complete.setInt(
                        1,
                        candidateId
                );

                complete.executeUpdate();
            }

            conn.commit();

        }
        catch (Exception e){

            try {

                if(conn!=null){

                    conn.rollback();
                }

            }catch (Exception ex){

                ex.printStackTrace();
            }

            e.printStackTrace();
        }
    }
    public void viewCandidateCourses(
            int candidateId
    ) {

        String sql =
                """
                SELECT
                cc.course_id,
                c.title,
                cc.course_status,
                cc.percentage
    
                FROM candidate_courses cc
    
                JOIN courses c
                ON cc.course_id=c.id
    
                WHERE cc.candidate_id=?
                """;

        try(
                Connection conn =
                        DatabaseConnection.getConnection();

                PreparedStatement pstmt =
                        conn.prepareStatement(sql)
        ){

            pstmt.setInt(1,candidateId);

            ResultSet rs =
                    pstmt.executeQuery();

            while(rs.next()) {

                System.out.println(
                        rs.getInt("course_id")
                                + " | "
                                + rs.getString("title")
                                + " | "
                                + rs.getString("course_status")
                                + " | "
                                + rs.getInt("percentage")
                                + "%"
                );
            }

        }catch (Exception e){

            e.printStackTrace();
        }
    }
}