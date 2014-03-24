package communication;

import robotcore.CommunicationType;
import robotcore.Configuration;
import lejos.nxt.comm.Bluetooth;
import lejos.nxt.comm.LCPResponder;
import lejos.nxt.comm.NXTCommConnector;
import lejos.nxt.comm.RS485;

/**
 * Runs on the remote NXT to respond to LCP requests
 *
 * @author Peter Henderson
 */
public class NXTLCPRespond
{
    /**
     * Our local Responder class so that we can over-ride the standard
     * behaviour. We modify the disconnect action so that the thread will
     * exit.
     */
    static class Responder extends LCPResponder
    {
        Responder(NXTCommConnector con)
        {
            super(con);
        }

        protected void disconnect()
        {
            super.disconnect();
            super.shutdown();
        }
    }

    /**
     * Main program to run on second brick
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception
    {
    	NXTCommConnector conn;
    	if(Configuration.INTERBRICK_COMM_METHOD == CommunicationType.RS485)
    		conn = RS485.getConnector();
    	else 
    		conn = Bluetooth.getConnector();

        Responder resp = new Responder(conn);
        
        resp.start();
        resp.join();
        
        //shouldn't exit until complete, might need to add fault tolerant behaviour in
        //case connection is dropped
    }
}
