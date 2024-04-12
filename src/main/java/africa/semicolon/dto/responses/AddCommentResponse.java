package africa.semicolon.dto.responses;

import lombok.Data;

@Data
public class AddCommentResponse {
    private String id;
    private String commenterName;
    private String comment;
}
