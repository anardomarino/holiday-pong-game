import javax.swing.JFrame;
public class Pong extends JFrame{
    public Pong(){
        add(new Game());
        setTitle("Pong");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 500);
        setVisible(true);
        setResizable(false);
        setLocation(200, 200);
    }
    public static void main(String[] args){
        new Pong();
    }
}