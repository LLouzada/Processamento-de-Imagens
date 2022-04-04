import ij.*;

public class FilteringSession {

	/*******************************************************************************
	 *
	 * E D G E   D E T E C T O R   S E C T I O N
	 *
	 ******************************************************************************/

	/**
	 * Detects the vertical edges inside an ImageAccess object.
	 * This is the non-separable version of the edge detector.
	 * The kernel of the filter has the following form:
	 *
	 *     -------------------
	 *     | -1  |  0  |  1  |
	 *     -------------------
	 *     | -1  |  0  |  1  |
	 *     -------------------
	 *     | -1  |  0  |  1  |
	 *     -------------------
	 *
	 * Mirror border conditions are applied.
	 */
	static public ImageAccess detectEdgeVertical_NonSeparable(ImageAccess input) {
		int nx = input.getWidth();
		int ny = input.getHeight();
		double arr[][] = new double[3][3];
		double pixel;
		ImageAccess out = new ImageAccess(nx, ny);
		for (int x = 0; x < nx; x++) {
			for (int y = 0; y < ny; y++) {
				input.getNeighborhood(x, y, arr);
				pixel = arr[2][0]+arr[2][1]+arr[2][2]-arr[0][0]-arr[0][1]-arr[0][2];
				pixel = pixel / 6.0;
				out.putPixel(x, y, pixel);
			}
		}
		return out;
	}

	/**
	 * Detects the vertical edges inside an ImageAccess object.
	 * This is the separable version of the edge detector.
	 * The kernel of the filter applied to the rows has the following form:
	 *     -------------------
	 *     | -1  |  0  |  1  |
	 *     -------------------
	 *
	 * The kernel of the filter applied to the columns has the following 
	 * form:
	 *     -------
	 *     |  1  |
	 *     -------
	 *     |  1  |
	 *     -------
	 *     |  1  |
	 *     -------
	 *
	 * Mirror border conditions are applied.
	 */

	static public ImageAccess detectEdgeVertical_Separable(ImageAccess input) {
		int nx = input.getWidth();
		int ny = input.getHeight();
		ImageAccess out = new ImageAccess(nx, ny);
		double rowin[]  = new double[nx];
		double rowout[] = new double[nx];
		for (int y = 0; y < ny; y++) {
			input.getRow(y, rowin);
			doDifference3(rowin, rowout);
			out.putRow(y, rowout);
		}
		
		double colin[]  = new double[ny];
		double colout[] = new double[ny];
		for (int x = 0; x < nx; x++) {
			out.getColumn(x, colin);
			doAverage3(colin, colout);
			out.putColumn(x, colout);
		}
		return out;
	}
	
	 /**
	 
	 *     -------------------
	 *     | -1  |  -1  |  -1  |
	 *     -------------------
	 *     |  0  |   0  |  0   |
	 *     -------------------
	 *     |  1  |   1  |  1   |
	 *     -------------------
	 *
	 */

	static public ImageAccess detectEdgeHorizontal_NonSeparable(ImageAccess input) {

		//Lucas Louzada - 7732483 - custom code
		int nx = input.getWidth();
		int ny = input.getHeight();
		double arr[][] = new double[3][3];
		double pixel;
		ImageAccess out = new ImageAccess(nx, ny);
		for (int x = 0; x < nx; x++) {
			for (int y = 0; y < ny; y++) {
				input.getNeighborhood(x, y, arr);
				pixel = arr[0][2]+arr[1][2]+arr[2][2]-arr[0][0]-arr[1][0]-arr[2][0];
				pixel = pixel / 6.0;
				out.putPixel(x, y, pixel);
			}
		}
		return out;
		//IJ.showMessage("Question 1");
		//return input.duplicate();
	}

	/**
	 *     -------------------
	 *     | 1  |  1  |  1  |
	 *     -------------------
	 
	 *     -------
	 *     | -1  |
	 *     -------
	 *     |  0  |
	 *     -------
	 *     |  1  |
	 *     -------
	 *
	 */

	static public ImageAccess detectEdgeHorizontal_Separable(ImageAccess input) {
		//Lucas Louzada - 7732483 - custom code
		int nx = input.getWidth();
		int ny = input.getHeight();
		ImageAccess out = new ImageAccess(nx, ny);
		double rowin[]  = new double[nx];
		double rowout[] = new double[nx];
		for (int y = 0; y < ny; y++) {
			input.getRow(y, rowin);
			doAverage3(rowin, rowout);
			out.putRow(y, rowout);
		}
		
		double colin[]  = new double[ny];
		double colout[] = new double[ny];
		for (int x = 0; x < nx; x++) {
			out.getColumn(x, colin);
			doDifference3(colin, colout);
			out.putColumn(x, colout);
		}
		return out;
	
		//IJ.showMessage("Question 1");
		//return input.duplicate();
	}

