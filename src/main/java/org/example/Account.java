package org.example;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Account {
    private final String login, password, pin;

    public Account(String login, String password, String pin) {
        this.login = login;
        this.password = password;
        this.pin = pin;
    }

    public static List<Account> loadAccountFromFile(String filePath, int amount) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(filePath));
        List<Account> accounts = new ArrayList<>();
        for (int i = 0; i < amount; i++) {
            String login = reader.readLine();
            String password = reader.readLine();
            String pin = reader.readLine();
            accounts.add(new Account(login, password, pin));
        }
        reader.close();
        return accounts;
    }


    public String getLogin() {
        return login;
    }

    public String getPassword() {
        return password;
    }

    public String getPin() {
        return pin;
    }
}
