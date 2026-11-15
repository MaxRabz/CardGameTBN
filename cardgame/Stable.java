package cardgame;
public class Stable {
    public enum Type {
        Grass,
        Fire,
        Water,
        Ground,
        Steel,
        Bug,
        None
    }

    public enum Condition {
        Paralyze,
        Burn,
        Sleep,
        Confusion,
        Skip,
        Poision,
        None
    }

    public enum Effect {
        None,
        Backrow,
        Condition,
        Double,
        Swap,
        Recoil,
        Heal
    }
}