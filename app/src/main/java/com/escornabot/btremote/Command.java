package com.escornabot.btremote;

public class Command {
    private String atCommand;
    private int imageResourceId;

    public Command(String atCommand, int imageResource) {
        this.atCommand = atCommand;
        this.imageResourceId = imageResource;
    }

    public String getAtCommand() {
        return atCommand;
    }

    public int getImageResourceId() {
        return imageResourceId;
    }

    public static Command up() {
        return new Command("n\n", R.drawable.btn_arrow_n);
    }

    public static Command down() {
        return new Command("s\n", R.drawable.btn_arrow_s);
    }

    public static Command left() {
        return new Command("w\n", R.drawable.btn_arrow_w);
    }

    public static Command right() {
        return new Command("e\n", R.drawable.btn_arrow_e);
    }

    public static Command reset() {
        return new Command("r\n", R.drawable.btn_arrow_reset);
    }

    public static Command go() {
        return new Command("g\n", R.drawable.btn_arrow_go);
    }
}
