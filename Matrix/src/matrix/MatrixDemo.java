package matrix;

import java.awt.EventQueue;

import javax.swing.JFrame;


public class MatrixDemo extends JFrame{
	private static final long serialVersionUID = 1L;
	
	public MatrixDemo() {
		super("Matrix");
		add(new MatrixPanel());
		pack();
		setResizable(false);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	
	public static void main(String[] args) {
		
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				new MatrixDemo().setVisible(true);
			}
		});
		
	}

}
