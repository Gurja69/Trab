import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.tiled.TiledMap;

import java.util.ArrayList;

public class Play extends BasicGameState {

    private boolean[][] blocked;
    private TiledMap map;
    private Camera camera;
    private int mapHeight, mapWidth;
    private int tileHeight, tileWidth;
    private int stateid;
    private float mx = 0, my = 0;
    private ArrayList<Entity> entities;
    private Rectangle minimapRect = new Rectangle(0,0,10,10);

    Play(int id){
        stateid = id;
    }

    @Override
    public int getID() {
        return stateid;
    }

    @Override
    public void init(GameContainer gc, StateBasedGame sbg) throws SlickException {
        map = new TiledMap("map/mapa.tmx");
        mapWidth = map.getWidth() * map.getTileWidth();
        mapHeight = map.getHeight() * map.getTileHeight();
        tileHeight = map.getTileHeight();
        tileWidth = map.getTileWidth();
        Items.init();
        In.init();
        entities = new ArrayList<>();
        //declarar na ordem BEHIND, BODY, FEET, LEGS, TORSO, BELT, HEAD, HANDS, DON'T PLACE WEAPONS HERE
        entities.add(new Larry(32,128, new String[]{"quiver", "male body", "armor shoes", "green pants", "white shirt", "rope belt", "blonde hair"}));
        entities.add(new Skeleton(1000, 1000, new int[]{1, 1, 1, 1},new String[]{"skeleton body", "armor shoes", "armor pants", "plate armor"}));
        camera = new Camera(mapWidth, mapHeight);
        blocked = new boolean[map.getWidth()][map.getHeight()];
        initBlocks();

    }

    @Override
    public void render(GameContainer gc, StateBasedGame sbg, Graphics g) throws SlickException {
        camera.translate(g, entities.get(0));
        map.render(0, 0);

        float[] hitbox;
        for(Entity e : entities) {
            e.render();
            hitbox = e.hitbox();
            g.drawOval(hitbox[0] - hitbox[2], hitbox[1] - hitbox[2], 2 * hitbox[2], 2 * hitbox[2]);
        }

        for(int x=0; x < map.getWidth(); x++){
            for(int y=0; y < map.getHeight(); y++){
                if(blocked[x][y]){
                    g.drawRect((float) x * tileWidth, (float) y * tileHeight, (float) tileWidth, (float) tileHeight);
                }
            }
        }
        g.drawImage(new Image("img/lifeHud.png"),camera.getX(),camera.getY() + Game.height - 64);
        g.drawImage(new Image("map/mapa128.png"),camera.getX() + Game.width - 128, camera.getY() + Game.height - 128);
        //g.draw(minimapRect);
        g.drawRect(camera.getX() + mx - 128 + Game.width,camera.getY() + my - 128 + Game.height,1,1);
    }

    @Override
    public void update(GameContainer gc, StateBasedGame sbg, int delta) throws SlickException {
        In.update();
        mx = entities.get(0).getX()/(mapWidth/128);
        my = entities.get(0).getY()/(mapHeight/128);
        minimapRect.setBounds(camera.getCameraRect().getX() + Game.width - 128, camera.getCameraRect().getY() + Game.height - 128,(camera.getCameraRect().getWidth()/128),(camera.getCameraRect().getHeight()/128));
        for(Entity e: entities) e.update(gc, delta, this);


        if (In.keyPressed("escape")) {
            sbg.enterState(0);
        }

        if (In.keyPressed("tab")){
            entities.get(0).setpos(32,128);
        }



    }

    boolean isBlocked(float x, float y, float radius) {
        int xBlock0 = (int) ((x-radius) / tileWidth);
        int yBlock0 = (int) ((y-radius) / tileHeight);
        int xBlock1 = (int) ((x + radius) / tileWidth);
        int yBlock1 = (int) ((y + radius) / tileHeight);
        return (blocked[xBlock0][yBlock0] || blocked[xBlock0][yBlock1] || blocked[xBlock1][yBlock0] || blocked[xBlock1][yBlock1]);
    }

    private void initBlocks() {
        for (int l = 0; l < map.getLayerCount(); l++) {
            String layerValue = map.getLayerProperty(l, "blocked", "false");

            if (layerValue.equals("true")) {
                for (int c = 0; c < map.getWidth(); c++) {
                    for (int r = 0; r < map.getHeight(); r++) {

                        if (map.getTileId(c, r, l) != 0) {
                            blocked[c][r] = true;
                        }
                    }
                }
            }
        }
    }

}