/**
 * This is the interface, which will ensure every person who is part of the database has the
 * crucial information which is common for each entity
 */

public interface Person {

    public String name = "";

    public String getName();

    public void setName(String name);

}
