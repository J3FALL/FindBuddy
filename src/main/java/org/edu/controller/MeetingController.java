package org.edu.controller;

import org.edu.converter.Converter;
import org.edu.model.Comment;
import org.edu.model.Meeting;
import org.edu.model.dto.MeetingDto;
import org.edu.service.MeetingService;
import org.edu.util.GenericResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.JoinColumn;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import javax.servlet.http.HttpServletResponse;
import java.security.Principal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Pavel on 13.11.2016.
 */
@RestController
@RequestMapping("/meetings")
@Transactional
public class MeetingController {

    @Autowired
    MeetingService meetingService;

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<?> createMeeting(Principal principal, @RequestBody Meeting meeting) {
        if (principal == null) { //user did not log-in
            return ResponseEntity.badRequest().body(new GenericResponse("Please login"));
        }

        meeting.setCreateDate(LocalDateTime.now());
        meeting.setStartDate(LocalDateTime.now());
        meetingService.createMeeting(meeting, principal);
        return ResponseEntity.accepted().body(new GenericResponse("Successful"));
    }

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<List<MeetingDto>> getAllMeetings() {
        List<Meeting> meetings = meetingService.getAllMeetings();
        List<MeetingDto> meetingDtos = new ArrayList<>();
        for (Meeting meeting : meetings) {
            meetingDtos.add(Converter.convert(meeting, MeetingDto.class));
        }

        return new ResponseEntity<>(meetingDtos, HttpStatus.OK);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ResponseEntity<Meeting> getMeetingById(@PathVariable("id") long id) {
        return new ResponseEntity<Meeting>(meetingService.getMeetingById(id), HttpStatus.OK);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public ResponseEntity<?> updateMeeting(HttpServletResponse response, @RequestBody Meeting meeting, @PathVariable("id")
                                                       long id, Principal principal) {
        if (principal == null) {
            return ResponseEntity.badRequest().body(new GenericResponse("Please login"));
        }

        boolean isSucceed = meetingService.updateMeeting(meeting, principal);
        if (isSucceed) {
            return new ResponseEntity<>(new GenericResponse("Successful"), HttpStatus.OK);
        }
        return new ResponseEntity<>(new GenericResponse("Failed"), HttpStatus.BAD_REQUEST);
     }
}
