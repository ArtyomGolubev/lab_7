package Client.Handlers;

import common.exceptions.*;
import common.model.*;
import common.network.AbstractUser;
import common.network.AuthorizedUser;
import common.network.GuestUser;
import common.requests.*;
import common.responses.AbstractResponse;
import common.responses.ErrorResponse;
import common.responses.RegardsResponse;

import java.io.*;
import java.lang.reflect.Field;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class ConsoleHandler {
    private final Scanner scanner = new Scanner(System.in);
    private final RequestHandler requestHandler;
    private final Sender sender;
    private ConsoleMode consoleMode;
    private final ResponseHandler responseHandler;
    private final Asker asker = new Asker();
    private final ScriptHandler scriptHandler = new ScriptHandler();
    private boolean auth = false;
    private AbstractUser user = new GuestUser();

    public ConsoleHandler(RequestHandler requestHandler, Sender sender, ResponseHandler responseHandler) {
        this.requestHandler = requestHandler;
        this.sender = sender;
        this.responseHandler = responseHandler;
        this.responseHandler.setConsoleHandler(this);
        this.consoleMode = ConsoleMode.INTERACTIVE;
        this.user = new GuestUser();
    }

    public void setAuthorized(boolean authorized) {
        this.auth = authorized;
    }

    public void setAbstractUser(AbstractUser user) {
        this.user = user;
    }

    public class Asker {
        public void MarineCreationFromInput(AddRequest request) {
            request.setName(askName());
            request.setCoordinates(askCoordinates());
            request.setHealth(askHealth());
            request.setHeartCount(askHeartCount());
            request.setCategory(askCategory());
            request.setWeapon(askWeapon());
            request.setChapter(askChapter());
        }

        public void updateElement(UpdateRequest request) {
            String answer = askWhatToChange();
            try {
                if (Checker.StringForIntegerCheck(answer)) {
                    String[] splitted = answer.split(" ");
                    int[] fieldsNumbers = new int[splitted.length];
                    for (int i = 0; i < fieldsNumbers.length; i++) {
                        fieldsNumbers[i] = Integer.parseInt(splitted[i]);
                    }
                    for (int num : fieldsNumbers) {
                        if (num > 7) {
                            throw new WrongParameterException("Number " + num + " does not correlate with any fields.");
                        }
                        switch (num) {
                            case 1 -> request.setName(askName());
                            case 2 -> request.setCoordinates(askCoordinates());
                            case 3 -> request.setHealth(askHealth());
                            case 4 -> request.setHeartCount(askHeartCount());
                            case 5 -> request.setCategory(askCategory());
                            case 6 -> request.setWeapon(askWeapon());
                            case 7 -> request.setChapter(askChapter());
                        }
                    }
                } else throw new WrongParameterException("Only numbers and spaces are allowed.");
            } catch (WrongParameterException e) {
                printError(e.toString());
            }

        }

        public String askName() {
            String name = ask("Enter Marine's name: ");
            try {
                if (Checker.ValidNameCheck(name)) {
                    return name;
                } else {
                    throw new WrongParameterException("Name can't be blank");
                }
            } catch (WrongParameterException e) {
                printError(e.toString());
                return askName();
            }
        }

        public Coordinates askCoordinates() {
            String response = ask("Enter marine's x and y coordinates using float number): ");
            Float x;
            float y;
            try {
                if (response.split(" ").length < 2) {
                    throw new WrongParameterException("Not all parameters entered.");
                }
                if (Checker.isCorrectNumber(response.split(" ")[0], Float.class) && Checker.isCorrectNumber(response.split(" ")[1], Float.class)) {
                    if (!Checker.NullCheck(response.split(" ")[0])) {
                        x = Float.parseFloat(response.split(" ")[0]);
                        y = Float.parseFloat(response.split(" ")[1]);
                        return new Coordinates(x,y);
                    } else {
                        throw new WrongParameterException("x can't be null.");
                    }
                } else {
                    throw new WrongParameterException("Wrong numbers entered.");
                }
            } catch (WrongParameterException e) {
                printError(e.toString());
                return askCoordinates();
            }
        }

        public float askHealth() {
            float result = -1.0f;
            String response = ask("Enter marine's HP count: ");

            try {
                if (Checker.NullCheck(response) || Checker.EmptyArrayCheck(response.split(" "))) {
                    throw new BlankRequestException("Blank string entered");
                }
                if (response.contains(" ")) {
                    String[] splitted = response.split(" ");
                    if (Checker.isCorrectNumber(splitted[0], Float.class)) {
                        result = Float.parseFloat(splitted[0]);
                    }
                } else if (Checker.isCorrectNumber(response, Long.class)) {
                    result = Long.parseLong(response);
                } else {
                    throw new WrongParameterException("Wrong number entered.");
                }
                if (result > 0) {
                    return result;
                } else {
                    throw new WrongParameterException("Field can't be null.");
                }
            } catch (WrongParameterException | BlankRequestException e) {
                printError(e.toString());
                return askHealth();
            }
        }

        public int askHeartCount() {
            int result = -1;
            String response = ask("Enter marine's heart count: ");

            try {
                if (Checker.NullCheck(response) || Checker.EmptyArrayCheck(response.split(" "))) {
                    throw new BlankRequestException("Blank string entered");
                }
                if (response.contains(" ")) {
                    String[] splitted = response.split(" ");
                    if (splitted.length > 1) {
                        throw new WrongParameterException("Something's wrong with the input format.");
                    }
                    if (Checker.isCorrectNumber(splitted[0], Integer.class)) {
                        if (Integer.valueOf(response) < 4) {
                            result = Integer.parseInt(splitted[0]);
                        }
                    }
                } else if (Checker.isCorrectNumber(response, Integer.class)) {
                    if (Integer.valueOf(response) < 4 && response.length()<2) {
                        result = Integer.parseInt(response);
                    }
                } else {
                    throw new WrongParameterException("This field can only take values from 1 to 3.");
                }
                if (result > 0) {
                    return result;
                } else {
                    throw new WrongParameterException("Field can't be less than zero.");
                }
            } catch (WrongParameterException | BlankRequestException e) {
                printError(e.toString());
                return askHeartCount();
            }
        }

        public AstartesCategory askCategory() {
            AstartesCategory[] values = AstartesCategory.values();
            StringBuilder question = new StringBuilder();
            question.append("Enter Astartes Category number: \n");
            for (AstartesCategory type : values) {
                question.append(type.ordinal() + 1).append(") ").append(type.name()).append("\n");
            }
            String response = ask(question.toString());

            String num;
            try {
                if (Checker.NullCheck(response) || Checker.EmptyArrayCheck(response.split(" "))) {
                    throw new BlankRequestException("Field can't be blank.");
                }
                if (response.contains(" ")) {
                    num = response.split(" ")[0];
                    if (Checker.NullCheck(num)) {
                        throw new WrongParameterException("Wrong string entered.");
                    }

                } else {
                    num = response;
                }
                if (Checker.isCorrectNumber(num, Integer.class)) {
                    if (Integer.parseInt(num) <= AstartesCategory.values().length && Integer.parseInt(num) >= 1) {
                        return AstartesCategory.values()[Integer.parseInt(num)-1];
                    } else {
                        throw new WrongParameterException("Wrong number entered.");
                    }
                } else {
                    throw new WrongParameterException("Wrong number entered.");
                }

            } catch (WrongParameterException | NumberFormatException | BlankRequestException e) {
                printError(e.toString());
                return askCategory();
            }
        }

        public Weapon askWeapon() {
            Weapon[] values = Weapon.values();
            StringBuilder question = new StringBuilder();
            question.append("Enter Weapon number: \n");
            for (Weapon type : values) {
                question.append(type.ordinal() + 1).append(") ").append(type.name()).append("\n");
            }
            String response = ask(question.toString());

            String num;
            try {
                if (Checker.NullCheck(response) || Checker.EmptyArrayCheck(response.split(" "))) {
                    throw new BlankRequestException("Field can't be blank.");
                }
                if (response.contains(" ")) {
                    num = response.split(" ")[0];
                    if (Checker.NullCheck(num)) {
                        throw new WrongParameterException("Wrong string entered.");
                    }

                } else {
                    num = response;
                }
                if (Checker.isCorrectNumber(num, Integer.class)) {
                    if (Integer.parseInt(num) <= Weapon.values().length && Integer.parseInt(num) >= 1) {
                        return Weapon.values()[Integer.parseInt(num)-1];
                    } else {
                        throw new WrongParameterException("Wrong number entered.");
                    }
                } else {
                    throw new WrongParameterException("Wrong number entered.");
                }

            } catch (WrongParameterException | NumberFormatException | BlankRequestException e) {
                printError(e.toString());
                return askWeapon();
            }
        }

        public Chapter askChapter() {
            String chapterName = ask("Enter marine's chapter: ");
            String loc = ask("Enter chapter's position (x and y as double, z as long) via space:  ");
            try {
                if (loc.split(" ").length < 3) {
                    throw new WrongParameterException("Неверно введены координаты локации");
                }
                if (Checker.StringForNumberCheck(loc) && Checker.ValidNameCheck(chapterName)) {
                    if (Checker.PositionParametersCheck(loc.split(" ")[0], loc.split(" ")[1], loc.split(" ")[2])) {
                        double x = Double.parseDouble(loc.split(" ")[0]);
                        double y = Double.parseDouble(loc.split(" ")[1]);
                        long z = Long.parseLong(loc.split(" ")[2]);
                        return new Chapter(chapterName, new Position(x,y,z));
                    } else {
                        throw new WrongParameterException("Wrong location parameters entered.");
                    }
                } throw new WrongParameterException("Wrong parameters entered.");
            } catch (WrongParameterException e) {
                printError(e.toString());
                return askChapter();
            }
        }

        public void setAuth(AbstractRequest request) {
            String login = askLogin();
            String password = askPassword();
            request.setUser(new AuthorizedUser(login, password));
        }

        public String askLogin() {
            return ask("Enter login: ");
        }

        public String askPassword() {
            return ask("Enter password: ");
        }
    }

    public class ScriptHandler {
        private Stack<String> filenames = new Stack<>();
        private Queue<String> commands = new ArrayDeque<>();

        public String nextLine() {
            try {
                return this.commands.poll();
            } finally {
                if (commands.isEmpty()) {
                    consoleMode = ConsoleMode.INTERACTIVE;
                    commands = new ArrayDeque<>();
                }
            }
        }

        public void clear() {
            this.commands.clear();
        }

        private void readScript(String filename) throws WrongParameterException {
            try {
                if (filenames.contains(filename)) {
                    throw new RecursionExecutionException("Рекурсивный вызов файла.");
                }
                filenames.push(filename);
                File file = new File(filename);
                InputStreamReader inputStreamReader = new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    if (line.equals("execute_script " + filename)) {
                        throw new RecursionExecutionException("Recursive file access attempt.");
                    }
                    this.commands.add(line);
                }
            } catch (IOException e) {
                throw new WrongParameterException("File is not found / You don't have access to the requested file.");
            }
        }
    }

    public void listen() {
        while (true) {
            try {
                print("> ");
                String request = next();
                AbstractResponse response = processUserRequest(request);
                println(this.responseHandler.handleResponse(response));
                if (!this.auth) {
                    this.auth = true;
                }
            } catch (AuthenticationFailedException e) {
                this.auth = false;
            } catch (Exception e) {
                printError(e.toString());
                break;
            }
        }
    }

    public String next() {
        if (this.consoleMode == ConsoleMode.INTERACTIVE) {
            return scanner.nextLine();
        } else if (this.consoleMode == ConsoleMode.FILE_READER) {
            return scriptHandler.nextLine();
        }
        return scanner.nextLine();
    }

    public AbstractResponse processUserRequest(String request) {
        try {
            String[] processed = splitUserRequest(request);
            AbstractRequest requestToServer = this.requestHandler.get(processed[0]);
            if (requestToServer instanceof RequestWithParameters) {
                String[] parameters = new String[processed.length-1];
                for (int i = 1; i < processed.length; i++) {
                    parameters[i-1] = processed[i];
                }
                ((RequestWithParameters) requestToServer).setParameters(parameters);
            }
            if (requestToServer instanceof AddRequest) {
                this.asker.MarineCreationFromInput((AddRequest) requestToServer);
            }
            if (requestToServer instanceof AuthenticationRequest ||  requestToServer instanceof AddUserRequest) {
                this.asker.setAuth(requestToServer);
                return this.sender.sendRequest(requestToServer);
            }
            if (requestToServer instanceof UpdateRequest) {
                this.asker.updateElement((UpdateRequest) requestToServer);
            }
            if (requestToServer instanceof ExecuteScriptRequest) {
                this.consoleMode = ConsoleMode.FILE_READER;
                scriptHandler.readScript(((ExecuteScriptRequest) requestToServer).getFilename());
                return new RegardsResponse(requestToServer.getCommandName(), "Script execution initiated");
            }
            if (!(requestToServer.getUser() instanceof GuestUser) && !(this.user instanceof GuestUser)) {
                requestToServer.setUser(this.user);
            }
            return this.sender.sendRequest(requestToServer);

        } catch (BlankRequestException | NonexistentCommandException | WrongParameterException e) {
            return new ErrorResponse(e.toString());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    private String[] splitUserRequest(String request) throws BlankRequestException {
        if (request.isEmpty()) throw new BlankRequestException("Blank string entered");
        if (!request.contains(" ")) return new String[]{request};
        String command = request.split(" ", 2)[0];
        String[] parameters = request.split(" ", 2)[1].split(" ");
        if (parameters.length != 0) {
            for (int i = 0; i < parameters.length; i++) {
                if (parameters[i].isEmpty()) {
                    parameters[i] = null;
                }
            }
        }

        String[] processed;
        if (Checker.isArrayConsistsOfOnlyNull(parameters)) {
            processed = new String[]{command};
            return processed;
        } else {
            processed = new String[parameters.length + 1];
            processed[0] = command;
            System.arraycopy(parameters, 0, processed, 1, parameters.length);
        }
        return processed;
    }

    public String ask(String message) {
        print(message);
        return next();
    }

    public String askWhatToChange() {
        println("Pick parameters you'd like to change and write them (via Space): ");
        Field[] fields = SpaceMarine.class.getDeclaredFields();
        List<Field> filteredFields = new ArrayList<>();

        for (Field field : fields) {
            if (!field.getName().equals("id") && !field.getName().equals("creationDate") && !field.getName().equals("ownerLogin")) {
                filteredFields.add(field);
            }
        }
        Field[] resultingArray = filteredFields.toArray(new Field[0]);
        for (int i = 1; i <= resultingArray.length; i++) {
            println(i + ") " + resultingArray[i-1].getName());
        }
        return next();
    }

    public void println(Object obj) {
        if (consoleMode == ConsoleMode.INTERACTIVE || obj != null) {
            System.out.println(obj.toString());
        }
    }

    public void print(Object obj) {
        if (consoleMode == ConsoleMode.INTERACTIVE) {
            System.out.print(obj.toString());
        }
    }

    public void printAdvice(String advice) {
        if (consoleMode == ConsoleMode.INTERACTIVE) {
            System.out.println("Advice: " + advice);
        }
    }

    public void printError(String message) {
        System.out.println("ERROR: " + message);
        if (this.consoleMode == ConsoleMode.FILE_READER) {
            this.scriptHandler.clear();
            System.out.println("File execution finished.");
            this.consoleMode = ConsoleMode.INTERACTIVE;
            listen();
        }
    }

}