import spark.Request;
import spark.Response;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;

import static spark.Spark.*;

public class Servidor {

    private static void enableCORS() {
        options("/*", (request, response) -> {
            String accessControlRequestHeaders = request.headers("Access-Control-Request-Headers");
            if (accessControlRequestHeaders != null) {
                response.header("Access-Control-Allow-Headers", accessControlRequestHeaders);
            }
            String accessControlRequestMethod = request.headers("Access-Control-Request-Method");
            if (accessControlRequestMethod != null) {
                response.header("Access-Control-Allow-Methods", accessControlRequestMethod);
            }
            return "OK";
        });

        before((request, response) -> {
            response.header("Access-Control-Allow-Origin", "*");
            response.header("Access-Control-Allow-Methods", "GET, POST, OPTIONS");
        });
    }

    private static BufferedImage binarizarImagem(BufferedImage imagem){
        BufferedImage imagemCinza = Processador.paraPB(imagem);
        int limiar = 127;
        return Processador.binarizacao(imagemCinza, limiar);
    }

    public static void main() {

        port(8080);
        enableCORS();

        post("/binarizacao", (Request request, Response response) -> {

            try{
                byte[] body = request.bodyAsBytes();
                if (body == null || body.length == 0){
                    halt(400, "Imagem não enviada");
                }

                InputStream entrada = new ByteArrayInputStream(body);
                BufferedImage imagem = ImageIO.read(entrada);
                if (imagem == null) {
                    System.err.println("Falha na decodificação da imagem");
                    halt(415, "Formato de arquivo não suportado ou envio incorreto.");
                }

                BufferedImage imagemProcessada = binarizarImagem(imagem);

                ByteArrayOutputStream imgData = new ByteArrayOutputStream();
                ImageIO.write(imagemProcessada, "png", imgData);

                response.type("image/png");
                return imgData.toByteArray();

            } catch (Exception e){
                e.printStackTrace();
                response.status(500);
                return "Erro interno: " + e.getMessage();
            }

        });

        System.out.println("[Servidor rodando] http://localhost:8000");
        System.out.println("[Rotas disponíveis]");
        System.out.println("() -> /binarizacao");

    }

}
