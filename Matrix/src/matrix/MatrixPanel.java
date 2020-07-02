package matrix;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.JToolBar;
import javax.swing.SpinnerNumberModel;


public class MatrixPanel extends JPanel{
	private static final long serialVersionUID = 1L;
	private final int WIDTH  = 650;
	private final int HEIGHT = 400;
	private int minValue   = 1;//Menor dimensao permitida na matriz
	private int maxValue   = 4;//Maior dimensao permitida na matriz
	private int spinHeight = 20;
	private int spinWidth  = 32;
	private int offsetX    = 45;//Espacamento entre dois JSpinner's
	
	private JToolBar toolBar;
	private JButton buttonClear;
	private JButton calculate;
	private JSpinner spin1A;
	private JSpinner spin2A;
	private JSpinner spin1B;
	private JSpinner spin2B;
	private static Matrix matrixA;
	private static Matrix matrixB;
	private static Matrix matrixC;
	
	private Color themeColor;
	
	public MatrixPanel() {
		themeColor = new Color(248, 145, 87); 
		
		toolBar = new JToolBar();
		toolBar.setFloatable(false);
		toolBar.setLayout(null);
		toolBar.setBackground(new Color(90, 105, 120));
		toolBar.setBounds(0, 0, WIDTH+20, height(0.1f));
		
		add(toolBar);
		
		buttonClear = new JButton("Clear");
		buttonClear.setBounds(13, 4, 60, 30);
		setButton(buttonClear);
		buttonClear.addActionListener(e -> buttonClearActionPerformed(e));
		toolBar.add(buttonClear);
		
		try {
			displayMatrix(matrixA = new Matrix('a', 20, height(0.35f), 4, 4, themeColor, this.getBackground()));
			displayMatrix(matrixB = new Matrix('b', matrixA.WIDTH + width(0.10f), matrixA.getX(), 4, 4, themeColor, this.getBackground()));
		} catch (Exception e) {e.printStackTrace();}
		
		spin1A = setSpinner(matrixA, 0, matrixA.rows);
		spin1A.addChangeListener(l -> spinValueChangedMatrixA((int) spin1A.getValue(), matrixA.columns));
			
		spin2A = setSpinner(matrixA, offsetX, matrixA.columns);
		spin2A.addChangeListener(l -> spinValueChangedMatrixA(matrixA.rows, (int) spin2A.getValue()));	
		
		spin1B = setSpinner(matrixB, 0, matrixB.rows);
		spin1B.addChangeListener(l -> spinValueChangedMatrixB((int) spin1B.getValue(), matrixB.columns));
		
		spin2B = setSpinner(matrixB, offsetX, matrixB.columns);
		spin2B.addChangeListener(l -> spinValueChangedMatrixB(matrixB.rows, (int) spin2B.getValue()));
		
		calculate = new JButton("=");
		calculate.setBounds(matrixB.getY() + matrixB.WIDTH+10, matrixB.getX()-15 + matrixB.HEIGHT/2, 46, 30);
		setButton(calculate);
		calculate.addActionListener(e -> calculateActionPerformed(e));
		add(calculate);
		
		setLayout(null);
		setFocusable(true);
		setPreferredSize(new Dimension(WIDTH, HEIGHT));
	}


	public void paint(Graphics g) {
		super.paint(g);
		Graphics2D g2 = (Graphics2D) g;
		g2.setColor(themeColor);
		g2.setStroke(new BasicStroke(3));
		matrixA.paintBars(g2);
		matrixB.paintBars(g2);
		if(matrixC != null) 
			matrixC.paintBars(g2);
		g2.dispose();
	}
	
	
	/**
	 * Adiciona os campos de texto da matriz ao Painel
	 * @param m - Matriz de campo de texto
	 */
	private void displayMatrix(Matrix m) {
		for(int i = 0; i < m.rows; i++) {
			for(int j = 0; j < m.columns; j++) {
				
				JTextField textField = m.matrix[i][j];
				m.matrix[i][j].addMouseListener(new MouseAdapter() {
					
					@Override
					public void mouseClicked(MouseEvent e) {
						super.mouseClicked(e);
						textFieldMouseClicked(textField);
					}
				});
				add(m.matrix[i][j]);	
			}
		}
	}
	
	/**
	 * Apaga o texto de um JTextField quando o usuario clica sobre o componente.
	 * O texto so sera apagado se o primeiro caracter for uma letra.
	 * @param textField - Campo de texto a ser configurado
	 */
	private void textFieldMouseClicked(JTextField textField) {
		if(!textField.getText().equals("")) {//Se o texto do campo nao esta apagado
			if(Character.isLetter(textField.getText().charAt(0)))//Se o primeiro caracter for uma letra, apaga o texto do campo
				textField.setText("");
		}
	}
	
	private void buttonClearActionPerformed(ActionEvent e) {
		clearMatrix(matrixA);
		clearMatrix(matrixB);
		if(matrixC != null) {
			removeMatrix(matrixC);
			matrixC = null;
			repaint();
		}
	}
	