	/**
	 * Implements an one-dimensional average filter of length 3.
	 * The filtered value of a pixel is the averaged value of
	 * its local neighborhood of length 3.
	 * Mirror border conditions are applied.
	 */
	static private void doAverage3(double vin[], double vout[]) {
		int n = vin.length;
		vout[0] = (vin[0] + 2.0 * vin[1]) / 3.0;
		for (int k = 1; k < n-1; k++) {
			vout[k] = (vin[k-1] + vin[k] + vin[k+1]) / 3.0;
		}
		vout[n-1] = (vin[n-1] + 2.0 * vin[n-2]) / 3.0;
	}

	/**
	 * Implements an one-dimensional centered difference filter of 
	 * length 3. The filtered value of a pixel is the difference of 
	 * its two neighborhing values.
	 * Mirror border conditions are applied.
	 */
	static private void doDifference3(double vin[], double vout[]) {
		int n = vin.length;
		vout[0] = 0.0;
		for (int k = 1; k < n-1; k++) {
			vout[k] = (vin[k+1] - vin[k-1]) / 2.0;
		}
		vout[n-1] = 0.0;
	}

	/*******************************************************************************
	 *
	 * M O V I N G   A V E R A G E   5 * 5   S E C T I O N
	 *
	 ******************************************************************************/

	static public ImageAccess doMovingAverage5_NonSeparable(ImageAccess input) {

		//Lucas Louzada - 7732483 - custom code
		int nx = input.getWidth();
		int ny = input.getHeight();
		double arr[][] = new double[5][5];
		double pixel;
		ImageAccess out = new ImageAccess(nx, ny);
		for (int x = 0; x < nx; x++) {
			for (int y = 0; y < ny; y++) {
				input.getNeighborhood(x, y, arr);
				pixel = (arr[0][0] + arr[0][1] + arr[0][2] + arr[0][3] + arr[0][4] + 
				arr[1][0] + arr[1][1] + arr[1][2] + arr[1][3] + arr[1][4] + 
				arr[2][0] + arr[2][1] + arr[2][2] + arr[2][3] + arr[2][4] + 
				arr[3][0] + arr[3][1] + arr[3][2] + arr[3][3] + arr[3][4] + 
				arr[4][0] + arr[4][1] + arr[4][2] + arr[4][3] + arr[4][4]) / 25;
				out.putPixel(x, y, pixel);
			}
		}
		return out;

		//IJ.showMessage("Question 2");
		//return input.duplicate();
	}


	static public ImageAccess doMovingAverage5_Separable(ImageAccess input) {
		//Lucas Louzada - 7732483 - custom code
		int nx = input.getWidth();
		int ny = input.getHeight();
		ImageAccess out = new ImageAccess(nx, ny);
		double rowin[] = new double[nx];
		double rowout[] = new double[nx];
		for (int y = 0; y < ny; y++) {
			input.getRow(y, rowin);
			doAverage5(rowin, rowout);
			out.putRow(y, rowout);
		}
		double colin[] = new double[ny];
		double colout[] = new double[ny];
		for (int x = 0; x < nx; x++) {
			out.getColumn(x, colin);
			doAverage5(colin, colout);
			out.putColumn(x, colout);
		}

		return out;
		//IJ.showMessage("Question 2");
		//return input.duplicate();
	}

 	/**
	 * Implements an one-dimensional average filter of length 5.
	 * The filtered value of a pixel is the averaged value of
	 * its local neighborhood of length 3.
	 * Mirror border conditions are applied.
	 */
	static private void doAverage5(double vin[], double vout[]) {
		//Lucas Louzada - 7732483 - custom code
		int n = vin.length;
		vout[0] = (vin[0] + (2.0 * vin[1]) + (2.0 * vin[2])) / 5.0;
		vout[1] = (vin[1] + (2.0 * vin[2]) + (2.0 * vin[3])) / 5.0;
		for (int k = 2; k < n-2; k++) {
			vout[k] = (vin[k-2] + vin[k-1] + vin[k] + vin[k+1] + vin[k+2]) / 5.0;
		}
		vout[n-2] = (vin[n-2] + (2.0 * vin[n-3]) + (2.0 * vin[n-4])) / 5.0;
		vout[n-1] = (vin[n-1] + (2.0 * vin[n-2]) + (2.0 * vin[n-3])) / 5.0;
	}

