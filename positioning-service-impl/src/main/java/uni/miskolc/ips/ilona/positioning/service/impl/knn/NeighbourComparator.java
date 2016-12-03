package uni.miskolc.ips.ilona.positioning.service.impl.knn;

import java.util.Comparator;
/**
 * 
 * @author ilona
 * The comparator class of the Neighbours based on the distance.
 */
public class NeighbourComparator implements Comparator<Neighbour> {
/**
 * Compare the Neighbours based on the distance, it allows an ascending order.
 * @param n1 One Neighbour
 * @param n2 Another Neighbour
 * @return the business logic value
 */
		public int compare(final Neighbour n1, final Neighbour n2) {
			int ret = -1;
			// business logic here
			if (n1.getDistance() == n2.getDistance()) {
				ret = 0;
			} else if (n1.getDistance() > n2.getDistance()) {
				ret = 1;
			} else if (n1.getDistance() < n2.getDistance()) {
				ret = -1;
			} // end business logic
			return ret;
		}
	}


