package nl.rug.oop.rpg.worldsystem;

import nl.rug.oop.rpg.*;
import nl.rug.oop.rpg.itemsystem.*;
import nl.rug.oop.rpg.npcsystem.Enemies;
import nl.rug.oop.rpg.npcsystem.InitEntity;
import nl.rug.oop.rpg.npcsystem.NPC;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

import static nl.rug.oop.rpg.Randomizers.randomMtrl;

public class World {
    protected int links = 50;//edit for requirement
    protected Map<Room, List<Room>> roomConnects;
    private static final Randomizers roomID = new Randomizers(7, ThreadLocalRandom.current());

    public void generateRoom(World map) {
        List<Door> doors = new ArrayList<>();
        String roomId = roomID.generateId();
        Attr1room att1 = randomMtrl(Attr1room.class);
        Attr2room att2 = randomMtrl(Attr2room.class);
        Holders storage = randomMtrl(Holders.class);
        Item loot = generateItem(roomId);
        NPC npc = generateNpc();
        boolean company = npc != null;
        Room room = new InitRoom()
                .id(roomId)
                .describe(att1, att2)
                .nrdors(0)
                .lDoors(doors)
                .pComp(company)
                .gNPC(npc)
                .storage(storage)
                .lLoot(loot)
                .create();
        npc.setLocation(room);
        map.roomConnects.put(room, new ArrayList<>());
    }

    public void generateDoor(Room out, Room goin, World map) {
        DoorcolorsDb clr = randomMtrl(DoorcolorsDb.class);
        Door door = new PrmtDoor()
                .exit(out)
                .enter(goin)
                .clr(clr)
                .create();
        out.doors.add(door);
        goin.doors.add(door);
        out.cdors++;
        goin.cdors++;
        map.roomConnects.get(out).add(goin);
        map.roomConnects.get(goin).add(out);
    }

    public NPC generateNpc() {
        SpeciesDb npcdata = randomMtrl(SpeciesDb.class);
        Inventory inventory = new Inventory().generateInv();
        Enemies npc = new InitEntity() //presence of npc may be randomized
                .name(npcdata.getSpname())
                .hdm(npcdata.getHealth(),
                        npcdata.getDamage() + ThreadLocalRandom.current().nextInt(2,
                                10), ThreadLocalRandom.current().nextInt(0, 15))
                .loc(null)
                .inv(inventory)
                .ith(null)//get this man his shield
                .createn();
        return npc;
    }

    public void removeRooms(Room rA, Room rB, List<Room> allrooms) {
        if (rA.getDoors().size() < 3) {
            allrooms.remove(rA);
        } else {
            allrooms.remove(rB);
        }
    }

    public Item generateItem(String in) {
        Item item;
        if (roomID.itemInsert(in)) {
            ConsumablesDb loot = randomMtrl(ConsumablesDb.class);
            item = new ItemBuilder()
                    .name(loot.getConsid())
                    .hh(loot.getHealth())
                    .val(loot.getValue())
                    .createCons();
        } else {
            WeaponsDb loot = randomMtrl(WeaponsDb.class);
            item = new ItemBuilder()
                    .name(loot.getWname())
                    .dmg(loot.getDmg())
                    .val(loot.getValue())
                    .createWep();
        }
        return item;
    }
    public Player generatePlayer(World map) {
        Inventory inv = new Inventory().generateInv(); //verify integrity
        List<Room> listrm = new ArrayList<>(map.roomConnects.keySet());
        Player player = new InitEntity()
                .name("user")
                .hdm(100, 10, 30)
                .inv(inv)
                .loc(listrm.get(0))
                .fl(false)
                .ith(new ItemBuilder().name("Raygun").dmg(14).val(10).createWep())
                .protagonist();
        Item itemc = map.generateItem("11a");
        Item itemw = map.generateItem("Xz");
        player.getInventory().getwList().put(itemw.getName(), (Weapons) itemw);
        player.getInventory().getcList().put(itemc.getName(), (Consumables) itemc);
        return player;
    }
    public World createMap() {
        World map = new World();
        int rooms = 50;
        map.roomConnects = new HashMap<>();
        for (int i = 0; i < rooms; i++) {
            map.generateRoom(map);
        }
        List<Room> allrooms = new ArrayList<>(map.roomConnects.keySet());
        Room mutate = new Room(new InitRoom());
        for (int i = 0; i < links; ) {
            Room rA = mutate.randomRoom(allrooms);
            Room rB = mutate.randomRoom(allrooms);
            if (!rA.id.equals(rB.id) && !map.roomConnects.get(rA).contains(rB)) {
                if (rA.cdors < 3 && rB.cdors < 3) {
                    generateDoor(rA, rB, map);
                    i++;
                } else {
                    removeRooms(rA, rB, allrooms);
                }
            }
            if (allrooms.size() == 0) {
                break;
            }
        }
        return map;
    }
}