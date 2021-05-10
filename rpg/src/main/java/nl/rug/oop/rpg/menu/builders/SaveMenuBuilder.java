package nl.rug.oop.rpg.menu.builders;

import nl.rug.oop.rpg.game.savingsys.QuickSavingLoading;
import nl.rug.oop.rpg.game.savingsys.SavingLoading;
import nl.rug.oop.rpg.menu.options.SaveMenuOptions;
import nl.rug.oop.rpg.worldsystem.Player;
import nl.rug.oop.rpg.worldsystem.WorldInteraction;

import java.lang.reflect.Method;
import java.util.HashMap;

public class SaveMenuBuilder {
    QuickSavingLoading qksave = new QuickSavingLoading();
    SavingLoading saveload = new SavingLoading();
    WorldInteraction winter = new WorldInteraction();

    public MenuTree setMainCommands() throws NoSuchMethodException {
        HashMap<String, Method> options = new HashMap<>();
        options.put("a", qksave.getClass().getMethod("saveSave", Player.class));
        options.put("b", qksave.getClass().getMethod("setupLoad", Player.class));
        options.put("c", saveload.getClass().getMethod("saveSave", Player.class));
        options.put("d", saveload.getClass().getMethod("setupLoad", Player.class));
        options.put("back", winter.getClass().getMethod("goBack", Player.class));
        return new MenuNodeBuilder().root(null).mNode(options).subM(new HashMap<>()).
                optL(SaveMenuOptions.getSaveOpt()).buildtree();
    }
}