package com.womensfoundation.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "get_involved")
public class GetInvolved {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private String state;
    private String aadhaarNumber;
    private String interest;
    private String roleAppliedFor;
    private String partnerType;
    private String organizationName;
    private String location;

    @Lob
    @Column(name = "cv_file", columnDefinition = "LONGBLOB")
    private byte[] cvFile;

    @Lob
    @Column(name = "image_file", columnDefinition = "LONGBLOB")
    private byte[] imageFile;

    @Lob
    @Column(name = "aadhaar_file", columnDefinition = "LONGBLOB")
    private byte[] aadhaarFile;

    private LocalDateTime createdAt = LocalDateTime.now();

    // Getters and Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getAadhaarNumber() {
        return aadhaarNumber;
    }

    public void setAadhaarNumber(String aadhaarNumber) {
        this.aadhaarNumber = aadhaarNumber;
    }

    public String getInterest() {
        return interest;
    }

    public void setInterest(String interest) {
        this.interest = interest;
    }

    public String getRoleAppliedFor() {
        return roleAppliedFor;
    }

    public void setRoleAppliedFor(String roleAppliedFor) {
        this.roleAppliedFor = roleAppliedFor;
    }

    public String getPartnerType() {
        return partnerType;
    }

    public void setPartnerType(String partnerType) {
        this.partnerType = partnerType;
    }

    public byte[] getCvFile() {
        return cvFile;
    }

    public void setCvFile(byte[] cvFile) {
        this.cvFile = cvFile;
    }

    public byte[] getImageFile() {
        return imageFile;
    }

    public void setImageFile(byte[] imageFile) {
        this.imageFile = imageFile;
    }

    public byte[] getAadhaarFile() {
        return aadhaarFile;
    }

    public void setAadhaarFile(byte[] aadhaarFile) {
        this.aadhaarFile = aadhaarFile;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public String getOrganizationName() {
        return organizationName;
    }

    public void setOrganizationName(String organizationName) {
        this.organizationName = organizationName;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}
