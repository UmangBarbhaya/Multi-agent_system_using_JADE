package auto.taxi;

import java.util.Random;

public class Main {

	public static void main(String[] args) {
		// TODO Auto-generated method stub	
		int maxRequest=100; //selecting the number of request of cab booking
		Random rand = new Random();
		for(int i=0;i<maxRequest;i++)
		{
			int nodeNo = rand.nextInt(10); //selecting the random value of the node on which agent resides
			String[] args0 = {"-gui", "-port", "1111", "-container", "Node"+nodeNo+""+i+":auto.taxi.InitiatorAgent"};
			//System.out.println("Node"+nodeNo+""+i+":auto.taxi.InitiatorAgent");
			jade.Boot.main(args0);
		}

		
	}

}
