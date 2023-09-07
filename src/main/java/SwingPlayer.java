import component.LandingFrame;

public class SwingPlayer {
    public static void main(String[] args) {
        System.setProperty("awt.useSystemAAFontSettings","on");
        System.setProperty("swing.aatext", "true");
        LandingFrame login = new LandingFrame();
        login.init();
    }
}
