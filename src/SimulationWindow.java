import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

public class SimulationWindow {

	private JFrame frame;

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					SimulationWindow window = new SimulationWindow();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public SimulationWindow() {
		initialize();
	}

	public static JLabel info_label;
	public static boolean return_home = false;
	boolean toogleStop = true;
	private void initialize() {
		frame = new JFrame();
		frame.setSize(1800,700);
		frame.setTitle("Drone Simulator");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		
		
		/*
		 * Stop\Resume
		 */
	
		JButton stopBtn = new JButton("Start/Pause");
		stopBtn.addActionListener(new ActionListener()
		{
			  public void actionPerformed(ActionEvent e)
			  {
				  if(toogleStop) {
					  CPU.stopAllCPUS();
				  } else {
					  CPU.resumeAllCPUS();
				  }
				  toogleStop = !toogleStop;
			  }
		});
		stopBtn.setBounds(1300, 0, 170, 50);
		frame.getContentPane().add(stopBtn);
		/*
		 * Speeds
		 */
		
		
		JButton speedBtn1 = new JButton("speedUp");
		speedBtn1.addActionListener(new ActionListener()
		{
			  public void actionPerformed(ActionEvent e)
			  {
				  algo1.speedUp();
			  }
		});
		speedBtn1.setBounds(1350, 50, 100, 50);
		frame.getContentPane().add(speedBtn1);
		
		JButton speedBtn2 = new JButton("speedDown");
		speedBtn2.addActionListener(new ActionListener()
		{
			  public void actionPerformed(ActionEvent e)
			  {
				  algo1.speedDown();
			  }
		});
		speedBtn2.setBounds(1350, 100, 100, 50);
		frame.getContentPane().add(speedBtn2);

		/*
		 * Toogle real map
		 */
		
		JButton toogleMapBtn = new JButton("toogle Map");
		toogleMapBtn.addActionListener(new ActionListener()
		{
			  public void actionPerformed(ActionEvent e)
			  {
				  toogleRealMap = !toogleRealMap;
			  }
		});
		toogleMapBtn.setBounds(1350, 150, 100, 50);
		frame.getContentPane().add(toogleMapBtn);
		
		/*
		 * Toogle AI
		 */
		
		JButton toogleAIBtn = new JButton("toogle AI");
		toogleAIBtn.addActionListener(new ActionListener()
		{
			  public void actionPerformed(ActionEvent e)
			  {
				  toogleAI = !toogleAI;
			  }
		});
		toogleAIBtn.setBounds(1350, 200, 100, 50);
		frame.getContentPane().add(toogleAIBtn);
		
		/*
		 * RETURN TO HOME
		 */
		

		JButton returnBtn = new JButton("Return Home");
		returnBtn.addActionListener(new ActionListener()
		{
			  public void actionPerformed(ActionEvent e)
			  {
				  return_home = !return_home;
				  algo1.speedDown();
				  algo1.spinBy(180, true, new Func() {
						@Override
						public void method() {
							algo1.speedUp();
						}
					});
			  }
		});
		returnBtn.setBounds(1350, 250, 120, 50);
		frame.getContentPane().add(returnBtn);
		
		JButton Graph = new JButton("Open Graph");
		Graph.addActionListener(new ActionListener()
		{
			  public void actionPerformed(ActionEvent e)
			  {
				  algo1.mGraph.drawGraph();
			  }
		});
		Graph.setBounds(1350, 300, 120, 50);
		frame.getContentPane().add(Graph);
		
		/*
		 * Info label 
		 */
		
		
		info_label = new JLabel();
		info_label.setBounds(1300, 350, 300, 200);
		frame.getContentPane().add(info_label);
		
		/*
		 * Info label 
		 */
		
		
		info_label2 = new JLabel();
		info_label2.setBounds(1300, 420, 300, 200);
		frame.getContentPane().add(info_label2);
		
		main();
	}
	public JLabel info_label2;
	public static boolean toogleRealMap = true;
	public static boolean toogleAI = false;
	
	public static AutoAlgo1 algo1;
	
	
	public void main() {
		int map_num = 5;
		Point[] startPoints = {
				new Point(100,50),
				new Point(50,60),
				new Point(73,68),
				new Point(84,73),
				new Point(92,100)};
		
		Map map = new Map("C:\\Users\\omer\\IdeaProjects\\DroneSimulator\\Maps\\p1" + map_num + ".png",startPoints[map_num-1]);
		algo1 = new AutoAlgo1(map);
		
		Painter painter = new Painter(algo1);
		painter.setBounds(0, 0, 2000, 2000);
		frame.getContentPane().add(painter);
		
		CPU painterCPU = new CPU(200,"painter"); // 60 FPS painter
		painterCPU.addFunction(frame::repaint);
		painterCPU.play();
		
		algo1.play();
		
		CPU updatesCPU = new CPU(60,"updates");
		updatesCPU.addFunction(algo1.drone::update);
		updatesCPU.play();
		
		CPU infoCPU = new CPU(6,"update_info");
		infoCPU.addFunction(this::updateInfo);
		infoCPU.play();
	}
	
	public void updateInfo(int deltaTime) {
		info_label.setText(algo1.drone.getInfoHTML());
		info_label2.setText("<html>" + "<BR>Counter:" + String.valueOf(algo1.counter) +
				"<BR>isRisky:" + String.valueOf(algo1.is_risky) +
				"<BR>riskyDist:" + String.valueOf(algo1.risky_dis) + "</html>");
	}
	
	public void stopCPUS() {
		CPU.stopAllCPUS();
	}
	
	public void resumseCPUS() {
		CPU.stopAllCPUS();
	}
}
