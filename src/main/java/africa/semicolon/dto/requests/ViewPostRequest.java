package africa.semicolon.dto.requests;

import africa.semicolon.data.models.User;
import lombok.Data;

@Data
public class ViewPostRequest {
    private String viewer;
    private String posterName;
    private String postTitle;
}
