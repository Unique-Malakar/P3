package student;

import PhotoLibrary.Photograph;
import PhotoLibrary.Pixel;

/**
 * The methods in this class are to be implemented by you.  
 * This class starter file provides various static methods that take 
 * a photograph and produce a new one based on it, but with various 
 * modifications.
 * 
 * See the project description for details of method implementations.
 * 
 * @author PUT YOUR NAME HERE
 *
 */
public class PhotoTools {


	//This method is provided as a starting point.  Please read through
	//  it and think about what it does and why - if you aren't sure of
	//  something, ask us in office hours.  
	//Do not alter this code.  It is used by the GUI.
	public static Photograph copy(Photograph photo) {
		Photograph newPhoto = new Photograph(photo.getWd(), photo.getHt());
		for (int x=0; x<photo.getWd(); x++) {
			for (int y=0; y<photo.getHt(); y++) {
				newPhoto.setPixel(x, y, photo.getPixel(x, y));
			}
		}
		return newPhoto;
	}


	public static Photograph isolateColor(Photograph photo, int type) {

		Photograph newPhoto= new Photograph(photo.getWd(),photo.getHt());
		for (int row=0; row<photo.getWd(); row++) {
			for (int col=0; col<photo.getHt(); col++) {
				if (type==0)
				{
					newPhoto.setPixel(row, col,new Pixel(photo.getPixel(row, col).getRed(),0,0));
				}
				else if(type==1)
				{
					newPhoto.setPixel(row, col,new Pixel(0,0,photo.getPixel(row, col).getBlue()));
				}

			}
		}
		return newPhoto;
	}


	public static Photograph makeGrayscale(Photograph photo) {

		Photograph newPhoto= new Photograph(photo.getWd(),photo.getHt());
		int G=0;
		for (int row=0; row<photo.getWd(); row++) {
			for (int col=0; col<photo.getHt(); col++) {

				G= (int)(photo.getPixel(row, col).getRed() * 0.1) +
						(int)(photo.getPixel(row, col).getGreen() * 0.1) +
						(int)(photo.getPixel(row, col).getBlue() * 0.8);

				newPhoto.setPixel(row, col,new Pixel(G,G,G));

			}
		}
		return newPhoto;
	}



	public static Photograph makeArtistic(Photograph photo) {

		Photograph newPhoto= new Photograph (photo.getWd(),photo.getHt());
		int sum= 0;
		for(int row=0;row<photo.getWd();row++) {
			for(int col=0;col<photo.getHt();col++) {

				sum= (int) (photo.getPixel(row, col).getRed() +
						(int) (photo.getPixel(row, col).getGreen())+
						(int) (photo.getPixel(row, col).getBlue())
						);

				if( sum>= 0 && sum<=191)
				{
					newPhoto.setPixel(row, col, new Pixel(0,0,0));
				}
				else if( sum>= 192 && sum<=343)
				{
					newPhoto.setPixel(row, col, new Pixel(63,63,63));
				}
				else if( sum>= 344 && sum<=575)
				{
					newPhoto.setPixel(row, col, new Pixel(127,127,127));
				}
				else if( sum>= 576 && sum<=765)
				{
					newPhoto.setPixel(row, col, new Pixel(255,255,255));
				}

			}
		}


		return newPhoto;
	}

	public static Photograph censorIt(Photograph photo) {


		Photograph newPhoto= new Photograph(photo.getWd(),photo.getHt());
		int AveR=0;int AveG=0; int AveB=0;

		// main loop which add 10 each time//
		for ( int row = 0 ; row < photo.getWd() ; row += 10 ) {
			for ( int col = 0 ; col < photo.getHt() ; col += 10 )  {
				int RSum=0;int GSum=0;int BSum=0;

				//  2nd loop  for 10*10                    //
				for( int row1 = row ; row1 < row + 10 ; row1++ ) {
					for( int col1 = col ; col1 < col + 10 ; col1++ ) {

						RSum=RSum+ (int)(photo.getPixel(row1, col1).getRed()) ;
						GSum=GSum+ (int)(photo.getPixel(row1, col1).getGreen());
						BSum=BSum+ (int)(photo.getPixel(row1, col1).getBlue());
					}
				}

				AveR=RSum/100; AveG=GSum/100; AveB=BSum/100;

				// 3rd() loop for coloring//
				for( int row2 = row ; row2 < row + 10 ; row2++ ) {
					for(int col2 = col ; col2 < col + 10 ; col2++ ) {
						newPhoto.setPixel(row2, col2,new Pixel(AveR,AveG,AveB));
					}
				}
				AveR=AveG=AveB=0;
			}
		}
		return newPhoto;
	}

	public static Photograph stretched(Photograph photo, int type) {

		Photograph newPhoto = null;

		if(type==0){ 
			newPhoto = new Photograph(photo.getWd()*2, photo.getHt());
			for (int row=0; row<photo.getHt();row++){
				for (int col=0; col<photo.getWd(); col++){
					newPhoto.setPixel(col*2, row, photo.getPixel(col, row));
					newPhoto.setPixel(col*2+1, row, photo.getPixel(col,row));
				}
			}
		}
		else { 
			newPhoto = new Photograph(photo.getWd(),photo.getHt()*2);
			for (int roww=0;roww<photo.getWd();roww++){
				for (int col=0; col<photo.getHt();col++){
					newPhoto.setPixel(roww, col*2, photo.getPixel(roww, col));
					newPhoto.setPixel(roww, col*2+1, photo.getPixel(roww, col));
				}
			}
		}
		return newPhoto;

	}

	public static Photograph mirrorIt(Photograph photo) {

		int last = photo.getWd()-1;
		Photograph newPhoto= new Photograph(photo.getWd(),photo.getHt());

		for (int row=0; row<photo.getWd(); row++) {
			for (int col=0; col<photo.getHt(); col++) {

				newPhoto.setPixel(last-row,col,photo.getPixel(row, col));
			}
		}
		return newPhoto;
	}


	public static Photograph makeDoubleWithMirror(Photograph photo) {

		int last = photo.getWd()-1;
		Photograph newPhoto= new Photograph(2*photo.getWd(),photo.getHt());

		for (int row=0; row<photo.getWd(); row++) {
			for (int col=0; col<photo.getHt(); col++) {

				newPhoto.setPixel(last-row,col,photo.getPixel(row, col));
				newPhoto.setPixel(newPhoto.getWd()/2+row, col, photo.getPixel(row, col));
			}
		}
		return newPhoto;

	}



	//Challenges down here.

	public static Photograph rotated(Photograph photo) {
		
		int Lheight=photo.getHt()-1;
		Photograph newPhoto = new Photograph(photo.getHt(), photo.getWd());
		for (int row=0; row<photo.getWd(); row++){
			for (int col=0; col<photo.getHt(); col++){
				newPhoto.setPixel(Lheight-col, row, photo.getPixel(row, col));
			}
		}
		return newPhoto;
		
	}

	public static Photograph upsideDown(Photograph photo) {

		return rotated(rotated(photo));

	
	}




	public static Photograph wacky(Photograph photo) {
		throw new RuntimeException("You might implement this...");

	}



}
