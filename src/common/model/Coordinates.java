package common.model;

import java.io.Serializable;
import java.util.Objects;

public class Coordinates implements Serializable {
    private final Float x; //Максимальное значение поля: 138; Поле не может быть null
    private final float y;

    public Coordinates(Float x, float y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public String toString() {
        return "Coordinates{" +
                "x=" + x +
                ", y=" + y +
                '}';
    }

    public Float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        Coordinates that = (Coordinates) object;
        return y == that.y && Objects.equals(x, that.x);
    }
    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }
}
