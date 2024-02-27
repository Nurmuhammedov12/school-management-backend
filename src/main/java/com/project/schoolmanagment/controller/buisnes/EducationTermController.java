package com.project.schoolmanagment.controller.buisnes;

import com.project.schoolmanagment.payload.request.buisnes.EducationTermRequest;
import com.project.schoolmanagment.payload.response.businnes.EducationTermResponse;
import com.project.schoolmanagment.payload.response.businnes.ResponseMessage;
import com.project.schoolmanagment.service.buisnes.EducationTermService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

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

    //TODO
    @PreAuthorize("hasAnyAuthority('Admin', 'Dean', 'ViceDean', 'Teacher')")
    @GetMapping("/getAll")
    public List<EducationTermResponse> getAllEducationTerms(){
        return null;
    }

    @PreAuthorize("hasAnyAuthority('Admin', 'Dean', 'ViceDean', 'Teacher')")
    @GetMapping("/{id}")
    public EducationTermResponse getEducationTermById(@PathVariable Long id){
        return educationTermService.findById(id);
    }


    @PreAuthorize("hasAnyAuthority('Admin', 'Dean', 'ViceDean', 'Teacher')")
    @GetMapping("/getAllEducationTermsByPage")
    public Page<EducationTermResponse> getAllEducationTermsByPage(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size,
            @RequestParam(value = "sort", defaultValue = "startDate") String sort,
            @RequestParam(value = "type", defaultValue = "desc") String type){
    return educationTermService.getAllByPage(page,size,sort,type);
    }


}
