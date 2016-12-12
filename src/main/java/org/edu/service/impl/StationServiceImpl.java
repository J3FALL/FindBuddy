package org.edu.service.impl;

import org.edu.dao.StationDao;
import org.edu.model.Station;
import org.edu.service.StationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.Principal;
import java.util.List;

/**
 * Created by Pavel on 27.11.2016.
 */

@Service
@Transactional
public class StationServiceImpl implements StationService {

    @Autowired
    StationDao stationDao;

    @Override
    public void createStation(Station station, Principal principal) {

    }

    @Override
    public Station getStationById(long id) {
        return null;
    }

    @Override
    public boolean updateStation(Station station, Principal principal) {
        return false;
    }

    @Override
    public boolean deleteStation(Station station, Principal principal) {
        return false;
    }

    @Override
    public List<Station> getAllStations() {
        return stationDao.findAll();
    }
}
