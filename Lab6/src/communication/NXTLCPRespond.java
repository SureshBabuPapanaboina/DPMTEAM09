package communication;

import lejos.nxt.comm.LCPResponder;
import lejos.nxt.comm.NXTCommConnector;
import lejos.nxt.comm.RS485;

/**
 * Runs on the remote NXT to respond to LCP requests
 *
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

    public static void main(String[] args) throws Exception
    {
        NXTCommConnector conn = RS485.getConnector();

        Responder resp = new Responder(conn);
        resp.start();
        resp.join();
    }
}
