package pl.arczynskiadam.cojesc.restaurant;

public abstract class Restaurant {

    private String name;
    private String[] menuKeyWords;
    private int menuDuration;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String[] getMenuKeyWords() {
        return menuKeyWords;
    }

    public void setMenuKeyWords(String[] menuKeyWords) {
        this.menuKeyWords = menuKeyWords;
    }

    public int getMenuDuration() {
        return menuDuration;
    }

    public void setMenuDuration(int menuDuration) {
        this.menuDuration = menuDuration;
    }
}
