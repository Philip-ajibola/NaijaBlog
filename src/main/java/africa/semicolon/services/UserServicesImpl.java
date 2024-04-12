package africa.semicolon.services;

import africa.semicolon.data.models.Comment;
import africa.semicolon.data.models.Post;
import africa.semicolon.data.models.User;
import africa.semicolon.data.models.View;
import africa.semicolon.data.repositories.UserRepository;
import africa.semicolon.dto.requests.*;
import africa.semicolon.dto.responses.*;
import africa.semicolon.exceptions.*;
import africa.semicolon.utils.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static africa.semicolon.utils.Mapper.*;

@Service
public class UserServicesImpl implements UserServices{

    @Autowired
    UserRepository userRepository;

    @Autowired
    PostServices postServices;

    @Override
    public UserRegisterResponse register(UserRegisterRequest userRegisterRequest) {
        validateUsername(userRegisterRequest.getUsername());

        User newUser = requestMap(userRegisterRequest);
        userRepository.save(newUser);


        return responseMap(newUser);
    }

    @Override
    public Long countNoOfUsers() {
        return userRepository.count();
    }

    @Override
        public CreatePostResponse createPost(CreatePostRequest createPostRequest) {
            validateRequest(createPostRequest);
            User foundUser = findUserByName(createPostRequest.getAuthor());
            validateLogin(foundUser);
            Post post = requestMap(createPostRequest);
            postServices.addPost(post);
            foundUser.getPosts().add(post);
            userRepository.save(foundUser);
            return createPostResponseMap(post);
        }

    private void validateRequest(CreatePostRequest createPostRequest) {
        if(createPostRequest.getAuthor().isEmpty())throw new PostAuthorException("Provide The Poster Name");
        if(createPostRequest.getTitle().isEmpty())throw new PostTitleException("PostTitle Must Not Be Empty");
        if(createPostRequest.getContent().isEmpty())throw new PostContentException("Post Content Must Not Be Empty");
    }

    private  void validateLogin(User foundUser) {
        if(!foundUser.isLoggedIn()) throw new UserNotLoggedInException("You need to log in to create post");
    }

    @Override
        public DeletePostResponse deletePost(DeletePostRequest deletePostRequest) {
            User foundUser = findUserByName(deletePostRequest.getAuthor());
            validateLogin(foundUser);
            Post post = postServices.findPostByTitleAndAuthor(deletePostRequest.getPostTitle(),deletePostRequest.getAuthor());
            postServices.deletePost(post);
            foundUser.getPosts().remove(post);
            userRepository.save(foundUser);
            return deletePostResponseMap(post);
        }

    @Override
    public UserPostsResponse getUserPosts(String username) {
        User user = findUserByName(username);
        validateLogin(user);
        List<Post> posts = user.getPosts();
        return allPostResponseMap(user, posts);
    }

    @Override
    public ViewPostResponse viewPost(ViewPostRequest viewPostRequest) {
        validateLogin(findUserByName(viewPostRequest.getViewer()));
        View view = postServices.addView(viewPostRequest);
        return Mapper.responseMap(view);
    }

    @Override
    public AddCommentResponse comment(CommentPostRequest commentPostRequest) {
        validateLogin(findUserByName(commentPostRequest.getCommenter()));
        Comment comment = postServices.addComment(commentPostRequest);
        return Mapper.responseMap(comment);
    }

    @Override
    public String deleteComment(DeleteCommentRequest deleteCommentREquest) {
        validateLogin(findUserByName(deleteCommentREquest.getCommenter()));
        postServices.deleteComment(deleteCommentREquest);
        return "Comment Deleted";
    }

    @Override
    public String deleteUser(String username) {
        User user = findUserByName(username);
        for(Post post :user.getPosts()) postServices.deletePost(post);
        userRepository.delete(user);
        return "User Deleted";
    }

    @Override
    public String login(UserLoginRequest userLoginRequest) {
        User user = findUserByName(userLoginRequest.getUsername().toLowerCase());
        validatePassword(userLoginRequest);
        user.setLoggedIn(true);
        userRepository.save(user);
        return "Login Successful";
    }

    @Override
    public String logout(UserLogoutRequest userLogoutRequest) {
        User user = findUserByName(userLogoutRequest.getUsername());
        user.setLoggedIn(false);
        userRepository.save(user);
        return "Log out successful";
    }

    private void validatePassword(UserLoginRequest userLoginRequest) {
        User user = findUserByName(userLoginRequest.getUsername());
        if(!user.getPassword().equals(userLoginRequest.getPassword())) throw new InvalidPasswordException("Wrong password");
    }


    @Override
    public boolean isUserLoggedIn(String username) {
        User user = findUserByName(username.toLowerCase());
        return user.isLoggedIn();
    }

    @Override
    public int getNoOfUserPosts(String username) {
        User user = findUserByName(username.toLowerCase());
        return user.getPosts().size();
    }

    public User findUserByName(String username) {
        User user = userRepository.findByUsername(username.toLowerCase());
        if (user == null) throw new UserNotFoundException(username + " does not exist");
        return user;
    }

    private void validateUsername(String username) {
        boolean userExists = userRepository.existsByUsername(username.toLowerCase());
        if (userExists) throw new UserAlreadyExistException(username + " already exist");
    }
}
