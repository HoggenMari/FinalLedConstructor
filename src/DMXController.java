import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.UnknownHostException;
import java.util.ArrayList;


public class DMXController extends Thread {

	private String IP_ADRESS;
	private int PORT;
	private int ID;
	private MulticastSocket socket;
	private boolean running;
	private long wait;
	private ArrayList<LEDScreen> screenList;
	private InetAddress dest;


	public DMXController(String ip, int port, int id) {
		IP_ADRESS = ip;
		PORT = port;
		ID = id;
		
		
		screenList = new ArrayList<LEDScreen>();
		
		try {
			socket = new MulticastSocket();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void add(LEDScreen... screens){
		for(LEDScreen screen : screens){
			screenList.add(screen);
		}
	}
	
	public void start () {
		if(running){
			super.stop();
		}
	    running = true;
	    super.start();
	}
	
	public void run () {
	    while (true) {
	      send();
	      try {
	        sleep((long)(wait));
	      } catch (Exception e) {
	      }
	    }
	  }
	
	public void send() {
		
		System.out.println("SEND");
		byte[] data = new byte[1472];
		
		int screenIndex=0;
		
		//for(int ctr=0; ctr<2; ctr++) {
		
		for(int i=0; i<1; i++){
			
		int dataIndex = 0;

		data[dataIndex++] = 'Y';
		data[dataIndex++] = 'T';
		data[dataIndex++] = 'K';
		data[dataIndex++] = 'J';
		
		data[dataIndex++] = (byte) ID;
		data[dataIndex++] = 0;
		data[dataIndex++] = 0x57;
		data[dataIndex++] = 0x05;
		
		data[dataIndex++] = (byte) 1;
		data[dataIndex++] = 0;
		
		int channel = i*2048;
		
		data[dataIndex++] = (byte) (channel & 0xff);
		data[dataIndex++] = (byte) ((channel >> 8) & 0xff);

		data[dataIndex++] = (byte) ((1458) & 0xff);
		data[dataIndex++] = (byte) (((1458) >> 8) & 0xff);
		
		//System.out.println("PORT: "+PORT_MAP[nozzle_count]);
		
		do{
			byte[] buffer = screenList.get(screenIndex).data;
			
			System.out.println("BufferLength: "+buffer.length);
			//Now write the RGB-values
			for(int j=0; j<buffer.length; j++) {
				data[dataIndex++] = buffer[j];
			}
			
			
			screenIndex++;
			//System.out.println(nozzle_count);
		}while(screenIndex<screenList.size());
				
		for(int j=dataIndex; j<1458; j++){
			data[dataIndex++]= (byte) 0;
		}
				
		DatagramPacket dp = new DatagramPacket(data, data.length);
        dp.setPort(PORT);
        try {
			dest = InetAddress.getByName(IP_ADRESS);
		} catch (UnknownHostException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
        dp.setAddress(dest);
        
        //System.out.println("SEND-TO-ADRESS: "+dp.getAddress());
        //System.out.println("SEND-TO-PORT: "+dp.getPort());
        
        //p.delay(50);
        
        //for(int k=0; k<data.length; k++){
        	//System.out.println("DATA: "+i+" "+k+" "+data[k]);
		//}

        try {
          socket.send(dp);
        } catch (IOException e) {
        	
        }
		
		}
		
		}
		
}
