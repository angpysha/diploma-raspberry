package io.github.angpysha.diploma_raspberry;

import org.ini4j.Wini;

import java.io.File;
import java.io.IOException;

public class AppConfig {
    private static volatile AppConfig instanse;
    private Wini wini;

    private String API_URL = "";
    private String SocketUrl = "";

    public static AppConfig getInstanse(String filename) {
        AppConfig localinstanse = instanse;

        if (localinstanse == null) {
            synchronized (AppConfig.class) {
                localinstanse = instanse;

                if (localinstanse ==null) {
                    localinstanse = new AppConfig(filename);
                }
            }
        }

        return localinstanse;
    }

    private AppConfig(String filepath) {
        try {
            IfExist(filepath);
            wini = new Wini(new File(filepath));
        } catch (IOException ex) {
            int i =0;
        }
    }

    private void IfExist(String filename) throws IOException {
        File file = new File(filename);

        if (!file.exists())
            file.createNewFile();
    }

    public <TClass> TClass GetValue(String category,String property, Class<TClass> tClass) {
        return wini.get(category,property,tClass);
    }

    public <TClass> void SetValue(String category,String property, TClass value) {
        wini.put(category,property,value);
        try {
            wini.store();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getapiUrl() {
        if (API_URL.isEmpty() || API_URL ==null)
            API_URL = GetValue("Remote","ApiUrl",String.class);
        return API_URL;
    }

    public void setapiUrl(String API_URL) {
        this.API_URL = API_URL;
        SetValue("Remote","ApiUrl",API_URL);
    }

    public String getSocketUrl() {
        if (SocketUrl.isEmpty() || SocketUrl == null)
            SocketUrl = GetValue("Remote","SocketUrl",String.class);
        return SocketUrl;
    }

    public void setSocketUrl(String socketUrl) {
        SocketUrl = socketUrl;
        SetValue("Remote","SocketUrl",socketUrl);
    }

}
