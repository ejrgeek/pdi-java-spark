import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public abstract class Processador {

    public static BufferedImage paraPB(BufferedImage image) {
        int altura = image.getHeight();
        int largura = image.getWidth();

        for (int lin = 0; lin < largura; lin++){
            for (int col = 0; col < altura; col++) {

                int pixel = image.getRGB(lin, col);
                Color cor = new Color(pixel);

                int pixelCorCinza = (int) ((cor.getRed() * .3) + (cor.getGreen() * .6) + (cor.getBlue() * .1));

                Color novaCor = new Color(pixelCorCinza);
                Color cinza = new Color(novaCor.getBlue(),novaCor.getBlue(),novaCor.getBlue());

                image.setRGB(lin, col, cinza.getRGB());

            }
        }
        return image;
    }

    public static BufferedImage binarizacao(BufferedImage image, int limiar) {

        BufferedImage imagemBin = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_3BYTE_BGR);

        int altura = imagemBin.getHeight();
        int largura = imagemBin.getWidth();


        for (int lin = 0; lin < largura; lin++){
            for (int col = 0; col < altura; col++){

                int pixel = image.getRGB(lin, col);
                Color cor = new Color(pixel);

                if (cor.getRed() >= limiar){
                    imagemBin.setRGB(lin, col, Color.white.getRGB());
                } else {
                    imagemBin.setRGB(lin, col, Color.black.getRGB());
                }

            }

        }

        return imagemBin;

    }



}
