package Problems;
// Name: Yinsheng Dong
// Student Number: 11148648
// NSID: yid164
// Lecture Section: CMPT 340
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Inbox;
import akka.actor.Props;
import akka.actor.UntypedActor;
import scala.concurrent.duration.Duration;

public class Problem2 {
	/**
	 * The abstract class actor for person, 
	 * This is the super class for menActor and womenActor
	 */
	public static abstract class PersonActor extends UntypedActor{
		// the unRankedList for men or women
		ArrayList<ActorRef> unRankedList;
		// the ranked list for men or women
		ArrayList<ActorRef> rankedList;
		// the matching person for current actor
		ActorRef matching;
		
		/*
		 * create the the random list of possible spouses 
		 */
		public void onCreate(ActorRef[] spouses)
		{
			this.unRankedList = new ArrayList<ActorRef>();
			this.rankedList = new ArrayList<ActorRef>();
			for(int i = 0; i< spouses.length; i++)
			{
				rankedList.add(spouses[i]);
			}
			Collections.shuffle(rankedList);
			unRankedList.addAll(rankedList);
			this.matching = null;
		}
		
	}
	
	/**
	 * The searching message that is sent to Actors,
	 *  so it would create a new ranked list for each actor
	 */
	public static class CreateMessage implements Serializable{
		private static final long serialVersionUID = 0L;
		ActorRef[] spouses;
		public CreateMessage(ActorRef[] spouses)
		{
			this.spouses = spouses;
		}
	}
	/**
	 * The ready message that is sent by a man or a women after createMessage is called
	 * Tell the system they are ready
	 */
	
	public static class ReadyMessage implements Serializable{
		private static final long serialVersionUID = 1L;
	}
	
	/**
	 * ManActor send the proposal to WomanActor
	 * so woman could decide accept or not
	 */
	public static class ProposalMessage implements Serializable{
		private static final long serialVersionUID = 2L;
	}
	
	/**
	 * the manActor will receive it, so they could go start
	 */
	public static class GoStartMessage implements Serializable{
		private static final long serialVersionUID= 3L;
	}
	
	/**
	 * the accept message from womanActor to manActor after they get the proposal
	 */
	public static class AcceptMessage implements Serializable{
		private static final long serialVersionUID = 4L;
	}
	
	/**
	 * the accept message from womanActor to manActor after they get the proposal
	 */
	public static class RejectMessage implements Serializable{
		private static final long serialVersionUID = 5L;
	}
	
	/**
	 * the system massage to men, tell them if they are matched the partners or not
	 */
	public static class MatchedOrNotMessage implements Serializable{
		private static final long serialVersionUID = 6L;
	}
	
	/**
	 * the message that shows if the person has been matched, who is the person
	 */
	public static class MatchedPersonMessage implements Serializable{
		private static final long serialVersionUID = 7L;
	}
	
	/**
	 * the message that shows if the person has been matched, what are his/her purpose partners
	 */
	public static class MatchedAndPreferedMessage implements Serializable{
		private static final long serialVersionUID = 8L;
		ActorRef matched;
		ArrayList<ActorRef> prefered;
		public MatchedAndPreferedMessage(ActorRef matched, ArrayList<ActorRef> prefered)
		{
			this.matched = matched;
			this.prefered = prefered;
		}
	}
	
	/**
	 * The man actor, which send the proposal to woman, and receive the women's acception or rejection message
	 */
	public static class ManActor extends PersonActor{
		
		// men send the proposal to women
		public void sendProposal()
		{
			if(!this.rankedList.isEmpty())
			{
				// the prefer women is in the 1st position of the list
				ActorRef prefer = rankedList.get(0);
				// send the proposal message 
				prefer.tell(new ProposalMessage(), getSelf());
				rankedList.remove(0);
			}
		}
		
		public void onReceive(Object message)
		{
			// if message is creating message, create the new list
			if(message instanceof CreateMessage)
			{
				this.onCreate((ActorRef[])((CreateMessage)message).spouses);
				getSender().tell(new ReadyMessage(), getSelf());
			}
			// if the message is go start, send the proposal
			else if(message instanceof GoStartMessage)
			{
				sendProposal();
			}
			// if the message is accept, matching get sender
			else if(message instanceof AcceptMessage)
			{
				this.matching = getSender();
			}
			// if the message is rejected, re-send the proposal
			else if(message instanceof RejectMessage)
			{
				sendProposal();
			}
			// get message if the man has been matched or not
			else if(message instanceof MatchedOrNotMessage)
			{
				Boolean r = new Boolean(this.matching!=null);
				getSender().tell(r, getSelf());
			}
			// get message who matched
			else if(message instanceof MatchedPersonMessage)
			{
				getSender().tell(new MatchedAndPreferedMessage(this.matching, this.unRankedList), getSelf());
			}
			else
			{
				unhandled(message);
			}
				
		}
	}
	
	/**
	 * the women actor, which makes the decisions (accept or reject) to a man
	 */
	public static class WomanActor extends PersonActor{
		// the accept action for a man
		public void accept(ActorRef man)
		{
			this.matching = man;
			man.tell(new AcceptMessage(), getSelf());
		}
		
		// the reject action for a man
		public void reject(ActorRef man)
		{
			man.tell(new RejectMessage(), getSelf());
		}
		// if the list in over load
		public boolean overLoad(ActorRef man)
		{
			boolean overload = false;
			if(rankedList.indexOf(matching)<rankedList.indexOf(man))
			{
				overload = true;
			}
			return overload;
		}
		
