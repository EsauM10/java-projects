package matrix;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.geom.Line2D;

import javax.swing.BorderFactory;
import javax.swing.JTextField;


public class Matrix {
	char name;
	int rows, columns;
	int xMatrix, yMatrix;
	int WIDTH, HEIGHT;
	int fieldSize = 32;
	
	Color matrixColor;
	Color background;
	Line2D.Float bar1;
	Line2D.Float bar2;
	JTextField[][] matrix;//Matriz de campos de texto
	
	
	public Matrix(char nameMatrix, int x, int y, int numRows, int numColumns, Color themeColor, Color bg) throws ArithmeticException {
		name = nameMatrix;
		xMatrix = y;
		yMatrix = x;
		rows = numRows;
		columns = numColumns;
		matrixColor = (themeColor == null) ? new Color(248, 145, 87) : themeColor;
		background = bg;
		createMatrix();
	}
	
	/**
	 * Cria uma matriz de campos de texto nas coordenadas x e y
	 * @param xMatrix Coordenada x da matriz
	 * @param yMatrix Coordenada y da matriz
	 * @param matrixName Nome da matriz
	 * @param rows Numero de linhas da matriz
	 * @param columns Numero de colunas da matriz
	 * @throws Exception 
	 */
	private void createMatrix() throws ArithmeticException {
		if(rows == 0 || columns == 0) throw new ArithmeticException("Numero de linhas/colunas deve ser != 0");
		
		matrix = new JTextField[rows][columns];
		
		for(int i = 0, x = xMatrix; i < rows; i++, x += 40) {
			for(int j = 0, y = yMatrix + 5; j < columns; j++, y += 40) {
				matrix[i][j] = new JTextField("" + name + (i+1) + "" + (j+1));
				setTextField(matrix[i][j], y, x);// (y, x) Primeiro preenche as Colunas depois as Linhas
			}	
		}
		setBars();
		setDimension();
	}

	private void setTextField(JTextField field, int x, int y) {
		field.setBounds(x, y, fieldSize, fieldSize);
		field.setFont(new Font("Roboto", 1, 14));
		field.setForeground(matrixColor);
		field.setBackground(background);
		field.setCaretColor(matrixColor);
		field.setBorder(BorderFactory.createLineBorder(matrixColor));
	}
	
	
	/**
	 * Configura as barras (Parenteses) da matriz de acordo com o tamanho e quantidade dos campos 
	 */
	private void setBars() {
		JTextField firstField = getFirst();//Primeiro campo da matriz
		JTextField lastField  = getLast();//Ultimo campo na matriz
		
		int xLine = lastField.getX()  + lastField.getWidth() + 4; 
		int yLine = firstField.getY() + (lastField.getHeight() * rows) + ((rows-1) * 10);
		
		bar1 = new Line2D.Float(yMatrix, xMatrix, yMatrix, yLine);
		bar2 = new Line2D.Float(xLine, xMatrix, xLine, yLine);
	}
	
	/**
	 * Configura a largura e altura da matriz de acordo com a quantidade de campos de texto
	 */
	private void setDimension() {
		WIDTH  = (getFirst().getWidth() * columns) + ((columns-1) * 10) + 10;
		HEIGHT = (getFirst().getWidth() * rows) + ((rows-1) * 10);
	}
	
	public void paintBars(Graphics2D g){
		g.draw(bar1);
		g.draw(bar2);
	}
	
	public void setHorizontalAlignment(int xPos, int yPos) {
		xMatrix = yPos;
		yMatrix = xPos;
		
		for(int i = 0, x = xMatrix; i < rows; i++, x += 40) 
			for(int j = 0, y = yMatrix + 5; j < columns; j++, y += 40) 
				matrix[i][j].setBounds(y, x, fieldSize, fieldSize);	
		
		bar1.x1 = bar1.x2 = yMatrix;
		
		bar2.x1 = bar2.x2 = getLast().getX() + getLast().getWidth() + 4;
	}
	
	public int getX() { return xMatrix; }	
	public int getY() { return yMatrix; }
	
	/**
	 * Retorna o primeiro campo da matriz
	 * @return
	 */
	public JTextField getFirst() { return matrix[0][0]; }
	/**
	 * Retorna o ultimo campo da matriz
	 * @return
	 */
	public JTextField getLast()  { return matrix[rows-1][columns-1]; }
}
