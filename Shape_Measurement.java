import java.io.*;
import ij.*;
import ij.process.*;
import ij.gui.*;
import java.awt.*;
import ij.plugin.*;
import ij.plugin.filter.*;

public class Shape_Measurement implements PlugInFilter {
	ImagePlus img;

	@Override
    public int setup(String arg, ImagePlus imp) {
        img = imp;
        ImageConverter ic = new ImageConverter(imp);
        ic.convertToGray8();
        return DOES_ALL;
    }

	@Override
	public void run(ImageProcessor arg) {

		GenericDialog gd = new GenericDialog("Vetor de características", IJ.getInstance());
        
        gd.addMessage("Diametro efetivo(de Heywood) = " + diameter() + "\n" +
        			  "Circularidade = " + formFactor() + "\n");
        // gd.addMessage("Diametro efetivo = " + vet[0] + "\n"
        //              + "Circularidade = " + vet[1] + " \n"
        //              + "Arredondamento = " + vet[2] + "\n"
        //              + "Razão de raio = " + vet[3] + " \n"
        //              + "Compactação = " + vet[4] + " \n");
        gd.showDialog();

	}


	// Area (Área)
	// The Area is calculated from the shapes periphery, i.e. the closed polygon that surrounds the feature.
	public double area() {
		double area = 0.0;
		try {
			ImageProcessor output = img.getProcessor();

			for(int x = 0; x < output.getWidth(); x++)
				for(int y = 0; y < output.getHeight(); y++)
					if(output.getPixel(x, y) != 0)
						area++;
		}
		catch(Exception e){
			String err = "Erro no cálculo da área da imagem \n" + e.toString();
			IJ.log(err);
		}
		return area;
	}

	// Perimeter (Perímetro)
	// A perimeter is a path that surrounds a two-dimensional shape
	public double perimeter() {
		double perimeter = 0.0;
        try {
            ImageProcessor output = img.getProcessor();

            for(int x = 0; x < output.getWidth(); x++)
            	for(int y = 0; y < output.getHeight(); y++) {
                	if(output.getPixel(x, y) != 0 && ((x-1) > 0) && ((y-1) > 0) && ((output.getPixel(x+1, y)== 0) || (output.getPixel(x-1, y)== 0) || (output.getPixel(x, y+1)== 0) || (output.getPixel(x, y-1)== 0)))
                		perimeter++;
                }
        }
        catch (Exception e){
            String err = "Erro no cálculo do perímetro da imagem \n" + e.toString();
            IJ.log(err);
        }
        return perimeter;
	}


	// Diameter (Diâmetro Efetivo)
	// The diameter (or Heywood diameter) is expressed as the diameter of a circle having an area equivalent to the shape's area.
	public double diameter() {
		double diameter = 0.0;
        try {
            diameter = Math.sqrt(4*(double)area()/Math.PI);
        }
        catch(Exception e){
            String err = "Erro no cálculo do diâmetro efetivo da imagem \n" + e.toString();
            IJ.log(err);
        }
        return diameter;
	}

	// Forma Factor (Circularidade)
	// Form Factor provides a measure that describes the Shape of a feature.
	public double formFactor() {
		double formFactor = 0.0;
        try {
            formFactor = (4 * Math.PI*(double)area()) / (Math.pow((double)perimeter(), 2));
        }
        catch(Exception e){
            String err = "Erro no cálculo da circularidade da imagem \n" + e.toString();
            IJ.log(err);
        }
        return formFactor;
	}

	// // Length
	// // Length is defined as the longest cord along the angle Q given by the moment's axis to the x-axis.
	// public double length() {

	// }

	// // Breadth
	// // Breadth (or width) is defined as the longest cord perpendicular to the angle Q given by the moments axis to the x-axis.
	// public double breadth() {

	// }

}
