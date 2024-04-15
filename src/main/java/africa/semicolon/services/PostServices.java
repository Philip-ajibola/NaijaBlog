package africa.semicolon.services;

import africa.semicolon.data.models.Comment;
import africa.semicolon.data.models.Post;
import africa.semicolon.data.models.User;
import africa.semicolon.data.models.View;
import africa.semicolon.dto.requests.CommentPostRequest;
import africa.semicolon.dto.requests.DeleteCommentRequest;
import africa.semicolon.dto.requests.ViewPostRequest;

public interface PostServices {
    void addPost(Post post);

    Long countNoOfPosts();

    void deletePost(Post post);

    Post findPostByTitleAndAuthor(String title, String poster);

    View addView(ViewPostRequest viewPostRequest);

    Comment addComment(CommentPostRequest commentPostRequest);

    void deleteComment(DeleteCommentRequest deleteCommentRequest);
}
