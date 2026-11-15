package cardgame;
import java.util.HashMap;

import cardgame.Stable.Effect;
import cardgame.Stable.Type;

public class CreateDecks {
    Card[] deck;
    Card[] deck2;

    public Card[] getDeck1() {
        return deck;
    }

    public Card[] getDeck2() {
        return deck2;
    }

    public CreateDecks() {
        deck = new Card[] {
            new Card("Emberfox", 70, new HashMap<>() {{
                put("Cinder Pounce", new Object[] {1, 40, Effect.Condition, 0});
            }}, 1, Type.Fire, Type.Grass, Type.Water),
            new Card("Tideback", 90, new HashMap<>() {{
                put("Rushing Current", new Object[] {2, 50, Effect.Backrow, 10});
                put("Shell Mend", new Object[] {1, 30, Effect.Heal, 10});
            }}, 2, Type.Water, Type.Fire, Type.Ground),
            new Card("Mossmaw", 110, new HashMap<>() {{
                put("Root Crush", new Object[] {2, 60, Effect.Double, 0});
            }}, 2, Type.Grass, Type.Water, Type.Bug),
            new Card("Ironwing", 130, new HashMap<>() {{
                put("Steel Dive", new Object[] {3, 80, Effect.Recoil, 10});
            }}, 4, Type.Steel, Type.Bug, Type.Fire),
            new Card("Burrowl", 80, new HashMap<>() {{
                put("Tunnel Jab", new Object[] {1, 40, Effect.Swap, 0});
            }}, 1, Type.Ground, Type.Steel, Type.Grass),
            new Card("Scarlet Mantis", 100, new HashMap<>() {{
                put("Petal Cutter", new Object[] {2, 60, Effect.Condition, 0});
                put("Wing Flick", new Object[] {1, 30, Effect.Backrow, 10});
            }}, 2, Type.Bug, Type.Grass, Type.Water),
            new Card("Flarehorn", 150, new HashMap<>() {{
                put("Blazing Charge", new Object[] {4, 100, Effect.Recoil, 20});
            }}, 4, Type.Fire, Type.Steel, Type.Water),
            new Card("Dewdropper", 60, new HashMap<>() {{
                put("Bubble Dart", new Object[] {1, 20, Effect.None, 0});
            }}, 1, Type.Water, Type.Bug, Type.Fire),
            new Card("Thornback", 120, new HashMap<>() {{
                put("Bramble Slam", new Object[] {2, 70, Effect.Double, 0});
                put("Spore Guard", new Object[] {1, 40, Effect.Heal, 20});
            }}, 3, Type.Grass, Type.Ground, Type.Steel),
            new Card("Copper Beetle", 90, new HashMap<>() {{
                put("Gear Snap", new Object[] {2, 50, Effect.Swap, 0});
            }}, 2, Type.Steel, Type.Fire, Type.Bug),
            new Card("Quakehorn", 140, new HashMap<>() {{
                put("Faultline Stomp", new Object[] {3, 90, Effect.Backrow, 20});
            }}, 4, Type.Ground, Type.Water, Type.Grass),
            new Card("Pollen Pixie", 70, new HashMap<>() {{
                put("Nectar Dust", new Object[] {1, 30, Effect.Condition, 0});
                put("Glimmer Heal", new Object[] {1, 20, Effect.Heal, 10});
            }}, 1, Type.Bug, Type.Grass, Type.Steel),
            new Card("Ashen Drake", 130, new HashMap<>() {{
                put("Inferno Talon", new Object[] {3, 80, Effect.Double, 0});
            }}, 3, Type.Fire, Type.Ground, Type.Water),
            new Card("Glacier Toad", 110, new HashMap<>() {{
                put("Frosty Splash", new Object[] {2, 60, Effect.Swap, 0});
                put("Cool Down", new Object[] {1, 40, Effect.Heal, 10});
            }}, 3, Type.Water, Type.Steel, Type.Fire),
            new Card("Boulder Badger", 120, new HashMap<>() {{
                put("Granite Claw", new Object[] {2, 70, Effect.Recoil, 10});
            }}, 3, Type.Ground, Type.Bug, Type.Grass),
        };
        deck2 = new Card[] {
            new Card("Cinder Lynx", 80, new HashMap<>() {{
                put("Soot Swipe", new Object[] {1, 40, Effect.Condition, 0});
                put("Coal Flick", new Object[] {1, 30, Effect.None, 0});
            }}, 1, Type.Fire, Type.Steel, Type.Water),
            new Card("Marshshell", 100, new HashMap<>() {{
                put("Bog Surge", new Object[] {2, 60, Effect.Backrow, 10});
                put("Reed Restore", new Object[] {1, 40, Effect.Heal, 10});
            }}, 2, Type.Water, Type.Ground, Type.Fire),
            new Card("Canopy Stag", 130, new HashMap<>() {{
                put("Antler Break", new Object[] {3, 80, Effect.Double, 0});
                put("Leaf Shelter", new Object[] {1, 30, Effect.Heal, 20});
            }}, 3, Type.Grass, Type.Bug, Type.Steel),
            new Card("Brass Falcon", 120, new HashMap<>() {{
                put("Rivet Dive", new Object[] {2, 70, Effect.Recoil, 10});
            }}, 3, Type.Steel, Type.Grass, Type.Fire),
            new Card("Dustmole", 70, new HashMap<>() {{
                put("Silt Burrow", new Object[] {1, 30, Effect.Swap, 0});
                put("Pebble Toss", new Object[] {1, 20, Effect.None, 0});
            }}, 1, Type.Ground, Type.Steel, Type.Water),
            new Card("Amber Cicada", 90, new HashMap<>() {{
                put("Resonant Chirp", new Object[] {2, 50, Effect.Condition, 0});
            }}, 2, Type.Bug, Type.Grass, Type.Ground),
            new Card("Pyrestone Ram", 150, new HashMap<>() {{
                put("Magma Headbutt", new Object[] {4, 100, Effect.Recoil, 20});
                put("Scorch Trail", new Object[] {2, 60, Effect.Backrow, 10});
            }}, 4, Type.Fire, Type.Water, Type.Steel),
            new Card("Rainfin", 60, new HashMap<>() {{
                put("Drizzle Shot", new Object[] {1, 20, Effect.Condition, 0});
            }}, 1, Type.Water, Type.Fire, Type.Bug),
            new Card("Brambleback", 140, new HashMap<>() {{
                put("Thicket Crash", new Object[] {3, 90, Effect.Backrow, 20});
                put("Sap Recovery", new Object[] {1, 40, Effect.Heal, 20});
            }}, 4, Type.Grass, Type.Ground, Type.Water),
            new Card("Ironclad Roach", 100, new HashMap<>() {{
                put("Carapace Bash", new Object[] {2, 60, Effect.Swap, 0});
                put("Mandible Pinch", new Object[] {1, 30, Effect.None, 0});
            }}, 2, Type.Steel, Type.Bug, Type.Fire),
            new Card("Rumble Tortoise", 150, new HashMap<>() {{
                put("Canyon Quake", new Object[] {4, 100, Effect.Double, 0});
            }}, 4, Type.Ground, Type.Water, Type.Grass),
            new Card("Velvet Moth", 80, new HashMap<>() {{
                put("Dusting Flutter", new Object[] {1, 40, Effect.Condition, 0});
                put("Silk Patch", new Object[] {1, 20, Effect.Heal, 10});
            }}, 1, Type.Bug, Type.Grass, Type.Steel),
            new Card("Searclaw", 110, new HashMap<>() {{
                put("Kindle Rend", new Object[] {3, 80, Effect.Double, 0});
                put("Warmup Swipe", new Object[] {1, 30, Effect.None, 0});
            }}, 3, Type.Fire, Type.Ground, Type.Water),
            new Card("Mistfin", 100, new HashMap<>() {{
                put("Foggy Undertow", new Object[] {2, 70, Effect.Swap, 0});
                put("Mist Veil", new Object[] {1, 40, Effect.Backrow, 10});
            }}, 2, Type.Water, Type.Steel, Type.Fire),
            new Card("Granite Scorpion", 130, new HashMap<>() {{
                put("Faultline Sting", new Object[] {3, 80, Effect.Condition, 0});
                put("Stone Guard", new Object[] {1, 40, Effect.Recoil, 10});
            }}, 3, Type.Ground, Type.Bug, Type.Grass),
        };
    }
}
