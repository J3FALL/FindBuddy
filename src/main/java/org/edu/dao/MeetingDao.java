package org.edu.dao;

import org.edu.dao.common.IOperations;
import org.edu.model.Meeting;

import java.util.List;

public interface MeetingDao extends IOperations<Meeting> {

    List<Meeting> findNewMeetings(int num, int pageNum);

    List<Meeting> findUpcomingMeetings(int num, int pageNum);

    List<Meeting> findPopularMeetings(int num, int pageNum);

    List<Meeting> findSubscribedCategoryMeetings(int num, int pageNum, String userName);

    long findMeetingsNumber();

    long findUpcomingMeetingsNumber();

    long findPopularMeetingsNumber();

    long findSubscribedCategoryMeetingsNumber(String userName);

    List<Meeting> findMeetingBySearchString(String searchString, int pageNum, int num);

    Long findMeetingBySearchStringNum(String searchString, int pageNum, int num);

    List<Meeting> findMeetingByCategory(long categoryId, int pageNum, int num);

    Long findMeetingByCategoryNum(long categoryId, int pageNum, int num);

}
