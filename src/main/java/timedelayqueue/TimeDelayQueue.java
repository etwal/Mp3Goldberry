package timedelayqueue;

import java.sql.Timestamp;
import java.util.*;
import java.util.stream.Collectors;

// TODO: write a description for this class
// TODO: complete all methods, irrespective of whether there is an explicit TODO or not
// TODO: write clear specs
// TODO: State the rep invariant and abstraction function
// TODO: what is the thread safety argument?
public class TimeDelayQueue {

    private int delay;
    private List<PubSubMessage> queue = new ArrayList<>();

    private int total = queue.size();

    // a comparator to sort messages
    private class PubSubMessageComparator implements Comparator<PubSubMessage> {
        public int compare(PubSubMessage msg1, PubSubMessage msg2) {
            return msg1.getTimestamp().compareTo(msg2.getTimestamp());
        }

    }


    /**
     * Create a new TimeDelayQueue
     * @param delay the delay, in milliseconds, that the queue can tolerate, >= 0
     */
    public TimeDelayQueue(int delay) {
        this.delay = delay;

    }

    // add a message to the TimeDelayQueue
    // if a message with the same id exists then
    // return false
    public boolean add(PubSubMessage msg) {
        for (PubSubMessage m: queue){
            if (msg.getId() == m.getId()){
                return false;
            }
        }
        queue.add(msg);
        total++;



        return true;
    }

    /**
     * Get the count of the total number of messages processed
     * by this TimeDelayQueue
     * @return
     */
    public long getTotalMsgCount() {

        return total;

    }

    // return the next message and PubSubMessage.NO_MSG
    // if there is ni suitable message
    public PubSubMessage getNext() {
        queue.sort(new PubSubMessageComparator());



        for (int i =0; i< queue.size(); i++) {
            if ( queue.get(i).isTransient() && System.currentTimeMillis() - queue.get(i).getTimestamp().getTime() > ((TransientPubSubMessage)queue.get(i)).getLifetime() ){
                queue.remove(i);
            }
            if ((System.currentTimeMillis()) - queue.get(i).getTimestamp().getTime() >= delay ) {
                PubSubMessage temp = queue.get(i);
                queue.remove(i);
                return temp;
            }
        }


        return PubSubMessage.NO_MSG;
    }

    // return the maximum number of operations
    // performed on this TimeDelayQueue over
    // any window of length timeWindow
    // the operations of interest are add and getNext
    public int getPeakLoad(int timeWindow) {
        return -1;
    }

}
