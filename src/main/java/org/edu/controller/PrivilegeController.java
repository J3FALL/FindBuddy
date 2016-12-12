package org.edu.controller;

import org.edu.converter.Converter;
import org.edu.model.Privilege;
import org.edu.model.dto.PrivilegeDto;
import org.edu.service.PrivilegeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/privileges")
public class PrivilegeController {

    @Autowired
    PrivilegeService privilegeService;

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ResponseEntity<PrivilegeDto> getPrivilege(@PathVariable("id") long id) {
        Privilege privilege = privilegeService.getPrivilegeById(id);
        return new ResponseEntity<>(Converter.convert(privilege, PrivilegeDto.class), HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<List<PrivilegeDto>> getAllPrivilege() {
        List<Privilege> privileges = privilegeService.getAllPrivileges();
        return new ResponseEntity<>(Converter.convert(privileges, PrivilegeDto.class), HttpStatus.OK);
    }
}
