package nl.rug.oop.rpg.itemsystem;
import nl.rug.oop.rpg.worldsystem.Player;
import nl.rug.oop.rpg.worldsystem.Room;
import nl.rug.oop.rpg.worldsystem.WorldInteraction;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.EnumMap;
import java.util.Scanner;


public class ItemInteraction extends WorldInteraction implements ItemCommands {
    public ItemInteraction() {
    }

    public void itemCheck(Player x) {
    }

    public void inspectItem(Player x) {
        Room r = x.getLocation();
        r.getLoot().inspect(r);
    }


    public void pickItem(Player x) {
        Room r = x.getLocation();
        r.getLoot().interact(x, 0);
    }


    public void consumeItem(Player x) {
        Room r = x.getLocation();
        x.setHold(r.getLoot());
        r.getLoot().Recycle(x);
    }


    public void purgeItem(Player x) {
        Room r = x.getLocation();
        r.getLoot().interact(x, 0);
    }


    public void consumeInvItem(Player x) {
        Room r = x.getLocation();
        x.setHold(r.getLoot());
        r.getLoot().Recycle(x);

    }


    public void equipInvItem(Player x) {
        Room r = x.getLocation();
        x.setHold(r.getLoot());
    }


    public void recycleItem(Player x) {
    }
}
