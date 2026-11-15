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
        Poison,
        None
    }

    public enum Effect {
        None,
        Backrow,
        Paralyze,
        Burn,
        Sleep,
        Confusion,
        Skip,
        Poison,
        Double,
        Swap,
        Recoil,
        Heal
    }
}