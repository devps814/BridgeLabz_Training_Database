package com.lmsapp.model;

public class Candidate {
    private int id;
    private String name;
    private String email;
    private String phone;
    private int jobId;
    private String status;

    public Candidate() {
    }

    public Candidate(String name, String email, String phone, int jobId, String status) {
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.jobId = jobId;
        this.status = status;
    }

    public Candidate(int id, String name, String email, String phone, int jobId, String status){
        this.id = id;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.jobId = jobId;
        this.status = status;
    }
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public int getJobId() { return jobId; }
    public void setJobId(int jobId) { this.jobId = jobId; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}