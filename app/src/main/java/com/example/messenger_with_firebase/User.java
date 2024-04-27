package com.example.messenger_with_firebase;

public class User {


    private String id;
    private String name;
    private String surname;
    private int age;
    private boolean online;

    public User() {
    }

    public User(String id, String name, String surname, int age, boolean online) {
        this.id = id;
        this.name = name;
        this.surname = surname;
        this.age = age;
        this.online = online;
    }

    public String getName() {
        return name;
    }

    public String getSurname() {
        return surname;
    }

    public int getAge() {
        return age;
    }

    public String getId() {
        return id;
    }

    public boolean isOnline() {
        return online;
    }

    @Override
    public String toString() {
        return "User{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", surname='" + surname + '\'' +
                ", age=" + age +
                ", online=" + online +
                '}';
    }
}
