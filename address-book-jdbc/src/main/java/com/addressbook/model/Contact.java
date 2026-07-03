package com.addressbook.model;

import java.time.LocalDateTime;

public class Contact {
    private int id;
    private String firstname;
    private String lastname;
    private String phone;
    private String email;
    private String address;
    private LocalDateTime createdAt;

    public Contact(){
    }
    public Contact(String firstname, String lastname, String phone, String email, String address){
        this.firstname = firstname;
        this.lastname = lastname;
        this.email = email;
        this.phone = phone;
        this.address = address;
    }
    public Contact(int id, String firstName, String lastName, String phone, String email, String address, LocalDateTime createdAt) {
        this.id = id;
        this.firstname = firstName;
        this.lastname = lastName;
        this.phone = phone;
        this.email = email;
        this.address = address;
        this.createdAt = createdAt;
    }
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getFirstName() { return firstname; }
    public void setFirstName(String firstName) { this.firstname = firstName; }

    public String getLastName() { return lastname; }
    public void setLastName(String lastName) { this.lastname = lastName; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public String toString() {
        return "Contact{" +
                "id=" + id +
                ", firstName='" + firstname + '\'' +
                ", lastName='" + lastname + '\'' +
                ", phone='" + phone + '\'' +
                ", email='" + email + '\'' +
                ", address='" + address + '\'' +
                ", createdAt=" + createdAt +
                '}';
    }
}
