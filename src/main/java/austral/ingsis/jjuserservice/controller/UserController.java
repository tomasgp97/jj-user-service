package austral.ingsis.jjuserservice.controller;

import austral.ingsis.jjuserservice.dto.CreateUserDto;
import austral.ingsis.jjuserservice.dto.UpdateUserDto;
import austral.ingsis.jjuserservice.dto.UserDto;
import austral.ingsis.jjuserservice.exception.UserNotFoundException;
import austral.ingsis.jjuserservice.model.UserDao;
import austral.ingsis.jjuserservice.service.UserService;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController()
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping(value = "/register")
    public ResponseEntity<UserDto> registerUser(@RequestBody CreateUserDto createUserDto) {
        UserDto userDto = this.userService.saveUser(this.mapDtoToModel(createUserDto));
        //TODO see how to do 500 response with try catch
        return new ResponseEntity<>(userDto, HttpStatus.CREATED);
    }

    @GetMapping(value = "")
    public ResponseEntity<List<UserDto>> getAllUsers() {
        List<UserDto> users = this.userService.getAllUsers();
        //TODO response headers
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    @PutMapping(value = "")
    public ResponseEntity<UserDto> updateUser(@RequestBody UpdateUserDto dto) {
        try {
            UserDto updated = this.userService.updateUser(dto);
            return ResponseEntity.ok(updated);
        } catch (UserNotFoundException e) {
            System.out.println(e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<UserDto> getUserById(@PathVariable("id") Long id) {
        try {
            UserDto user = this.userService.getUserById(id);
            return new ResponseEntity<>(user, HttpStatus.OK);
        } catch (UserNotFoundException e) {
            System.out.println(e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<String> deleteUserById(@PathVariable Long id){
        try {
            this.userService.deleteUser(id);
            return new ResponseEntity<>("Deleted user of id: " + id + ".", HttpStatus.OK);
        } catch (UserNotFoundException e) {
            System.out.println(e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }


    private UserDao mapDtoToModel(CreateUserDto createUserDto) {
        UserDao user = new UserDao();
        user.setEmail(createUserDto.getEmail());
        user.setUsername(createUserDto.getUsername());
        user.setPassword(createUserDto.getPassword());
        user.setFirstName(createUserDto.getFirstName());
        user.setLastName(createUserDto.getLastName());
        return user;
    }
}
