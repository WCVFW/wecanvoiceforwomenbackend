package com.womensfoundation.controller;

import com.womensfoundation.model.GetInvolved;
import com.womensfoundation.service.GetInvolvedService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/get-involved")
@CrossOrigin(origins = {
    "https://wecanvoiceforwomen.org/",
    "http://localhost:5173" // optional for local testing
})
public class GetInvolvedController {

    @Autowired
    private GetInvolvedService service;

    @PostMapping("/submit")
    public ResponseEntity<?> handleForm(
            @RequestParam String firstName,
            @RequestParam String lastName,
            @RequestParam String email,
            @RequestParam String phone,
            @RequestParam String state,
            @RequestParam String aadhaarNumber,
            @RequestParam String interest,
            @RequestParam(required = false) String roleAppliedFor,
            @RequestParam(required = false) String partnerType,
            @RequestParam(required = false) String organizationName,
            @RequestParam(required = false) MultipartFile cv,
            @RequestParam(required = false) MultipartFile image,
            @RequestParam(required = false) MultipartFile aadhaar
    ) {
        try {
            GetInvolved data = new GetInvolved();
            data.setFirstName(firstName);
            data.setLastName(lastName);
            data.setEmail(email);
            data.setPhone(phone);
            data.setState(state);
            data.setAadhaarNumber(aadhaarNumber);
            data.setInterest(interest);
            data.setRoleAppliedFor(roleAppliedFor);
            data.setPartnerType(partnerType);
            data.setOrganizationName(organizationName);

            if (cv != null && !cv.isEmpty()) {
                data.setCvFile(cv.getBytes());
            }
            if (image != null && !image.isEmpty()) {
                data.setImageFile(image.getBytes());
            }
            if (aadhaar != null && !aadhaar.isEmpty()) {
                data.setAadhaarFile(aadhaar.getBytes());
            }

            GetInvolved saved = service.save(data);
            return ResponseEntity.ok(saved);
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Error reading uploaded files.");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Failed to save form data.");
        }
    }
}
