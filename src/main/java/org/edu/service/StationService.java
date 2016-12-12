package org.edu.service;

import org.edu.model.Station;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.List;

/**
 * Created by Pavel on 27.11.2016.
 */
@Service
public interface StationService {
    void createStation(Station station, Principal principal);

    Station getStationById(long id);

    boolean updateStation(Station station, Principal principal);

    boolean deleteStation(Station station, Principal principal);

    List<Station> getAllStations();

}
