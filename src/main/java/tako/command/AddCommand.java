package tako.command;

import java.io.IOException;

import tako.Storage;
import tako.TaskList;
import tako.Ui;

import tako.task.Task;

public class AddCommand extends Command {
    private Task task;

    public AddCommand(Task task) {
        this.task = task;
    }

    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) throws IOException {
        tasks.add(task);
        storage.saveToFile(task);
        ui.showAdd(task, tasks.getSize());
    }

    @Override
    public boolean isExit() {
        return false;
    }
}
