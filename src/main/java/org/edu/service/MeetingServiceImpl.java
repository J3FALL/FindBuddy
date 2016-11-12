package org.edu.service;

import org.edu.model.Meeting;

import java.security.Principal;

/**
 * Created by Pavel on 13.11.2016.
 */
public class MeetingServiceImpl implements MeetingService {
    @Override
    public void createMeeting(Meeting meeting, Principal principal) {

    }

    @Override
    public Meeting getMeetingById(long id) {
        return null;
    }

    @Override
    public boolean updateMeeting(Meeting meeting, Principal principal) {
        return false;
    }

    @Override
    public void removeMeeting(long id) {

    }
}
