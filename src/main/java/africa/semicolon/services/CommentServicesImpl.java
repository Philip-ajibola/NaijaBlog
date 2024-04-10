package africa.semicolon.services;

import africa.semicolon.data.models.Comment;
import africa.semicolon.data.models.Post;
import africa.semicolon.data.models.User;
import africa.semicolon.data.repositories.CommentRepository;
import africa.semicolon.data.repositories.UserRepository;
import africa.semicolon.dto.requests.CommentPostRequest;
import africa.semicolon.dto.requests.DeleteCommentRequest;
import africa.semicolon.exceptions.CommentNotFoundException;
import africa.semicolon.exceptions.PostNotFoundException;
import africa.semicolon.exceptions.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static africa.semicolon.utils.Mapper.requestMap;

@Service
public class CommentServicesImpl implements CommentServices{
    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private UserRepository userRepository;

    @Override
    public Comment saveComment(CommentPostRequest commentPostRequest) {
        User user = userRepository.findByUsername(commentPostRequest.getCommenter());
        if(user == null) throw new UserNotFoundException("User Not Found");
        Comment comment = requestMap(commentPostRequest.getComment(),user);
        commentRepository.save(comment);
        return comment;
    }

    @Override
    public long countNoOfViews() {
        return commentRepository.count();
    }

    @Override
    public Comment findByComment(String comment1) {
        Comment comment = commentRepository.findByComment(comment1);
        if (comment == null) throw new CommentNotFoundException("Comment not found");
        return comment;
    }

    @Override
    public Comment removeComment(DeleteCommentRequest deleteCommentRequest) {
        User user = userRepository.findByUsername(deleteCommentRequest.getCommenter());
        if(user == null) throw new UserNotFoundException("User Can't Comment Cus User Does Not exist");
        Comment comment = commentRepository.findByCommentAndCommenter(deleteCommentRequest.getComment(),user);
        commentRepository.delete(comment);
        return comment;
    }

}
