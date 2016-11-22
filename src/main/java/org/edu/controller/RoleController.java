package org.edu.controller;

import org.edu.converter.Converter;
import org.edu.model.Privilege;
import org.edu.model.Role;
import org.edu.model.dto.PrivilegeDto;
import org.edu.model.dto.RoleDto;
import org.edu.service.RoleService;
import org.edu.util.GenericResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/roles")
public class RoleController {

    @Autowired
    RoleService roleService;

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<GenericResponse> createRole(Principal principal, @RequestBody RoleDto roleDto) {
        if (principal == null) {
            return new ResponseEntity<>(new GenericResponse("Please login"), HttpStatus.BAD_GATEWAY);
        }
        roleService.createRole(Converter.convert(roleDto, Role.class), principal);
        return new ResponseEntity<>(new GenericResponse("Successful"), HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<List<RoleDto>> getAllRoles() {
        List<Role> roles = roleService.getAllRoles();
        return new ResponseEntity<>(Converter.convert(roles, RoleDto.class), HttpStatus.OK);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ResponseEntity<RoleDto> getRoleById(@PathVariable("id") long id) {
        Role role = roleService.getRoleById(id);
        return new ResponseEntity<>(Converter.convert(role, RoleDto.class), HttpStatus.OK);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public ResponseEntity<GenericResponse> updateRole(@RequestBody RoleDto roleDto, @PathVariable("id") long id, Principal principal) {
        if (principal == null)
            return new ResponseEntity<>(new GenericResponse("Please login."), HttpStatus.BAD_REQUEST);
        Role role = Converter.convert(roleDto, Role.class);
        role.setId(id);
        boolean isSuccess = roleService.updateRole(role, principal);
        if (isSuccess)
            return new ResponseEntity<>(new GenericResponse("Successful."), HttpStatus.OK);
        return new ResponseEntity<>(new GenericResponse("Fail."), HttpStatus.BAD_REQUEST);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<GenericResponse> deleteRole(@PathVariable("id") long id, Principal principal) {
        if (principal == null)
            return new ResponseEntity<>(new GenericResponse("Please login."), HttpStatus.BAD_REQUEST);
        boolean isSuccess = roleService.removeRole(id, principal);
        if (isSuccess)
            return new ResponseEntity<>(new GenericResponse("Successful."), HttpStatus.OK);
        return new ResponseEntity<>(new GenericResponse("Fail."), HttpStatus.BAD_REQUEST);
    }

    @RequestMapping(value = "/{id}/set_privileges", method = RequestMethod.POST)
    public ResponseEntity<GenericResponse> setPrivileges(@RequestBody PrivilegeDto[] privilegeDtoList, @PathVariable("id") long id, Principal principal) {
        if (principal == null)
            return new ResponseEntity<>(new GenericResponse("Please login."), HttpStatus.BAD_REQUEST);
        List<Privilege> privileges = Converter.convert(Arrays.asList(privilegeDtoList), Privilege.class);
        boolean isSuccess = roleService.setPrivileges(id, privileges);
        if (isSuccess)
            return new ResponseEntity<>(new GenericResponse("Successful."), HttpStatus.OK);
        return new ResponseEntity<>(new GenericResponse("Fail."), HttpStatus.BAD_REQUEST);
    }

    @RequestMapping(value = "/{id}/remove_privileges", method = RequestMethod.POST)
    public ResponseEntity<GenericResponse> removePrivileges(@RequestBody PrivilegeDto[] privilegeDtoList, @PathVariable("id") long id, Principal principal) {
        if (principal == null)
            return new ResponseEntity<>(new GenericResponse("Please login."), HttpStatus.BAD_REQUEST);
        List<Privilege> privileges = Converter.convert(Arrays.asList(privilegeDtoList), Privilege.class);
        boolean isSuccess = roleService.removePrivileges(id, privileges);
        if (isSuccess)
            return new ResponseEntity<>(new GenericResponse("Successful."), HttpStatus.OK);
        return new ResponseEntity<>(new GenericResponse("Fail."), HttpStatus.BAD_REQUEST);
    }

    @RequestMapping(value = "/{id}/privileges", method = RequestMethod.GET)
    public ResponseEntity<List<PrivilegeDto>> getRolePrivileges(@PathVariable("id") long id) {
        List<Privilege> privileges = roleService.getRolePrivileges(id);
        return new ResponseEntity<>(Converter.convert(privileges, PrivilegeDto.class), HttpStatus.OK);
    }

}
