package org.icechamps.common;

import java.io.Serializable;

/**
 * A pet
 *
 * @author Robert.Diaz
 * @since 1.0, 01/06/2014
 */
public class Pet implements Serializable {
    private String name;
    private int age;
    private Person owner;

    public Pet() {
    }

    public Pet(int age, String name, Person owner) {
        this.age = age;
        this.name = name;
        this.owner = owner;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Person getOwner() {
        return owner;
    }

    public void setOwner(Person owner) {
        this.owner = owner;
    }
}
