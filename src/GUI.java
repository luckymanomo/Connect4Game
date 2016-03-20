import java.awt.Color;
import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.Toolkit;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.util.Date;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.OverlayLayout;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;


public class GUI extends JFrame{
	/**
	 * @author luckymanomo
	 */
	private static final long serialVersionUID = -366066146996471172L;
	JFrame jf;
	JPanelStage jpStage;
	JButton jbDrawMoving;
	JPanel jpMain,jp1,jp2;
	int i=0;
	static final int STARTED_X=10;
	static final int STARTED_Y=50;
	static final int STARTED_IMAGE_X=STARTED_X+55;
	static final int STARTED_IMAGE_Y=STARTED_Y+6;
	
	int jfWidth=550;
	int jfHeight=500;
	ImageIcon imPlate1,imPlate2;
	ImageIcon imMYellow,imMRed;
	int xPoint=0;
	int yPoint=0;
	
	static final int TABLE_ROW=6;
	static final int TABLE_COLUMN=7;
	JLabelMedal[][] jLabelMedalArray=new JLabelMedal[TABLE_ROW][TABLE_COLUMN];
	
	static final int STARTED_MEDAL_X=95;
	static final int STARTED_MEDAL_Y=67;
	
	static final int MOVING_MEDAL_XY=56;
	
	int positionMarked=-1;
	static final int MEDAL_YELLOW=0;
	static final int MEDAL_RED=1;
	
