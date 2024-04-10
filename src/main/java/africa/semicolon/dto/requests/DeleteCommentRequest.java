package africa.semicolon.dto.requests;

import lombok.Data;

@Data
public class DeleteCommentRequest {
    private String postTitle;
    private String poster;
    private String commenter;
    private String comment;
}
