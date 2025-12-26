package utils.cli;

import utils.cli.utils.Token;
import utils.Ansi;

public class CommandError {

    public final String rawMessage;
    public final String command;
    public final CommandErrors type;
    public final int start;
    public final int end;
    public final boolean isSimple;

    public String getMessage(boolean isHtml) {
        if (this.isSimple) {
            return Ansi.applyChoose(rawMessage, Ansi.Colors.RED, isHtml)
                    + (command == null ? "" : "\n" + command);
        }
        return highlightError(rawMessage, command, start, end, isHtml);
    }

    CommandError(CommandErrors type, String command, int start, int end, Object... args) {
        this.type = type;
        this.start = start;
        this.end = end;
        this.command = command;
        this.isSimple = false;

        this.rawMessage = type.getMessage().formatted(args);
    }

    /**
     * .
     *
     * @param command Команда для отображения. Может быть null
     * @param type тип ошибки (см. {@link CommandErrors})
     * @param args аргументы для форматирования строки, если таковые нужны
     */
    public CommandError(String command, CommandErrors type, Object... args) {
        this.type = type;
        this.start = 0;
        this.end = 0;
        this.command = command;
        this.isSimple = true;

        this.rawMessage = type.getMessage().formatted(args);
    }

    CommandError(CommandErrors type, String command, Token token, Object... args) {
        this(type, command, token.start(), token.end(), args);
    }

    /**
     * .
     *
     * @param type тип ошибки (см. {@link CommandErrors})
     */
    public CommandError(CommandErrors type, Context<?> context, Object... args) {
        this(type, context.command, context.currentToken(), args);
    }

    private static String highlightError(String msg, String command,
                                         int start, int end, boolean html) {
        var cl = command.length();

        var leftPos = Math.min(start, cl);
        var rightPos = Math.min(end, cl);

        var goesOutBounds = start > cl || end > cl;
        var extension = goesOutBounds
            ? " ".repeat(end - start)
            : "";

        var errorSentence = Ansi.applyChoose(
            command.substring(leftPos, rightPos) + extension,
            Ansi.Colors.RED.and(Ansi.Modes.UNDERLINE),
                html
        );

        var rightSentence = Ansi.applyChoose(
            goesOutBounds ? "" : command.substring(end),
            Ansi.Colors.RED,
                html
        );

        return Ansi.applyChoose(msg, Ansi.Colors.RED, html)
            + '\n'
            + command.substring(0, leftPos)
            + errorSentence
            + rightSentence
            + '\n'
            + " ".repeat(leftPos)
            + Ansi.applyChoose("^".repeat(end - start), Ansi.Colors.RED, html);
    }

    @Override
    public String toString() {
        return getMessage(false);
    }

    public void explain() {
        System.out.println(getMessage(false));
    }
}
