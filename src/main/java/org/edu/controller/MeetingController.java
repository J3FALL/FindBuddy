package org.edu.controller;

import org.edu.model.Comment;
import org.edu.model.Meeting;
import org.edu.service.MeetingService;
import org.edu.util.GenericResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
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
