package africa.semicolon.data.repositories;

import africa.semicolon.data.models.Comment;
import africa.semicolon.data.models.User;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface CommentRepository extends MongoRepository<Comment, String> {
    Comment findByComment(String comment);

    Comment findByCommentAndCommenter(String comment, User commenter);
}
