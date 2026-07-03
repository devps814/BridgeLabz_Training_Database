package com.lmsapp.dao;
import com.lmsapp.config.DatabaseConnection;
import java.sql.*;

public class CandidateDAO {

    public CandidateDAO() {
        createTables();
        createIndexes();
    }

    private void createTables() {

        try (
                Connection conn =
                        DatabaseConnection.getConnection();

                Statement stmt =
                        conn.createStatement()
        ) {

            stmt.execute("""
                    CREATE TABLE IF NOT EXISTS candidates(
                    
                    id SERIAL PRIMARY KEY,
                    name VARCHAR(100),
                    email VARCHAR(100) UNIQUE,
                    phone VARCHAR(20),
                    job_id INT REFERENCES jobs(id),
                    status VARCHAR(50)
                    
                    )
                    """);

            stmt.execute("""
                    CREATE TABLE IF NOT EXISTS candidate_skills(
                    
                    candidate_id INT
                    REFERENCES candidates(id)
                    ON DELETE CASCADE,
                    
                    skill VARCHAR(100)
                    
                    )
                    """);

            stmt.execute("""
                    CREATE TABLE IF NOT EXISTS candidate_experience(
                    
                    candidate_id INT
                    REFERENCES candidates(id)
                    ON DELETE CASCADE,
                    
                    company_name VARCHAR(100)
                    
                    )
                    """);

            stmt.execute("""
                    CREATE TABLE IF NOT EXISTS interviews(
                    
                    id SERIAL PRIMARY KEY,
                    
                    candidate_id INT
                    REFERENCES candidates(id),
                    
                    interviewer_id INT
                    REFERENCES interviewers(id),
                    
                    date_time VARCHAR(100),
                    
                    score INT,
                    
                    feedback TEXT,
                    
                    status VARCHAR(50)
                    
                    )
                    """);

        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void createIndexes() {
        try (
                Connection conn =
                        DatabaseConnection.getConnection();

                Statement stmt =
                        conn.createStatement()
        ) {

            stmt.execute("""
                CREATE INDEX IF NOT EXISTS
                idx_candidate_email
                ON candidates(email)
                """);

            stmt.execute("""
                CREATE INDEX IF NOT EXISTS
                idx_candidate_status
                ON candidates(status)
                """);

            stmt.execute("""
                CREATE INDEX IF NOT EXISTS
                idx_interviews_candidate
                ON interviews(candidate_id)
                """);

        }
        catch (Exception e) {

            e.printStackTrace();
        }
    }
    public boolean applyJob(
            String name,
            String email,
            String phone,
            int jobId,
            String[] skills,
            String[] companies
    ) {

        Connection conn = null;

        try {

            conn =
                    DatabaseConnection.getConnection();

            conn.setAutoCommit(false);

            String candidateSql =
                    """
                    INSERT INTO candidates
                    (name,email,phone,job_id,status)
                    VALUES(?,?,?,?,?)
                    """;

            PreparedStatement candidateStmt =
                    conn.prepareStatement(
                            candidateSql,
                            Statement.RETURN_GENERATED_KEYS
                    );

            candidateStmt.setString(1,name);
            candidateStmt.setString(2,email);
            candidateStmt.setString(3,phone);
            candidateStmt.setInt(4,jobId);
            candidateStmt.setString(
                    5,
                    "Applied"
            );

            candidateStmt.executeUpdate();

            ResultSet keys =
                    candidateStmt.getGeneratedKeys();

            int candidateId = 0;

            if(keys.next()) {

                candidateId =
                        keys.getInt(1);
            }

            String skillSql =
                    """
                    INSERT INTO candidate_skills
                    (candidate_id,skill)
                    VALUES(?,?)
                    """;

            PreparedStatement skillStmt =
                    conn.prepareStatement(skillSql);

            for(String skill : skills) {

                skillStmt.setInt(
                        1,
                        candidateId
                );

                skillStmt.setString(
                        2,
                        skill
                );

                skillStmt.addBatch();
            }

            skillStmt.executeBatch();

            String expSql =
                    """
                    INSERT INTO candidate_experience
                    (candidate_id,company_name)
                    VALUES(?,?)
                    """;

            PreparedStatement expStmt =
                    conn.prepareStatement(expSql);

            for(String company : companies) {

                expStmt.setInt(
                        1,
                        candidateId
                );

                expStmt.setString(
                        2,
                        company
                );

                expStmt.addBatch();
            }

            expStmt.executeBatch();

            conn.commit();

            return true;

        }
        catch (Exception e) {

            try {

                if(conn != null) {

                    conn.rollback();
                }

            }
            catch (Exception ex) {

                ex.printStackTrace();
            }

            e.printStackTrace();
        }

        return false;
    }
    public void scheduleInterview(
            int candidateId,
            int interviewerId,
            String dateTime
    ) {

        String sql =
                """
                INSERT INTO interviews
                (candidate_id,interviewer_id,date_time,status)
                VALUES(?,?,?,?)
                """;

        try(
                Connection conn =
                        DatabaseConnection.getConnection();

                PreparedStatement pstmt =
                        conn.prepareStatement(sql)
        ){

            pstmt.setInt(1,candidateId);
            pstmt.setInt(2,interviewerId);
            pstmt.setString(3,dateTime);
            pstmt.setString(4,"Scheduled");

            pstmt.executeUpdate();

            System.out.println(
                    "Interview Scheduled"
            );

        }catch (Exception e){

            e.printStackTrace();
        }
    }
    public void submitInterviewScore(
            int interviewId,
            int score,
            String feedback,
            String status
    ) {

        String sql =
                """
                UPDATE interviews
                SET score=?,
                    feedback=?,
                    status=?
                WHERE id=?
                """;

        try(
                Connection conn =
                        DatabaseConnection.getConnection();

                PreparedStatement pstmt =
                        conn.prepareStatement(sql)
        ){

            pstmt.setInt(1,score);
            pstmt.setString(2,feedback);
            pstmt.setString(3,status);
            pstmt.setInt(4,interviewId);

            pstmt.executeUpdate();

            System.out.println(
                    "Interview Updated"
            );

        }catch (Exception e){

            e.printStackTrace();
        }
    }
    public void listOpenJobs() {

        String sql =
                "SELECT * FROM jobs";

        try(
                Connection conn =
                        DatabaseConnection.getConnection();

                Statement stmt =
                        conn.createStatement();

                ResultSet rs =
                        stmt.executeQuery(sql)
        ){

            while(rs.next()) {

                System.out.println(
                        rs.getInt("id")
                                + " | " +
                                rs.getString("title")
                                + " | " +
                                rs.getString("department")
                );
            }

        }catch (Exception e){

            e.printStackTrace();
        }
    }
    public void listAllCandidates() {

        String sql =
                "SELECT * FROM candidates";

        try(
                Connection conn =
                        DatabaseConnection.getConnection();

                Statement stmt =
                        conn.createStatement();

                ResultSet rs =
                        stmt.executeQuery(sql)
        ){

            while(rs.next()) {

                System.out.println(
                        rs.getInt("id")
                                + " | " +
                                rs.getString("name")
                                + " | " +
                                rs.getString("status")
                );
            }

        }catch (Exception e){

            e.printStackTrace();
        }
    }
    public void listInterviewers() {

        String sql =
                "SELECT * FROM interviewers";

        try(
                Connection conn =
                        DatabaseConnection.getConnection();

                Statement stmt =
                        conn.createStatement();

                ResultSet rs =
                        stmt.executeQuery(sql)
        ){

            while(rs.next()) {

                System.out.println(
                        rs.getInt("id")
                                + " | " +
                                rs.getString("name")
                );
            }

        }catch (Exception e){

            e.printStackTrace();
        }
    }
    public void listInterviews() {

        String sql =
                "SELECT * FROM interviews";

        try(
                Connection conn =
                        DatabaseConnection.getConnection();

                Statement stmt =
                        conn.createStatement();

                ResultSet rs =
                        stmt.executeQuery(sql)
        ){

            while(rs.next()) {

                System.out.println(
                        rs.getInt("id")
                                + " | Candidate "
                                + rs.getInt("candidate_id")
                                + " | "
                                + rs.getString("status")
                );
            }

        }catch (Exception e){

            e.printStackTrace();
        }
    }
    public void viewCandidateDetails(
            int candidateId
    ) {

        String sql =
                """
                SELECT *
                FROM candidates
                WHERE id=?
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

            if(rs.next()) {

                System.out.println(
                        rs.getString("name")
                );

                System.out.println(
                        rs.getString("email")
                );

                System.out.println(
                        rs.getString("status")
                );
            }

        }catch (Exception e){

            e.printStackTrace();
        }
    }
}
