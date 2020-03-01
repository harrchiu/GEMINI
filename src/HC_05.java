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

public class HC_05 {

	boolean scanFinished = false;
	String hc05Url =
			"btspp://001403062B23:1;authenticate=false;encrypt=false;master=false"; //Replace this with your bluetooth URL

	public static void main(String[] args) {

		try {
			new HC_05().go();
		} catch (Exception ex) {
			Logger.getLogger(HC_05.class.getName()).log(Level.SEVERE, null, ex);
		}
	}
	private void go() throws Exception {

		StreamConnection streamConnection = (StreamConnection)
				Connector.open(hc05Url);
		OutputStream os = streamConnection.openOutputStream();
		InputStream is = streamConnection.openInputStream();

		// IMPORTANT
		int entriesExpected = 9;			// trying to detect FOUR photodiodes (change to 8)
		int avgLen = 10;
		
		int id = -1;
		
		int[] vals_0_3 = new int[4];			// array to send back
		int[] vals_12_15 = new int[4];			// array to send back
		double curVoltage = 0;						// voltage 
		String curString = "";					// the current string for this id slot

		
		int[] mins_0_3 = {1023,1023,1023,1023};			// array to send back
		int[] maxes_0_3 = {0,0,0,0};			// array to send back
		int[] mins_12_15 = {1023,1023,1023,1023};			// array to send back
		int[] maxes_12_15 = {0,0,0,0};			// array to send back
		
		int [] avg0 = new int[avgLen];
		int [] avg1 = new int[avgLen];
		int [] avg2 = new int[avgLen];
		int [] avg3 = new int[avgLen];
		int [] avg12 = new int[avgLen];
		int [] avg13 = new int[avgLen];
		int [] avg14 = new int[avgLen];
		int [] avg15 = new int[avgLen];

		int[] avgAng1 = new int[avgLen];
		int[] avgAng2 = new int[avgLen];
		int[] avgAng3 = new int[avgLen];


		APP_summ3 window = new APP_summ3 ();

		while (true){

			byte[] b = new byte[200];
			Thread.sleep(350);
			is.read(b);
			String fullThing = new String(b);

			for (int curCharID = 0; curCharID < fullThing.length(); curCharID += 1) {
				if (fullThing.charAt(curCharID) == '[') {
					id = 0;
					vals_0_3 = new int[4];			// clear everything at '['
					vals_12_15 = new int[4];			// clear everything at '['
					curString = "";		
					continue;
				}

				else if (fullThing.charAt(curCharID) == '|') {
					try {
						if (id <= 3)
							vals_0_3[id] = Integer.parseInt(curString);
						else if (id <= 7)
							vals_12_15[id-4] = Integer.parseInt(curString);
					}catch (Exception ex) {
						
					}
					curString = "";
					id += 1;
					continue;
				}

				else if (fullThing.charAt(curCharID) == ']' && id != -1) {
				
					if (id == 8) {
						curVoltage = new Double(curString).doubleValue()/100+1;
					}
					
					// print the raw sensor values. all eight.
					for (int num: vals_0_3) {
						System.out.print("-" + Integer.toString(num));
					}	
					System.out.print("--");
					for (int num: vals_12_15) {
						System.out.print("-" + Integer.toString(num));
					}	
					
					// print the moving average of the values
					System.out.print("  =");
					for (int a = 0; a < avgLen - 1; a += 1) {		// shift all values down one
						avg0[a] = avg0[a+1];
						avg1[a] = avg1[a+1];						// sensors
						avg2[a] = avg2[a+1];
						avg3[a] = avg3[a+1];
						avg12[a] = avg12[a+1];
						avg13[a] = avg13[a+1];						// sensors
						avg14[a] = avg14[a+1];
						avg15[a] = avg15[a+1];
						
						avgAng1[a] = avgAng1[a+1];					// angles
						avgAng2[a] = avgAng2[a+1];
						avgAng3[a] = avgAng3[a+1];


					}
					avg0[avgLen-1] = vals_0_3[0];
					avg1[avgLen-1] = vals_0_3[1];					// sensors
					avg2[avgLen-1] = vals_0_3[2];
					avg3[avgLen-1] = vals_0_3[3];
					avg12[avgLen-1] = vals_12_15[0];				// shift last value as newest reading
					avg13[avgLen-1] = vals_12_15[1];
					avg14[avgLen-1] = vals_12_15[2];				// sensors
					avg15[avgLen-1] = vals_12_15[3];
						
					
					int finalAvg0 = 0;	
					int finalAvg1 = 0;								// compute the final average value
					int finalAvg2 = 0;
					int finalAvg3 = 0;
					int finalAvg12 = 0;	
					int finalAvg13 = 0;								// compute the final average value
					int finalAvg14 = 0;
					int finalAvg15 = 0;
					
					for (int a = 0; a < avgLen; a += 1) {			// summing the last ten vals for the 8 sensors
						finalAvg0 += avg0[a];
						finalAvg1 += avg1[a];
						finalAvg2 += avg2[a];
						finalAvg3 += avg3[a];
						finalAvg12 += avg12[a];
						finalAvg13 += avg13[a];
						finalAvg14 += avg14[a];
						finalAvg15 += avg15[a];
					}
					
					finalAvg0 /= avgLen;
					finalAvg1 /= avgLen;							// dividing by quantity
					finalAvg2 /= avgLen;
					finalAvg3 /= avgLen;
					finalAvg12 /= avgLen;
					finalAvg13 /= avgLen;							// dividing by quantity
					finalAvg14 /= avgLen;
					finalAvg15 /= avgLen;
					
					int[] avgFinal0_3 = {finalAvg0, finalAvg1, finalAvg2, finalAvg3};
					int[] avgFinal12_15 = {finalAvg12, finalAvg13, finalAvg14, finalAvg15};
					
					int finalAvgAng1 = 0;
					int finalAvgAng2 = 0;
					int finalAvgAng3 = 0;
					
					avgAng1[avgLen-1] = returnAngleSphere2(avgFinal12_15)[0];		// angles
					avgAng2[avgLen-1] = returnAngleSphere1(avgFinal0_3)[0];	
					avgAng3[avgLen-1] = returnAngleSphere1(avgFinal0_3)[1];	

					
					for (int a = 0; a < avgLen; a += 1) {			// summing the last ten vals for angles
						finalAvgAng1 += avgAng1[a];
						finalAvgAng2 += avgAng2[a];
						finalAvgAng3 += avgAng3[a];
					}
		
					// printing the moving average sensor and angle values
					System.out.print("= " + Integer.toString(finalAvg0));
					System.out.print("= " + Integer.toString(finalAvg1));
					System.out.print("= " + Integer.toString(finalAvg2));
					System.out.print("= " + Integer.toString(finalAvg3));
					System.out.print("  = " + Integer.toString(finalAvg12));
					System.out.print("= " + Integer.toString(finalAvg13));
					System.out.print("= " + Integer.toString(finalAvg14));
					System.out.print("= " + Integer.toString(finalAvg15));

					finalAvgAng1 /= avgLen; 						// dividing by quantity
					finalAvgAng2 /= avgLen;
					finalAvgAng3 /= avgLen;

				;

					if (vals_0_3[0] < mins_0_3[0])				// finding mins
						mins_0_3[0] = vals_0_3[0];
					if (vals_0_3[1] < mins_0_3[1])
						mins_0_3[1] = vals_0_3[1];
					if (vals_0_3[2] < mins_0_3[2])
						mins_0_3[2] = vals_0_3[2];
					if (vals_0_3[3] < mins_0_3[3])
						mins_0_3[3] = vals_0_3[3];
					
					if (vals_0_3[0] > maxes_0_3[0])				// finding maxes
						maxes_0_3[0] = vals_0_3[0];
					if (vals_0_3[1] > maxes_0_3[1])
						maxes_0_3[1] = vals_0_3[1];
					if (vals_0_3[2] > maxes_0_3[2])
						maxes_0_3[2] = vals_0_3[2];
					if (vals_0_3[3] > maxes_0_3[3])
						maxes_0_3[3] = vals_0_3[3];
					
					if (vals_12_15[0] < mins_12_15[0])				// finding mins
						mins_12_15[0] = vals_12_15[0];
					if (vals_12_15[1] < mins_12_15[1])
						mins_12_15[1] = vals_12_15[1];
					if (vals_12_15[2] < mins_12_15[2])
						mins_12_15[2] = vals_12_15[2];
					if (vals_12_15[3] < mins_12_15[3])
						mins_12_15[3] = vals_12_15[3];
					
					if (vals_12_15[0] > maxes_12_15[0])				// finding maxes
						maxes_12_15[0] = vals_12_15[0];
					if (vals_12_15[1] > maxes_12_15[1])
						maxes_12_15[1] = vals_12_15[1];
					if (vals_12_15[2] > maxes_12_15[2])
						maxes_12_15[2] = vals_12_15[2];
					if (vals_12_15[3] > maxes_12_15[3])
						maxes_12_15[3] = vals_12_15[3];
					
					// printing mins for 0_3
					
					System.out.print("    min::" + Integer.toString(mins_0_3[0]));
					System.out.print(":" + Integer.toString(mins_0_3[1]));
					System.out.print(":" + Integer.toString(mins_0_3[2]));
					System.out.print(":" + Integer.toString(mins_0_3[3]));
					
					// printing maxes
					System.out.print("    //" + Integer.toString(maxes_0_3[0]));
					System.out.print("/" + Integer.toString(maxes_0_3[1]));
					System.out.print("/" + Integer.toString(maxes_0_3[2]));
					System.out.print("/" + Integer.toString(maxes_0_3[3]));
					
					// printing mins for 12_15
					System.out.print("    min::" + Integer.toString(mins_12_15[0]));
					System.out.print(":" + Integer.toString(mins_12_15[1]));
					System.out.print(":" + Integer.toString(mins_12_15[2]));
					System.out.print(":" + Integer.toString(mins_12_15[3]));
					
					// printing maxes
					System.out.print("    //" + Integer.toString(maxes_12_15[0]));
					System.out.print("/" + Integer.toString(maxes_12_15[1]));
					System.out.print("/" + Integer.toString(maxes_12_15[2]));
					System.out.print("/" + Integer.toString(maxes_12_15[3]));

					// print angle averages
					System.out.print(" angle1: " +  Integer.toString( finalAvgAng1));
					System.out.print(" angle2: " + Integer.toString( finalAvgAng2 ));
					
					System.out.println("  volt: " + Double.toString( curVoltage ));

					finalAvgAng1 = principleAng(finalAvgAng1);
					finalAvgAng2 = principleAng(finalAvgAng2);
					finalAvgAng3 = principleAng(finalAvgAng3);

					
					window.appendText("\n" + Integer.toString(finalAvgAng1) + printSpaces(10 - Integer.toString(finalAvgAng1).length())  );
					window.appendText(Integer.toString(finalAvgAng2) + printSpaces(13 - Integer.toString(finalAvgAng2).length())  );
					window.appendText(Integer.toString(finalAvgAng3) + printSpaces(11 - Integer.toString(finalAvgAng3).length())  );

					window.updateTitle(finalAvgAng1,finalAvgAng2,finalAvgAng3,curVoltage);
					window.appendText(Double.toString(curVoltage));

					window.angle1 = principleAng(finalAvgAng1);
					window.angle2 = principleAng(finalAvgAng2);
					window.angle3 = principleAng(finalAvgAng3);

					window.setVisible (true);
					window.repaint();

			        //os.write(Integer.toString(finalAvgAng1/100).getBytes()); //Integer.toString(finalAvgAng1%180).getBytes()); //just send '1' to the device
//finalAvgAng1 % 180a
			       
			        continue;
				}
				else if ((int) fullThing.charAt(curCharID) >= 48 && (int) fullThing.charAt(curCharID) <= 57) { 
					curString += fullThing.charAt(curCharID);
				}
			}
		}
	}
	public int[] returnAngleSphere1(int[] valueArray) {
		
		int angles[] = {0,0};
		
		int val0 = valueArray[0]*3/2;
		int val1 = valueArray[1];
		int val2 = valueArray[2];
		int val3 = valueArray[3];

		if (val0 == 0) val0 = 1;
		if (val1 == 0) val1 = 1;
		if (val2 == 0) val2 = 1;
		if (val3 == 0) val3 = 1;

		double x0_1 = 52.3/(1.0* val0/ val1) - 2.3;
		double x0_2 = 52.3/(1.0* val0/ val2) - 2.3;

		double x2_3 = 52.3/(1.0* val2/ val3) - 2.3;
		
		//angles[0] = (int) (110 * x0_1/100);
		
		if (val2 > val3) {
			angles[0] = (int) (110 * x0_1/100);
		}
		else {
			angles[0] = (int) (-250 * x0_1/100) + 360;
		}
		
		// yeah idek
		if (val1 > val3) {
			angles[1] = (int) (110 * x0_2/100);
		}
		else {
			angles[1] = (int) (-250 * x0_2/100) + 360;
		}
		
		return angles;
	}
	
