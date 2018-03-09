import io.github.angpysha.diploma_raspberry.AppConfig;
import org.junit.Assert;
import org.junit.Test;

public class TestIni {
    @Test
    public void TestWrite() {
        AppConfig config = AppConfig.getInstanse("test.ini");
        config.SetValue("test","test",55);
    }

    @Test
    public void TetGet() {
        AppConfig config = AppConfig.getInstanse("test.ini");
        int al = config.GetValue("test","test",Integer.class);
        Assert.assertEquals(al,55);
    }

    @Test
    public void TestPropertyWrite() {
        AppConfig config = AppConfig.getInstanse("test.ini");
        config.setapiUrl("http://diplomaapi:8081/api/v1");
    }

    @Test
    public void TestPropertyRead() {
        AppConfig config = AppConfig.getInstanse("test.ini");
        String url = config.getapiUrl();
        Assert.assertEquals(url,"http://diplomaapi:8081/api/v1");
    }
}
