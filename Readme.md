### Реализовать консольное приложение, которое реализует управление коллекцией объектов в интерактивном режиме. В коллекции необходимо хранить объекты класса SpaceMarine, описание которого приведено ниже.

### Разработанная программа должна удовлетворять следующим требованиям:

*Организовать хранение коллекции в реляционной СУБД (PostgresQL). Убрать хранение коллекции в файле.
*Для генерации поля id использовать средства базы данных (sequence).
*Обновлять состояние коллекции в памяти только при успешном добавлении объекта в БД
*Все команды получения данных должны работать с коллекцией в памяти, а не в БД
*Организовать возможность регистрации и авторизации пользователей. У пользователя есть возможность указать пароль.
*Пароли при хранении хэшировать алгоритмом `SHA-224`
*Запретить выполнение команд не авторизованным пользователям.
*При хранении объектов сохранять информацию о пользователе, который создал этот объект.
*Пользователи должны иметь возможность просмотра всех объектов коллекции, но модифицировать могут только принадлежащие им.
*Для идентификации пользователя отправлять логин и пароль с каждым запросом.

### Необходимо реализовать многопоточную обработку запросов.

### В интерактивном режиме программа должна поддерживать выполнение следующих команд:

* `help` : вывести справку по доступным командам
* `info` : вывести в стандартный поток вывода информацию о коллекции (тип, дата инициализации, количество элементов и т.д.)
* `show` : вывести в стандартный поток вывода все элементы коллекции в строковом представлении
* `add` {element} : добавить новый элемент в коллекцию
* `update id {element}` : обновить значение элемента коллекции, id которого равен заданному
* `remove_by_id id` : удалить элемент из коллекции по его id
* `clear` : очистить коллекцию
* `save` : сохранить коллекцию в файл
* `execute_script file_name` : считать и исполнить скрипт из указанного файла. В скрипте содержатся команды в таком же виде, в котором их вводит пользователь в интерактивном режиме.
* `exit` : завершить программу (без сохранения в файл)
* `shuffle`   : перемешать элементы коллекции в случайном порядке
* `remove_greater {element}` : удалить из коллекции все элементы, превышающие заданный
* `remove_lower {element}` : удалить из коллекции все элементы, меньшие, чем заданный
* `remove_any_by_heart_count` : удалить из коллекции один элемент, значение поля heartCount которого эквивалентно заданному
* `filter_contains_name name` : вывести элементы, значение поля name которых содержит заданную подстроку
* `filter_less_than_health health` : вывести элементы, значение поля health которых меньше заданного

### Формат ввода команд:

*Для многопоточного чтения запросов использовать `Fixed thread pool`
*Для многопотчной обработки полученного запроса использовать создание нового потока `Fork join pool`
*Для многопоточной отправки ответа использовать создание нового потока `Fork join pool`
*Для синхронизации доступа к коллекции использовать синхронизацию чтения и записи с помощью `java.utils.concurrent`

### Описание хранимых в коллекции классов:
```
public class SpaceMarine {
    private Long id; //Поле не может быть null, Значение поля должно быть больше 0, Значение этого поля должно быть уникальным, Значение этого поля должно генерироваться автоматически
    private String name; //Поле не может быть null, Строка не может быть пустой
    private Coordinates coordinates; //Поле не может быть null
    private java.util.Date creationDate; //Поле не может быть null, Значение этого поля должно генерироваться автоматически
    private float health; //Значение поля должно быть больше 0
    private Integer heartCount; //Поле не может быть null, Значение поля должно быть больше 0, Максимальное значение поля: 3
    private AstartesCategory category; //Поле не может быть null
    private Weapon weaponType; //Поле не может быть null
    private Chapter chapter; //Поле не может быть null
}
public class Coordinates {
    private Float x; //Максимальное значение поля: 138, Поле не может быть null
    private float y;
}
public class Chapter {
    private String chapterName; //Поле не может быть null, Строка не может быть пустой
    private Position position;
}
public class Position {
    private double x;
    private double y;
    private int z;
}
public enum AstartesCategory {
    ASSAULT,
    SUPPRESSOR,
    TERMINATOR;
}
public enum Weapon {
    HEAVY_BOLTGUN,
    FLAMER,
    GRENADE_LAUNCHER,
    MULTI_MELTA;
}
