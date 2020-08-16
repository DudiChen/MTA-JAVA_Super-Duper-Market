package entities;


public class OLD_ID<T> {
    private T ID;

    public OLD_ID(T ID) {
        this.ID = ID;
    }

    public T getID() {
        return ID;
    }

    public void setID(T ID) {
        this.ID = ID;
    }
}
