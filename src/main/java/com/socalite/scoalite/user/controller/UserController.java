package com.socalite.scoalite.user.controller;

import com.socalite.scoalite.user.dto.RegisterUserRequestDTO;
import com.socalite.scoalite.user.dto.RegisterUserResponseDTO;
import com.socalite.scoalite.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(originPatterns = "*")
@RestController
@RequestMapping("users")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping
    public ResponseEntity<RegisterUserResponseDTO> registerUser(@RequestBody RegisterUserRequestDTO request) {
        var response = userService.register(request);
        if (response.getUserId() == -1) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
        return ResponseEntity.ok(response);
    }
}
