package africa.semicolon.services;

import africa.semicolon.data.models.Comment;
import africa.semicolon.dto.requests.CommentPostRequest;
import africa.semicolon.dto.requests.DeleteCommentRequest;

public interface CommentServices {
    Comment saveComment(CommentPostRequest commentPostRequest);

    long countNoOfViews();

    Comment findByComment(String comment);

    Comment removeComment(DeleteCommentRequest deleteCommentRequest);
}
