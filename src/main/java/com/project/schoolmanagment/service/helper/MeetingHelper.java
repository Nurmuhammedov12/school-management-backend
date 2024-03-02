package com.project.schoolmanagment.service.helper;

import com.project.schoolmanagment.entity.concretes.buisnes.Meet;
import com.project.schoolmanagment.entity.concretes.user.User;
import com.project.schoolmanagment.entity.enums.RoleType;
import com.project.schoolmanagment.exception.BadRequest_exception;
import com.project.schoolmanagment.exception.Conflikt;
import com.project.schoolmanagment.exception.NotFoundExceptions;
import com.project.schoolmanagment.payload.messages.ErrorMessages;
import com.project.schoolmanagment.repository.buisnes.MeetingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Component
public class MeetingHelper {
    private final MetodHelper methodHelper;
    private final MeetingRepository meetingRepository;


    public void checkMeetingConflicts(
            List<Long> studentIdList, Long teacherId, LocalDate meetingDate, LocalTime startTime, LocalTime stopTime){

        List<Meet>existingMeetings = new ArrayList<>();
        for (Long id:studentIdList){
            //check student really exist + is a student
            methodHelper.checkRole(methodHelper.idUserExist(id), RoleType.STUDENT);
            existingMeetings.addAll(meetingRepository.findByStudentList_IdEquals(id));
        }
        existingMeetings.addAll(meetingRepository.getByAdvisoryTeacher_IdEquals(teacherId));

        for (Meet meet:existingMeetings){
            LocalTime existingStartTime = meet.getStartTime();
            LocalTime existingStopTime = meet.getStopTime();

            if(meet.getDate().equals(meetingDate) && (
                    (startTime.isAfter(existingStartTime) && startTime.isBefore(existingStopTime)) ||
                            (stopTime.isAfter(existingStartTime) && stopTime.isBefore(existingStopTime)) ||
                            (startTime.isBefore(existingStartTime) && stopTime.isAfter(existingStopTime)) ||
                            (startTime.equals(existingStartTime) || stopTime.equals(existingStopTime))
            )) {
                throw new Conflikt(ErrorMessages.MEET_HOURS_CONFLICT);
            }
        }
    }

    public void isMeetingMatchedWithTeacher(Meet meet, HttpServletRequest httpServletRequest){
        String username = (String) httpServletRequest.getAttribute("username");
        User teacher = methodHelper.loadUserByName(username);
        methodHelper.isAdvisor(teacher);
        if(!meet.getAdvisoryTeacher().getId().equals(teacher.getId())){
            throw new BadRequest_exception(ErrorMessages.NOT_PERMITTED);
        }
    }

    public Meet isMeetingExistById(Long id){
        return meetingRepository.findById(id)
                .orElseThrow(
                        ()->new NotFoundExceptions(String.format(ErrorMessages.MEET_NOT_FOUND_MESSAGE,id)));
    }
}
