package com.yan.cursomc.resourses.exceptions;

import java.util.ArrayList;
import java.util.List;

public class ValidationError extends StandardError {

    private static final long serialVersionUID = 1L;

    private List<FieldMenssage>errors = new ArrayList<>();

    public ValidationError(Long timestamp,Integer status,String error,String message,String path){
        super(timestamp,status,error,message,path);
    }

    public List<FieldMenssage> getErrors() {
        return errors;
    }

 
    public void addError(String fieldName,String message){
        errors.add(new FieldMenssage(fieldName, message));
    }
}