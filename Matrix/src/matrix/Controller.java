package matrix;

import java.util.ArrayList;
import java.util.List;



public final class Controller {
	
	
	public static Matrix calculateMatrix(Matrix a, Matrix b, int xMatrixC, int yMatrixC) throws Exception {
		if(a.columns != b.rows) 
			throw new ArithmeticException("O número de colunas de A deve ser igual ao número de linhas de B. ["
											+ a.columns + " != " + b.rows + "]");
		//Se as matrizes nao sao validas
		if(!matrixIsValid(a) || !matrixIsValid(b))
			throw new Exception("Apenas números são permitidos!");
		
		Matrix c = new Matrix('c', xMatrixC, yMatrixC, a.rows, b.columns, a.matrixColor, a.background);
		
		List<Integer[]> listA = linearize(a);
		List<Integer[]> listB = linearize(transpost(b));
		
		for(int i = 0; i < c.rows; i++) {
			for(int j = 0; j < c.columns; j++) {
				int product = dotProduct(listA.get(i), listB.get(j));
				c.matrix[i][j].setText("" + product);
			}
		}
		
		return c;
	}
	
	/**
	 * Realiza um produto escalar entre dois vetores
	 * @param a - Vetor da matriz A
	 * @param b - Vetor da matriz B
	 * @return O produto escalar entre os vetores A e B
	 */
	private static int dotProduct(Integer[] a, Integer[] b) {
		int product = 0;
		for(int i = 0; i < a.length; i++)
			product += a[i] * b[i];
		
		return product;
	}
	
	
	/**
	 * Verifica se todos os campos de texto da matriz sao validos.
	 * Uma matriz e valida se todos os campos de texto contem apenas numeros.
	 * @param m - Matriz a ser percorrida
	 * @return true Se a matriz for valida, false caso contrario.
	 */
	private static boolean matrixIsValid(Matrix m) {
		for(int i = 0; i < m.rows; i++)
			for(int j = 0; j < m.columns; j++) {
				String s = m.matrix[i][j].getText();
				if(!s.matches("\\d{1,}") && !s.matches("-\\d{1,}")) return false;
			}
		return true;
	}
	
	/**
	 * Lineariza uma matriz de campos de texto (cada linha da matriz e um vetor).
	 * @param m - Matriz de campo de texto
	 * @return Uma lista com vetores dentro
	 */
	private static List<Integer[]> linearize(Matrix m){
		List<Integer[]> list = new ArrayList<>(m.rows);
		
		for(int i = 0; i < m.rows; i++) {
			Integer[] v = new Integer[m.columns];
			
			for(int j = 0; j < m.columns; j++) 
				v[j] = Integer.parseInt(m.matrix[i][j].getText());
			
			list.add(v);
		}
		return list;
	}
	
	/**
	 * @param m - Matriz a ser transposta
	 * @return Uma matriz transposta
	 */
	private static Matrix transpost(Matrix m) {
		Matrix transpost = new Matrix(m.name, m.getX(), m.getY(), m.columns, m.rows, m.matrixColor, m.background);
		for(int i = 0; i < transpost.rows; i++)
			for(int j = 0; j < transpost.columns; j++)
				transpost.matrix[i][j] = m.matrix[j][i];
		
		return transpost;
	}
}
