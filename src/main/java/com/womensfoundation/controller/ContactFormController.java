package com.womensfoundation.controller;

import com.womensfoundation.model.GetInvolved;
import com.womensfoundation.repository.GetInvolvedRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/get-involved")
@CrossOrigin(origins = "https://wecanvoiceforwomen.org")
public class GetInvolvedController {

    @Autowired
    private GetInvolvedRepository getInvolvedRepository;

    @PostMapping("/submit")
    public ResponseEntity<String> submitForm(
            @RequestParam String firstName,
            @RequestParam String lastName,
            @RequestParam String email,
            @RequestParam(required = false) String phone,
            @RequestParam(required = false) String state,
            @RequestParam(required = false) String aadhaarNumber,
            @RequestParam String interest,
            @RequestParam(required = false) String roleAppliedFor,
            @RequestParam(required = false) String partnerType,
            @RequestParam(required = false) MultipartFile cvFile,
            @RequestParam(required = false) MultipartFile imageFile,
            @RequestParam(required = false) MultipartFile aadhaarFile
    ) {
        try {
            GetInvolved gi = new GetInvolved();
            gi.setFirstName(firstName);
            gi.setLastName(lastName);
            gi.setEmail(email);
            gi.setPhone(phone);
            gi.setState(state);
            gi.setAadhaarNumber(aadhaarNumber);
            gi.setInterest(interest);
            gi.setRoleAppliedFor(roleAppliedFor);
            gi.setPartnerType(partnerType);
            gi.setCreatedAt(LocalDateTime.now());

            if (cvFile != null && !cvFile.isEmpty()) {
                gi.setCvFile(cvFile.getBytes());
            }
            if (imageFile != null && !imageFile.isEmpty()) {
                gi.setImageFile(imageFile.getBytes());
            }
            if (aadhaarFile != null && !aadhaarFile.isEmpty()) {
                gi.setAadhaarFile(aadhaarFile.getBytes());
            }

            getInvolvedRepository.save(gi);

            return ResponseEntity.ok("Get Involved form submitted successfully!");
        } catch (IOException e) {
            return ResponseEntity.badRequest().body("File upload error: " + e.getMessage());
        }
    }
}
