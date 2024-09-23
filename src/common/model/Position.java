package common.model;

import java.io.Serializable;
import java.util.Objects;

public class Position implements Serializable {

    private final double pos_x;
    private final double pos_y;
    private final long pos_z;

    public Position(double pos_x, double pos_y, long pos_z) {
        this.pos_x = pos_x;
        this.pos_y = pos_y;
        this.pos_z = pos_z;
    }

    public double getPos_x() {
        return pos_x;
    }

    public double getPos_y() {
        return pos_y;
    }

    public long getPos_z() {
        return pos_z;
    }

    @Override
    public String toString() {
        return "Location{" +
                "x=" + pos_x +
                ", y=" + pos_y +
                ", z=" + pos_z +
                '}';
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        Position position = (Position) object;
        return Double.compare(pos_x, position.pos_x) == 0 && Double.compare(pos_y, position.pos_y) == 0 && pos_z == position.pos_z;
    }

    @Override
    public int hashCode() {
        return Objects.hash(pos_x, pos_y, pos_z);
    }
}
