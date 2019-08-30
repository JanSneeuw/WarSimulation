package nl.jansneeuw.warsimulation.validators;

import org.bukkit.entity.Player;

public class HasPermissionValidation implements Validation{

    private String errorMessage;
    private Player player;
    private String permission;

    public HasPermissionValidation(Player player){
        this.player = player;
    }
    public HasPermissionValidation(Player player, String permission){
        this.player = player;
        this.permission = permission;
    }

    public HasPermissionValidation permission(String permission){
        this.permission = permission;
        return this;
    }

    @Override
    public boolean isValid() {
        boolean result = this.player.hasPermission(this.permission);
        if (!result){
            this.errorMessage = "You cannot do that!";
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