	private void calculateActionPerformed(ActionEvent e) {
		if(matrixC != null) {
			removeMatrix(matrixC);
			matrixC = null;
		}
		try {
			displayMatrix(matrixC = Controller.calculateMatrix(matrixA, matrixB, calculate.getX() + calculate.getWidth()+15, matrixB.getX()));
		}catch (Exception ex) {JOptionPane.showMessageDialog(this, ex.getMessage());}
		repaint();
	}
	
	
	
	/**
	 * Configura as propriedades de um botao
	 * @param b - JButton a ser configurado
	 */
	private void setButton(JButton b) {
		b.setFont(new Font("Roboto", 1, 14));
		b.setForeground(Color.WHITE);
		b.setBackground(themeColor);
		b.setFocusable(false);
		b.setBorderPainted(false);
		hoverButton(b);
	}
	
	/**
	 * Adiciona o efeito hover a um botao
	 * @param b - JButton a ser configurado
	 */
	private void hoverButton(JButton b) {
		b.addMouseListener(new MouseAdapter() {
			public void mouseEntered(MouseEvent e) {b.setBackground(new Color(252, 121, 48));};
			public void mouseExited(MouseEvent e)  {b.setBackground(themeColor);};
		});
	}
	
	
	/**
	 * Limpa os campos de uma matriz de campos de texto
	 * @param m - Objeto matriz de campo de texto
	 */
	private void clearMatrix(Matrix m) {
		for(int i = 0; i < m.rows; i++)
			for(int j = 0; j < m.columns; j++)
				m.matrix[i][j].setText("");
	}
	
	/**
	 * Configura as propriedades de um JSpinner
	 * @param m - Matriz de campos de texto
	 * @param offsetX - Incremento na variavel X (Alinhamento horizontal)
	 * @param value - Valor inicial do spinner
	 * @return O spinner configurado
	 */
	private JSpinner setSpinner(Matrix m, int offsetX, int value) {
		JSpinner spinner = new JSpinner(new SpinnerNumberModel(value, minValue, maxValue, 1));
		spinner.setBounds(m.getY()-1 + offsetX, m.getX() - 25, spinWidth, spinHeight);
		spinner.setFont(new Font("Roboto", 1, 14));
		spinner.setBorder(BorderFactory.createLineBorder(themeColor));
		setSpinnerColor(spinner, getBackground(), themeColor);
		add(spinner);
		return spinner;		
	}
	
	/**
	 * Configura a cor do texto e do background de um JSpinner
	 * @param spinner - JSpinner a ser configurado
	 * @param background - Cor do background
	 * @param foreground - Cor do texto
	 */
	private void setSpinnerColor(JSpinner spinner, Color background, Color foreground) {
		JComponent editor = spinner.getEditor();
        int n = editor.getComponentCount();
        
        for (int i=0; i < n; i++){
            Component c = editor.getComponent(i);
   
            if (c instanceof JTextField){
                c.setForeground(foreground);
                c.setBackground(background);
            }
        }
	}

	/**
	 * Remove os campos de texto de uma matriz do JPanel
	 * @param m - Matriz de campo de texto
	 */
	private void removeMatrix(Matrix m) {
		for(int i = 0; i < m.rows; i++)
			for(int j = 0; j < m.columns; j++) 
				remove(m.matrix[i][j]);
	}
	
	
	/**
	 * Reposiciona e alinha as matrizes e os componentes 
	 * @param rows 
	 * @param columns
	 */
	private void spinValueChangedMatrixA(int rows, int columns) {
		removeMatrix(matrixA);
		try {
			displayMatrix(matrixA = new Matrix(matrixA.name, matrixA.getY(), matrixA.getX(), rows, columns, themeColor, this.getBackground()));
		} catch (Exception ex) {ex.printStackTrace();}
		matrixB.setHorizontalAlignment(matrixA.WIDTH + width(0.10f), matrixA.getX());
		setHorinzotalAlignment();
	}
	
	private void spinValueChangedMatrixB(int rows, int columns) {
		removeMatrix(matrixB);
		try {
			displayMatrix(matrixB = new Matrix(matrixB.name, matrixB.getY(), matrixB.getX(), rows, columns, themeColor, this.getBackground()));
		} catch (Exception ex) {ex.printStackTrace();}
		setHorinzotalAlignment();
	}
	
	private void setHorinzotalAlignment() {
		spin1B.setBounds(matrixB.getY(), spin1B.getY(), spinWidth, spinHeight);
		spin2B.setBounds(spin1B.getX() + offsetX, spin1B.getY(), spinWidth, spinHeight);
		calculate.setBounds(matrixB.getY() + matrixB.WIDTH + 10, calculate.getY(), 46, 30);
		if(matrixC != null)
			matrixC.setHorizontalAlignment(calculate.getX() + calculate.getWidth()+10, matrixC.getX());
		repaint();
	}
	
	private int width(float percent)  { return (int) (WIDTH  * percent); }
	private int height(float percent) { return (int) (HEIGHT * percent); }
	
}
