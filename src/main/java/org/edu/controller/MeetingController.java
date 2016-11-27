package org.edu.controller;

import org.edu.converter.Converter;
import org.edu.model.Meeting;
import org.edu.model.dto.MeetingDto;
import org.edu.service.MeetingService;
import org.edu.util.GenericResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/meetings")
@Transactional
@PropertySource({"classpath:ui.properties"})
public class MeetingController {

    @Value("${meetings.per.page}")
    private int meetingOnPageNum;

    @Autowired
    MeetingService meetingService;

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<GenericResponse> createMeeting(Principal principal, @RequestBody MeetingDto meetingDto) {
        if (principal == null) { //user did not log-in
            return new ResponseEntity<>(new GenericResponse("Please login"), HttpStatus.BAD_GATEWAY);
        }
        meetingDto.setCreateDate(LocalDateTime.now());
        Meeting meeting = Converter.convert(meetingDto, Meeting.class);
        meetingService.createMeeting(meeting, principal);

        return new ResponseEntity<>(new GenericResponse("Successful"), HttpStatus.OK);
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
    public ResponseEntity<MeetingDto> getMeetingById(@PathVariable("id") long id) {
        Meeting meeting = meetingService.getMeetingById(id);
        if (meeting == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(Converter.convert(meeting, MeetingDto.class), HttpStatus.OK);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public ResponseEntity<GenericResponse> updateMeeting(@RequestBody MeetingDto meetingDto, @PathVariable("id")
            long id, Principal principal) {
        if (principal == null) {
            return ResponseEntity.badRequest().body(new GenericResponse("Please login"));
        }

        Meeting meeting = Converter.convert(meetingDto, Meeting.class);
        meeting.setId(id);
        boolean isSuccess = meetingService.updateMeeting(meeting, principal);
        if (isSuccess) {
            return new ResponseEntity<>(new GenericResponse("Successful"), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(new GenericResponse("Failed"), HttpStatus.BAD_REQUEST);
        }
    }

    @RequestMapping(value = "/subscribe", method = RequestMethod.POST)
    public ResponseEntity<GenericResponse> subscribeUser(@RequestBody MeetingDto meetingDto, Principal principal) {
        if (principal == null) {
            return new ResponseEntity<>(new GenericResponse("Please login"), HttpStatus.BAD_REQUEST);
        }
        meetingService.subscribeMeeting(Converter.convert(meetingDto, Meeting.class), principal);
        return new ResponseEntity<>(new GenericResponse("Successful"), HttpStatus.OK);
    }

    @RequestMapping(value = "/unsubscribe", method = RequestMethod.POST)
    public ResponseEntity<GenericResponse> unSubscribeUser(@RequestBody MeetingDto meetingDto, Principal principal) {
        if (principal == null) {
            return new ResponseEntity<>(new GenericResponse("Please login"), HttpStatus.BAD_REQUEST);
        }
        meetingService.unSubscribeMeeting(Converter.convert(meetingDto, Meeting.class), principal);
        return new ResponseEntity<>(new GenericResponse("Successful"), HttpStatus.OK);
    }

    @RequestMapping(value = "/newMeetings", method = RequestMethod.GET)
    public ResponseEntity<List<MeetingDto>> getNewMeetings(@RequestParam(required = false) int pageNum) {
        List<Meeting> meetings = meetingService.getNewMeetings(meetingOnPageNum, pageNum);
        return new ResponseEntity<>(Converter.convert(meetings, MeetingDto.class), HttpStatus.OK);
    }

    @RequestMapping(value = "/upcomingMeetings", method = RequestMethod.GET)
    public ResponseEntity<List<MeetingDto>> getUpcomingMeetings(@RequestParam(required = false) int pageNum) {
        List<Meeting> meetings = meetingService.getUpcomingMeetings(meetingOnPageNum, pageNum);
        return new ResponseEntity<>(Converter.convert(meetings, MeetingDto.class), HttpStatus.OK);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<GenericResponse> deleteMeeting(@PathVariable("id") long id, Principal principal) {
        if (principal == null) {
            return new ResponseEntity<>(new GenericResponse("Please login."), HttpStatus.BAD_REQUEST);
        }
        boolean isSuccess = meetingService.deleteMeeting(id, principal);
        if (isSuccess)
            return new ResponseEntity<>(new GenericResponse("Successful."), HttpStatus.OK);
        return new ResponseEntity<>(new GenericResponse("Fail."), HttpStatus.BAD_REQUEST);
    }
}
