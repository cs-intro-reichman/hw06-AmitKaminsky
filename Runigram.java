// This class uses the Color class, which is part of a package called awt,
// which is part of Java's standard class library.
import java.awt.Color;

import javax.xml.transform.Source;

/** A library of image processing functions. */
public class Runigram {

	public static void main(String[] args) {
	    
		//// Hide / change / add to the testing code below, as needed.
		
		// Tests the reading and printing of an image:	
		Color[][] tinypic = read("tinypic.ppm");
		Color[][] cake = read("cake.ppm");

		// Creates an image which will be the result of various 
		// image processing operations:
		Color[][] imageOut;

		// Tests functions of image:
		// print(tinypic);
		// System.out.println();
		// print(grayScaled(tinypic));
		// imageOut = blend(grayScaled(tinypic), tinypic, 0.25);
		// System.out.println();
		// print(imageOut);

		// // test colors
		// Color c1 = new Color(100, 0, 0);
		// Color c2 = new Color(29, 29, 29);
		// print(blend(c1, c2, 0.25));

		// test morph
		// Runigram.setCanvas(cake);
		morph(cake, grayScaled(cake), 10);
		
		//// Write here whatever code you need in order to test your work.
		//// You can reuse / overide the contents of the imageOut array.
	}

	/** Returns a 2D array of Color values, representing the image data
	 * stored in the given PPM file. */
	public static Color[][] read(String fileName) {
		In in = new In(fileName);
		// Reads the file header, ignoring the first and the third lines.
		in.readString();
		int numCols = in.readInt();
		int numRows = in.readInt();
		in.readInt();
		// Creates the image array
		Color[][] image = new Color[numRows][numCols];
		// Reads the RGB values from the file, into the image array. 
		// For each pixel (i,j), reads 3 values from the file,
		// creates from the 3 colors a new Color object, and 
		// makes pixel (i,j) refer to that object.
		for (int row = 0; row < image.length; row++) {
			for (int col = 0; col < image[0].length; col++) {
				image[row][col] = new Color(in.readInt(), in.readInt(), in.readInt());
			}
		}
		return image;
	}

    // Prints the RGB values of a given color.
	private static void print(Color c) {
	    System.out.print("(");
		System.out.printf("%3s,", c.getRed());   // Prints the red component
		System.out.printf("%3s,", c.getGreen()); // Prints the green component
        System.out.printf("%3s",  c.getBlue());  // Prints the blue component
        System.out.print(")  ");
	}

	// Prints the pixels of the given image.
	// Each pixel is printed as a triplet of (r,g,b) values.
	// This function is used for debugging purposes.
	// For example, to check that some image processing function works correctly,
	// we can apply the function and then use this function to print the resulting image.
	private static void print(Color[][] image) {
		for (int row = 0; row < image.length; row++) {
			for (int col = 0; col < image[0].length; col++) {
				print(image[row][col]);
			}
			System.out.println("");
		}
	}
	
	/**
	 * Returns an image which is the horizontally flipped version of the given image. 
	 */
	public static Color[][] flippedHorizontally(Color[][] image) {
		int numRows = image.length;
		int numCols = image[0].length;
		Color[][] imageFlippedHoriz = new Color[numRows][numCols];
		for (int row = 0; row < numRows; row++) {
			for (int col = 0; col < numCols; col++) {
				imageFlippedHoriz[row][col] = image[row][numCols - (col + 1)];
			}
		}
		return imageFlippedHoriz;
	}
	
	/**
	 * Returns an image which is the vertically flipped version of the given image.
	 * @param image
	 * @return image vertically flipped
	 */
	public static Color[][] flippedVertically(Color[][] image){
		int numRows = image.length;
		int numCols = image[0].length;
		Color[][] imageFlippedVert = new Color[numRows][numCols];
		for (int col = 0; col < numCols; col++) {
			for (int row = 0; row < numRows; row++) {
				imageFlippedVert[row][col] = image[numRows - (row + 1)][col];
			}
		}
		return imageFlippedVert;
	}
	
	// Computes the luminance of the RGB values of the given pixel, using the formula 
	// lum = 0.299 * r + 0.587 * g + 0.114 * b, and returns a Color object consisting
	// the three values r = lum, g = lum, b = lum.
	public static Color luminance(Color pixel) {
		double lumRed = 0.299 * pixel.getRed();
		double lumGreen = 0.587 * pixel.getGreen();
		double lumBlue = 0.114 * pixel.getBlue();
		int lum = (int) (lumRed + lumGreen + lumBlue);
		Color lumPixel = new Color(lum, lum, lum);
		return lumPixel;
	}
	
