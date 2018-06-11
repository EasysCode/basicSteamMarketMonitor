/*
 * Steam Item Monitor (First Monitor Test Program)
 * Programmer: Easy
*/

package steamitemmonitor;

import java.io.IOException;
import java.util.Timer;

public class SteamItemMonitor {
    //previous array

    
    public static void main(String[] args) throws IOException {
        Timer timer = new Timer();
        timer.schedule(new realSteamMonitor(), 0, 60000);

    }
    
}
