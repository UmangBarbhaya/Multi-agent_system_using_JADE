package auto.taxi;

import jade.core.Agent;
import java.io.*;

import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.proto.ContractNetResponder;
import jade.domain.FIPANames;
import jade.domain.FIPAAgentManagement.NotUnderstoodException;
import jade.domain.FIPAAgentManagement.RefuseException;
import jade.domain.FIPAAgentManagement.FailureException;

public class BaseAgent4 extends Agent{
	int path[][]= {
			{1,3,6,5,7},
			{7,5,2,3,1},
			{1,1,4,3,5},
			{7,5,2,3,1},
			{6,4,1,2,2},
			{3,1,4,3,5},
			{5,3,2,1,3},
			{3,1,2,1,3},
			{1,3,6,5,7},
			{4,2,3,2,4}
			};
	int myindex=3;
	int taxi[]=new int[5];
	protected void setup() {
		System.out.println("Agent "+getLocalName()+" waiting for CFP...");
		MessageTemplate template = MessageTemplate.and(
				MessageTemplate.MatchProtocol(FIPANames.InteractionProtocol.FIPA_CONTRACT_NET),
				MessageTemplate.MatchPerformative(ACLMessage.CFP) );

		addBehaviour(new ContractNetResponder(this, template) {
			@Override
			protected ACLMessage handleCfp(ACLMessage cfp) throws NotUnderstoodException, RefuseException {
				int nodeNo=Integer.parseInt(cfp.getSender().getName().substring(4, 5));
				System.out.println("Agent "+getLocalName()+": CFP received from "+cfp.getSender().getName()+". Action is "+cfp.getContent());
				int proposal = findAllocation(nodeNo);
				if (proposal >=0) {
					// We provide a proposal
					removeTaxi();
					System.out.println("Agent "+getLocalName()+": Cab Fare "+proposal);
					ACLMessage propose = cfp.createReply();
					//Proposing Stage
					propose.setPerformative(ACLMessage.PROPOSE);
					propose.setContent(String.valueOf(proposal));
					return propose;
				}
				else {
					// We refuse to provide a proposal
					System.out.println("Agent "+getLocalName()+": Refuse, Cab availability not there from this base");
					throw new RefuseException("evaluation-failed");
				}
			}

			@Override
			protected ACLMessage handleAcceptProposal(ACLMessage cfp, ACLMessage propose,ACLMessage accept) throws FailureException {
				System.out.println("Agent "+getLocalName()+": Cab booked");
				System.out.println("Agent "+getLocalName()+": Waiting for cab to get free");
				//Making to sleep for 5 seconds to make sure cab reaches destination
				int nodeNo=Integer.parseInt(cfp.getSender().getName().substring(4, 5));
				try {
					Thread.sleep(5000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				System.out.println("Agent "+getLocalName()+": Tourist reached the destination");
				//Finding the nearest Base and sending the cab there
				int x=-1;
				if(path[nodeNo][0]<=path[nodeNo][1] && path[nodeNo][0]<=path[nodeNo][2] &&
						path[nodeNo][0]<=path[nodeNo][3] &&path[nodeNo][0]<=path[nodeNo][4])
				{
					addTaxi(0);
				}
				else if(path[nodeNo][1]<=path[nodeNo][2] && path[nodeNo][1]<=path[nodeNo][3] &&
						path[nodeNo][1]<=path[nodeNo][4])
				{
					addTaxi(1);
				}
				else if(path[nodeNo][2]<=path[nodeNo][3] && path[nodeNo][2]<=path[nodeNo][4])
				{
					addTaxi(2);
				}
				else if(path[nodeNo][3]<=path[nodeNo][4])
				{
					addTaxi(3);
				}
				else
				{
					addTaxi(4);
				}
				
				System.out.println("Agent "+getLocalName()+": Cab reached nearest Base");
				//Informing stage
				ACLMessage inform = accept.createReply();
				inform.setPerformative(ACLMessage.INFORM);
				return inform;	
			}

			protected void handleRejectProposal(ACLMessage cfp, ACLMessage propose, ACLMessage reject) {
				System.out.println("Agent "+getLocalName()+": Proposal rejected, Cannot provide the cab");
				addTaxi(myindex);
			}
		} );
	}

	private int findAllocation(int nodeNo) {
		File file = new File("C:\\Users\\H P\\Desktop\\JADE_Assignment\\jade\\src\\auto\\taxi\\taxi.txt");
		BufferedReader br;
		try {
			br = new BufferedReader(new FileReader(file));
			String st="";
			int co=0;
	        while ((st = br.readLine()) != null) 
	        {
	        	taxi[co]=Integer.parseInt(st); //Getting the number of Taxis
	            co++;
	        }
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		int mycost=0;
		if(taxi[myindex]>0) //Checking if the Taxis are available
		{
			if(taxi[myindex]==1) //If last Taxi add 1 to the cost
			{
				mycost=path[nodeNo][myindex]+1;
			}
			else
			{
				mycost=path[nodeNo][myindex];
			}
			if(mycost<=path[nodeNo][0]
				&& mycost<=path[nodeNo][1]	
				&& mycost<=path[nodeNo][2]
				&& mycost<=path[nodeNo][4]) //Checking if lowest cost, if yes return cost, else reject cost
			{
				return mycost; //all condition satisfied return the cost
			}
		}
		return -1;
		
	}

	private void addTaxi(int baseNo)
	{
		System.out.println("Adding Taxi");
		File file = new File("C:\\Users\\H P\\Desktop\\JADE_Assignment\\jade\\src\\auto\\taxi\\taxi.txt");
		BufferedReader br;
		try {
			br = new BufferedReader(new FileReader(file));
			String st="";
			String Finalstr="";
			int count=0;
	        while ((st = br.readLine()) != null) 
	        {
	        	if(count==baseNo)
	        	{
	        		Finalstr+=(Integer.parseInt(st)+1)+"\n";
	        	}
	        	else
	        	{
	        	Finalstr+=st+"\n";
	        	}
	        	count++;
	        }
	        System.out.println(Finalstr);
	        FileWriter fWriter = new FileWriter("C:\\Users\\H P\\Desktop\\JADE_Assignment\\jade\\src\\auto\\taxi\\taxi.txt",false);
	        fWriter.write(Finalstr);
	 
	        fWriter.close();
	     
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	private void removeTaxi()
	{
		System.out.println("Removing Taxi");
		File file = new File("C:\\Users\\H P\\Desktop\\JADE_Assignment\\jade\\src\\auto\\taxi\\taxi.txt");
		BufferedReader br;
		try {
			br = new BufferedReader(new FileReader(file));
			String st="";
			String Finalstr="";
			int count=0;
	        while ((st = br.readLine()) != null) 
	        {
	        	if(count==myindex)
	        	{
	        		Finalstr+=(Integer.parseInt(st)-1)+"\n";
	        	}
	        	else
	        	{
	        		Finalstr+=st+"\n";
	        	}
	        	count++;
	        }
	        System.out.println(Finalstr);
	        FileWriter fWriter = new FileWriter("C:\\Users\\H P\\Desktop\\JADE_Assignment\\jade\\src\\auto\\taxi\\taxi.txt",false);
	        fWriter.write(Finalstr);
	 
	        fWriter.close();

	     
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}