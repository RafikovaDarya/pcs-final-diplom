import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.text.ParseException;
import java.util.Scanner;

public class Client {
    public static void main(String[] args) {

        try (Socket socket = new Socket("127.0.0.1", 8989);
             PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()))
        ) {
            //подключение к серверу

            writer.println(jsonWord());
            String response;
            while ((response = reader.readLine()) != null) {
                System.out.println(response);
            }

        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
    }

    public static String jsonWord() throws ParseException {

        Scanner scanner = new Scanner(System.in);
        String word = scanner.nextLine();

        String jsonText = "{\"word\": \"" + word + "\"}";
        //System.out.println(jsonText);


        return jsonText;
    }


}

