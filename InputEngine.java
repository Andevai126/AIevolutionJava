import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class InputEngine implements KeyListener{
    
    @Override
    public void keyPressed(KeyEvent e){
        Global.keystatus.set(e.getKeyCode(), true);
        // System.out.println(e.getKeyChar() + " = " + e.getKeyCode());
        if (e.getKeyCode() == KeyEvent.VK_ESCAPE){
            // Global.mouseLock = false;
            //some code to change gamemodes (in global) for example
        }
        if (e.getKeyCode() == KeyEvent.VK_L){
            // Global.mouseLock = true;
        }
    }

    @Override
    public void keyReleased(KeyEvent e){
        Global.keystatus.set(e.getKeyCode(), false);
    }

    @Override
    public void keyTyped(KeyEvent e){}
}
