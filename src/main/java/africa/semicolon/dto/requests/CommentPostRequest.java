package africa.semicolon.dto.requests;

import africa.semicolon.data.models.User;
import lombok.Data;

@Data
public class CommentPostRequest {
    private String postTitle;
    private String poster;
    private String commenter;
    private String comment;
}
