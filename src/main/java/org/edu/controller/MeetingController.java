package org.edu.controller;

import org.edu.model.Meeting;
import org.edu.service.MeetingService;
import org.edu.util.GenericResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
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
        meetingService.createMeeting(meeting, principal);
        return ResponseEntity.accepted().body(new GenericResponse("Successful"));
    }

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<List<Meeting>> getAllComments() {
        return new ResponseEntity<>(meetingService.getAllMeetings(), HttpStatus.OK);
    }
}
