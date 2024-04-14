package africa.semicolon.services;

import africa.semicolon.data.models.Comment;
import africa.semicolon.data.repositories.CommentRepository;
import africa.semicolon.data.repositories.PostRepository;
import africa.semicolon.data.repositories.UserRepository;
import africa.semicolon.data.repositories.ViewRepository;
import africa.semicolon.dto.requests.*;
import africa.semicolon.exceptions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
public class UserServicesTest {
    @Autowired
    private UserServices userServices;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PostRepository postRepository;
    @Autowired
    private PostServices postServices;
    @Autowired
    private ViewServices viewServices;
    @Autowired
    private ViewRepository viewRepository;
    @Autowired
    private CommentServices commentServices;
    @Autowired
    private CommentRepository commentRepository;

    private UserRegisterRequest userRegisterRequest;
    private UserLoginRequest userLoginRequest;
    @BeforeEach
    public void beforeEach(){
        userRepository.deleteAll();
        postRepository.deleteAll();
        viewRepository.deleteAll();
        commentRepository.deleteAll();
        userRegisterRequest = new UserRegisterRequest();
        userRegisterRequest.setFirstName("firstName");
        userRegisterRequest.setPassword("password");
        userRegisterRequest.setLastName("LastName");
        userRegisterRequest.setUsername("username");
        userServices.register(userRegisterRequest);


        userLoginRequest = new UserLoginRequest();
        userLoginRequest.setUsername("username");
        userLoginRequest.setPassword("password");
        userServices.login(userLoginRequest);
    }
    @Test
    public void testThatUserCanCanBeCreated(){
        assertEquals(1,userServices.countNoOfUsers());
    }
    @Test
    public void testThatUserCanLogin(){
        UserLoginRequest userLoginRequest = new UserLoginRequest();
        userLoginRequest.setUsername("username");
        userLoginRequest.setPassword("password");
        userServices.login(userLoginRequest);
        assertTrue(userServices.findUserByName("username").isLoggedIn());
    }
    @Test
    public void testThatUserCanLogout(){
        UserLogoutRequest logoutRequest = new UserLogoutRequest();
        logoutRequest.setUsername("username");
        userServices.logout(logoutRequest);
        assertFalse(userServices.isUserLoggedIn("username"));
    }
    @Test
    public void testThatNothingCanBeDoneWhenUserIsNotLoggedIn(){
        UserLogoutRequest logoutRequest = new UserLogoutRequest();
        logoutRequest.setUsername("username");
        userServices.logout(logoutRequest);

        CreatePostRequest createPostRequest = new CreatePostRequest();
        createPostRequest.setAuthor("username");
        createPostRequest.setTitle("title");
        createPostRequest.setContent("content");
        assertThrows(UserNotLoggedInException.class,()->userServices.createPost(createPostRequest));
    }
    @Test
    public void testThatUserCanPost(){
        CreatePostRequest createPostRequest = new CreatePostRequest();
        createPostRequest.setAuthor("username");
        createPostRequest.setTitle("title");
        createPostRequest.setContent("content");
        userServices.createPost(createPostRequest);
        assertEquals(1,userServices.findUserByName("username").getPosts().size());
    }
    @Test
    public void testThatWhenAuthorIsNullExceptionIsThrown(){
        CreatePostRequest createPostRequest = new CreatePostRequest();
        createPostRequest.setAuthor("");
        createPostRequest.setTitle("title");
        createPostRequest.setContent("content");
        assertThrows(PostAuthorException.class,()->userServices.createPost(createPostRequest));
    }
    @Test
    public void testThatExceptionIsThrownWhenPostTitleIsEmpty(){
        CreatePostRequest createPostRequest = new CreatePostRequest();
        createPostRequest.setAuthor("username");
        createPostRequest.setTitle("");
        createPostRequest.setContent("content");
        assertThrows(PostTitleException.class,()->userServices.createPost(createPostRequest));

    }
    @Test
    public void testThatWhenPostContentIsEmptyExceptionIsThrown(){
        CreatePostRequest createPostRequest = new CreatePostRequest();
        createPostRequest.setAuthor("username");
        createPostRequest.setTitle("title");
        createPostRequest.setContent("");
        assertThrows(PostContentException.class,()->userServices.createPost(createPostRequest));
    }
    @Test
    public void testThatUserCanDeletePost(){
        CreatePostRequest createPostRequest = new CreatePostRequest();
        createPostRequest.setAuthor("username");
        createPostRequest.setTitle("title");
        createPostRequest.setContent("content");
        userServices.createPost(createPostRequest);
        DeletePostRequest deletePostRequest = new DeletePostRequest();
        deletePostRequest.setAuthor("username");
        deletePostRequest.setPostTitle("title");
        userServices.deletePost(deletePostRequest);
        assertEquals(0,userServices.findUserByName("username").getPosts().size());
    }
    @Test
    public void testThatItIsOnlyPosterThatCanDeletePost(){
        UserRegisterRequest userRegisterRequest1 = new UserRegisterRequest();
        userRegisterRequest1.setLastName("lastname");
        userRegisterRequest1.setFirstName("firstname");
        userRegisterRequest1.setPassword("password");
        userRegisterRequest1.setUsername("username1");
        userServices.register(userRegisterRequest1);

        UserLoginRequest userLoginRequest = new UserLoginRequest();
        userLoginRequest.setUsername("username1");
        userLoginRequest.setPassword("password");
        userServices.login(userLoginRequest);

        assertEquals(2,userServices.countNoOfUsers());

        CreatePostRequest createPostRequest = new CreatePostRequest();
        createPostRequest.setAuthor("username");
        createPostRequest.setTitle("title");
        createPostRequest.setContent("content");
        userServices.createPost(createPostRequest);
        DeletePostRequest deletePostRequest = new DeletePostRequest();
        deletePostRequest.setAuthor("username1");
        deletePostRequest.setPostTitle("title");
        assertThrows(PostNotFoundException.class,()->userServices.deletePost(deletePostRequest));
        assertEquals(1,userServices.findUserByName("username").getPosts().size());
    }
    @Test
    public void testThatCommentCanBeMadeOnPost(){
        CreatePostRequest createPostRequest = new CreatePostRequest();
        createPostRequest.setAuthor("username");
        createPostRequest.setTitle("title");
        createPostRequest.setContent("content");
        userServices.createPost(createPostRequest);

        UserRegisterRequest userRegisterRequest1 = new UserRegisterRequest();
        userRegisterRequest1.setLastName("lastname");
        userRegisterRequest1.setFirstName("firstname");
        userRegisterRequest1.setPassword("password");
        userRegisterRequest1.setUsername("username1");
        userServices.register(userRegisterRequest1);

        UserLoginRequest userLoginRequest = new UserLoginRequest();
        userLoginRequest.setUsername("username1");
        userLoginRequest.setPassword("password");
        userServices.login(userLoginRequest);

        CommentPostRequest commentPostRequest = new CommentPostRequest();
        commentPostRequest.setComment("comment");
        commentPostRequest.setCommenter("username1");
        commentPostRequest.setPostTitle("title");
        commentPostRequest.setPoster("username");
        userServices.comment(commentPostRequest);
        assertEquals(1, postServices.findPostByTitleAndAuthor("title","username").getComments().size());
    }
    @Test
    public void testThatCommentCanBeDeletedFromPost(){
        CreatePostRequest createPostRequest = new CreatePostRequest();
        createPostRequest.setAuthor("username");
        createPostRequest.setTitle("title");
        createPostRequest.setContent("content");
        userServices.createPost(createPostRequest);

        UserRegisterRequest userRegisterRequest1 = new UserRegisterRequest();
        userRegisterRequest1.setLastName("lastname");
        userRegisterRequest1.setFirstName("firstname");
        userRegisterRequest1.setPassword("password");
        userRegisterRequest1.setUsername("username1");
        userServices.register(userRegisterRequest1);

        UserLoginRequest userLoginRequest = new UserLoginRequest();
        userLoginRequest.setUsername("username1");
        userLoginRequest.setPassword("password");
        userServices.login(userLoginRequest);

        CommentPostRequest commentPostRequest = new CommentPostRequest();
        commentPostRequest.setComment("comment");
        commentPostRequest.setCommenter("username1");
        commentPostRequest.setPostTitle("title");
        commentPostRequest.setPoster("username");
        userServices.comment(commentPostRequest);
        assertEquals(1, postServices.findPostByTitleAndAuthor("title","username").getComments().size());

        DeleteCommentRequest deleteCommentRequest = new DeleteCommentRequest();
        deleteCommentRequest.setId(commentServices.findByComment("comment").getId());
        deleteCommentRequest.setCommenter("username1");
        deleteCommentRequest.setPoster("username");
        deleteCommentRequest.setPostTitle("title");
        userServices.deleteComment(deleteCommentRequest);
        assertEquals(0, postServices.findPostByTitleAndAuthor("title","username").getComments().size());
    }
    @Test
    public void testThatPostCanBeViewed(){
        CreatePostRequest createPostRequest = new CreatePostRequest();
        createPostRequest.setAuthor("username");
        createPostRequest.setTitle("title");
        createPostRequest.setContent("content");
        userServices.createPost(createPostRequest);

        UserRegisterRequest userRegisterRequest1 = new UserRegisterRequest();
        userRegisterRequest1.setLastName("lastname");
        userRegisterRequest1.setFirstName("firstname");
        userRegisterRequest1.setPassword("password");
        userRegisterRequest1.setUsername("username1");
        userServices.register(userRegisterRequest1);

        UserLoginRequest userLoginRequest = new UserLoginRequest();
        userLoginRequest.setUsername("username1");
        userLoginRequest.setPassword("password");
        userServices.login(userLoginRequest);

        ViewPostRequest viewPostRequest = new ViewPostRequest();
        viewPostRequest.setPostTitle("title");
        viewPostRequest.setPosterName("username");
        viewPostRequest.setViewer("username1");
        userServices.viewPost(viewPostRequest);
        assertEquals(1,postServices.findPostByTitleAndAuthor("title","username").getViews().size());
    }
    @Test
    public void testThatWhenUserCommentPostMustBeViewed(){
        CreatePostRequest createPostRequest = new CreatePostRequest();
        createPostRequest.setAuthor("username");
        createPostRequest.setTitle("title");
        createPostRequest.setContent("content");
        userServices.createPost(createPostRequest);

        UserRegisterRequest userRegisterRequest1 = new UserRegisterRequest();
        userRegisterRequest1.setLastName("lastname");
        userRegisterRequest1.setFirstName("firstname");
        userRegisterRequest1.setPassword("password");
        userRegisterRequest1.setUsername("username1");
        userServices.register(userRegisterRequest1);

        UserLoginRequest userLoginRequest = new UserLoginRequest();
        userLoginRequest.setUsername("username1");
        userLoginRequest.setPassword("password");
        userServices.login(userLoginRequest);

        CommentPostRequest commentPostRequest = new CommentPostRequest();
        commentPostRequest.setComment("comment");
        commentPostRequest.setCommenter("username1");
        commentPostRequest.setPostTitle("title");
        commentPostRequest.setPoster("username");
        userServices.comment(commentPostRequest);
        assertEquals(1, postServices.findPostByTitleAndAuthor("title","username").getComments().size());
        assertEquals(1,postServices.findPostByTitleAndAuthor("title","username").getViews().size());
    }
    @Test
    public void testThatWhenPostIsDeletedEverythingAboutThePostIsDeleted(){
        CreatePostRequest createPostRequest = new CreatePostRequest();
        createPostRequest.setAuthor("username");
        createPostRequest.setTitle("title");
        createPostRequest.setContent("content");
        userServices.createPost(createPostRequest);

        UserRegisterRequest userRegisterRequest1 = new UserRegisterRequest();
        userRegisterRequest1.setLastName("lastname");
        userRegisterRequest1.setFirstName("firstname");
        userRegisterRequest1.setPassword("password");
        userRegisterRequest1.setUsername("username1");
        userServices.register(userRegisterRequest1);

        UserLoginRequest userLoginRequest = new UserLoginRequest();
        userLoginRequest.setUsername("username1");
        userLoginRequest.setPassword("password");
        userServices.login(userLoginRequest);

        CommentPostRequest commentPostRequest = new CommentPostRequest();
        commentPostRequest.setComment("comment");
        commentPostRequest.setCommenter("username1");
        commentPostRequest.setPostTitle("title");
        commentPostRequest.setPoster("username");
        userServices.comment(commentPostRequest);

        DeletePostRequest deletePostRequest = new DeletePostRequest();
        deletePostRequest.setPostTitle("title");
        deletePostRequest.setAuthor("username");
        userServices.deletePost(deletePostRequest);

        assertEquals(0, postServices.countNoOfPosts());
        assertEquals(0,commentServices.countNoOfComments());
        assertEquals(0,viewServices.countNoOfViews());
    }
    @Test
    public void testThatUserCanBeDeleted(){
        userServices.deleteUser(userRegisterRequest.getUsername());
        assertEquals(0,userServices.countNoOfUsers());
    }
    @Test
    public void testThatWhenUserIsDeletedUserPostIsDeleted(){
        CreatePostRequest createPostRequest = new CreatePostRequest();
        createPostRequest.setAuthor("username");
        createPostRequest.setTitle("title");
        createPostRequest.setContent("content");
        userServices.createPost(createPostRequest);

        UserRegisterRequest userRegisterRequest1 = new UserRegisterRequest();
        userRegisterRequest1.setLastName("lastname");
        userRegisterRequest1.setFirstName("firstname");
        userRegisterRequest1.setPassword("password");
        userRegisterRequest1.setUsername("username1");
        userServices.register(userRegisterRequest1);

        UserLoginRequest userLoginRequest = new UserLoginRequest();
        userLoginRequest.setUsername("username1");
        userLoginRequest.setPassword("password");
        userServices.login(userLoginRequest);

        CommentPostRequest commentPostRequest = new CommentPostRequest();
        commentPostRequest.setComment("comment");
        commentPostRequest.setCommenter("username1");
        commentPostRequest.setPostTitle("title");
        commentPostRequest.setPoster("username");
        userServices.comment(commentPostRequest);

        DeletePostRequest deletePostRequest = new DeletePostRequest();
        deletePostRequest.setPostTitle("title");
        deletePostRequest.setAuthor("username");
        userServices.deleteUser("username");

        assertEquals(1,userServices.countNoOfUsers());

        assertEquals(0, postServices.countNoOfPosts());
        assertEquals(0,commentServices.countNoOfComments());
        assertEquals(0,viewServices.countNoOfViews());
    }

}