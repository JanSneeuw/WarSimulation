package nl.jansneeuw.warsimulation.validators;

import nl.jansneeuw.warsimulation.Warsimulation;

public class GameStartValidation implements Validation {

    private Warsimulation warsimulation;
    private String errorMessage;

    public GameStartValidation(Warsimulation mainInstance){
        this.warsimulation = mainInstance;
    }

    @Override
    public boolean isValid() {
        boolean result = warsimulation.lobbyList.size() == warsimulation.getConfig().getInt("Game-Start");
        if (!result){
            this.errorMessage = "Not enough players";
        }
        return result;
    }

    @Override
    public boolean isInValid() {
        return !isValid();
    }

    @Override
    public String getErrorMessage() {
        return null;
    }
}
