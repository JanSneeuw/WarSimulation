package nl.jansneeuw.warsimulation.validators;

import java.util.ArrayList;
import java.util.List;

public class Validator {
    private List<Validation> validations = new ArrayList<Validation>();
    private List<String> errors = new ArrayList<String>();
    private String errormessage;
    public Validator addValidation(Validation validation){
        this.validations.add(validation);
        return this;
    }

    public boolean executeValidations(){
        boolean isValid = true;
        for (Validation validation : validations){
            isValid = isValid && validation.isValid();
            if (!isValid){
                errormessage = validation.getErrorMessage();
            }
        }
        return isValid;
    }
    public String getErrormessage(){
        return this.errormessage;
    }

}
