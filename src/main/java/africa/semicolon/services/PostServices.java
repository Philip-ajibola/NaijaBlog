package africa.semicolon.services;

import africa.semicolon.data.models.Post;
import africa.semicolon.dto.requests.CommentPostRequest;
import africa.semicolon.dto.requests.DeleteCommentRequest;
import africa.semicolon.dto.requests.ViewPostRequest;

public interface PostServices {
    void addPost(Post post);

    Long countNoOfPosts();

    void deletePost(Post post);

    Post findPostByTitleAndPoster(String title,String poster);

    void addView(ViewPostRequest viewPostRequest);

    void addComment(CommentPostRequest commentPostRequest);

    void deleteComment(DeleteCommentRequest deleteCommentRequest);
}
