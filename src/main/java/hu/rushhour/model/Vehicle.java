package hu.rushhour.model;
import hu.rushhour.model.*;
import hu.rushhour.io.*;
import hu.rushhour.db.*;
import hu.rushhour.game.*;




/** Immutable vehicle position; orientation cannot change. */
public record Vehicle(char id, int row, int col, int length, boolean horizontal) {
    public Vehicle {
        if (!Character.isLetterOrDigit(id) || row < 0 || col < 0 || length < 2) {
            throw new IllegalArgumentException("Invalid vehicle");
        }
    }
    public Vehicle shifted(int distance) {
        return new Vehicle(id, row + (horizontal ? 0 : distance),
                col + (horizontal ? distance : 0), length, horizontal);
    }
}
