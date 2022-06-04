package me.yarinlevi.quapiguiapi.exceptions;

/**
 * @author YarinQuapi
 */
public class InventoryDoesNotExistException extends Exception {
    public InventoryDoesNotExistException() {
        super("InventoryType was not found!");
    }
}
