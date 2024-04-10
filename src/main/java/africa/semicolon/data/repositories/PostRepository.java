package africa.semicolon.data.repositories;

import africa.semicolon.data.models.Post;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface PostRepository extends MongoRepository<Post, String> {
    Post findByTitleAndPoster(String title,String poster);
}
