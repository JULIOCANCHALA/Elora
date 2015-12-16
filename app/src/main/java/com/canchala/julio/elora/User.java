package com.canchala.julio.elora;

public class User {
    private String Nombre;
    private int Edad;
    private int Cedula;


    public User(String nombre, int edad, int cedula) {
        Nombre = nombre;
        Edad = edad;
        Cedula = cedula;
    }

    public String getNombre() {
        return Nombre;
    }

    public int getEdad() {
        return Edad;
    }

    public int getCedula() {
        return Cedula;
    }
}