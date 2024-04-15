package africa.semicolon.controller;

import africa.semicolon.dto.requests.*;
import africa.semicolon.dto.responses.*;
import africa.semicolon.exceptions.NaijaGossipsExceptions;
import africa.semicolon.services.UserServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpStatus.*;

@RestController
@RequestMapping("/api/Blogspot")
public class UserControllers {

    @Autowired
    private UserServices userServices;

    @PostMapping("/sign_up")
    public ResponseEntity<?> register(@RequestBody UserRegisterRequest userRegisterRequest){
        try {
            var response = userServices.register(userRegisterRequest);
            return new ResponseEntity<>(new UserApiResponse(true, response), CREATED);
        } catch(NaijaGossipsExceptions e){
            return new ResponseEntity<>(new UserApiResponse(false, e.getMessage()), BAD_REQUEST);
        }
    }

    @PostMapping("/sign_in")
    public ResponseEntity<?> login(@RequestBody UserLoginRequest userLoginRequest){
        try {
            var response = userServices.login(userLoginRequest);
            return new ResponseEntity<>(new UserApiResponse(true, response), ACCEPTED);
        } catch (NaijaGossipsExceptions e){
            return new ResponseEntity<>(new UserApiResponse(false, e.getMessage()), FORBIDDEN);
        }
    }

    @PostMapping("/create_post")
    public ResponseEntity<?> createPost(@RequestBody CreatePostRequest createPostRequest){
        try {
            var response = userServices.createPost(createPostRequest);
            return new ResponseEntity<>(new UserApiResponse(true, response), CREATED);
        } catch (NaijaGossipsExceptions e){
            return new ResponseEntity<>(new UserApiResponse(false, e.getMessage()), BAD_REQUEST);
        }
    }

    @DeleteMapping("/delete_post")
    public ResponseEntity<?> deletePost(@RequestBody DeletePostRequest deletePostRequest){
        try {
            var response = userServices.deletePost(deletePostRequest);
            return new ResponseEntity<>(new UserApiResponse(true, response), ACCEPTED);
        }catch (NaijaGossipsExceptions e) {
            return new ResponseEntity<>(new UserApiResponse(false, e.getMessage()), BAD_REQUEST);
        }
    }

    @PostMapping("/sign_out")
    public ResponseEntity<?> logout(@RequestBody UserLogoutRequest userLogoutRequest){
        try {
            var response = userServices.logout(userLogoutRequest);
            return new ResponseEntity<>(new UserApiResponse(true, response), ACCEPTED);
        } catch (NaijaGossipsExceptions e){
            return new ResponseEntity<>(new UserApiResponse(false, e.getMessage()), BAD_REQUEST);
        }
    }

    @GetMapping("/all_posts/{username}")
    public ResponseEntity<?> viewAllPosts(@PathVariable("username") String username){
        try {
            var response = userServices.getUserPosts(username);
            return new ResponseEntity<>(new UserApiResponse(true, response), ACCEPTED);
        } catch (NaijaGossipsExceptions e){
            return new ResponseEntity<>(new UserApiResponse(false, e.getMessage()), BAD_REQUEST);
        }
    }
    @PostMapping("/comment")
    public ResponseEntity<?> comment(@RequestBody CommentPostRequest commentPostRequest){
        try {
            var response = userServices.comment(commentPostRequest);
            return new ResponseEntity<>(new UserApiResponse(true, response), ACCEPTED);
        } catch (NaijaGossipsExceptions e){
            return new ResponseEntity<>(new UserApiResponse(false, e.getMessage()), BAD_REQUEST);
        }
    }
    @DeleteMapping("/delete_comment")
    public ResponseEntity<?> deleteComment(@RequestBody DeleteCommentRequest deleteCommentRequest){
        try {
            var response = userServices.deleteComment(deleteCommentRequest);
            return new ResponseEntity<>(new UserApiResponse(true, response), ACCEPTED);
        } catch (NaijaGossipsExceptions e){
            return new ResponseEntity<>(new UserApiResponse(false, e.getMessage()), BAD_REQUEST);
        }
    }
    @PostMapping("/view_post")
    public ResponseEntity<?> viewPost(@RequestBody ViewPostRequest viewPostRequest){
        try {
            var response = userServices.viewPost(viewPostRequest);
            return new ResponseEntity<>(new UserApiResponse(true, response), ACCEPTED);
        } catch (NaijaGossipsExceptions e){
            return new ResponseEntity<>(new UserApiResponse(false, e.getMessage()), BAD_REQUEST);
        }
    }
    @DeleteMapping("/delete_user/{username}")
    public ResponseEntity<?> deleteUserAccount(@PathVariable("username") String username){
        try {
            var response = userServices.deleteUser(username);
            return new ResponseEntity<>(new UserApiResponse(true, response), ACCEPTED);
        } catch (NaijaGossipsExceptions e){
            return new ResponseEntity<>(new UserApiResponse(false, e.getMessage()), BAD_REQUEST);
        }
    }
}
