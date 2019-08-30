package nl.jansneeuw.warsimulation.validators;

import org.bukkit.entity.Player;

public class InstanceOfPlayerValidation implements Validation {

    private Object object;
    private String errorMessage;

    public InstanceOfPlayerValidation(Object o){
        this.object = o;
    }

    @Override
    public boolean isValid() {
       boolean result = object instanceof Player;
       if (!result) {
           this.errorMessage = "You are not a player";
       }
       return result;
    }

    @Override
    public boolean isInValid() {
        return !isValid();
    }

    @Override
    public String getErrorMessage() {
        return this.errorMessage;
    }
}
