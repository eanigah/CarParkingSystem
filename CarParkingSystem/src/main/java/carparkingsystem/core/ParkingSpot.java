package carparkingsystem.core;

public class ParkingSpot {
    private final int id;
    private boolean occupied;

    public ParkingSpot(int id) {
        this.id = id;
        this.occupied = false;
    }

    public int getId() {
        return id;
    }

    public boolean isOccupied() {
        return !occupied;
    }

    public void setOccupied(boolean occupied) {
        this.occupied = occupied;
    }
}
