package org.edu.service;

import org.edu.model.Meeting;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.Principal;
import java.util.List;

@Service
@Transactional
public interface MeetingService {
    void createMeeting(Meeting meeting, Principal principal);

    Meeting getMeetingById(long id);

    boolean updateMeeting(Meeting meeting, Principal principal);

    boolean deleteMeeting(long id, Principal principal);

    List<Meeting> getAllMeetings();

    void subscribeMeeting(Meeting meeting, Principal principal);

    void unSubscribeMeeting(Meeting meeting, Principal principal);

    List<Meeting> getNewMeetings(int num, int pageNum);

    List<Meeting> getUpcomingMeetings(int num, int pageNum);

    List<Meeting> getPopularMeetings(int num, int pageNum);

    List<Meeting> getFeed(int num, int pageNum, String userName);

    long getMeetingsNumber();

    long getUpcomingMeetingsNumber();

    long getPopularMeetingsNumber();

    long getFeedNumber(String userName);

    List<Meeting> getNewCreatedMeetings(String userName);

    void addUserToWaiting(String userName);
    
    void removeUserFromWaiting(String userName);

    List<Meeting> getMeetingBySearchString(String searchString, int pageNum, int num);

    Long getMeetingBySearchStringNum(String searchString, int pageNum, int num);
}
