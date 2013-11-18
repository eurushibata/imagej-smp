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
        			  "Circularidade = " + formFactor() + "\n" +
        			  "Arredondamento = " + roundness() + "\n");
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
	// A perimeter is a path that surrounds a two-dimensional shape.
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

	// Euclidian Distance (Distância Euclidiana)
	// the Euclidean distance is the "ordinary" distance between two points.
	public double euclidianDistance(int x1, int y1, int x2, int y2) {
		return Math.sqrt(Math.pow((x2-x1),2) + Math.pow(y2-y1,2));
	}

	// Length (Maior Eixo)
	// Length is defined as the longest cord along the angle Q given by the moment's axis to the x-axis.
	public double length() {
		double length = 0.0;
		int x1, y1, x2, y2;
		int perimeter = (int)perimeter();
        try {
			ImageProcessor output = img.getProcessor();

            int[][] pixelPerimeter = new int[2][perimeter];
            int k = 0;
            
            for (int x = 0; x < output.getWidth(); x++)
				for (int y = 0; y < output.getHeight(); y++) {
                    if(output.getPixel(x, y) != 0 && ((x-1) > 0) && ((y-1) > 0) && ((output.getPixel(x+1, y)==0) || (output.getPixel(x-1, y)==0) || (output.getPixel(x, y+1)==0) || (output.getPixel(x, y-1)==0))) {
						pixelPerimeter[0][k] = x;
						pixelPerimeter[1][k] = y;
						k++;
                    }
                }

            double temp;
            for(int i = 0; i < perimeter; i++)
                for(int j = i+1; j < perimeter; j++) {
                    x1 = pixelPerimeter[0][i];
                    y1 = pixelPerimeter[1][i];
                    x2 = pixelPerimeter[0][j];
                    y2 = pixelPerimeter[1][j];    
                    temp = euclidianDistance(x1,x2,y1,y2);
                    if(temp > length)
                        length = temp;
                }
        }
        catch(Exception e){
            String err = "Erro no cálculo do maior eixo da imagem \n" + e.toString();
            IJ.log(err);
        }
        return length;
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

	// Form Factor (Circularidade)
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

	// Roundness (Arredondamento) 
	// Roundness describes the Shape's resemblance to a circle. The roundness factor of a Shape will approach 1.0 the closer the Shape resembles a circle.
	public double roundness() {
        double roundness = 0.0;
        
        try {
            roundness = (4 * area())/(Math.PI * Math.sqrt(length()));
        }
        catch(Exception e){
            String err = "Erro no cálculo do arredondamento da imagem \n" + e.toString();
            IJ.log(err);
        }
        return roundness;
    }



	// // Breadth
	// // Breadth (or width) is defined as the longest cord perpendicular to the angle Q given by the moments axis to the x-axis.
	// public double breadth() {

	// }

}
