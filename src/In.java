/*
Acompanha todas as entradas do mouse e do teclado
Três estados diferentes são identificados:
pressed - somente no frame em que foi apertado
held - sempre que está apertado
released - somente no frame em que foi solto
 */

import org.lwjgl.input.Mouse;
import org.lwjgl.input.Keyboard;
import java.util.*;

abstract class In {

    private static Map<String, Integer> buttons = new HashMap<>();
    private static Set<String> allButtons = new HashSet<>();
    private static Set<String> pressedButtons = new HashSet<>();
    private static Set<String> heldButtons = new HashSet<>();
    private static Set<String> releasedButtons = new HashSet<>();

    private static Map<String, Integer> keys = new HashMap<>();
    private static Set<String> allKeys = new HashSet<>();
    private static Set<String> pressedKeys = new HashSet<>();
    private static Set<String> heldKeys = new HashSet<>();
    private static Set<String> releasedKeys = new HashSet<>();

    static int[] getMouse(){
        return new int[] {Mouse.getX(), Game.height - Mouse.getY()};
    }

    static boolean mouseIsOver(int[] area){ //x, y, width, height
        int[] mouse = getMouse();
        return area[0] < mouse[0] && mouse[0] < area[0] + area[2] && area[1] < mouse[1] && mouse[1] < area[1] + area[3];
    }

    static boolean buttonPressed(String button){
        return pressedButtons.contains(button);
    }

    static boolean buttonHeld(String button){
        return heldButtons.contains(button);
    }

    static boolean buttonReleased(String button){
        return releasedButtons.contains(button);
    }

    static boolean keyPressed(String key){
        return pressedKeys.contains(key);
    }

    static boolean keyHeld(String key){
        return heldKeys.contains(key);
    }

    static boolean keyReleased(String key){
        return releasedKeys.contains(key);
    }

    //traduz todas as entradas
    static void init(){
        buttons.put("lmb", 0);
        buttons.put("rmb", 1);
        buttons.put("mmb", 2);
        buttons.put("mb4", 3);
        buttons.put("mb5", 4);
        allButtons = buttons.keySet();

        keys.put("up", Keyboard.KEY_UP);
        keys.put("down", Keyboard.KEY_DOWN);
        keys.put("left", Keyboard.KEY_LEFT);
        keys.put("right", Keyboard.KEY_RIGHT);
        keys.put("w", Keyboard.KEY_W);
        keys.put("a", Keyboard.KEY_A);
        keys.put("s", Keyboard.KEY_S);
        keys.put("d", Keyboard.KEY_D);
        keys.put("space", Keyboard.KEY_SPACE);
        keys.put("escape", Keyboard.KEY_ESCAPE);
        keys.put("lshift", Keyboard.KEY_LSHIFT);
        keys.put("tab", Keyboard.KEY_TAB);
        keys.put("q", Keyboard.KEY_Q);
        keys.put("e", Keyboard.KEY_E);
        keys.put("r", Keyboard.KEY_R);
        keys.put("o", Keyboard.KEY_O);
        keys.put("m", Keyboard.KEY_M);
        keys.put("n", Keyboard.KEY_N);
        keys.put("g", Keyboard.KEY_G);
        keys.put("h", Keyboard.KEY_H);
        keys.put("t", Keyboard.KEY_T);
        keys.put("y", Keyboard.KEY_Y);
        keys.put("i", Keyboard.KEY_I);
        allKeys = keys.keySet();
    }

    //atualiza todas as teclas e botões pressionados, segurados e soltos
    static void update(){
        releasedButtons.clear();
        releasedKeys.clear();
        pressedButtons.clear();
        pressedKeys.clear();

        for(String button : heldButtons){
            if(!Mouse.isButtonDown(buttons.get(button))) {
                releasedButtons.add(button);
            }
        }
        for(String key : heldKeys) {
            if (!Keyboard.isKeyDown(keys.get(key))) {
                releasedKeys.add(key);
            }
        }

        for(String button : releasedButtons) heldButtons.remove(button);
        for(String key : releasedKeys) heldKeys.remove(key);

        for(String button : allButtons){
            if(Mouse.isButtonDown(buttons.get(button)) && !heldButtons.contains(button)){
                pressedButtons.add(button);
                heldButtons.add(button);
            }
        }

        for(String key : allKeys){
            if(Keyboard.isKeyDown(keys.get(key)) && !heldKeys.contains(key)){
                pressedKeys.add(key);
                heldKeys.add(key);
            }
        }
    }
}