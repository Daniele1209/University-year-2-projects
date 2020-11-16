package View;

public abstract class Command {
    String key, desc;

    public Command(String k, String d) {
        key = k;
        desc = d;
    }

    public abstract void execute();

    public String getKey() {
        return key;
    }

    public String getDescription() {
        return desc;
    }
}
