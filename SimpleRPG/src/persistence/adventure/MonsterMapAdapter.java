package persistence.adventure;

import business.entities.monster.Boss;
import business.entities.monster.Monster;
import business.entities.monster.StandardMonster;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * This class is a TypeAdapter for a map of Monsters and their Integer values.
 * It provides methods for reading and writing the map to and from JSON.
 */
public class MonsterMapAdapter extends TypeAdapter<Map<Monster, Integer>> {
    private static final String MONSTER = "monster";
    private static final String VALUE = "value";
    private static final String NAME = "name";
    private static final String CHALLENGE = "challenge";
    private static final String EXPERIENCE = "experience";
    private static final String HIT_POINTS = "hitPoints";
    private static final String INITIATIVE = "initiative";
    private static final String DAMAGE_DICE = "damageDice";
    private static final String DAMAGE_TYPE = "damageType";
    private static final String BOSS = "Boss";

    /**
     * Writes the given map of Monsters and their Integer values to a JSON writer.
     *
     * @param out The JSON writer to write the map to.
     * @param map The map of Monsters and their Integer values to be written.
     * @throws IOException If an error occurs while writing to the JSON writer.
     */
    @Override
    public void write(JsonWriter out, Map<Monster, Integer> map) throws IOException {
        out.beginArray();

        for (Map.Entry<Monster, Integer> entry : map.entrySet()) {
            out.beginObject();

            out.name(MONSTER);
            writeMonster(out, entry.getKey());
            out.name(VALUE).value(entry.getValue());

            out.endObject();
        }

        out.endArray();
    }

    /**
     * Reads a map of Monsters and their Integer values from a JSON reader.
     *
     * @param in The JSON reader to read the map from.
     * @return The map of Monsters and their Integer values read from the JSON reader.
     * @throws IOException If an error occurs while reading from the JSON reader.
     */
    @Override
    public Map<Monster, Integer> read(JsonReader in) throws IOException {
        in.beginArray();

        Map<Monster, Integer> map = new LinkedHashMap<>();

        while (in.hasNext()) {
            in.beginObject();

            Monster monster = null;
            int value = 0;

            while (in.hasNext()) {
                switch (in.nextName()) {
                    case MONSTER -> monster = readMonster(in);
                    case VALUE -> value = in.nextInt();
                }
            }
            map.put(monster, value);
            in.endObject();
        }

        in.endArray();

        return map;
    }

    /**
     * Writes a Monster object to a JSON writer.
     *
     * @param out The JSON writer to write the Monster to.
     * @param monster The Monster object to be written.
     * @throws IOException If an error occurs while writing to the JSON writer.
     */
    private void writeMonster(JsonWriter out, Monster monster) throws IOException {
        out.beginObject();

        out.name(NAME).value(monster.getName());
        out.name(CHALLENGE).value(monster.getChallenge());
        out.name(EXPERIENCE).value(monster.getExperience());
        out.name(HIT_POINTS).value(monster.getHitPoints());
        out.name(INITIATIVE).value(monster.getInitiative());
        out.name(DAMAGE_DICE).value(monster.getDamageDice());
        out.name(DAMAGE_TYPE).value(monster.getDamageType());

        out.endObject();
    }

    /**
     * Reads a Monster object from a JSON reader.
     *
     * @param in The JSON reader to read the Monster from.
     * @return The Monster object read from the JSON reader.
     * @throws IOException If an error occurs while reading from the JSON reader.
     */
    private Monster readMonster(JsonReader in) throws IOException {
        in.beginObject();

        String name = null;
        String challenge = null;
        int experience = 0;
        int hp = 0;
        int initiative = 0;
        String damageDice = null;
        String damageType = null;

        while (in.hasNext()) {
            switch (in.nextName()) {
                case NAME -> name = in.nextString();
                case CHALLENGE -> challenge = in.nextString();
                case EXPERIENCE -> experience = in.nextInt();
                case HIT_POINTS -> hp = in.nextInt();
                case INITIATIVE -> initiative = in.nextInt();
                case DAMAGE_DICE -> damageDice = in.nextString();
                case DAMAGE_TYPE -> damageType = in.nextString();
            }
        }

        Monster monster;
        if (BOSS.equals(challenge)) {
            monster = new Boss(name, challenge, experience, hp, initiative, damageDice, damageType);
        } else {
            monster = new StandardMonster(name, challenge, experience, hp, initiative, damageDice, damageType);
        }
        in.endObject();

        return monster;
    }
}
