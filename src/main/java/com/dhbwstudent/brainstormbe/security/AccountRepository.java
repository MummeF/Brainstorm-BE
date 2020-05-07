package com.dhbwstudent.brainstormbe.security;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class AccountRepository {
    private List<Account> accounts = new ArrayList<>();
    public AccountRepository(){
        this.init();
    }

    private void init(){
        this.addAcc(Account.builder()
                .name("fe-tech-user")
                .password("+vq#3RL!ygE%f&HLM?t_")
                .role(Role.Admin)
                .build());
    }

    public void addAcc(Account acc){
        this.accounts.add(acc);
    }

    public boolean removeAccByName(String name){
        for (int i = 0; i < this.accounts.size(); i++) {
            if(accounts.get(i).getName().equals(name)){
                accounts.remove(i);
                return true;
            }
        }
        return false;
    }

    public Account findByUserName(String name){
        for (int i = 0; i < this.accounts.size(); i++) {
            if(accounts.get(i).getName().equals(name)){
                return accounts.get(i);
            }
        }
        return null;
    }
}
