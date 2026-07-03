package com.addressbook;

import com.addressbook.dao.ContactDAO;
import com.addressbook.dao.ContactDAOImpl;
import com.addressbook.model.Contact;
import com.addressbook.util.CSVParser;

import java.util.List;
import java.util.Scanner;

public class AddressBookApp {

    private static final ContactDAO contactDAO = new ContactDAOImpl();
    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {

        while (true) {

            printMenu();

            try {

                System.out.print("Enter your choice: ");

                int choice = Integer.parseInt(scanner.nextLine());

                switch (choice) {

                    case 1:
                        addContact();
                        break;

                    case 2:
                        viewAllContacts();
                        break;

                    case 3:
                        searchContacts();
                        break;

                    case 4:
                        updateContact();
                        break;

                    case 5:
                        deleteContact();
                        break;

                    case 6:
                        importFromCsv();
                        break;

                    case 7:
                        System.out.println("Exiting Application...");
                        scanner.close();
                        return;

                    default:
                        System.out.println("Invalid Choice!");
                }

            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }

            System.out.println();
        }
    }

    private static void printMenu() {

        System.out.println("\n=================================");
        System.out.println("      ADDRESS BOOK SYSTEM");
        System.out.println("=================================");
        System.out.println("1. Add Contact");
        System.out.println("2. View All Contacts");
        System.out.println("3. Search Contacts");
        System.out.println("4. Update Contact");
        System.out.println("5. Delete Contact");
        System.out.println("6. Import Contacts From CSV");
        System.out.println("7. Exit");
        System.out.println("=================================");
    }

    private static void addContact() throws Exception {

        System.out.print("First Name: ");
        String firstName = scanner.nextLine();

        System.out.print("Last Name: ");
        String lastName = scanner.nextLine();

        System.out.print("Phone: ");
        String phone = scanner.nextLine();

        System.out.print("Email: ");
        String email = scanner.nextLine();

        System.out.print("Address: ");
        String address = scanner.nextLine();

        Contact contact = new Contact(
                firstName,
                lastName,
                phone,
                email,
                address
        );

        contactDAO.addContact(contact);

        System.out.println("Contact Added Successfully!");
    }

    private static void viewAllContacts() throws Exception {

        List<Contact> contacts = contactDAO.getAllContacts();

        if (contacts.isEmpty()) {
            System.out.println("No Contacts Found.");
            return;
        }

        contacts.forEach(System.out::println);
    }

    private static void searchContacts() throws Exception {

        System.out.print("Enter Search Keyword: ");
        String keyword = scanner.nextLine();

        List<Contact> contacts =
                contactDAO.searchContacts(keyword);

        if (contacts.isEmpty()) {
            System.out.println("No Matching Contact Found.");
            return;
        }

        contacts.forEach(System.out::println);
    }

    private static void updateContact() throws Exception {

        System.out.print("Enter Contact ID: ");
        int id = Integer.parseInt(scanner.nextLine());

        System.out.print("First Name: ");
        String firstName = scanner.nextLine();

        System.out.print("Last Name: ");
        String lastName = scanner.nextLine();

        System.out.print("Phone: ");
        String phone = scanner.nextLine();

        System.out.print("Email: ");
        String email = scanner.nextLine();

        System.out.print("Address: ");
        String address = scanner.nextLine();

        Contact contact =
                new Contact(
                        firstName,
                        lastName,
                        phone,
                        email,
                        address
                );

        contact.setId(id);

        contactDAO.updateContact(contact);

        System.out.println("Contact Updated Successfully!");
    }

    private static void deleteContact() throws Exception {

        System.out.print("Enter Contact ID: ");
        int id = Integer.parseInt(scanner.nextLine());

        contactDAO.deleteContact(id);

        System.out.println("Contact Deleted Successfully!");
    }

    private static void importFromCsv() throws Exception {

        System.out.print("Enter CSV File Path: ");
        String path = scanner.nextLine();

        List<Contact> contacts =
                CSVParser.parseContacts(path);

        contactDAO.importContactsBatch(contacts);

        System.out.println(
                contacts.size() +
                        " contacts imported successfully."
        );
    }
}