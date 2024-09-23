package server.managers;

import Client.Handlers.Checker;
import common.exceptions.WrongParameterException;
import server.interfaces.FileHandler;
import common.model.*;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.LinkedList;
import java.util.Scanner;

public class FileProcessor implements FileHandler {

    @Override
    public LinkedList<SpaceMarine> read(String fileName) {
        LinkedList<SpaceMarine> collection = new LinkedList<>();
        try {
            File file = new File(fileName);
            Scanner in = new Scanner(file, StandardCharsets.UTF_8.name());

            while (in.hasNextLine()) {
                String line = in.nextLine();
                String[] values = line.split(",");
                if (values.length == 1) {
                    continue;
                }
                try {
                    SpaceMarine marine = parseSpaceMarines(values);
                    collection.add(marine);
                } catch (Exception ex) {
                    System.out.println("Collection can't be processed.");
                    return null;
                }
            }
            System.out.println("Collection uploaded.");
            return collection;
        } catch (IOException ex) {
            System.out.println("File not found.");
            return null;
        }
    }

    @Override
    public void write(LinkedList<SpaceMarine> collection, String fileName) {
        String line;
        try {
            BufferedWriter out = new BufferedWriter(new FileWriter(fileName));
            for (SpaceMarine marine : collection) {
                line = marine.getId().toString() + "," +
                        marine.getName() + "," +
                        marine.getCoordinates().getX() + "," +
                        marine.getCoordinates().getY() + "," +
                        marine.getCreationDate().toString() + "," +
                        Float.valueOf(marine.getHealth()).toString() + "," +
                        marine.getHeartCount().toString() + "," +
                        marine.getCategory().ordinal() + "," +
                        marine.getWeapon().ordinal() + "," +
                        marine.getChapter().getChapterName() + "," +
                        marine.getChapter().getChapterPosition().getPos_x() + "," +
                        marine.getChapter().getChapterPosition().getPos_y() + "," +
                        marine.getChapter().getChapterPosition().getPos_z();
                out.write(line + "\n");
            }
            out.close();
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    private SpaceMarine parseSpaceMarines(String[] values) {
        try {
            Long id = Long.parseLong(values[0]); //Поле не может быть null, Значение поля должно быть больше 0, Значение этого поля должно быть уникальным, Значение этого поля должно генерироваться автоматически
            LocalDate creationDate = LocalDate.parse(values[4]); //Поле не может быть null, Значение этого поля должно генерироваться автоматически
            if (Checker.ValidNameCheck(values[1]) &&
                    Float.valueOf(values[2]) <= 138.0 &&
                    Checker.isCorrectNumber(values[2], Float.class) && Checker.isCorrectNumber(values[3], Float.class) &&
                    Float.valueOf(values[5]) > 0 && Checker.isCorrectNumber(values[5], Float.class) &&
                    Integer.valueOf(values[6]) > 0 && Integer.valueOf(values[6]) < 4 && Checker.isCorrectNumber(values[6], Integer.class) && !Checker.NullCheck(values[6]) &&
                    Integer.valueOf(values[7]) > 0 && Integer.valueOf(values[7]) < 4 && Checker.isCorrectNumber(values[7], Integer.class) && !Checker.NullCheck(values[7]) &&
                    Integer.valueOf(values[8]) > 0 && Integer.valueOf(values[8]) < 5 && Checker.isCorrectNumber(values[8], Integer.class) && !Checker.NullCheck(values[8]) &&
                    !Checker.NullCheck(values[9]) && !values[9].isEmpty()) {
                String name = values[1]; //Поле не может быть null, Строка не может быть пустой
                Coordinates coordinates = new Coordinates(Float.parseFloat(values[2]), Float.parseFloat(values[3])); //Поле не может быть null
                float health = Float.parseFloat(values[5]); //Значение поля должно быть больше 0
                Integer heartCount = Integer.parseInt(values[6]); //Поле не может быть null, Значение поля должно быть больше 0, Максимальное значение поля: 3
                AstartesCategory category = AstartesCategory.values()[Integer.parseInt(values[7])]; //Поле может быть null
                Weapon weapon = Weapon.values()[Integer.parseInt(values[8])]; //Поле может быть null
                Position position = new Position(Double.parseDouble(values[10]), Double.parseDouble(values[11]), Long.parseLong(values[12])); //Поле может быть null
                Chapter chapter = new Chapter(values[9], position); //Поле может быть null
                return new SpaceMarine(id, name, coordinates, creationDate, health, heartCount, category, weapon, chapter);
            } else throw new WrongParameterException("Collection can't be uploaded due to incorrect parameters");
        } catch (WrongParameterException ex) {
            System.out.println(ex);
            System.exit(0);
            return null;
        }
    }
}