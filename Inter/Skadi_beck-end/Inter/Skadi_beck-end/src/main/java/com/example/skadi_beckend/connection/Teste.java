package com.example.skadi_beckend.connection;

import io.github.cdimascio.dotenv.Dotenv;

public class Teste {
    public static void main(String[] args) {
        Dotenv dotenv = Dotenv.load();
        System.out.println(dotenv.get("POSTGRES_URL"));
    }
        
    
}
