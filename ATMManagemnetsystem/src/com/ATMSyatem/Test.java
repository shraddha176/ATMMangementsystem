package com.ATMSyatem;
import java.io.Console;
public class Test {
    public static void main(String[] args) {

        Console console = System.console();

        if (console == null) {
            System.out.println("Console NOT Available");
        } else {
            System.out.println("Console Available");
        }
    }
}