import java.awt.Color;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SpringLayout;

import algorithms.FastConvexHull;
import algorithms.SlowConvexHull;


public class Gui extends JFrame implements Observer
{
	private State state;
	
	private DrawArea drawArea;
	
	public Gui(State state)
	{
		this.state = state;
		
		state.addObserver(this);
		
		setTitle("Convex Hull Algorithm");
		setBounds(100, 100, 500, 500);
		Container frame = getContentPane();
		
		SpringLayout layout = new SpringLayout();
		frame.setLayout(layout);
		
		drawArea = new DrawArea();
		frame.add(drawArea);
		
		layout.putConstraint(SpringLayout.NORTH, drawArea, 5, SpringLayout.NORTH, frame);
		layout.putConstraint(SpringLayout.WEST, drawArea, 5, SpringLayout.WEST, frame);
		layout.putConstraint(SpringLayout.EAST, drawArea, -5, SpringLayout.EAST, frame);
		
		ButtonPanel bp = new ButtonPanel();
		frame.add(bp);
		
		layout.putConstraint(SpringLayout.SOUTH, drawArea, -5, SpringLayout.NORTH, bp);
		layout.putConstraint(SpringLayout.WEST, bp, 0, SpringLayout.WEST, frame);
		layout.putConstraint(SpringLayout.EAST, bp, 0, SpringLayout.EAST, frame);
		layout.putConstraint(SpringLayout.SOUTH, bp, -5, SpringLayout.SOUTH, frame);
		
		setVisible(true);
	}
	
	private class DrawArea extends JPanel implements MouseListener, MouseMotionListener
	{
		private static final int pointRadius = 5;
		
		private boolean mouseActive;
		private int mouseX, mouseY;
		
		public DrawArea()
		{
			setBackground(Color.WHITE);
			
			mouseActive = false;
			mouseX = mouseY = 0;
			
			addMouseListener(this);
			addMouseMotionListener(this);
		}
		
		@Override
		protected void paintComponent(Graphics g) 
		{
			super.paintComponent(g);
			
			Graphics2D g2 = (Graphics2D) g;
			g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			
			for (Point p : state.getPoints())
			{
				g2.setColor(Color.BLUE);
				g2.fillOval(p.x - pointRadius, p.y - pointRadius, 2 * pointRadius, 2 * pointRadius);
			}
			
			Polygon hull = new Polygon();
			
			for (Point p : state.getConvexHull())
			{
				hull.addPoint(p.x, p.y);
			}
			
			if (hull.npoints > 0)
			{
				g2.setColor(Color.BLACK);
				g2.drawPolygon(hull);
			}
			
			if (mouseActive)
			{
				g2.setColor(Color.GRAY);
				g2.fillOval(mouseX - pointRadius, mouseY - pointRadius, 2 * pointRadius, 2 * pointRadius);
			}
		}

		@Override
		public void mousePressed(MouseEvent e) 
		{
			state.addPoint(e.getX(), e.getY());
		}

		@Override
		public void mouseMoved(MouseEvent e) 
		{
			mouseX = e.getX();
			mouseY = e.getY();
			
			repaint();
		}

		@Override
		public void mouseEntered(MouseEvent e) 
		{ 
			mouseActive = true; 
		}

		@Override
		public void mouseExited(MouseEvent e) 
		{ 
			mouseActive = false; 
			repaint(); 
		}

		@Override
		public void mouseClicked(MouseEvent e) {}

		@Override
		public void mouseReleased(MouseEvent e) {}

		@Override
		public void mouseDragged(MouseEvent e) {}
	}
	
	private class ButtonPanel extends JPanel
	{
		public ButtonPanel()
		{
			setLayout(new FlowLayout(FlowLayout.LEFT, 5, 0));

			JButton hull = new JButton("Fast Compute Hull");
			add(hull);
			
			hull.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					state.computeConvexHull(new FastConvexHull());
				}
			});
			
			JButton slowHull = new JButton("Slow Compute Hull");
			add(slowHull);
			
			slowHull.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					state.computeConvexHull(new SlowConvexHull());
				}
			});
			
			JButton clear = new JButton("Clear");
			add(clear);
			
			clear.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					state.removeAllPoints();
				}
			});
		}
	}

	@Override
	public void update(Observable o, Object arg) 
	{
		drawArea.repaint();
	}
}
