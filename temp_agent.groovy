import org.arl.fjage.Message
//import org.arl.fjage.*
//import org.arl.unet.*
import org.arl.unet.phy.*
import org.arl.unet.localization.*
import org.arl.unet.PDU
import org.arl.fjage.*
import org.arl.unet.*
class temp_ag extends UnetAgent {
    def phy,router,n,txNtf

def timeout=10000
def myloc = PDU.withFormat {
int32('nodeid')
chars('x', 10)
chars('y', 10)
chars('z', 10)
//chars("hash",10)
}
def pdu = PDU.withFormat {
int32('nodeid')
chars('x', 10)
chars('y', 10)
chars('z', 10)
chars("hash",10)
}
def bytes = pdu.encode([nodeid:4,x: 400,y:0,z:-15,hash:1102321])
  void startup() {
    // TODO
    
    	phy = agentForService Services.PHYSICAL;
    	
		subscribe topic(phy);
		router = agentForService Services.ROUTING;
  n = agentForService org.arl.unet.Services.NODE_INFO
  /*def rxNtf = receive({it instanceof RxFrameNtf},timeout)
    def recdata = myloc.decode(rxNtf.data)
    System.out.println(recdata.x +' '+ recdata.y + ' '+recdata.z);
    
   // add new WakerBehavior(70, {
       phy << new DatagramReq(
         to: Address.BROADCAST,
         protocol: Protocol.MAC,
         data: bytes
       )*/
   //    System.out.println("ADDEDD")
 //    });
   // phy << new DatagramReq(to: Address.BROADCAST, protocol: Protocol.MAC, data:bytes)
 //System.out.println(n.location)
 
//def bytes = pdu.encode([hash:29])
  // phy << new DatagramReq(to: Address.BROADCAST, protocol: Protocol.MAC, data: [x:2,y:6])
   
   // txNtf = receive(TxFrameNtf, 1000)
    //System.out.println(txNtf)
   

  }

  void processMessage(Message msg) {
    // TODO
    
if (msg instanceof RxFrameNtf && msg.protocol == Protocol.MAC) {
      // respond to protocol USER datagram with protocol DATA datagram
   /  phy = agentForService Services.PHYSICAL;
		subscribe topic(phy);
		def rxNtf = receive({it instanceof RxFrameNtf},timeout)
    def recdata = myloc.decode(rxNtf.data)
    System.out.println(recdata.x +' '+ recdata.y + ' '+recdata.z);
    
   // add new WakerBehavior(70, {
       phy << new DatagramReq(
         to: Address.BROADCAST,
         protocol: Protocol.MAC,
         data: bytes
       )
      phy << new DatagramReq(to: Address.BROADCAST, protocol: Protocol.MAC, data: [x:2,y:6])
    //  txNtf = receive(TxFrameNtf, 1000)
    //System.out.println(txNtf)
}

}
}