package com.blackcubes.skarger.goldmine.backend.model;

public enum Booster {
    SHOVEL(1, 10, 1.122f),
    PICK(2, 35, 1.124f),
    PERFORATOR(7, 100, 1.125f),
    JACKHAMMER(15, 250, 1.125f),
    DRILLING_MACHINE(35, 700, 1.125f),
    ROADHEADER(65, 1500, 1.124f),
    TUNNEL_BORING_COMPLEX(125, 3500, 1.124f),
    DWARF(250, 8000, 1.124f);

    private final int boostAmount;
    private final int priceOfFirst;
    private final float priceIncrease;

    Booster(int boostAmount, int priceOfFirst, float priceIncrease) {
        this.boostAmount = boostAmount;
        this.priceOfFirst = priceOfFirst;
        this.priceIncrease = priceIncrease;
    }

    public int boostAmount() {
        return boostAmount;
    }

    public int priceOfFirst() {
        return priceOfFirst;
    }

    public float priceIncrease() {
        return priceIncrease;
    }
}
