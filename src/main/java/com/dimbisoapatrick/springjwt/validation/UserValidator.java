package com.dimbisoapatrick.springjwt.validation;

import com.dimbisoapatrick.springjwt.dto.UserDTO;
import org.springframework.stereotype.Component;
import java.util.ArrayList;
import java.util.List;
import com.dimbisoapatrick.springjwt.constant.ErrorType;
import com.dimbisoapatrick.springjwt.entity.ErrorModel;

@Component
public class UserValidator {

    public List<ErrorModel> validateRequest(UserDTO user){

        List<ErrorModel> errorModelList = new ArrayList<>();

        if(null != user && user.getEmail() == null){
            ErrorModel errorModel = new ErrorModel();
            errorModel.setCode(ErrorType.NOT_EMPTY.toString());
            errorModel.setMessage("Email cannot be empty");

            errorModelList.add(errorModel);
        }

        if(null != user && user.getPassword() == null){
            ErrorModel errorModel = new ErrorModel();
            errorModel.setCode(ErrorType.NOT_EMPTY.toString());
            errorModel.setMessage("Password cannot be empty");

            errorModelList.add(errorModel);
        }
        return errorModelList;
    }
}
