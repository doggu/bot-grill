package events.gameroom.battleship;

public class ShipSpace {
    private boolean hit;

    ShipSpace() {
        hit = false;
    }

    public void hit() { this.hit = true; }
    public boolean isHit() { return hit; }
}
