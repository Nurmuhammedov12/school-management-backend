package com.project.schoolmanagment.service.buisnes;

import com.project.schoolmanagment.entity.concretes.buisnes.Lesson;
import com.project.schoolmanagment.exception.NotFoundExceptions;
import com.project.schoolmanagment.payload.mappers.LessonMapper;
import com.project.schoolmanagment.payload.messages.ErrorMessages;
import com.project.schoolmanagment.payload.messages.SuccesMessages;
import com.project.schoolmanagment.payload.request.buisnes.LessonRequest;
import com.project.schoolmanagment.payload.response.businnes.LessonResponse;
import com.project.schoolmanagment.payload.response.businnes.ResponseMessage;
import com.project.schoolmanagment.repository.buisnes.LessonRepository;
import com.project.schoolmanagment.service.helper.PageableHelper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LessonService {

    private final LessonRepository lessonRepository;
    private final LessonMapper lessonMapper;
    private final PageableHelper pageableHelper;



    public ResponseMessage<LessonResponse> saveLesson(LessonRequest lessonRequest) {
        //lessons must be unique
        isLessonUnique(lessonRequest.getLessonName());
        //map DTO -> entity
        Lesson savedLesson = lessonMapper.mapLessonRequestToLesson(lessonRequest);
        return ResponseMessage.<LessonResponse>builder()
                .object(lessonMapper.mapLessonToLessonResponse(savedLesson))
                .message(SuccesMessages.LESSON_SAVE)
                .httpStatus(HttpStatus.CREATED)
                .build();

    }

    private void isLessonUnique(String lessonName){
        if (lessonRepository.getLessonNameEqualsIgnoreCase(lessonName).isPresent()){
            throw new NotFoundExceptions(String.format(ErrorMessages.ALREADY_CREATED_LESSON_MESSAGE, lessonName));
        }
    }

   Lesson isLessonExistById(Long id){
       return lessonRepository.findById(id).orElseThrow(
               () -> new NotFoundExceptions(String.format(ErrorMessages.NOT_FOUND_LESSON_MESSAGE, id)));
    }

    public ResponseMessage deleteLesson(Long id) {
        isLessonExistById(id);
        lessonRepository.deleteById(id);
        return ResponseMessage.builder()
                .message(SuccesMessages.LESSON_DELETE)
                .httpStatus(HttpStatus.OK)
                .build();
    }

    public ResponseMessage<LessonResponse> getLessonByName(String lessonName) {
        if (lessonRepository.getLessonNameEqualsIgnoreCase(lessonName).isPresent()){
            return ResponseMessage.<LessonResponse>builder()
                    .object(lessonMapper.mapLessonToLessonResponse(lessonRepository.getLessonNameEqualsIgnoreCase(lessonName).get()))
                    .message(SuccesMessages.LESSON_FOUND)
                    .httpStatus(HttpStatus.OK)
                    .build();
        }else {
            return ResponseMessage.<LessonResponse>builder()
                    .message(ErrorMessages.NOT_FOUND_LESSON_MESSAGE)
                    .httpStatus(HttpStatus.NOT_FOUND)
                    .build();
        }
    }


    public Page<LessonResponse> findLessonByPage(int page, int size, String sort, String type) {
        Pageable pageable = pageableHelper.getPageableWithProperties(page, size, sort, type);
        return lessonRepository
                .findAll(pageable)
                .map(lessonMapper::mapLessonToLessonResponse);
    }
}
