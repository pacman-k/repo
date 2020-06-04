package by.epam.training.command;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;

@FunctionalInterface
public interface ServletCommand {
    void execute(HttpServletRequest req, HttpServletResponse resp) throws CommandException;
}
