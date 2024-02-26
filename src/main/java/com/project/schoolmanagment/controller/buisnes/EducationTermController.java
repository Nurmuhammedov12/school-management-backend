package com.project.schoolmanagment.controller.buisnes;

import com.project.schoolmanagment.payload.request.buisnes.EducationTermRequest;
import com.project.schoolmanagment.payload.response.businnes.EducationTermResponse;
import com.project.schoolmanagment.payload.response.businnes.ResponseMessage;
import com.project.schoolmanagment.service.buisnes.EducationTermService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/educationTerms")
@RequiredArgsConstructor
public class EducationTermController {

    private final EducationTermService educationTermService;

    @PreAuthorize("hasAnyAuthority('Admin', 'Dean')")
    @PostMapping("/save")
    public ResponseMessage<EducationTermResponse> saveEducationTerm(@RequestBody @Valid
                                                                    EducationTermRequest educationTermRequest){
        return educationTermService.saveEducationTerm(educationTermRequest);
    }
}
