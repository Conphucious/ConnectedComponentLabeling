import java.io.File;
import java.util.Scanner;

import javax.imageio.ImageIO;

import javafx.application.Application;
import javafx.stage.Stage;

public class Driver extends Application {

	// change file paths into /binary, /ccl, /greyscale

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		Scanner input = new Scanner(System.in);

		
		System.out.println("\t[ Jimmy's CCL ]\n--------------------------------------------------");
		System.out.print("Clear prior files? [y/n]\n>: ");
		if (input.nextLine().toLowerCase().equals("y")) {
			eraseFiles(new File("images/greyscale"));
			eraseFiles(new File("images/binary"));
			eraseFiles(new File("images/ccl"));
			eraseFiles(new File("analysis/"));
		}
		String answer = "";

		while (!answer.equals("exit")) {
			JIP.lc.label = 1;
			System.out.print("Enter an image file: ");
			String fileSrc = input.nextLine();
			System.out.println("--------------------------------------------------\n\n");

			File img = new File(fileSrc);
			if (img.isFile()) {
				File imgGrey = JIP.greyThresholding(img);
				File imgBin = JIP.binarizedImage(imgGrey);
//				File imgBin = Otsu.threshold(imgGrey.toString());
				int[][] binaryAnalysis = JIP.traversePixels(ImageIO.read(imgBin));
				binaryAnalysis = JIP.calculateCCL(binaryAnalysis);
				binaryAnalysis = JIP.ps(binaryAnalysis);
				JIP.save(binaryAnalysis);
				
				
				//binaryAnalysis = JIP.passThrough(binaryAnalysis);
				// this won't display until end because stupid JFX thread different so don't make obj.
				
				//arr list, add it. index = lowest num.
				
				//binaryAnalysis = JIP.ps(binaryAnalysis);
				
				
				
				
//				for (String item : ls) {
//					String[] line = item.split(",");
//					if (Integer.valueOf(line[2]) != 0 && binaryAnalysis[]) 
//						val = Integer.valueOf(line[2]);
//					
//					System.out.print(item + "\t");
//				}
 				
				
				
				new DisplayWindow(primaryStage, binaryAnalysis, img, imgGrey, imgBin);
			} else {
				System.out.println("Invalid file path entered.");
			}
			
			System.out.println("\n\nContinue or [exit]?");
			answer = input.nextLine();

		}
		input.close();
	}
	
	

	private static void eraseFiles(File dir) {
		for (File file : dir.listFiles()) {
			if (file.isDirectory())
				eraseFiles(file);
			file.delete();
		}
	}

}
