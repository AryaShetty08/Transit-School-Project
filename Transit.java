package transit;

import java.util.ArrayList;

/**
 * This class contains methods which perform various operations on a layered linked
 * list to simulate transit
 * 
 * @author Ishaan Ivaturi
 * @author Prince Rawal
 */
public class Transit {
	private TNode trainZero; // a reference to the zero node in the train layer

	/* 
	 * Default constructor used by the driver and Autolab. 
	 * DO NOT use in your code.
	 * DO NOT remove from this file
	 */ 
	public Transit() { trainZero = null; }

	/* 
	 * Default constructor used by the driver and Autolab. 
	 * DO NOT use in your code.
	 * DO NOT remove from this file
	 */
	public Transit(TNode tz) { trainZero = tz; }
	
	/*
	 * Getter method for trainZero
	 *
	 * DO NOT remove from this file.
	 */
	public TNode getTrainZero () {
		return trainZero;
	}

	/**
	 * Makes a layered linked list representing the given arrays of train stations, bus
	 * stops, and walking locations. Each layer begins with a location of 0, even though
	 * the arrays don't contain the value 0. Store the zero node in the train layer in
	 * the instance variable trainZero.
	 * 
	 * @param trainStations Int array listing all the train stations
	 * @param busStops Int array listing all the bus stops
	 * @param locations Int array listing all the walking locations (always increments by 1)
	 */
	public void makeList(int[] trainStations, int[] busStops, int[] locations) {
		TNode t = null;
		TNode b = null; 
		TNode w = null;
	    // UPDATE THIS METHOD
		for(int i = locations.length-1; i >= 0; i--) {
			w = new TNode(locations[i], w, null);
			for(int j = busStops.length-1; j >= 0; j--) {
				if(busStops[j] == locations[i]) {
				b = new TNode(locations[i], b, w);
				}
			}
			for(int k = trainStations.length-1; k >= 0; k--) {
				if(trainStations[k] == locations[i]) {
				t = new TNode(locations[i], t, b);
				}
			}
		}
		w = new TNode(0, w, null);
		b = new TNode(0, b, w);
		t = new TNode(0, t, b);
		trainZero = t;
	}
	
	/**
	 * Modifies the layered list to remove the given train station but NOT its associated
	 * bus stop or walking location. Do nothing if the train station doesn't exist
	 * 
	 * @param station The location of the train station to remove
	 */
	public void removeTrainStation(int station) {
	    // UPDATE THIS METHOD
		TNode current = trainZero;
		TNode prev = null;
		while(current != null && current.getLocation() != station) {
			prev = current; 
			current = current.getNext();
		}
		if(current != null) {
			prev.setNext(current.getNext());
		}
	}

	/**
	 * Modifies the layered list to add a new bus stop at the specified location. Do nothing
	 * if there is no corresponding walking location.
	 * 
	 * @param busStop The location of the bus stop to add
	 */
	public void addBusStop(int busStop) {
	    // UPDATE THIS METHOD
		TNode current = trainZero.getDown();
		TNode prev = null;
		while(current != null) {
			if(current.getLocation() == busStop) {
				return;
			}
			else if(current.getLocation() > busStop) {
				TNode n = new TNode(busStop, current, null);
				prev.setNext(n);
				current = prev.getDown();
				while(current.getLocation() != busStop) {
					current = current.getNext();
				}
				n.setDown(current);
				return;
			}
			prev = current;
			current = current.getNext();
		} 
		
	}
	
	/**
	 * Determines the optimal path to get to a given destination in the walking layer, and 
	 * collects all the nodes which are visited in this path into an arraylist. 
	 * 
	 * @param destination An int representing the destination
	 * @return
	 */
	public ArrayList<TNode> bestPath(int destination) {
	    // UPDATE THIS METHOD
		ArrayList<TNode> myList = new ArrayList<TNode>();
		TNode current = trainZero;
		myList.add(current);
		while(current != null && current.getLocation() != destination) {
			if(current.getNext().getLocation() < destination) {
				current = current.getNext();
				myList.add(current);
				while(current.getNext() == null) {
					current = current.getDown();
					myList.add(current);
				}
			}
			else if(current.getNext().getLocation() == destination) {
				current = current.getNext();
				myList.add(current);
				while (current.getDown() != null) {
					current = current.getDown();
					myList.add(current);
				}
				return myList;
			}
			else {
				current = current.getDown();
				myList.add(current);
			}
		}
		return myList;
	}

	/**
	 * Returns a deep copy of the given layered list, which contains exactly the same
	 * locations and connections, but every node is a NEW node.
	 * 
	 * @return A reference to the train zero node of a deep copy
	 */
	public TNode duplicate() {
	    // UPDATE THIS METHOD
		
		TNode w = null;
		TNode b = null;
		TNode t = null;
		TNode currentT = trainZero;
		TNode currentB = trainZero.getDown();
		TNode currentW = trainZero.getDown().getDown();
		ArrayList<Integer> myTrain = new ArrayList<Integer>();
		ArrayList<Integer> myBus = new ArrayList<Integer>();
		ArrayList<Integer> myWalk = new ArrayList<Integer>();

		while(currentT != null) {
			myTrain.add(currentT.getLocation());
			currentT = currentT.getNext();
		}
		while(currentB != null) {
			myBus.add(currentB.getLocation());
			currentB = currentB.getNext();
		}
		while(currentW != null) {
			myWalk.add(currentW.getLocation());
			currentW = currentW.getNext();
		}

		for(int i = myWalk.size()-1; i >= 0; i--) {
			w = new TNode(myWalk.get(i), w, null);
			if(myBus.get(myBus.size()-1) == myWalk.get(i)) {
				b = new TNode(myBus.get(myBus.size()-1), b, w);
				myBus.remove(myBus.size()-1);
			}
			if(myTrain.get(myTrain.size()-1) == myWalk.get(i)) {
				t = new TNode(myTrain.get(myTrain.size()-1), t, b);
				myTrain.remove(myTrain.size()-1);
			}
		}

		return t;
	}