	static public ImageAccess doMovingAverage5_Recursive(ImageAccess input) {
		
		//Lucas Louzada - 7732483 - custom code
		int nx = input.getWidth();
		int ny = input.getHeight();
		ImageAccess out = new ImageAccess(nx, ny);
		double rowin[] = new double[nx];
		double rowout[] = new double[nx];
		for (int y = 0; y < ny; y++) {
			input.getRow(y, rowin);
			doAverage5_Recursive(rowin, rowout);
			out.putRow(y, rowout);
		}
		double colin[] = new double[ny];
		double colout[] = new double[ny];
		for (int x = 0; x < nx; x++) {
			out.getColumn(x, colin);
			doAverage5_Recursive(colin, colout);
			out.putColumn(x, colout);
		}
		return out;	
	}

	static private void doAverage5_Recursive(double vin[], double vout[]) {
		//Lucas Louzada - 7732483 - custom code
		
		int n = vin.length;
		int l = 5;// tamanho do vetor da media 
	
		int soma = 0;
		vout[0] = (vin[0] + (2.0 * vin[1]) + (2.0 * vin[2])) / 5.0;
		vout[1] = (vin[1] + (2.0 * vin[2]) + (2.0 * vin[3])) / 5.0;
		for (int k = 2; k < n-2; k++) {
			vout[k] = soma_r(vin, k, l, soma) / 5.0; //(vin[k-2] + vin[k-1] + vin[k] + vin[k+1] + vin[k+2]) / 5.0;
		}
		vout[n-2] = (vin[n-2] + (2.0 * vin[n-3]) + (2.0 * vin[n-4])) / 5.0;
		vout[n-1] = (vin[n-1] + (2.0 * vin[n-2]) + (2.0 * vin[n-3])) / 5.0;
	}
	
	static private double soma_r(double vin[], int k, int l, double soma) {
		if (l == 0) {					 
			return soma;
		} else {
			soma = soma + vin[k + 2];
			l--;
			k--;
			return soma_r(vin, k, l, soma);
		}
	}

	/*******************************************************************************
	 *
	 * S O B E L
	 *
	 ******************************************************************************/

	static public ImageAccess doSobel(ImageAccess input) {
		//Lucas Louzada - 7732483 - custom code
		int nx = input.getWidth();
		int ny = input.getHeight();
		ImageAccess out = new ImageAccess(nx, ny);
		ImageAccess gx = new ImageAccess(nx, ny); //primeiro gradiente
		ImageAccess gy = new ImageAccess(nx, ny); //segundo gradiente
		double arr[][] = new double[3][3]; //armazenar os vizinhos
		double pixel = 0;

		//Gx
		for(int x = 0; x < nx; x++) {
			for(int y = 0; y < ny; y++) {
				input.getNeighborhood(x, y, arr);
				//mascara do gradiente Gx
				pixel = (- arr[0][0] - (2 * arr[0][1]) - arr[0][2] + arr[2][0] + arr[2][1] + arr[2][2]) / 6;
				gx.putPixel(x, y, pixel);
			}
		}

		//Gy
		for(int x = 0; x < nx; x++) {
			for(int y = 0; y < ny; y++) {
				input.getNeighborhood(x, y, arr);
				//mascara do gradiente Gy
				pixel = (- arr[0][0] - (2 * arr[1][0]) - arr[2][0] + arr[0][2] + arr[1][2] + arr[2][2]) / 6;
				gy.putPixel(x, y, pixel);
			}
		}
		//exibe os gradientes
		gx.show("Gradiente Gx");
		gy.show("Gradiente Gy");

		//aritmeticas da formula
		gx.pow(2.0);
		gy.pow(2.0);
		out.add(gx, gy);
		out.sqrt();

		return out;
		//IJ.showMessage("Question 4");
		//return input.duplicate();
	}


	/*******************************************************************************
	 *
	 * M O V I N G   A V E R A G E   L * L   S E C T I O N
	 *
	 ******************************************************************************/

	static public ImageAccess doMovingAverageL_Recursive(ImageAccess input, int length) {
		//IJ.showMessage("Question 5");
		return input.duplicate();
	}

}