	/**
	 * Returns an image which is the grayscaled version of the given image.
	 */
	public static Color[][] grayScaled(Color[][] image) {
		int numRows = image.length;
		int numCols = image[0].length;
		Color[][] greyScaledImage = new Color[numRows][numCols];
		for (int row = 0; row < numRows; row++) {
			for (int col = 0; col < numCols; col++) {
				greyScaledImage[row][col] = luminance(image[row][col]);
			}
		}
		return greyScaledImage;
	}	
	
	/**
	 * Returns an image which is the scaled version of the given image. 
	 * The image is scaled (resized) to have the given width and height.
	 */
	public static Color[][] scaled(Color[][] image, int width, int height) {
		int orgHeight = image.length;
		int orgWidth = image[0].length;
		int numRows = height;
		int numCols = width;
		Color[][] scaledImage = new Color[numRows][numCols];
		for (int row = 0; row < numRows; row++) {
			for (int col = 0; col < numCols; col++) {
				scaledImage[row][col] = image[(int) (row * orgHeight / height)]
											 [(int) (col * orgWidth / width)];
			}
		}
		return scaledImage;
	}
	
	/**
	 * Computes and returns a blended color which is a linear combination of the two given
	 * colors. Each r, g, b, value v in the returned color is calculated using the formula 
	 * v = alpha * v1 + (1 - alpha) * v2, where v1 and v2 are the corresponding r, g, b
	 * values in the two input color.
	 */
	public static Color blend(Color c1, Color c2, double alpha) {
		double blendRed = alpha * c1.getRed() + (1 - alpha) * c2.getRed();
		double blendGreen = alpha * c1.getGreen() + (1 - alpha) * c2.getGreen();
		double blendBlue = alpha * c1.getBlue() + (1 - alpha) * c2.getBlue();
		Color blendedColor = new Color((int) blendRed,(int) blendGreen, (int)blendBlue);
		return blendedColor;
	}

	/**
	 * Cosntructs and returns an image which is the blending of the two given images.
	 * The blended image is the linear combination of (alpha) part of the first image
	 * and (1 - alpha) part the second image.
	 * The two images must have the same dimensions.
	 */
	public static Color[][] blend(Color[][] image1, Color[][] image2, double alpha) {
		int numRows = image1.length;
		int numCols = image1[0].length;
		Color[][] blendedImage = new Color[numRows][numCols];
		for (int row = 0; row < numRows; row++) {
			for (int col = 0; col < numCols; col++) {
				Color pixel1 = image1[row][col];
				Color pixel2 = image2[row][col];
				blendedImage[row][col] = blend(pixel1, pixel2, alpha);
			}
		}
		return blendedImage;
	}

	/**
	 * Morphs the source image into the target image, gradually, in n steps.
	 * Animates the morphing process by displaying the morphed image in each step.
	 * Before starting the process, scales the target image to the dimensions
	 * of the source image.
	 */
	public static void morph(Color[][] source, Color[][] target, int n) {
		int sourceRows = source.length;
		int sourceCols = source[0].length;
		Color[][] blendedImage = scaled(target, sourceCols, sourceRows);
		for(int step = 0; step < n + 1; step++) {
			double alphaStep = (double) (n - step) / n;
			Color[][] morphed = blend(source, blendedImage, alphaStep);
			Runigram.display(morphed);
			StdDraw.pause(500);
		}
	}
	
	/** Creates a canvas for the given image. */
	public static void setCanvas(Color[][] image) {
		StdDraw.setTitle("Runigram 2023");
		int height = image.length;
		int width = image[0].length;
		StdDraw.setCanvasSize(height, width);
		StdDraw.setXscale(0, width);
		StdDraw.setYscale(0, height);
        // Enables drawing graphics in memory and showing it on the screen only when
		// the StdDraw.show function is called.
		StdDraw.enableDoubleBuffering();
	}

	/** Displays the given image on the current canvas. */
	public static void display(Color[][] image) {
		int height = image.length;
		int width = image[0].length;
		for (int i = 0; i < height; i++) {
			for (int j = 0; j < width; j++) {
				// Sets the pen color to the pixel color
				StdDraw.setPenColor( image[i][j].getRed(),
					                 image[i][j].getGreen(),
					                 image[i][j].getBlue() );
				// Draws the pixel as a filled square of size 1
				StdDraw.filledSquare(j + 0.5, height - i - 0.5, 0.5);
			}
		}
		StdDraw.show();
	}
}

