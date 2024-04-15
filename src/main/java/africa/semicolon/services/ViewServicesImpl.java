package africa.semicolon.services;

import africa.semicolon.data.models.Post;
import africa.semicolon.data.models.User;
import africa.semicolon.data.models.View;
import africa.semicolon.data.repositories.UserRepository;
import africa.semicolon.data.repositories.ViewRepository;
import africa.semicolon.dto.requests.ViewPostRequest;
import africa.semicolon.exceptions.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static africa.semicolon.utils.Mapper.requestMap;

@Service
public class ViewServicesImpl implements ViewServices{

    @Autowired
    private ViewRepository viewRepository;
    @Autowired
    private UserRepository userRepository;


    @Override
    public View saveView(ViewPostRequest viewPostRequest) {
        User user = userRepository.findByUsername(viewPostRequest.getViewer());
        if(user == null) throw new UserNotFoundException("User Not Found");
        View view = requestMap(user);
        viewRepository.save(view);
        return view;
    }

    @Override
    public Long countNoOfViews() {
        return viewRepository.count();
    }

    @Override
    public void deleteView(View view) {
        viewRepository.delete(view);
    }

    @Override
    public List<View> findAll() {
        return viewRepository.findAll();
    }
}
