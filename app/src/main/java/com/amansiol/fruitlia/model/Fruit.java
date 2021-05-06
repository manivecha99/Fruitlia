package com.amansiol.fruitlia.model;

public class Fruit {
    String name;
    int cal;
    String[] nutrients;
    int imageid;
    public Fruit() {
    }

    public Fruit(String name, int cal, String[] nutrients, int imageid) {
        this.name = name;
        this.cal = cal;
        this.nutrients = nutrients;
        this.imageid = imageid;
    }

    public int getImageid() {
        return imageid;
    }

    public void setImageid(int imageid) {
        this.imageid = imageid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCal() {
        return cal;
    }

    public void setCal(int cal) {
        this.cal = cal;
    }

    public String[] getNutrients() {
        return nutrients;
    }

    public void setNutrients(String[] nutrients) {
        this.nutrients = nutrients;
    }
}
