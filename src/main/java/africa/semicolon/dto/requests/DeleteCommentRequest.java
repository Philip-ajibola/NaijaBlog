package africa.semicolon.dto.requests;

import lombok.Data;

@Data
public class DeleteCommentRequest {
    private String id;
    private String postTitle;
    private String poster;
    private String commenter;
}
