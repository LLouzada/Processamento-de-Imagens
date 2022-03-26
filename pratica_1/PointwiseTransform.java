
public class PointwiseTransform extends Object {

	/**
	* Question 2.1 Contrast reversal
	*/
	static public ImageAccess inverse(ImageAccess input) {
		int nx = input.getWidth();
		int ny = input.getHeight();
		ImageAccess output = new ImageAccess(nx, ny);
		double value = 0.0;
		for (int x=0; x<nx; x++)
		for (int y=0; y<ny; y++) {
			value = input.getPixel(x, y);
			value = 255 - value;
			output.putPixel(x, y, value);
		}
		return output;	
	}

	/**
	* Question 2.2 Stretch normalized constrast
	*/
	static public ImageAccess rescale(ImageAccess input) {
		int nx = input.getWidth();
		int ny = input.getHeight();
		double max = input.getMaximum();
		double min = input.getMinimum();
		ImageAccess output = new ImageAccess(nx, ny);
		// Add your code here
		//Lucas Louzada - 7732483
		double beta = min;
		double alfa = 255 / (max - min);
		double value = 0.0;
		for (int x=0; x<nx; x++)
			for (int y=0; y<ny; y++) {
				value = input.getPixel(x, y);
				value = alfa * (value - beta);
				output.putPixel(x, y, value);
			}
		return output;	
	}

	/**
	* Question 2.3 Saturate an image
	*/
	static public ImageAccess saturate(ImageAccess input) {
		int nx = input.getWidth();
		int ny = input.getHeight();
		ImageAccess output = new ImageAccess(nx, ny);
		// Add your code here
		//Lucas Louzada - 7732483
		double value = 0.0;
		for (int x=0; x<nx; x++)
			for (int y=0; y<ny; y++) {
				value = input.getPixel(x, y);
				if(value >= 10000) {
					value = 10000;
				}
				output.putPixel(x, y, value);
			}
		return output;
	}
	
	/**
	* Question 4.1 Maximum Intensity Projection
	*/
	static public ImageAccess zprojectMaximum(ImageAccess[] zstack) {
		int nx = zstack[0].getWidth();
		int ny = zstack[0].getHeight();
		int nz = zstack.length;
		ImageAccess output = new ImageAccess(nx, ny);
		// Add your code here
		//Lucas Louzada - 7732483
		double maxIntensity = 0.0;
		double aux = 0.0;
		for (int x=0; x < nx; x++)
			for (int y=0; y < ny; y++) {
				maxIntensity = 0.0;
				for(int z = 0; z < nz; z++){
					aux = zstack[z].getPixel(x, y);
					if (aux >= maxIntensity) {
						maxIntensity = aux;
					}
				}
				output.putPixel(x, y, maxIntensity);
			}

		return output;	
	}

	/**
	* Question 4.2 Z-stack mean
	*/
	static public ImageAccess zprojectMean(ImageAccess[] zstack) {
		int nx = zstack[0].getWidth();
		int ny = zstack[0].getHeight();
		int nz = zstack.length;
		ImageAccess output = new ImageAccess(nx, ny);
		// Add your code here
		return output;	
	}

}
