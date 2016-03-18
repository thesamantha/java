import java.util.List;
import java.util.LinkedList;
import java.util.HashMap;


public class ShortFlightPlanner implements ShortFlightPlan {
    FlightDatabase database1;
    FlightDatabase database2;
    
    public ShortFlightPlanner(){
        database1 = new FlightDatabase("airports.csv", "flights_distance.csv");
        database2 = new FlightDatabase("airports.csv", "flights_price.csv");
    }
     /**
     * Returns the minimum distance traveled to when flying from start_fh to ziel_fh using Dijkstra's algorithm
     * @param start_fh Start Airport
     * @param ziel_fh Destination Airport
     * @return
     */
    public double getMinimumDistance(List<Airport> airports, Airport start_fh, Airport ziel_fh){
        if((airports == null) || (start_fh == null) || (ziel_fh == null)){
            return 0.0;
        }
        
        List<Flight> shortestPath = findShortestPath(airports, start_fh, ziel_fh);
        /*
         * Dies kann sowohl die geographische Distanz sein, als auch der Preis der Flüge.
         * -> also sollen wir beide Distanzen berechenen und nur das gewünschte wird zurückgegeben?
         */
        double result = 0.0;
        for(Flight f : shortestPath){
            result += f.getWeight();
        }
        
        return result;
    }

    /**
     * Find the shortest path (lowest distance) between start_fh and ziel_fh using Dijkstra's algorithm
     * @param start_fh Start Airport
     * @param ziel_fh Destination Airport
     * @return
     */
    public List<Flight> findShortestPath(List<Airport> airports, Airport start_fh, Airport ziel_fh){
        //Wenn ich HashMap put mit gleichen keys, aber unterschiedlichen values, wird dann automatisch überschrieben?
        if((airports == null) || (start_fh == null) || (ziel_fh == null)){
            return null;
        }
        HashMap<Airport, Double> distance = new HashMap<Airport, Double>();
        HashMap<Airport, Character> color = new HashMap<Airport, Character>();
        HashMap<Airport, Flight> previous = new HashMap<Airport, Flight>();
        TUPriorityQueue<Airport> prioqueue = new TUPriorityQueue<Airport>();
        
    
        
        for(Airport a : airports){
            distance.put(a, Double.POSITIVE_INFINITY);
            color.put(a, 'w');
            previous.put(a, null);
            prioqueue.add(distance.get(a), a);
            
        }

        distance.put(start_fh, 0.0);
        prioqueue.updatePriority(distance.get(start_fh), start_fh);
        
        while(prioqueue.isEmpty() == false){
            Airport tmp = prioqueue.poll();
            if(color.get(tmp) == 'w');{
                color.put(tmp, 's');
                distance.put(tmp, (distance.get(tmp)));
                for(Flight f : tmp.getAllDepartures()){
                    if(distance.get(f.getTo()) > (distance.get(tmp) + f.getWeight())){
                        distance.put(f.getTo(), (distance.get(tmp) + f.getWeight()));
                        prioqueue.updatePriority(distance.get(f.getTo()), f.getTo());
                        previous.put(f.getTo(), f);
                    }
                    
                }
                
            }
            
        }
        
        LinkedList<Flight> result = new LinkedList<Flight>();
        
        Airport tmp2 = ziel_fh;
        for(int i = 1; i < distance.get(ziel_fh); i++){
            if(previous.get(tmp2) != null){
                result.add(previous.get(tmp2));
                if(previous.get(tmp2).getFrom() != null){
                    tmp2 = previous.get(tmp2).getFrom();
                }
            
            }
        }
        
        return result;
    }
    
}