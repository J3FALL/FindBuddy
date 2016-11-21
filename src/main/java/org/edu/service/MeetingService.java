package org.edu.service;

import org.edu.model.Meeting;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.Principal;
import java.util.List;

/**
 * Created by Pavel on 13.11.2016.
 */
@Service
@Transactional
public interface MeetingService {
    void createMeeting(Meeting meeting, Principal principal);

    Meeting getMeetingById(long id);

    boolean updateMeeting(Meeting meeting, Principal principal);

    void removeMeeting(long id);

    List<Meeting> getAllMeetings();

    void subscribeMeeting(Meeting meeting, Principal principal);

    void unSubscribeMeeting(Meeting meeting, Principal principal);
}
