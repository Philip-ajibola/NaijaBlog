package africa.semicolon.utils;

import africa.semicolon.data.models.Comment;
import africa.semicolon.data.models.Post;
import africa.semicolon.data.models.User;
import africa.semicolon.data.models.View;
import africa.semicolon.dto.requests.*;
import africa.semicolon.dto.responses.*;

import java.time.format.DateTimeFormatter;
import java.util.List;

public class Mapper {

    public static User requestMap(UserRegisterRequest userRegisterRequest){
        User user = new User();
        user.setUsername(userRegisterRequest.getUsername().toLowerCase());
        user.setPassword(userRegisterRequest.getPassword());
        user.setFirstName(userRegisterRequest.getFirstName());
        user.setLastName(userRegisterRequest.getLastName());
        return user;
    }

    public static Post requestMap(CreatePostRequest createPostRequest){
        Post post = new Post();
        post.setTitle(createPostRequest.getTitle());
        post.setAuthor(createPostRequest.getAuthor());
        post.setContent(createPostRequest.getContent());
        return post;
    }

    public static AddCommentResponse responseMap(Comment comment){
        AddCommentResponse addCommentResponse = new AddCommentResponse();
        addCommentResponse.setCommenterName(comment.getCommenter().getUsername());
        addCommentResponse.setId(comment.getId());
        addCommentResponse.setComment(comment.getComment());
        return addCommentResponse;
    }

    public static ViewPostResponse responseMap(View view){
        ViewPostResponse viewPostResponse = new ViewPostResponse();
        viewPostResponse.setViewId(view.getId());
        viewPostResponse.setViewersName(view.getViewer().getUsername());
        return viewPostResponse;
    }

    public static View requestMap(User user){
        View view = new View();
        view.setViewer(user);
        return view;
    }

    public static Comment requestMap(String userComment,User user){
        Comment comment = new Comment();
        comment.setCommenter(user);
        comment.setComment(userComment);
        return comment;
    }

    public static UserRegisterResponse responseMap(User user){
        UserRegisterResponse response = new UserRegisterResponse();
        response.setUsername(user.getUsername());
        response.setId(user.getId());
        response.setDateCreated(DateTimeFormatter.ofPattern("dd/MM/yyyy").format(user.getDateCreated()));
        return response;
    }


    public static CreatePostResponse createPostResponseMap(Post post){
        CreatePostResponse createPostResponse = new CreatePostResponse();
        createPostResponse.setPostId(post.getId());
        createPostResponse.setPostTitle(post.getTitle());
        createPostResponse.setPostContent(post.getContent());
        return createPostResponse;
    }

    public static DeletePostResponse deletePostResponseMap(Post post){
        DeletePostResponse deletePostResponse = new DeletePostResponse();
        deletePostResponse.setPostId(post.getId());
        deletePostResponse.setPostTitle(post.getTitle());
        return deletePostResponse;
    }

    public static UserPostsResponse allPostResponseMap(User user, List<Post> posts){
        UserPostsResponse userPostsResponse = new UserPostsResponse();
        userPostsResponse.setUsername(user.getUsername());
        userPostsResponse.setPosts(posts);
        return userPostsResponse;
    }
}
