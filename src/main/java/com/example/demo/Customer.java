package com.example.demo;

public class Customer {

    private String name;
    private String age;
    private String number;

    public Customer(String name, String age, String number) {
        this.name = name;
        this.age = age;
        this.number = number;
    }

    public Customer() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }
}