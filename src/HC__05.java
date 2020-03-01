package harrison;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.bluetooth.DeviceClass;
import javax.bluetooth.DiscoveryAgent;
import javax.bluetooth.DiscoveryListener;
import javax.bluetooth.LocalDevice;
import javax.bluetooth.RemoteDevice;
import javax.bluetooth.ServiceRecord;
import javax.bluetooth.UUID;
import javax.microedition.io.Connector;
import javax.microedition.io.StreamConnection;

public class HC__05 {

    boolean scanFinished = false;
    RemoteDevice hc05device;
    String hc05Url = "btspp://001403062B23:1;authenticate=false;encrypt=false;master=false";

    public static void main(String[] args) {
        try {
            new HC__05().go();
        } catch (Exception ex) {
            Logger.getLogger(HC__05.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void go() throws Exception {

        //if you know your hc05Url this is all you need:
        StreamConnection streamConnection = (StreamConnection) Connector.open(hc05Url);
        OutputStream os = streamConnection.openOutputStream();
        InputStream is = streamConnection.openInputStream();

        os.write("1".getBytes()); //just send '1' to the device
        os.close();
        is.close();
        streamConnection.close();
    }
}


