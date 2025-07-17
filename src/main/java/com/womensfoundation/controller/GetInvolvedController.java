package com.womensfoundation.controller;

import com.womensfoundation.model.GetInvolved;
import com.womensfoundation.service.GetInvolvedService;
import org.springframework.beans.factory.annotation.Autowired;
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
    public GetInvolved handleForm(
            @RequestParam String firstName,
            @RequestParam String lastName,
            @RequestParam String email,
            @RequestParam String phone,
            @RequestParam String state,
            @RequestParam String aadhaarNumber,
            @RequestParam String interest,
            @RequestParam(required = false) String roleAppliedFor,
            @RequestParam(required = false) String partnerType,
            @RequestParam(required = false) MultipartFile cv,
            @RequestParam(required = false) MultipartFile image,
            @RequestParam(required = false) MultipartFile aadhaar
    ) throws IOException {

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

        // Read file content as byte[] and set into the entity
        if (cv != null && !cv.isEmpty()) {
            data.setCvFile(cv.getBytes());
        }
        if (image != null && !image.isEmpty()) {
            data.setImageFile(image.getBytes());
        }
        if (aadhaar != null && !aadhaar.isEmpty()) {
            data.setAadhaarFile(aadhaar.getBytes());
        }

        return service.save(data);
    }
}
