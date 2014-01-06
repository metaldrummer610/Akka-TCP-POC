package org.icechamps.common;

import java.io.Serializable;

/**
 * A person
 *
 * @author Robert.Diaz
 * @since 1.0, 01/06/2014
 */
public class Person implements Serializable {
    private String firstName;
    private String lastName;
    private int age;

    public Person() {
    }

    public Person(int age, String firstName, String lastName) {
        this.age = age;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
}
