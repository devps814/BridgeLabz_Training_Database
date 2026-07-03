package com.addressbook.dao;

import com.addressbook.model.Contact;
import com.addressbook.config.DatabaseConfig;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class ContactDAOImpl implements ContactDAO {

    @Override
    public void addContact(Contact contact) throws Exception {

        String sql =
                "INSERT INTO contacts(first_name,last_name,phone,email,address) VALUES(?,?,?,?,?)";

        try (
                Connection conn = DatabaseConfig.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)
        ) {

            pstmt.setString(1, contact.getFirstName());
            pstmt.setString(2, contact.getLastName());
            pstmt.setString(3, contact.getPhone());
            pstmt.setString(4, contact.getEmail());
            pstmt.setString(5, contact.getAddress());

            pstmt.executeUpdate();

            System.out.println("Contact Added Successfully");
        }
    }

    @Override
    public List<Contact> getAllContacts() throws Exception {

        List<Contact> contacts = new ArrayList<>();

        String sql =
                "SELECT * FROM contacts ORDER BY last_name, first_name";

        try (
                Connection conn = DatabaseConfig.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql);
                ResultSet rs = pstmt.executeQuery()
        ) {

            while (rs.next()) {

                Contact contact = new Contact(
                        rs.getInt("id"),
                        rs.getString("first_name"),
                        rs.getString("last_name"),
                        rs.getString("phone"),
                        rs.getString("email"),
                        rs.getString("address"),
                        rs.getTimestamp("created_at")
                                .toLocalDateTime()
                );

                contacts.add(contact);
            }
        }

        return contacts;
    }

    @Override
    public List<Contact> searchContacts(String keyword) throws Exception {

        List<Contact> contacts = new ArrayList<>();

        String sql =
                "SELECT * FROM contacts " +
                        "WHERE first_name ILIKE ? " +
                        "OR last_name ILIKE ? " +
                        "OR email ILIKE ?";

        try (
                Connection conn = DatabaseConfig.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)
        ) {

            String search = "%" + keyword + "%";

            pstmt.setString(1, search);
            pstmt.setString(2, search);
            pstmt.setString(3, search);

            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {

                Contact contact = new Contact(
                        rs.getInt("id"),
                        rs.getString("first_name"),
                        rs.getString("last_name"),
                        rs.getString("phone"),
                        rs.getString("email"),
                        rs.getString("address"),
                        rs.getTimestamp("created_at")
                                .toLocalDateTime()
                );

                contacts.add(contact);
            }
        }

        return contacts;
    }

    @Override
    public void updateContact(Contact contact) throws Exception {

        String sql =
                "UPDATE contacts " +
                        "SET first_name=?, last_name=?, phone=?, email=?, address=? " +
                        "WHERE id=?";

        try (
                Connection conn = DatabaseConfig.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)
        ) {

            pstmt.setString(1, contact.getFirstName());
            pstmt.setString(2, contact.getLastName());
            pstmt.setString(3, contact.getPhone());
            pstmt.setString(4, contact.getEmail());
            pstmt.setString(5, contact.getAddress());
            pstmt.setInt(6, contact.getId());

            int rowsAffected = pstmt.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("Contact Updated Successfully");
            } else {
                System.out.println("Contact Not Found");
            }
        }
    }

    @Override
    public void deleteContact(int id) throws Exception {

        String sql = "DELETE FROM contacts WHERE id=?";

        try (
                Connection conn = DatabaseConfig.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)
        ) {

            pstmt.setInt(1, id);

            int rowsAffected = pstmt.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("Contact Deleted Successfully");
            } else {
                System.out.println("Contact Not Found");
            }
        }
    }

    @Override
    public void importContactsBatch(List<Contact> contacts)
            throws Exception {

        String sql =
                "INSERT INTO contacts(first_name,last_name,phone,email,address) " +
                        "VALUES(?,?,?,?,?)";

        Connection conn = null;

        try {

            conn = DatabaseConfig.getConnection();

            conn.setAutoCommit(false);

            try (
                    PreparedStatement pstmt =
                            conn.prepareStatement(sql)
            ) {

                for (Contact contact : contacts) {

                    pstmt.setString(1,
                            contact.getFirstName());

                    pstmt.setString(2,
                            contact.getLastName());

                    pstmt.setString(3,
                            contact.getPhone());

                    pstmt.setString(4,
                            contact.getEmail());

                    pstmt.setString(5,
                            contact.getAddress());

                    pstmt.addBatch();
                }

                pstmt.executeBatch();

                conn.commit();

                System.out.println(
                        "CSV Imported Successfully");
            }

        } catch (Exception e) {

            if (conn != null) {
                conn.rollback();
            }

            System.out.println(
                    "Transaction Rolled Back");

            throw e;

        } finally {

            if (conn != null) {

                conn.setAutoCommit(true);

                conn.close();
            }
        }
    }
}