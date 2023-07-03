import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;

public class Main {
    public static void main(String[] args) throws Exception {
        BooleanSearchEngine engine = new BooleanSearchEngine(new File("pdfs"));
        //System.out.println(engine.search("управление"));

        // здесь создайте сервер, который отвечал бы на нужные запросы
        // слушать он должен порт 8989
        // отвечать на запросы /{word} -> возвращённое значение метода search(word) в JSON-формате

        try (ServerSocket serverSocket = new ServerSocket(8989)) { // стартуем сервер один(!) раз


            while (true) { // в цикле(!) принимаем подключения
                try (
                        Socket socket = serverSocket.accept();
                        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                        PrintWriter out = new PrintWriter(socket.getOutputStream())
                ) {
                    String request = in.readLine();

                    //Request r = new Gson().fromJson(request, Request.class);
                    //List<PageEntry> result = engine.search(r.getWord());

                    List<PageEntry> result = engine.search(request);
                    var gson = new GsonBuilder().setPrettyPrinting().create();
                    var response = gson.toJson(result);
                    out.println(response);
                }
            }
        } catch (IOException e) {
            System.out.println("Не могу стартовать сервер");
            e.printStackTrace();
        }
    }
}