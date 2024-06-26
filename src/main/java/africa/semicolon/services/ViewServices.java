package africa.semicolon.services;

import africa.semicolon.data.models.View;
import africa.semicolon.dto.requests.ViewPostRequest;

import java.util.List;

public interface ViewServices {
    View saveView(ViewPostRequest viewPostRequest);

    Long countNoOfViews();

    void deleteView(View view);

    List<View> findAll();
}
