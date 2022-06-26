package auto.taxi;

import jade.core.Agent;
import jade.core.AID;
import jade.lang.acl.ACLMessage;
import jade.proto.ContractNetInitiator;
import jade.domain.FIPANames;

import java.util.Date;
import java.util.Vector;
import java.util.Enumeration;

public class InitiatorAgent extends Agent {
	
private int nResponders;
	
	protected void setup() { 
  	// Read names of responders as arguments
  	String basename[]= {"Base1@umang:1111/JADE", "Base2@umang:1112/JADE", "Base3@umang:1113/JADE", "Base4@umang:1114/JADE", "Base5@umang:1115/JADE"};
  	String address[]={"http://UMANG:2221/acc", "http://UMANG:2222/acc", "http://UMANG:2223/acc", "http://UMANG:2224/acc", "http://UMANG:2225/acc"};
  	if (basename != null && basename.length > 0) {
  		nResponders = basename.length;
  		
  		// Fill the CFP message
  		ACLMessage msg = new ACLMessage(ACLMessage.CFP);
  		//sending messages to all 5 base station and select the best
  		for (int i = 0; i < basename.length; ++i) {
  			AID aidobj = new AID(basename[i], true);
  		
  			aidobj.addAddresses(address[i]);
  			msg.addReceiver(aidobj);
  		}
			msg.setProtocol(FIPANames.InteractionProtocol.FIPA_CONTRACT_NET);
			msg.setReplyByDate(new Date(System.currentTimeMillis() + 60000));//receive reply in 20 seconds
			msg.setContent("M20CS017_M20CS020 want to book a cab");
			
			addBehaviour(new ContractNetInitiator(this, msg) {
				
				protected void handlePropose(ACLMessage cost, Vector v) {
					System.out.println("Agent "+cost.getSender().getName()+" given Cost "+cost.getContent());
				}
				
				protected void handleRefuse(ACLMessage refuse) {
					System.out.println("Agent "+refuse.getSender().getName()+" refused, Need to Book Cab again");
				}
				
				protected void handleFailure(ACLMessage failure) {
					if (failure.getSender().equals(myAgent.getAMS())) {
						// FAILURE notification from the JADE runtime: the receiver
						// does not exist
						System.out.println("Responder does not exist");
					}
					else {
						System.out.println("Agent "+failure.getSender().getName()+" failed");
					}
					// Immediate failure --> we will not receive a response from this agent
					nResponders--;
				}
				
				protected void handleAllResponses(Vector responses, Vector acceptances) {
					if (responses.size() < nResponders) {
						// Some responder didn't reply within the specified timeout
						System.out.println("Timeout expired: missing "+(nResponders - responses.size())+" responses");
					}
					// Evaluate proposals.
					int bestProposal = -1;
					AID bestProposer = null;
					ACLMessage accept = null;
					Enumeration e = responses.elements();
					while (e.hasMoreElements()) {
						ACLMessage msg = (ACLMessage) e.nextElement();
						if (msg.getPerformative() == ACLMessage.PROPOSE) {
							ACLMessage reply = msg.createReply();
							reply.setPerformative(ACLMessage.REJECT_PROPOSAL);
							acceptances.addElement(reply);
							int proposal = Integer.parseInt(msg.getContent());
							if (proposal > bestProposal) {
								bestProposal = proposal;
								bestProposer = msg.getSender();
								accept = reply;
							}
						}
					}
					// Accept the proposal of the best proposer
					if (accept != null) {
						System.out.println("Accepting proposal "+bestProposal+" from responder "+bestProposer.getName());
						accept.setPerformative(ACLMessage.ACCEPT_PROPOSAL);
					}						
				}
				
				protected void handleInform(ACLMessage inform) {
					System.out.println("Agent "+inform.getSender().getName()+" successfully performed the requested action");
				}
			} );
  	}
  	else {
  		System.out.println("No responder specified.");
  	}
  } 

}