	boolean toggleTurn=false;
	public GUI(){
		super();
		this.jf = this;
		
		imPlate1=new ImageIcon(Toolkit.getDefaultToolkit().getImage(getClass().getResource("connect4Plate1.png")));
		imPlate2=new ImageIcon(Toolkit.getDefaultToolkit().getImage(getClass().getResource("connect4Plate2.png")));
		
		imMRed=new ImageIcon(Toolkit.getDefaultToolkit().getImage(getClass().getResource("medalRed.png")));
		imMYellow=new ImageIcon(Toolkit.getDefaultToolkit().getImage(getClass().getResource("medalYellow.png")));
		
		jpMain=new JPanel();
		jp1=new JPanel(){
			private static final long serialVersionUID = 1L;
			@Override
			protected void paintComponent(Graphics g) {
				// TODO Auto-generated method stub
				super.paintComponent(g);
				g.drawImage(imPlate2.getImage(),STARTED_IMAGE_X,STARTED_Y,imPlate2.getIconWidth(),imPlate2.getIconHeight(),null);
				//System.out.println("Repaint jp1:"+i++);
				
			}
		};
		jp1.setOpaque(false);
		jp2=new JPanel(){
			private static final long serialVersionUID = 1L;
			@Override
			protected void paintComponent(Graphics g) {
				// TODO Auto-generated method stub
				super.paintComponent(g);
				g.drawImage(imPlate1.getImage(),STARTED_X,STARTED_IMAGE_Y,imPlate1.getIconWidth(),imPlate1.getIconHeight(),null);
				//System.out.println("Repaint jp2:"+i++);
				
			}
		};
		jp2.setOpaque(false);
		
		jpStage=new JPanelStage();
		
		
		OverlayLayout ol=new OverlayLayout(jpMain);
		jpMain.setLayout(ol);
		
		jpMain.add(jp2);
		jpMain.add(jpStage);
		jpMain.add(jp1);
		jbDrawMoving=new JButton("Draw");
		//setFocusable(true);
		/*addKeyListener(new KeyAdapter() {

			@Override
			public void keyReleased(KeyEvent e) {
				if(e.getKeyCode()==KeyEvent.VK_UP){
					yPoint--;
				}else if(e.getKeyCode()==KeyEvent.VK_DOWN){
					yPoint++;
				}else if(e.getKeyCode()==KeyEvent.VK_LEFT){
					xPoint--;
				}else if(e.getKeyCode()==KeyEvent.VK_RIGHT){
					xPoint++;
				}
				jpStage.removeAll();
				JLabel jlabel=new JLabelMedal(xPoint,yPoint,MEDAL_YELLOW);
				jpStage.add(jlabel);
				jpStage.repaint();
				jpStage.validate();
				System.out.println("Position: x:"+xPoint+", y:"+yPoint);
			}
		});*/
		
		addMouseMotionListener(new MouseMotionAdapter() {
			@Override
			public void mouseMoved(MouseEvent e) {
				// TODO Auto-generated method stub
				int y=STARTED_IMAGE_Y+20; //76
				int yEnd=y+354;
				int yp=(int)e.getPoint().getY();
				int x=STARTED_IMAGE_X+30; //95
				int xp=(int)e.getPoint().getX();
				
				boolean hasMarked=false;
				for(int i=0;i<TABLE_COLUMN;i++){
					int fixedX=x+i*MOVING_MEDAL_XY;
					if(xp>=fixedX && xp<=fixedX+(MOVING_MEDAL_XY-10) && yp>=y && yp<=yEnd){
						//System.out.println("i:"+(i+1)+", x:"+(int)e.getPoint().getX()+" ,y:"+(int)e.getPoint().getY());
						positionMarked=i;
						hasMarked=true;
						break;
					}
				}
				if(hasMarked) setCursor(new Cursor(Cursor.HAND_CURSOR));
				else {
					setCursor(null);
					positionMarked=-1;
				}
				
				
				//System.out.println("Marked:"+positionMarked);
			}
		});
		addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
				// TODO Auto-generated method stub
				if(positionMarked!=-1){
					//System.out.println("Put to column: "+positionMarked);
					for(int i=0;i<jLabelMedalArray.length;i++){
						if(jLabelMedalArray[i][positionMarked]==null){
							
							int xMarked=STARTED_MEDAL_X+(positionMarked*MOVING_MEDAL_XY);
							int yMarked=STARTED_MEDAL_Y+(((jLabelMedalArray.length-1)-i)*MOVING_MEDAL_XY);
							int yStared=STARTED_MEDAL_Y-2*MOVING_MEDAL_XY;
							
							JLabelMedal jlabel=new JLabelMedal(i,positionMarked,xMarked,yStared,toggleTurn?MEDAL_RED:MEDAL_YELLOW,"luckymanomo",new Date());
							toggleTurn=!toggleTurn;
							jpStage.add(jlabel);
							jLabelMedalArray[i][positionMarked]=jlabel; //assign to the array
							new Thread(new Runnable() {
								public void run() {
									int startingPointIndex=0;
									int endingPointIndex=0;
									int y=yStared;
									boolean hasReaction=true;
									while(hasReaction){
										hasReaction=false;
										startingPointIndex=y;
										while(startingPointIndex<=yMarked){
											jlabel.setLocation(xMarked, startingPointIndex+=2);
											//System.out.println("xMarked:"+xMarked+", y:"+startingPointIndex);
											try {Thread.sleep(3);} catch (InterruptedException e) {e.printStackTrace();}
											//jpStage.repaint();
											//jpStage.validate();
										}
										//ensuring object has reached the expecting point
										jlabel.setLocation(xMarked, yMarked);
										
										endingPointIndex=yMarked;
										//reaction
										//System.out.println("Y: "+y+", yMarked:"+yMarked+", STARTED_MEDAL_Y:"+STARTED_MEDAL_Y);
										int distanceLeft=(int)((yMarked-y)*3/5.0); //first time decided to be divided by 2
										//System.out.println("distanceLeft: "+distanceLeft);
										if(distanceLeft>0){
											hasReaction=true;
											y+=distanceLeft;
											//System.out.println("y+distance: "+y+"\n---");
											while(endingPointIndex>=y){
												jlabel.setLocation(xMarked, endingPointIndex-=2);
												try {Thread.sleep(2);} catch (InterruptedException e) {e.printStackTrace();}
											}
										}
									}
								}
							}).start();
							//System.out.println("xMarked:"+xMarked+", yMarked:"+yMarked);
							break;
						}
					}
				}
			}
		});
		
		
		/*jbDrawMoving.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				new Thread(new Runnable() {
					
					@Override
					public void run() {
						int xPoint=STARTED_MEDAL_X;
						int yPoint=STARTED_MEDAL_Y;
						for(int i=1;i<=(7*6);i++){
							JLabel jlabel=new JLabelMedal(0,0,xPoint,yPoint,MEDAL_YELLOW,"luckymanomo",new Date());
							jpStage.add(jlabel);
							xPoint+=MOVING_MEDAL_XY;
							
							jpStage.repaint();
							jpStage.validate();
							try {
								Thread.sleep(100);
							} catch (InterruptedException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
							if(i%7==0){
								xPoint=STARTED_MEDAL_X;
								yPoint+=MOVING_MEDAL_XY;
								
							}
						}
					}
				}).start();
			}
		});*/
		
		//JFrame Configuration
		setTitle("Connect4Game");
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		add(jpMain);
		//add(jbDrawMoving,BorderLayout.SOUTH);
		
		setSize(jfWidth,jfHeight);
		int scrWidth=(int)Toolkit.getDefaultToolkit().getScreenSize().getWidth();
		int scrHeight=(int)Toolkit.getDefaultToolkit().getScreenSize().getHeight();
		setLocation((scrWidth/2)-(jfWidth/2), (scrHeight/2)-(jfHeight/2));
		setVisible(true);
	}
	
	public static void main(String[] args){
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				new GUI();
			}
		});
	}
	
	public void putMedalToTray(int x,int y,ImageIcon im){
		
	}
	
	public class JLabelMedal extends JLabel{
		ImageIcon im;
		int x;
		int y;
		int xPoint;
		int yPoint;
		int color;
		String codeName;
		Date dateTime;
		private static final long serialVersionUID = 2981119759459086673L;
		public JLabelMedal(int x, int y, int xPoint, int yPoint, int color, String codeName, Date dateTime) {
			super();
			if(color==MEDAL_YELLOW) this.im = imMYellow;
			else if(color==MEDAL_RED) this.im = imMRed;
			this.x = x;
			this.y = y;
			this.xPoint = xPoint;
			this.yPoint = yPoint;
			this.color = color;
			this.codeName = codeName;
			this.dateTime = dateTime;
			setIcon(im);
			setBounds(xPoint,yPoint,im.getIconWidth(),im.getIconHeight());
			repaint();
		}
		@Override
		protected void paintComponent(Graphics g) {
			// TODO Auto-generated method stub
			super.paintComponent(g);
			g.setColor(Color.BLACK);
			g.drawString(x+","+y, 12, 30);
		}
		
		
	}
	
	public class JPanelStage extends JPanel{
		private static final long serialVersionUID = 8385673498603958536L;
		int positionY=0;
		
		public JPanelStage(){
			setLayout(null);
			setOpaque(false);
		}
		/*@Override
		public void paintComponent(Graphics g) {
			// TODO Auto-generated method stub
			super.paintComponent(g);
			g.setColor(Color.YELLOW);
			
			int w=20;
			int h=20;
			g.fillOval((jf.getWidth())/2-(w/2), positionY, w, h);
			
			System.out.println("Repaint jpStage:"+i++);
			
		}*/		
	}
}
