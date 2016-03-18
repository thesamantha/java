import java.util.List;
import java.util.LinkedList;
import java.util.HashMap;

/** Implementation of Dynamic Box-Sorting Algorithm **/
/**Samantha Tite-Webber, 2015 **/

public class BananenTransporter implements BananenTransport {

	public List<BananenKiste> erzeugeBeladungsPlan(BananenKiste[] alleKisten) {

		//hash map links a predecessor with every banana crate
		//hash map verknuepft einen Vorgaenger mit jeder BananenKiste
		HashMap<BananenKiste, BananenKiste> predecessor = new HashMap<BananenKiste, BananenKiste>();

		//hash map links a maximum count with every banana crate
		//hash map verknuepft eine max Anzahl mit jeder BananenKiste
		HashMap<BananenKiste, Integer> maxBananen = new HashMap<BananenKiste, Integer>();

		//hashmap which informs us if a crate has been visited or not
		//Hashmap sagt, ob eine Kiste schon mal besucht wurde oder nicht
		HashMap<BananenKiste, Boolean> visited = new HashMap<BananenKiste, Boolean>();

		//sort list of banana crates
		//sortiere Liste von BananenKisten
		bubbleSort(alleKisten);


		//initialize predecessor Hashmap
		for(int i = 0; i < alleKisten.length; i++) {

			//currently the predecessor of every crate is itself
			//der Vorgaenger jeder Kiste gerade ist sie selber
			predecessor.put(alleKisten[i], alleKisten[i]);
		}

		//initialize max Bananen count Hashmap
		for(int i = 0; i < alleKisten.length; i++) {
			
			//the current maximum count of every crate is how many bananas are in that crate
			//die aktuelle max Anzahl jeder Kiste ist gerade wie viele in einer es gibt
			maxBananen.put(alleKisten[i], alleKisten[i].getBananas());
		}

		//initialize visited hashmap
		//initialisiere visited HashMap
		for(int i = 0; i < alleKisten.length; i++) {

			//so far no crate has been visited
			//bis jetzt wurde keine Kiste besucht
			visited.put(alleKisten[i], false);
		}

		//go through actual algorithm
		for(int i = 1; i < alleKisten.length; i++) {

			for(int j = 0; j < i - 1; j++) {

				if(alleKisten[i].passtAuf(alleKisten[j])) {
	
					//initialize max count of bananas in current crate (alleKisten[i])
					//aktuelisiere max Anzahl Bananen in aktueller Kiste (alleKisten[i])
					if(maxBananen.get(alleKisten[j]) + alleKisten[i].getBananas() > maxBananen.get(alleKisten[i])) {

						//now the count is the sum of how many bananas are in the crate plus the total count of those crates underneath it
						//jetzt ist die Anzahl also die Anzahl in der Kiste plus die insgesamte Anzahl von den Anzahlen der unteren Kisten
						maxBananen.put(alleKisten[i], maxBananen.get(alleKisten[j]) + alleKisten[i].getBananas());

						//set the predecessor of the current crate
						//aktuelisiere den Vorgaenger der aktuellen Kiste
						predecessor.put(alleKisten[i], alleKisten[j]);
					}
				}
			}
		}

		//obtain the crate which has the maximum count of bananas
		//krieg die Kiste, die die max Anzahl von Bananen hat
		int max = -1;
		BananenKiste maxKiste = alleKisten[0];	//(irrelevant with which crate we start) egal mit wem wir anfangen, denn sie wird auf jeden Fall veraendert
	
		for(int i = 0; i < alleKisten.length; i++) {

			if(maxBananen.get(alleKisten[i]) > max) {

				max = maxBananen.get(alleKisten[i]);
				maxKiste = alleKisten[i];
			}
		}

		//build list out of crate with maximum count and its predecessors (afterwards we will need to sort it in reverse order)
		//baue Liste aus dieser Kiste und ihren Vorgaenger (wir werden das danach in umgekerhter Richtung machen muessen)
		LinkedList<BananenKiste> maxList = new LinkedList<BananenKiste>();
		
		BananenKiste current = maxKiste;
		
		//as long as the current crate has a predecessor which has not been visited
		//solange die aktuelle Kiste einen Vorgaenger hat, der noch nicht besucht wurde
		while(visited.get((predecessor.get(current))) != true) {

			maxList.add(current);
			visited.put(current, true);
			current = predecessor.get(current);
		}

		//sort list in reverse order (largest to smallest)
		//mach List in umgekerhter Richtung (groesste zur kleinsten)
		LinkedList<BananenKiste> righted = new LinkedList<BananenKiste>();
		for(int j = maxList.size()-1; j >= 0; j--) {
			righted.add(maxList.get(j));
		}
		

		//make sure that everything is in order (if a crate should be between two others, but isn't, place it between those two)
		//sicher mach, dass alles in Ordnung ist (wenn eine Kiste zwischen zwei anderen Kisten sein soll, aber ist nicht, stecken wir die dort)
		for(int i = 0; i < righted.size(); i++) {

			for(int j = 0; j < alleKisten.length; j++) {

				if(alleKisten[j].passtAuf(righted.get(i)) && righted.get(i+1).passtAuf(alleKisten[j])) {
					righted.add(i+1, alleKisten[j]);
				}
			}
		}

		return righted;
	}


	/**
	 * Sorts an array of BananenKisten in descending order, denoting "smaller" as that whose product of length, width, and depth, is less than the product of 		 * the same from the Bananenkiste with which we are comparing
	 */
	public static void bubbleSort(BananenKiste[] array){
		/*we want to compare every two sets of numbers, and if they are out of order, swap them. We run through the array repeatedly until we reach a run in 				which no swaps are made, at which point we are finished.*/

		int arrayLength = array.length;
		int swapMade = 0;		//(to check if we made a swap) um zu checken, ob wir eine Vertausch gemacht haben
		
		//because we want to compare the i-th element with the (i+1)-th element, our run-length must be 1 less than the length of the array
		//weil wir das i-te Element mit dem (i+1)-ten Element vergleichen wollen, muss unsere Ablauflänge eins kleiner als die Länge des Arrays sein
		for(int i = 0; i < arrayLength - 1; i++) {	
	
			if(array[i].height*array[i].width*array[i].depth < array[i+1].width*array[i+1].depth*array[i+1].height) {

				BananenKiste tmp = array[i];
				array[i] = array[i+1];
				array[i+1] = tmp;
				swapMade++;			//(mark when we've made a swap) bemerken wenn eine Vertausch gemacht wurde
			}
		}

		if(swapMade != 0)		//(if we've made a swap, sort through the array again) wenn eine Vertausch gemacht wurde, müssen wir durch das Array wieder sortieren
			bubbleSort(array);
		
	}

}
