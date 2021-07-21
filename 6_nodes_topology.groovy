import org.arl.fjage.*
import org.arl.unet.*
import org.arl.unet.sim.channels.*
import org.arl.unet.net.Router
import org.arl.unet.net.RouteDiscoveryNtf
///////////////////////////////////////////////////////////////////////////////

println '''
2-node network
--------------
Node A:           tcp://localhost:1106, http://localhost:8086/
Node B(Source):   tcp://localhost:1102, http://localhost:8082/
Node C:           tcp://localhost:1107, http://localhost:8087/
Node D:           tcp://localhost:1105, http://localhost:8085/
Node E(Attacker): tcp://localhost:1108, http://localhost:8088/
Node F(Sink):     tcp://localhost:1109, http://localhost:8089/
'''

///////////////////////////////////////////////////////////////////////////////
// simulator configuration

platform = RealTimePlatform   // use real-time mode

// run the simulation forever
simulate {

  node 'B', address:2,location: [ 1.km, 0.km, -15.m], web: 8082, api: 1102, stack: {container-> 
 container.add 'router', new org.arl.unet.net.Router() 
  container.add 'uwlink',  new org.arl.unet.link.ReliableLink()
container.add 'tec1',new temp1_ag()
}

 node 'A',address:1, location: [ 1500.m, 0.km, -15.m], web: 8086, api: 1106,stack: {container-> 
  container.add 'router', new org.arl.unet.net.Router() 
  container.add 'uwlink',  new org.arl.unet.link.ReliableLink()
       container.add 'teca',new tempa_ag()
  
}
node 'C',address:3, location: [ 500.m, 500.m, -15.m], web: 8087, api: 1107,stack: {container-> 
  container.add 'router', new org.arl.unet.net.Router() 
  container.add 'uwlink',  new org.arl.unet.link.ReliableLink()
       container.add 'teca',new tempa_ag()
  
}
  node 'D',address:4, location: [ 400.m, 0.km, -15.m], web: 8085, api: 1105, stack: {container-> 
  container.add 'router', new org.arl.unet.net.Router() 
  container.add 'uwlink',  new org.arl.unet.link.ReliableLink()
  container.add 'teca',new tempa_ag()
}
  node 'E',address:5, location: [ 1.km, 20.m, -15.m], web: 8088, api: 1108, stack: {container-> 
  container.add 'router', new org.arl.unet.net.Router() 
  container.add 'uwlink',  new org.arl.unet.link.ReliableLink()
  container.add 'teca',new tempa_ag()
  }
    node 'F',address:6, location: [ 1.5.km, 20.m, -15.m], web: 8089, api: 1109, stack: {container-> 
  container.add 'router', new org.arl.unet.net.Router() 
  container.add 'uwlink',  new org.arl.unet.link.ReliableLink()
}
}