	public int[] returnAngleSphere2(int[] valueArray) {
		
		int angles[] = {0,0};
		
		int val12 = valueArray[0];
		int val13 = valueArray[1];
		int val14 = valueArray[2]*1024/250;
		int val15 = valueArray[3];

		if (val12 == 0) val12 = 1;
		if (val13 == 0) val13 = 1;
		if (val14 == 0) val14 = 1;
		if (val15 == 0) val15 = 1;

		double x13_15 = 52.3/(1.0* val13/ val15) - 2.3;
		double x12_14 = 52.3/(1.0* val12/ val14) - 2.3;
		
		//System.out.print("  ++14: ");
		//System.out.print(val14);
		//angles[0] = (int) (112 * x13_15/100);

		
		if (x12_14 >= 77.9) {
			angles[0] = (int) (112 * x13_15/100);
		}
		else {
			angles[0] = (int) (-248 * x13_15/100) + 360;
		}
		
		
		//System.out.println("here: " + Integer.toString(angles[0]));
			
		return angles;
	}

	public int principleAng(int angle) {
		while (angle < 0)
			angle += 360;
		return angle % 360;
	}
	
	public String printSpaces(int spaces) {
		String returnStr = "";
		for (int x = 0 ; x < spaces; x += 1) {
			returnStr += " ";
		}
		return returnStr;
	}
}