package com.freshspire.api.model;

import javax.persistence.*;

@Entity
@Table(name="chains")
public class Chain {

    @Id
    @Column(name = "chainId")
    @GeneratedValue
    private int chainId;

    @Column(name = "displayName")
    private String displayName;

    public Chain() {

    }

    public Chain(String displayName) {
        this.displayName = displayName;
    }

    public int getChainId() {
        return chainId;
    }

    public void setChainId(int chainId) {
        this.chainId = chainId;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }
}
