package com.project.schoolmanagment.service.validator;

import com.project.schoolmanagment.entity.concretes.buisnes.LessonProgram;
import com.project.schoolmanagment.exception.BadRequest_exception;
import com.project.schoolmanagment.payload.messages.ErrorMessages;
import org.springframework.stereotype.Component;

import java.time.LocalTime;
import java.util.HashSet;
import java.util.Set;

@Component
public class DateTimeValidator {

    public boolean checkTime(LocalTime start, LocalTime stop) {
        return start.isAfter(stop) || start.equals(stop);
    }


    public void checkTimeWithException(LocalTime start, LocalTime stop) {
        if (checkTime(start, stop)) {
            throw new BadRequest_exception(ErrorMessages.TIME_NOT_VALID_MESSAGE);
        }
    }


    public void checkLessonPrograms(Set<LessonProgram> existingLessonPrograms,
                                    Set<LessonProgram> lessonProgramRequest) {
        if (existingLessonPrograms.isEmpty() && lessonProgramRequest.size() > 1) {
            //we have NO lessonPrograms assigned to student
            checkDuplicateLessonPrograms(lessonProgramRequest);
        } else {
            //we have lessonPrograms assigned to student
            checkDuplicateLessonPrograms(lessonProgramRequest);
            checkDuplicateLessonPrograms(existingLessonPrograms, lessonProgramRequest);
        }
    }


    private void checkDuplicateLessonPrograms(Set<LessonProgram> lessonPrograms) {
        Set<String> uniqueLessonProgramDays = new HashSet<>();
        Set<LocalTime> existingLessonProgramStartTimes = new HashSet<>();
        Set<LocalTime> existingLessonProgramStopTimes = new HashSet<>();

        for (LessonProgram lessonProgram : lessonPrograms) {
            String lessonProgramDay = lessonProgram.getDay().name();
            //check if they are the same day
            if (uniqueLessonProgramDays.contains(lessonProgramDay)) {
                //check realated to start time
                for (LocalTime startTime : existingLessonProgramStartTimes) {
                    //if start times are the same
                    if (lessonProgram.getStartTime().equals(startTime)) {
                        throw new BadRequest_exception(ErrorMessages.LESSON_PROGRAM_ALREADY_EXIST);
                    }
                    //check if there is a conflict
                    if (lessonProgram.getStartTime().isBefore(startTime) && lessonProgram.getStopTime().isAfter(startTime)) {
                        throw new BadRequest_exception(ErrorMessages.LESSON_PROGRAM_ALREADY_EXIST);
                    }
                    //if stop times are the same
                    for (LocalTime stopTime : existingLessonProgramStopTimes) {
                        if (lessonProgram.getStartTime().isBefore(stopTime) && lessonProgram.getStopTime().isAfter(stopTime)) {
                            throw new BadRequest_exception(ErrorMessages.LESSON_PROGRAM_ALREADY_EXIST);
                        }
                    }
                }
                uniqueLessonProgramDays.add(lessonProgramDay);
                existingLessonProgramStartTimes.add(lessonProgram.getStartTime());
                existingLessonProgramStopTimes.add(lessonProgram.getStopTime());
            }
        }
    }

    private void checkDuplicateLessonPrograms(Set<LessonProgram> existLessonProgram,
                                              Set<LessonProgram> lessonProgramRequest) {
        for (LessonProgram requestLessonProgram : lessonProgramRequest) {
            String requestLessonProgramDay = requestLessonProgram.getDay().name();
            LocalTime requestStart = requestLessonProgram.getStartTime();
            LocalTime requestStop = requestLessonProgram.getStopTime();

            // Check for any match where the LessonProgram's start or stop time is within existing LessonPrograms
            if (existLessonProgram.stream()
                    .anyMatch(lessonProgram ->
                            lessonProgram.getDay().name().equals(requestLessonProgramDay)
                                    && (lessonProgram.getStartTime().equals(requestStart) // lp1(sali 09:00) / lp2(sali 09:00)
                                    || (lessonProgram.getStartTime().isBefore(requestStart) && lessonProgram.getStopTime().isAfter(requestStart)) // lp1( Sali 09:00 - 11:00) / lp2 ( Sali 10:00- 12:00)
                                    || (lessonProgram.getStartTime().isBefore(requestStop) && lessonProgram.getStopTime().isAfter(requestStop))
                                    || (lessonProgram.getStartTime().isAfter(requestStart) && lessonProgram.getStopTime().isBefore(requestStop))))) {
                throw new BadRequest_exception(ErrorMessages.LESSON_PROGRAM_ALREADY_EXIST);
            }
        }
    }
}