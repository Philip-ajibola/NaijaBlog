package africa.semicolon.dto.requests;

import lombok.Data;

@Data
public class DeletePostRequest {
    private String postTitle;
    private String author;
}
