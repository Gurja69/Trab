import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Vector2f;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;
import java.util.ArrayList;
import java.lang.reflect.*;

class Play extends BasicGameState {
    static Larry larry;
    private Camera camera;
    private int stateid;
    private ArrayList<Entity> entities;
    private MiniMap minimap;
    Mapa map, cave, currentmap;
    private Hud hud;
    private Image inventoryHud;


    Play(int id){
        stateid = id;
    }

    @Override
    public void init(GameContainer gc, StateBasedGame sbg) throws SlickException {
        String[] larryEquip = {"Larry", "Brown Shoes", "Blue Pants", "White Shirt" ,"Short Sword Male", "Spear", "Bow"}; // Usando NPC Lara
        In.init();
        map = new Mapa("map/mapa.tmx", "map/mapa2.tmx");
        map.init();
        map.setEnemySpawnPosition(new int[][]{{20, 9}, {20, 25}, {43, 15}, {41, 24}});
        cave = new Mapa("map/cave.tmx", "map/cave2.tmx");
        cave.init();
        cave.setEnemySpawnPosition(new int[][]{{47, 31}, {43, 47}, {1, 47}, {0, 20}, {4, 14}, {8, 14}, {8, 0}, {24, 0}, {47, 0}, {47, 27}, {21, 24}, {21, 29}});
        currentmap = map;

        hud = new Hud("img/lifeHud.png");
        camera = new Camera();
        minimap = new MiniMap(new Image("map/mapa128.png"), map);
        entities = new ArrayList<>();
        //declarar na ordem BEHIND, BODY, FEET, LEGS, TORSO, BELT, HEAD, HANDS, WEAPONS
        larry = new Larry(100,128, larryEquip, new int[]{5, 5, 15});
        entities.add(larry);
        inventoryHud = new Image("img/inventoryHud.png");
        map.spawnEnemies(entities);

    }

    @Override
    public void render(GameContainer gc, StateBasedGame sbg, Graphics g) throws SlickException {

        camera.render(g, larry, currentmap);
        currentmap.renderWithEntities(entities, g);
        minimap.render(g, camera, larry);
        larry.getInventory().render(camera.getX(), camera.getY());
        hud.render(camera.getX(), camera.getY(), larry.hp, larry.getInventory().getGold());
        DamageBox.render(g);

    }

    @Override
    public void update(GameContainer gc, StateBasedGame sbg, int delta) throws SlickException {

        In.update();
        Entity.deleteDeadEntities(entities);
        DamageBox.update(entities, delta);
        Entity.update(entities, delta, currentmap, larry);

        if (In.keyPressed("escape")) sbg.enterState(0);

        if (In.keyPressed("h")) larry.getInventory().setItem(7, "Recurve Bow", larry);


        if (larry.getX() > 41*32 & larry.getX() < 43*32){
            if (larry.getY() < 4*32 & currentmap == map){
                Entity.clearArea(entities);
                currentmap = cave;
                minimap.setMinimap(new Image("map/cave128.png"), cave);
                cave.spawnEnemies(entities);
                larry.setpos(47,40);

            }
        }

        if (larry.getX() > 21*32 & larry.getX() < 23*32){
            if (larry.getY() < 24*32 && larry.getY() > 22*32 && currentmap == cave){
                sbg.enterState(2);
            }
        }

        if (In.buttonReleased("rmb")) larry.setpos((int) (camera.getX() + In.getMouse()[0] - 16)/32,(int) (camera.getY() + In.getMouse()[1] - 32)/32);

        if (larry.getDied()){
            sbg.enterState(3);
        }
    }

    @Override
    public int getID() {
        return stateid;
    }
}