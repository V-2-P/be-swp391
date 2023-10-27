package com.v2p.swp391.application.service;

import com.v2p.swp391.application.model.User;
import com.v2p.swp391.application.request.PersonalUpdateRequest;
import com.v2p.swp391.application.request.UserUpdateRequest;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface PersonalService {
    User getPersonalInformation();
    User updatePersonalInformation(PersonalUpdateRequest update);
    User uploadAvatar(MultipartFile imageFile) throws IOException;
}
