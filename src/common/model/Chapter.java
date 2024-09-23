package common.model;

import java.io.Serializable;
import java.util.Objects;

public class Chapter implements Serializable {

    private final String chapterName; //Поле не может быть null
    private final Position chapterPosition; //Поле не может быть null

    public Chapter(String chapterName, Position chapterPosition) {
        this.chapterName = chapterName;
        this.chapterPosition = chapterPosition;
    }

    public String getChapterName() {
        return chapterName;
    }

    public Position getChapterPosition() {
        return chapterPosition;
    }

    @Override
    public String toString() {
        return "Address{" +
                "chapter name='" + chapterName + '\'' +
                ", chapter position=" + chapterPosition +
                '}';
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        Chapter chapter = (Chapter) object;
        return Objects.equals(chapterName, chapter.chapterName) && Objects.equals(chapterPosition, chapter.chapterPosition);
    }

    @Override
    public int hashCode() {
        return Objects.hash(chapterName, chapterPosition);
    }
}
