package utils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import model.File;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;


/**
 * @author Orel Gershonovich
 * @since 15-July-2022
 */

public class HttpClient {

    private HttpURLConnection connection;

    public HttpClient(String url) {
        try {
            URL url1 = new URL(url);
            connection = (HttpURLConnection) url1.openConnection();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public List<File> createPostRequest(String path) {
        List<File> response = new ArrayList<>();
        try {
            connection.setRequestMethod("POST");
            connection.setDoOutput(true);
            connection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            byte[] out = path.getBytes(StandardCharsets.UTF_8);
            int length = out.length;
            connection.setFixedLengthStreamingMode(length);
            connection.connect();

            try (OutputStream os = connection.getOutputStream()) {
                try {
                    os.write(out);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            StringBuilder content = null;
            try (BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
                String line;
                content = new StringBuilder();
                while ((line = br.readLine()) != null) {
                    content.append(line);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            Gson gson = new Gson();
            Type type = new TypeToken<ArrayList<File>>() {
            }.getType();
            if (content != null) {
                response = gson.fromJson(content.toString(), type);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return response;
    }
}