	/**
	 * Modifies the given layered list to add a scooter layer in between the bus and
	 * walking layer.
	 * 
	 * @param scooterStops An int array representing where the scooter stops are located
	 */
	public void addScooter(int[] scooterStops) {
	    // UPDATE THIS METHOD
		TNode s = null;
		TNode w = trainZero.getDown().getDown();
		TNode b = trainZero.getDown();
		for(int i = scooterStops.length - 1; i >= 0; i--) {
			while(w != null) {
				if(w.getLocation() == scooterStops[i]) {
					s = new TNode(scooterStops[i], s, w);
					break;
				}
				w = w.getNext();
			}
			while(b != null) {
				if(b.getLocation() == scooterStops[i]) {
					b.setDown(s);
					break;
				}
				b = b.getNext();
			}
			w = trainZero.getDown().getDown();
			b = trainZero.getDown();
		}
		s = new TNode(0, s, w);
		b.setDown(s);
	}

	/**
	 * Used by the driver to display the layered linked list. 
	 * DO NOT edit.
	 */
	public void printList() {
		// Traverse the starts of the layers, then the layers within
		for (TNode vertPtr = trainZero; vertPtr != null; vertPtr = vertPtr.getDown()) {
			for (TNode horizPtr = vertPtr; horizPtr != null; horizPtr = horizPtr.getNext()) {
				// Output the location, then prepare for the arrow to the next
				StdOut.print(horizPtr.getLocation());
				if (horizPtr.getNext() == null) break;
				
				// Spacing is determined by the numbers in the walking layer
				for (int i = horizPtr.getLocation()+1; i < horizPtr.getNext().getLocation(); i++) {
					StdOut.print("--");
					int numLen = String.valueOf(i).length();
					for (int j = 0; j < numLen; j++) StdOut.print("-");
				}
				StdOut.print("->");
			}

			// Prepare for vertical lines
			if (vertPtr.getDown() == null) break;
			StdOut.println();
			
			TNode downPtr = vertPtr.getDown();
			// Reset horizPtr, and output a | under each number
			for (TNode horizPtr = vertPtr; horizPtr != null; horizPtr = horizPtr.getNext()) {
				while (downPtr.getLocation() < horizPtr.getLocation()) downPtr = downPtr.getNext();
				if (downPtr.getLocation() == horizPtr.getLocation() && horizPtr.getDown() == downPtr) StdOut.print("|");
				else StdOut.print(" ");
				int numLen = String.valueOf(horizPtr.getLocation()).length();
				for (int j = 0; j < numLen-1; j++) StdOut.print(" ");
				
				if (horizPtr.getNext() == null) break;
				
				for (int i = horizPtr.getLocation()+1; i <= horizPtr.getNext().getLocation(); i++) {
					StdOut.print("  ");

					if (i != horizPtr.getNext().getLocation()) {
						numLen = String.valueOf(i).length();
						for (int j = 0; j < numLen; j++) StdOut.print(" ");
					}
				}
			}
			StdOut.println();
		}
		StdOut.println();
	}
	
	/**
	 * Used by the driver to display best path. 
	 * DO NOT edit.
	 */
	public void printBestPath(int destination) {
		ArrayList<TNode> path = bestPath(destination);
		for (TNode vertPtr = trainZero; vertPtr != null; vertPtr = vertPtr.getDown()) {
			for (TNode horizPtr = vertPtr; horizPtr != null; horizPtr = horizPtr.getNext()) {
				// ONLY print the number if this node is in the path, otherwise spaces
				if (path.contains(horizPtr)) StdOut.print(horizPtr.getLocation());
				else {
					int numLen = String.valueOf(horizPtr.getLocation()).length();
					for (int i = 0; i < numLen; i++) StdOut.print(" ");
				}
				if (horizPtr.getNext() == null) break;
				
				// ONLY print the edge if both ends are in the path, otherwise spaces
				String separator = (path.contains(horizPtr) && path.contains(horizPtr.getNext())) ? ">" : " ";
				for (int i = horizPtr.getLocation()+1; i < horizPtr.getNext().getLocation(); i++) {
					StdOut.print(separator + separator);
					
					int numLen = String.valueOf(i).length();
					for (int j = 0; j < numLen; j++) StdOut.print(separator);
				}

				StdOut.print(separator + separator);
			}
			
			if (vertPtr.getDown() == null) break;
			StdOut.println();

			for (TNode horizPtr = vertPtr; horizPtr != null; horizPtr = horizPtr.getNext()) {
				// ONLY print the vertical edge if both ends are in the path, otherwise space
				StdOut.print((path.contains(horizPtr) && path.contains(horizPtr.getDown())) ? "V" : " ");
				int numLen = String.valueOf(horizPtr.getLocation()).length();
				for (int j = 0; j < numLen-1; j++) StdOut.print(" ");
				
				if (horizPtr.getNext() == null) break;
				
				for (int i = horizPtr.getLocation()+1; i <= horizPtr.getNext().getLocation(); i++) {
					StdOut.print("  ");

					if (i != horizPtr.getNext().getLocation()) {
						numLen = String.valueOf(i).length();
						for (int j = 0; j < numLen; j++) StdOut.print(" ");
					}
				}
			}
			StdOut.println();
		}
		StdOut.println();
	}
}
