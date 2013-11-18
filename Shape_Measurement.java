import ij.*;
import ij.process.*;
import ij.gui.*;
import java.awt.*;
import ij.plugin.*;
import ij.plugin.frame.*;

public class Shape_Measurement implements PlugIn {
	ImagePlus img;


	public void run(ImageProcessor arg) {
		IJ.showMessage("My_Plugin","Hello world!");
	}


	// Area
	// The Area is calculated from the shapes periphery, i.e. the closed polygon that surrounds the feature.
	public double area() {
		double area = 0;
		try {
			ImageProcessor output = img.getProcessor();

			for(int x = 0; x < output.getWidth(); x++)
				for(int y = 0; y < output.getHeight(); y++)
					if(output.getPixel(x, y) != 0)
						area++;
		}
		catch(Exception e){
			this.err = 'Error in measuring the area \n' + e.toString();
			IJ.log(this.err);
		}
		return area;
	}


	// Diameter (Diâmetro Efetivo)
	// The diameter (or Heywood diameter) is expressed as the diameter of a circle having an area equivalent to the shape's area.
	public double diameter() {
		double out = 0.0;
        double area;
        area = areaImagem(img);
        try {
            diametro = 2 * Math.sqrt((double)area/Math.PI );
        }
        catch(Exception e){
            this.errMessage = "Erro no calculo do diametro efetivo da imagem \n" + e.toString();
            IJ.log(this.errMessage);
        }
        return diametro;
	}

	// Length
	// Length is defined as the longest cord along the angle Q given by the moment's axis to the x-axis.
	public double length() {

	}

	// Breadth
	// Breadth (or width) is defined as the longest cord perpendicular to the angle Q given by the moments axis to the x-axis.
	public double breadth() {

	}

}
