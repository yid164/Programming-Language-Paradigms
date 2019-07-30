package Problems;

// Name: Yinsheng Dong
// Student Number: 11148648
// NSID: yid164
// Lecture Section: CMPT 350

import java.io.Serializable;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Inbox;
import akka.actor.Props;
import akka.actor.UntypedActor;
import akka.pattern.Patterns;
import akka.util.Timeout;
import scala.concurrent.Await;
import scala.concurrent.Future;
import scala.concurrent.duration.Duration;


public class Problem1 {
	// the scanner in for input testing, so you can input a position, and get the number
	private static Scanner in;

	/**
	 * the actor for fib number
	 */
	public static class FibonacciActor extends UntypedActor{
		/**
		 * to get the position of fibonacci number by using 2 child actors
		 */
		public ReceivedMessage fib(int fibNum, long maxTime)
		{
			// if 0
			if(fibNum == 0)
			{
				return new ReceivedMessage(0);
			}
			// if fibNum in 1 or 2
			else if (fibNum <=2)
			{
				return new ReceivedMessage(1);
			}
			// if fibNum in 3 or more
			else
			{
				// 2 child actors
				final ActorRef child1 = getContext().actorOf(Props.create(FibonacciActor.class), (fibNum+"child1"));
				final ActorRef child2 = getContext().actorOf(Props.create(FibonacciActor.class), (fibNum+"child2"));
				Timeout t = new Timeout(Duration.create(maxTime, TimeUnit.SECONDS));
				//async calls
				Future<Object> futureChild1 = Patterns.ask(child1, new RequestMessage(fibNum-1, maxTime),t);
				Future<Object> futureChild2 = Patterns.ask(child2, new RequestMessage(fibNum-2, maxTime),t);
				ReceivedMessage m1 = null;
				ReceivedMessage m2 = null;
				
				try{
					m1 = (ReceivedMessage)Await.result(futureChild1, t.duration());
					m2 = (ReceivedMessage)Await.result(futureChild2, t.duration());
				}catch(Exception e)
				{
					System.out.println("Timed out or received a Failure message");
				}
				return new ReceivedMessage(m1.resultNum + m2.resultNum);
			}
		}
		
		
		public void onReceive(Object message) throws Exception
		{
			// if the message get is from Request
			if(message instanceof RequestMessage)
			{
				int fibNums = ((RequestMessage)message).fibNums;
				long maxTime = ((RequestMessage)message).maxTime;
				// do calculation
				ReceivedMessage resultNum = fib(fibNums, maxTime);
				getSender().tell(resultNum, getSelf());
				
			}
			// unknown action
			else
			{
				unhandled(message);
			}
		}
	}
	
	/**
	 * The request message for fib number, to find where the fib number is
	 */
	public static class RequestMessage implements Serializable{
		private static final long serialVersionUID = 1234L;
		public int fibNums;
		public long maxTime;
		
		public RequestMessage(int fibNums, long maxTime)
		{
			this.fibNums = fibNums;
			this.maxTime = maxTime;
		}
	}
	
	/**
	 * The received message for fib number, so can get what the number is
	 */
	public static class ReceivedMessage implements Serializable{
		private static final long serialVersionUID = 4321L;
		public long resultNum;
		
		public ReceivedMessage(long resultNum)
		{
			this.resultNum = resultNum;
		}
	}
	/**
	 * the recursive function that fibonacci number that assignment requires
	 */
	public static long recursive(int fibNum)
	{
		if(fibNum == 0)
		{
			return 0;
		}
		else if(fibNum<=2)
		{
			return 1;
		}
		else
		{
			return recursive(fibNum-1)+recursive(fibNum-2);
		}
	}
	
	public static void main(String[] args)
	{
		int maxTime = 60;
		int fibNum;
		System.out.println("Welcome to test Fib-System");
		System.out.println("Please enter the positon of the fib number: ");
		in = new Scanner(System.in);
		fibNum = in.nextInt();
		
		final ActorSystem fibSystem = ActorSystem.create("Fibonacci");
		ActorRef fibActor = fibSystem.actorOf(Props.create(FibonacciActor.class),"fibActor");
		final Inbox client = Inbox.create(fibSystem);
		client.send(fibActor, new RequestMessage(fibNum, maxTime));
		ReceivedMessage reply = null;
		try{
			reply = (ReceivedMessage) client.receive(Duration.create(maxTime, TimeUnit.SECONDS));
		}
		catch(TimeoutException e)
		{
			System.out.println("Time out waiting for reply");
		}
		
		System.out.println("The Result of test by concurrent system: ");
		System.out.println("The position of the fibonacci number: "+fibNum);
		System.out.println("The fiboacci number in this position: " + reply.resultNum);
		fibSystem.terminate();
		
		long rec = recursive(fibNum);
		if(reply.resultNum==rec)
		{
			System.out.println("The number is tested, and the test succeed ");
		}
		else
		{
			System.out.println("The number is wrong");
		}
		
		
		
		
	}

}
