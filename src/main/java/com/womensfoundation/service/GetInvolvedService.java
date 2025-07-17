package com.womensfoundation.service;

import com.womensfoundation.model.GetInvolved;
import com.womensfoundation.repository.GetInvolvedRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GetInvolvedService {

    @Autowired
    private GetInvolvedRepository repository;

    @Autowired
    private EmailService emailService;

    public GetInvolved save(GetInvolved data) {
        GetInvolved saved = repository.save(data);
        emailService.sendGetInvolvedFormToAdmin(saved); // notify admin only
        return saved;
    }
}
