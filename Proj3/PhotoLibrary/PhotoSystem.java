package PhotoLibrary;

import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JRadioButton;
import javax.swing.filechooser.FileNameExtensionFilter;

import student.PhotoTools;

/**
 * Manages photo editing system.  A dialog box is displayed which allows the
 * user to load an image from the local machine or using an Internet URL.  
 * 
 * Once the image is loaded, it is displayed and the user will be given several 
 * options for editing the image.  After displaying the edited image, the user
 * may choose to edit the image further, or to load a different image.
 * 
 * @author (c)2007 Fawzi Emad {with many later changes by Evan Golub}
 */
public class PhotoSystem {

	// To make Serializable interface happy
	static final long serialVersionUID = 20181009;

	static final int NUM_RADIO_BUTTONS = 12;

	/**
	 * Begin the application.
	 */
	public static void begin(String initialPhoto) {


		JRadioButton[] radioButtons = new JRadioButton[NUM_RADIO_BUTTONS];
		radioButtons[0] = new JRadioButton("Red Only");
		radioButtons[2] = new JRadioButton("Blue Only"); 
		radioButtons[4] = new JRadioButton("Grayscale");
		radioButtons[6] = new JRadioButton("Artistic");
		radioButtons[8] = new JRadioButton("Censor It");
		radioButtons[10] = new JRadioButton("Horizontal Stretch");
		
		radioButtons[1] = new JRadioButton("Vertical Stretch");
		radioButtons[3] = new JRadioButton("Mirror It");
		radioButtons[5] = new JRadioButton("Me and My Mirror");
		radioButtons[7] = new JRadioButton("Rotated");
		radioButtons[9] = new JRadioButton("Upside Down");
		radioButtons[11] = new JRadioButton("Wacky");



		while(true) {
			try {
				String fileToOpen = pickFile(initialPhoto);
				Photograph masterPhoto = new Photograph(fileToOpen);
				Photograph displayedPhoto = new Photograph(fileToOpen);
				boolean displaying = true;
				while(displaying) {
					synchronized(displayedPhoto) {
						int[] flag = new int[1]; //a hack for a mutable integer
						new PhotoFrame(flag, radioButtons, displayedPhoto, 
								fileToOpen, 0, 0);
						displayedPhoto.wait();
						switch (flag[0]) { 
						case 0: //modify
							displayedPhoto = doModification(radioButtons, displayedPhoto);
							break;
						case 1: //reset
							displayedPhoto = PhotoTools.copy(masterPhoto);
							break;
						case 2: //save
							saveIt(displayedPhoto);
							break;
						case 3: //load
							displaying = false;
							break;
						}
					}

				}
			}
			catch(InterruptedException e) {
				throw new RuntimeException(e);
			}
		} 
	}

	public static Photograph doModification(JRadioButton[] radioButtons, Photograph displayedPhoto) {
		if (radioButtons[0].isSelected()) {
			return PhotoTools.isolateColor(displayedPhoto, 0);					
		} else if (radioButtons[2].isSelected()) {
			return PhotoTools.isolateColor(displayedPhoto, 1);
		} else if (radioButtons[4].isSelected()) {
			return PhotoTools.makeGrayscale(displayedPhoto);
		}  else if (radioButtons[6].isSelected()) {
			return PhotoTools.makeArtistic(displayedPhoto);
		} else if (radioButtons[8].isSelected()) {
			return PhotoTools.censorIt(displayedPhoto);
		}  else if (radioButtons[10].isSelected()) {
			return PhotoTools.stretched(displayedPhoto, 0);
		} else if (radioButtons[1].isSelected()) {
			return PhotoTools.stretched(displayedPhoto, 1);
		} else if (radioButtons[3].isSelected()) {
			return PhotoTools.mirrorIt(displayedPhoto);
		} else if (radioButtons[5].isSelected()) {
			return PhotoTools.makeDoubleWithMirror(displayedPhoto);
		} else if (radioButtons[7].isSelected()) {
			return PhotoTools.rotated(displayedPhoto);
		} else if (radioButtons[9].isSelected()) {
			return PhotoTools.upsideDown(displayedPhoto);
		} else if (radioButtons[11].isSelected()) {
			return PhotoTools.wacky(displayedPhoto);
		} else {
			throw new RuntimeException("error -- no radio button selected");
		}
	}

	public static String pickFile(String initialPhoto) {
		boolean filePicked = false;
		String fileToOpen = null;
		while (!filePicked) {
			JFileChooser fileChooser = new JFileChooser(System.getProperty("user.dir"));
			FileNameExtensionFilter fileFilter = 
					new FileNameExtensionFilter("Image Files (gif, jpg, png, tif)", "gif","jpg","png","tif");
			fileChooser.setFileFilter(fileFilter);

			if (fileChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
				fileToOpen = fileChooser.getSelectedFile().getAbsolutePath();
				filePicked = true;
			}
		}
		return fileToOpen;
	}



	public static void saveIt(Photograph displayedPhoto) {
		try {
			JFileChooser fileChooser = new JFileChooser(System.getProperty("user.dir")){
				private static final long serialVersionUID = 20181011L;

				@Override
			    public void approveSelection(){ //https://stackoverflow.com/questions/3651494/jfilechooser-with-confirmation-dialog
			        File f = getSelectedFile();
			        if(f.exists() && getDialogType() == SAVE_DIALOG){
			            int result = JOptionPane.showConfirmDialog(this,
			            		"A file named " + f.getName() + " already exists, overwrite?",
			            		"Overwrite?",
			            		JOptionPane.YES_NO_CANCEL_OPTION
			            );
			            switch(result){
			                case JOptionPane.YES_OPTION:
			                    super.approveSelection();
			                    return;
			                case JOptionPane.NO_OPTION:
			                    return;
			                case JOptionPane.CLOSED_OPTION:
			                    return;
			                case JOptionPane.CANCEL_OPTION:
			                    cancelSelection();
			                    return;
			            }
			        }
			        super.approveSelection();
			    }        
			};
			FileNameExtensionFilter fileFilter = 
					new FileNameExtensionFilter("PNG Images", "png");
			fileChooser.setFileFilter(fileFilter);
			if (fileChooser.showSaveDialog(null) == JFileChooser.APPROVE_OPTION) {
				String fileChoice = fileChooser.getSelectedFile().getAbsolutePath();
				if (!fileChoice.endsWith(".png")) {
					fileChoice+=".png";
				}
				File fileDestination = new File(fileChoice);
				ImageIO.write(
						displayedPhoto.getImage(),
						"png", 
						fileDestination
				);
			}
		} catch (IOException e) {
			System.err.println("Oooops!");
		}
	}
}
