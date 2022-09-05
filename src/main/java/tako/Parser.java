package tako;

import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;

import tako.command.AddCommand;
import tako.command.Command;
import tako.command.DeleteCommand;
import tako.command.ExitCommand;
import tako.command.FindCommand;
import tako.command.ListCommand;
import tako.command.MarkCommand;

import tako.task.Deadline;
import tako.task.Event;
import tako.task.Todo;

/**
 * Parses input from the user
 * such that they are valid commands.
 */
public class Parser {
    private enum Keyword {
        BYE, LIST, MARK, TODO, DEADLINE, EVENT, DELETE, FIND;

        private static boolean contains(String s) {
            for (Keyword k : Keyword.values()) {
                if (k.name().equals(s)) {
                    return true;
                }
            }
            return false;
        }
    }

    /**
     * Returns the command parsed from the user's input.
     *
     * @param input User input from interacting with Tako.
     * @return Command.
     * @throws TakoException If the input cannot be parsed.
     */
    public static Command parse(String input) throws TakoException {
        String[] splitInput = input.trim().split(" ", 2);
        String stringKeyword = splitInput[0].toUpperCase();
        Keyword command;
        if (Keyword.contains(stringKeyword)) {
            command = Keyword.valueOf(stringKeyword);
        } else {
            throw new TakoException("The input is invalid.");
        }

        switch (command) {
        case BYE:
            if (splitInput.length == 1) {
                return new ExitCommand();
            } else {
                throw new TakoException("The input is invalid.");
            }
        case LIST:
            if (splitInput.length == 1) {
                return new ListCommand();
            } else {
                throw new TakoException("The input is invalid.");
            }
        case MARK:
            try {
                if (splitInput.length == 1) {
                    throw new TakoException("The task number to mark cannot be empty.");
                } else if (splitInput.length == 2) {
                    int taskNumber = Integer.parseInt(splitInput[1]) - 1;
                    return new MarkCommand(taskNumber);
                }
            } catch (NumberFormatException e) {
                throw new TakoException("The task number to mark is invalid.");
            }
            break;
        case TODO:
            if (splitInput.length == 1) {
                throw new TakoException("The description of this todo cannot be empty.");
            }
            String todoDescription = splitInput[1];
            assert !todoDescription.equals("") : "Empty description.";
            Todo todo = new Todo(todoDescription);
            return new AddCommand(todo);
        case DEADLINE:
            if (splitInput.length == 1) {
                throw new TakoException("The description of this deadline cannot be empty.");
            }
            String[] splitDeadline = splitInput[1].split(" /by ", 2);
            if (splitDeadline.length == 2) {
                try {
                    LocalDateTime dateTime = LocalDateTime.parse(
                            splitDeadline[1].replace(' ', 'T'));
                    String deadlineDescription = splitDeadline[0];
                    assert !deadlineDescription.equals("") : "Empty description.";
                    Deadline deadline = new Deadline(deadlineDescription, dateTime);
                    return new AddCommand(deadline);
                } catch (DateTimeParseException e) {
                    throw new TakoException("Invalid date and time.\n"
                            + "Date and time should be of the format: yyyy-mm-dd hh:mm\n"
                            + "For example: 2019-10-15 10:30");
                }
            } else {
                throw new TakoException(
                        "The description of this deadline's date and time cannot be empty.");
            }
        case EVENT:
            if (splitInput.length == 1) {
                throw new TakoException("The description of this event cannot be empty.");
            }
            String[] splitEvent = splitInput[1].split(" /at ", 2);
            if (splitEvent.length == 2) {
                try {
                    LocalDateTime dateTime = LocalDateTime.parse(
                            splitEvent[1].replace(' ', 'T'));
                    String eventDescription = splitEvent[0];
                    assert !eventDescription.equals("") : "Empty description.";
                    Event event = new Event(splitEvent[0], dateTime);
                    return new AddCommand(event);
                } catch (DateTimeParseException e) {
                    throw new TakoException("Invalid date and time.\n"
                            + "Date and time should be of the format: yyyy-mm-dd hh:mm\n"
                            + "For example: 2019-10-15 10:30");
                }
            } else {
                throw new TakoException(
                        "The description of this event's date and time cannot be empty.");
            }
        case DELETE:
            try {
                if (splitInput.length == 1) {
                    throw new TakoException("The task number to delete cannot be empty.");
                } else if (splitInput.length == 2) {
                    int taskNumber = Integer.parseInt(splitInput[1]) - 1;
                    return new DeleteCommand(taskNumber);
                }
            } catch (NumberFormatException e) {
                throw new TakoException("The task number to delete is invalid.");
            }
            break;
        case FIND:
            if (splitInput.length == 1) {
                throw new TakoException("The task to find cannot be empty.");
            } else if (splitInput.length == 2) {
                return new FindCommand(splitInput[1]);
            }
            break;
        default:
            assert false : "Unknown Command: " + command;
            break;
        }
        throw new TakoException("The input is invalid.");
    }
}
