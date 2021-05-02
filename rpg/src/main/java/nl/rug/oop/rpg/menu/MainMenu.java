package nl.rug.oop.rpg.menu;

import nl.rug.oop.rpg.game.Gameplay;
import nl.rug.oop.rpg.Typewriter;

import java.util.Scanner;

public class MainMenu {


    public void initMenu() {
        Typewriter tw = new Typewriter();
        Scanner rdtxt = new Scanner(System.in);
   //     tw.type("Initiating porting with remote node..."); //move all to materials
   //     tw.poeticPause("Routing in progress...", 2000);
 //       for (int i = 0; i < 3; i++) {
  //          tw.poeticPause(".", 1000);
  //      }
  //      tw.type("Connection succesfully established");
  //      tw.poeticPause("", 0);
       tw.poeticPause("(y) PROCEED?", 0);
       tw.poeticPause("(n) Exit", 0);

        String input = rdtxt.nextLine();
        if (input.equals("y")) {
            Gameplay start = new Gameplay();
            start.Launch(rdtxt);
        } else {
            rdtxt.close();
            System.exit(0);
        }
        rdtxt.close();
    }
}