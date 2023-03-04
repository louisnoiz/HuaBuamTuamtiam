import java.awt.*;
import java.util.Timer;
import java.util.TimerTask;
public class Score extends Rectangle{
	static int GAME_WIDTH;
	static int GAME_HEIGHT;
	private int sec, min, hour, min2,sec2;
	int player1;
	int player2;
        int num = 61;
	final Timer timer = new Timer();
	Score(int GAME_WIDTH, int GAME_HEIGHT){
		Score.GAME_WIDTH = GAME_WIDTH;
		Score.GAME_HEIGHT = GAME_HEIGHT;
	}
	public void draw(Graphics g) {

                
                 
            timer.scheduleAtFixedRate(new TimerTask() {
                int i = 60;
                public void run() {
                    if (num > i){
                        num = i;
                    }
                    i--;
//                    System.out.println(i--);
                    if (i < 0){
                        timer.cancel();
                    }
                }
            }, 0, 1000);
//            System.out.println("ooo"+num);
		g.setColor(Color.white);
		g.setFont(new Font("Consolas",Font.PLAIN,40));

//		g.drawLine(GAME_WIDTH/2, 0, GAME_WIDTH/2, GAME_HEIGHT+15);
		g.drawString(String.valueOf("Timer : " + num), (GAME_WIDTH/2)+220, 65);
		g.drawString(String.valueOf(player1/10)+String.valueOf(player1%10), (GAME_WIDTH/2)-85, GAME_HEIGHT-10);
		g.drawString(String.valueOf(player2/10)+String.valueOf(player2%10), (GAME_WIDTH/2)+20, GAME_HEIGHT-10);
	}
}