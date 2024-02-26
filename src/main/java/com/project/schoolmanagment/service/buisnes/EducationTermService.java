package com.project.schoolmanagment.service.buisnes;

import com.project.schoolmanagment.entity.concretes.buisnes.EducationTerm;
import com.project.schoolmanagment.exception.BadRequest_exception;
import com.project.schoolmanagment.exception.Conflikt;
import com.project.schoolmanagment.exception.NotFoundExceptions;
import com.project.schoolmanagment.payload.mappers.EducationTermMapper;
import com.project.schoolmanagment.payload.messages.ErrorMessages;
import com.project.schoolmanagment.payload.messages.SuccesMessages;
import com.project.schoolmanagment.payload.request.buisnes.EducationTermRequest;
import com.project.schoolmanagment.payload.response.businnes.EducationTermResponse;
import com.project.schoolmanagment.payload.response.businnes.ResponseMessage;
import com.project.schoolmanagment.repository.buisnes.EducationTermRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EducationTermService {

    private final EducationTermRepository educationTermRepository;
    private final EducationTermMapper educationTermMapper;


    public ResponseMessage<EducationTermResponse> saveEducationTerm(EducationTermRequest educationTermRequest) {
        validateEducationTermDates(educationTermRequest);
        EducationTerm educationTerm =educationTermMapper.mapEducationTermRequestToEducationTerm(educationTermRequest);

        EducationTerm savedEducationTerm = educationTermRepository.save(educationTerm);

        return ResponseMessage.<EducationTermResponse>builder()
                .message(SuccesMessages.EDUCATION_TERM_SAVE)
                .object(educationTermMapper.mapEducationTermToEducationTermResponse(savedEducationTerm))
                .httpStatus(HttpStatus.CREATED)
                .build();
    }


    private void  validateEducationTermDates(EducationTermRequest educationTermRequest){
        validateEducationTermsDatesForRequest(educationTermRequest);
        //only one education term can exist in a year
        if(educationTermRepository.existByTermAndYear(
                educationTermRequest.getTerm(),educationTermRequest.getStartDate().getYear())){

            throw new NotFoundExceptions(ErrorMessages.EDUCATION_TERM_IS_ALREADY_EXIST_BY_TERM_AND_YEAR_MESSAGE);
        }

        //validate not to have any conflict with other education terms
        if(educationTermRepository.findByYear(educationTermRequest.getStartDate().getYear())
                .stream()
                .anyMatch(educationTerm ->
                        (educationTerm.getStartDate().equals(educationTermRequest.getStartDate())
                                || (educationTerm.getStartDate().isBefore(educationTermRequest.getStartDate())
                                && educationTerm.getEndDate().isAfter(educationTermRequest.getStartDate()))
                                || (educationTerm.getStartDate().isBefore(educationTermRequest.getEndDate())
                                && educationTerm.getEndDate().isAfter(educationTermRequest.getEndDate()))
                                || (educationTerm.getStartDate().isAfter(educationTermRequest.getStartDate())
                                && educationTerm.getEndDate().isBefore(educationTermRequest.getEndDate()))))) {
            throw new BadRequest_exception(ErrorMessages.EDUCATION_TERM_CONFLICT_MESSAGE);
        }

        //TODO try to write a query for this algorithm

        // 1.start - 1.stop
        // 2.start - 2.stop

        // first case 1. education term is earlier than 2 .--> 1.stop  > 2.start
        // second case  2. education term is earlier than 1. --> 2.stop > 1.start

        //you should find which one is starting first (stop time) then compare it with second (start time)

    }

    private void validateEducationTermsDatesForRequest(EducationTermRequest educationTermRequest){
        //registration > startDate
        if(educationTermRequest.getLastRegistrationDate().isAfter(educationTermRequest.getStartDate())){
            throw new NotFoundExceptions(
                    ErrorMessages.EDUCATION_START_DATE_IS_EARLIER_THAN_LAST_REGISTRATION_DATE
            );
        }
        // endDate > startDate
        if(educationTermRequest.getEndDate().isBefore(educationTermRequest.getStartDate())){
            throw new NotFoundExceptions(ErrorMessages.EDUCATION_END_DATE_IS_EARLIER_THAN_START_DATE);
        }
    }

}
