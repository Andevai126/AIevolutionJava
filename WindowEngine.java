import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

public class WindowEngine extends ComponentAdapter {
    @Override
    public void componentResized(ComponentEvent e) {
        // System.out.println("WindowSize Changed");
        Global.isSizeChanged = true;
    }

    @Override
    public void componentMoved(ComponentEvent e) {
        // System.out.println("Window Moved");
        Global.isMoved = true;
    }
}