		public void onReceive(Object message)
		{
			// message get to create a new list for women
			if(message instanceof CreateMessage)
			{
				this.onCreate((ActorRef[])((CreateMessage)message).spouses);
				getSender().tell(new ReadyMessage(), getSelf());
			}
			// get the proposal message
			else if(message instanceof ProposalMessage)
			{
				ActorRef man = getSender();
				// if the there are not matching current, accept it
				if(matching==null)
				{
					accept(man);
				}
				else
				{
					// if the list is exceed the size, reject
					if(overLoad(man))
					{
						reject(man);
					}
					// otherwise, reject the current matching, and accept the man
					else
					{
						reject(matching);
						accept(man);
					}
				}
			}
			// to get current match and list
			else if(message instanceof MatchedPersonMessage)
			{
				getSender().tell(new MatchedAndPreferedMessage(this.matching,this.unRankedList), getSelf());
			}
			else
			{
				unhandled(message);
			}
		}
	}

	/**
	 * The help testing class
	 */
	public static class Marriage{
		ActorRef woman;
		ActorRef man;
		ArrayList<ActorRef> women;
		ArrayList<ActorRef> men;
		
		
		// readable list 
		public String toStringByList(String name, ArrayList<ActorRef> list)
		{
			String matched = name + "  Ranked List: ";
			for(int i =0; i< list.size(); i++)
			{
				matched+= list.get(i).path().name()+"   ";
			}
			return matched;
		}
		
		public String toString()
		{
			String woman = this.woman.path().name();
			String man = this.man.path().name();
			String toString = "("+man+","+ woman+")";
			toString += "\n" + toStringByList(man, this.men) + "\n" + toStringByList(woman, this.women) + "\n";
			return toString;
		}
		public Marriage(ActorRef man, ActorRef woman, ArrayList<ActorRef> men, ArrayList<ActorRef>women)
		{
			this.woman = woman;
			this.man = man;
			this.women = women;
			this.men = men;
		}
		
		public boolean equals(Object other)
		{
			if(other == null && !(other instanceof Marriage))
			{
				return false;
			}
			if(other == this)
			{
				return true;
			}
			Marriage x = (Marriage) other;
			return (this.man.path().name() == x.man.path().name())&&(this.woman.path().name() == x.woman.path().name());
		}
		
	}
	
	
	public static void main(String[] args)
	{
		// the default value for system
		int couples = 10;
		int timeout = 3;
		
		// arrays for storing actors
		ActorRef[] men = new ActorRef[couples];
		ActorRef[] women = new ActorRef[couples];
		
		// testing array
		ArrayList<Marriage> menMatched = new ArrayList<Marriage>();
		ArrayList<Marriage> womenMatched = new ArrayList<Marriage>();
		
		// new actor system
		final ActorSystem marriageSystem = ActorSystem.create("MarriageSystem");
		
		// actor in box
		final Inbox client = Inbox.create(marriageSystem);
		
		// using for-loops create all of the actors
		for(int i = 0; i < couples; i++)
		{
			men[i] = marriageSystem.actorOf(Props.create(ManActor.class),"man"+i);
			women[i] = marriageSystem.actorOf(Props.create(WomanActor.class),"woman"+i);
		}
		
		// createMessage() for men
		for(int i = 0; i< men.length; i++)
		{
			client.send(men[i], new CreateMessage(women.clone()));
			try{
				client.receive(Duration.create(timeout, TimeUnit.SECONDS));
			}
			catch(TimeoutException e)
			{
				System.out.println("Timeout waiting for reply form man: "+ men[i].path().name());
			}
		}
		
		// createMessage() for women
		for(int i = 0; i< women.length; i++)
		{
			client.send(women[i], new CreateMessage(men.clone()));
			try{
				client.receive(Duration.create(timeout, TimeUnit.SECONDS));
			}
			catch(TimeoutException e)
			{
				System.out.println("Timeout waiting for reply form woman: "+ women[i].path().name());
			}
		}
		
		// men start to sent proposal message
		for(int i = 0; i<couples; i++)
		{
			men[i].tell(new GoStartMessage(), ActorRef.noSender());
		}
		// fill the men's list
		for(int i = 0; i<men.length; i++)
		{
			client.send(men[i], new MatchedPersonMessage());
			MatchedAndPreferedMessage reply = null;
			try
			{
				reply = (MatchedAndPreferedMessage) client.receive(Duration.create(timeout, TimeUnit.SECONDS));
				Marriage m = new Marriage(men[i], reply.matched, reply.prefered, null);
				menMatched.add(m);
			}
			catch(TimeoutException e)
			{
				System.out.println("Timeout waiting for reply from man: " + men[i]);
			}
		}
		// fill the women's list
		for(int i = 0; i<women.length; i++)
		{
			client.send(women[i], new MatchedPersonMessage());
			MatchedAndPreferedMessage reply = null;
			try
			{
				reply = (MatchedAndPreferedMessage) client.receive(Duration.create(timeout, TimeUnit.SECONDS));
				Marriage m = new Marriage(reply.matched, women[i],null, reply.prefered);
				womenMatched.add(m);
			}
			catch(TimeoutException e)
			{
				System.out.println("Timeout waiting for reply from woman: " + women[i]);
			}
		}
		
		// print out every paired and every ranked list
		System.out.println("TESTING FOR PAIR MEN AND WOMEN");
		ArrayList<Marriage> testPair = new ArrayList<Marriage>();
		for(int i = 0; i < menMatched.size(); i++)
		{
			Marriage current = menMatched.get(i);
			int wi = womenMatched.indexOf(current);
			Marriage m1 = new Marriage(current.man, current.woman, current.men, womenMatched.get(wi).women);
			testPair.add(m1);
			System.out.println(testPair.get(i));
		}
		
		// system stop
		marriageSystem.terminate();
		
		
	}
	
	
	
	

	

}
