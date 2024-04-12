package africa.semicolon.services;

import africa.semicolon.data.models.Comment;
import africa.semicolon.data.models.Post;
import africa.semicolon.data.models.View;
import africa.semicolon.data.repositories.PostRepository;
import africa.semicolon.dto.requests.CommentPostRequest;
import africa.semicolon.dto.requests.DeleteCommentRequest;
import africa.semicolon.dto.requests.ViewPostRequest;
import africa.semicolon.exceptions.PostNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PostServicesImpl implements PostServices {

    @Autowired
    PostRepository postRepository;
    @Autowired
    ViewServices viewServices;
    @Autowired
    CommentServices commentServices;

    @Override
    public void addPost(Post post) {
        postRepository.save(post);
    }

    @Override
    public Long countNoOfPosts() {
        return postRepository.count();
    }

    @Override
    public void deletePost(Post post) {
        for(View view : post.getViews()) viewServices.deleteView(view);
        for(Comment comment : post.getComments()) commentServices.delete(comment);
        postRepository.delete(post);
    }

    @Override
    public Post findPostByTitleAndAuthor(String title, String username) {
        Post post = postRepository.findByTitleAndAuthor(title, username);
        if (post == null) throw new PostNotFoundException("Post not found");
        return post;
    }

    @Override
    public View addView(ViewPostRequest viewPostRequest) {
        Post post = findPostByTitleAndAuthor(viewPostRequest.getPostTitle(), viewPostRequest.getPosterName());
        View view = viewServices.saveView(viewPostRequest);
        post.getViews().add(view);
        postRepository.save(post);
        return view;
    }

    @Override
    public Comment addComment(CommentPostRequest commentPostRequest) {
        Post post = findPostByTitleAndAuthor(commentPostRequest.getPostTitle(), commentPostRequest.getPoster());
        if(!checkIfPostIsViewedBy(commentPostRequest.getCommenter(), post)) viewPostIfNotViewed(commentPostRequest, post);

        Comment comment = commentServices.saveComment(commentPostRequest);
        post.getComments().add(comment);
        postRepository.save(post);
        return comment;
    }

    private void viewPostIfNotViewed(CommentPostRequest commentPostRequest, Post post) {
        ViewPostRequest viewPostRequest = new ViewPostRequest();
        viewPostRequest.setPostTitle(post.getTitle());
        viewPostRequest.setPosterName(post.getAuthor());
        viewPostRequest.setViewer(commentPostRequest.getCommenter());
        View view = viewServices.saveView(viewPostRequest);
        post.getViews().add(view);
        postRepository.save(post);
    }

    private static boolean checkIfPostIsViewedBy(String username, Post post) {
        boolean condition = false;
        for(View view : post.getViews()){
            if(view.getViewer().getUsername().equals(username)) {
                condition = true;
                break;
            }
        }
        return condition;
    }

    @Override
    public void deleteComment(DeleteCommentRequest deleteCommentRequest) {
        Post post = findPostByTitleAndAuthor(deleteCommentRequest.getPostTitle(), deleteCommentRequest.getPoster());
        Comment comment = commentServices.removeComment(deleteCommentRequest);
        post.getComments().remove(comment);
        postRepository.save(post);
    }

}
