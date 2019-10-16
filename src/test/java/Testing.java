import io.github.angpysha.diploma_raspberry.Socket.Socket;
import io.github.angpysha.diploma_raspberry.TestObj;
import io.socket.client.IO;
import org.junit.Assert;
import org.junit.Test;

public class Testing {
    @Test
    public void TestInterval() {
          //  DHTRunner runner = new DHTRunner(15,this);
            System.out.println("aaaaaaaa");
//        while (true) {
//
//        }
    }

//    @Test
//    public void TestSensor() {
//        DHT11 dht11 = new DHT11();
//
//        float[] da = dht11.readData(7);
//        Assert.assertTrue(da[0]>0);
//        Assert.assertTrue(da[1]>0);
//    }

    @Test
    public void TestSocket() {
        Socket socket = Socket.getInstanse("https://raspi-info-bot.herokuapp.com/");


//        while(true) {
//
//        }
    }

    @Test
    public void TestScala1() {
        TestObj testObj = new TestObj();
        int tt = testObj.tset(3);
        Assert.assertEquals(tt,27);
    }

}
