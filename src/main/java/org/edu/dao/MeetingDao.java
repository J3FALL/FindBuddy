package org.edu.dao;

import org.edu.dao.common.IOperations;
import org.edu.model.Meeting;

import java.util.List;

public interface MeetingDao extends IOperations<Meeting> {

    List<Meeting> findNewMeetings(int num, int pageNum);

    List<Meeting> findUpcomingMeetings(int num, int pageNum);

    List<Meeting> findPopularMeetings(int num, int pageNum);

}
