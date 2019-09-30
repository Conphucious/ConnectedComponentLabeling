import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

import javax.imageio.ImageIO;

import javafx.beans.binding.Bindings;
import javafx.embed.swing.SwingFXUtils;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class DisplayWindow {

	public DisplayWindow(Stage x, int[][] binaryAnalysis, File img, File imgGrey, File imgBin) {
		Stage stage = new Stage();
		ScrollPane scroll = new ScrollPane();
		StackPane sp = new StackPane();
		VBox paneP = new VBox(10);

		HBox paneA = new HBox(10);
		HBox paneB = new HBox(10);
		Image image = new Image("file:" + img.toString());
		Image greyImage = new Image("file:images/greyscale/" + imgGrey.getName());
		Image binaryImage = new Image("file:images/binary/" + imgBin.getName());
		Image cclImage = cclWindow(image, binaryAnalysis, img);

		paneA.getChildren().addAll(new Text(""), new ImageView(image), new ImageView(greyImage), new Text(""));
		paneB.getChildren().addAll(new Text(""), new ImageView(binaryImage), new ImageView(cclImage), new Text(""));
		paneP.getChildren().addAll(paneA, paneB);
		
		paneA.setAlignment(Pos.CENTER);
		paneB.setAlignment(Pos.CENTER);
		paneP.setAlignment(Pos.CENTER);
		
		sp.getChildren().add(paneP);
		
		
		Label oLb = new Label("Original");
		Label gLb = new Label("Greyscale");
		Label bLb = new Label("Binary");
		Label cLb = new Label("CCL");
		sp.getChildren().addAll(oLb, gLb, bLb, cLb);
		
		StackPane.setAlignment(oLb, Pos.TOP_LEFT);
		StackPane.setAlignment(gLb, Pos.TOP_RIGHT);
		StackPane.setAlignment(bLb, Pos.BOTTOM_LEFT);
		StackPane.setAlignment(cLb, Pos.BOTTOM_RIGHT);
		StackPane.setAlignment(paneP, Pos.CENTER);
		scroll.setContent(sp);
		
		sp.minWidthProperty().bind(
				Bindings.createDoubleBinding(() -> scroll.getViewportBounds().getWidth(), scroll.viewportBoundsProperty()));
		
		stage.setMinHeight(image.getHeight() * 2);
		stage.setMinWidth(image.getWidth() * 2);
		stage.setScene(new Scene(scroll, 150, 150));
		stage.setTitle("Jimmy's CCL");
		//stage.setResizable(false);
		stage.show();
	}

	public Image cclWindow(Image image, int[][] binaryAnalysis, File img) {
		int width = (int) image.getWidth();
		int height = (int) image.getHeight();

		WritableImage wImage = new WritableImage(width, height);
		PixelWriter writer = wImage.getPixelWriter();
		
		ArrayList<Color> colorList = new ArrayList<>();
		
		
		for (int label = 1; label < JIP.lc.label; label++) {
			// generate a random colour here that doesn't already exist
			Random random = new Random();
			Color randomColor = Color.rgb(random.nextInt(255), random.nextInt(255), random.nextInt(255));
			
			// make sure not already exist or make another
			for (int i = 0; i < colorList.size(); i++)
				while (randomColor == colorList.get(i))
					randomColor = Color.rgb(random.nextInt(255), random.nextInt(255), random.nextInt(255));
			
			for (int x = 0; x < binaryAnalysis.length; x++)
				for (int y = 0; y < binaryAnalysis[0].length; y++)					
					if (binaryAnalysis[x][y] == label)
						writer.setColor(y, x, randomColor);
		}
		
		try {
			String ext = "";
			int i = img.getName().lastIndexOf('.');
			if (i > 0)
			    ext = img.getName().substring(i+1);
			
			File output = new File("images/ccl/ccl_" + img.getName());
			ImageIO.write(SwingFXUtils.fromFXImage(wImage, null), ext, output);
			
			System.out.println("Exporting Image....\n"
	         		+ "--------------------------------------------------\n"
					+ "[ " + output.getName() + " ] rendered at " + output.length() + " KB.");
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
//		 fill in white space. colour is always light grey.
		PixelReader reader = wImage.getPixelReader();
		for (int x = 0; x < width; x++)
			for (int y = 0; y < height; y++)
				if (reader.getColor(x, y).toString().equals("0x00000000"))
					writer.setColor(x, y, Color.rgb(232, 232, 232)); //	System.out.println(color.toString());
		
		return wImage;
	}

}
