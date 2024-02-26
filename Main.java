package org.example;

import entities.Personne;
import service.PersonneService;
import utils.DataSource;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Main {



    public static void main(String[] args) {

        Personne p1=new Personne("3A16","Elyes");

        PersonneService ps=new PersonneService();
      //  ps.add(p1);
       // ps.addPst(p1);

        ps.readAll().forEach(System.out::println);
    }
}