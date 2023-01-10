import java.util.Arrays;

public class ClosestAntennaPair {

    private double closestDistance = Double.POSITIVE_INFINITY;
    private long counter = 0;

    public ClosestAntennaPair(Point2D[] aPoints, Point2D[] bPoints) {
        // Insert your solution here.

        int n = aPoints.length;
        int m = bPoints.length;

        Point2D[] aPointsSortedByX = new Point2D[n];
        Point2D[] bPointsSortedByX = new Point2D[m];
        for (int i = 0; i < n; i++) {
            aPointsSortedByX[i] = aPoints[i];
        }
        for (int i = 0; i < m; i++){
            bPointsSortedByX[i] = bPoints[i];
        }

        Arrays.sort(aPointsSortedByX, Point2D.Y_ORDER);
        Arrays.sort(aPointsSortedByX, Point2D.X_ORDER);

        Arrays.sort(bPointsSortedByX, Point2D.Y_ORDER);
        Arrays.sort(bPointsSortedByX, Point2D.X_ORDER);

        for (int i = 0; i < n-1; i++) {
            for (int j = 0; j < m-1; j++){
                if (aPointsSortedByX[i].equals(bPointsSortedByX[j])){
                    closestDistance = 0.0;
                    return;
                }
            }
        }

        for (int i = 0; i < m-1; i++) {
            for (int j = 0; j < n-1; j++){
                if (aPointsSortedByX[j].equals(bPointsSortedByX[i])){
                    closestDistance = 0.0;
                    return;
                }
            }
        }

        Point2D[] aPointsSortedByY = new Point2D[n];
        Point2D[] bPointsSortedByY = new Point2D[m];

        for (int i = 0; i < n; i++)
            aPointsSortedByY[i] = aPointsSortedByX[i];

        for (int i = 0; i < m; i++)
            bPointsSortedByY[i] = bPointsSortedByX[i];

        // auxiliary array
        Point2D[] auxA = new Point2D[n];
        Point2D[] auxB = new Point2D[m];

        if (n == 0 | m == 0){
            return;
        }
        else {
            closest(aPointsSortedByX, bPointsSortedByX, aPointsSortedByY, bPointsSortedByY, auxA, auxB, 0, 0, n - 1, m - 1);
        }


    }

    public double closest(Point2D[] aPointsSortedByX, Point2D[] bPointsSortedByX, Point2D[] aPointsSortedByY, Point2D[] bPointsSortedByY, Point2D[] auxA, Point2D[] auxB, int lowA, int lowB, int highA, int highB) {
        // please do not delete/modify the next line!
        counter++;

        if (highA < lowA){
            return Double.POSITIVE_INFINITY;
        }
        else if (highA == lowA){
            for (Point2D bPoint : bPointsSortedByX) {
                double curDistance = (aPointsSortedByX[lowA]).distanceTo(bPoint);
                if (curDistance < closestDistance)
                    closestDistance = curDistance;
                }
            //Arrays.sort(aPointsSortedByY, Point2D.Y_ORDER);

            return closestDistance;
        }
        else if (highB < lowB){
            return Double.POSITIVE_INFINITY;
        }
        else if (highB == lowB){
            for (Point2D aPoint : aPointsSortedByX) {
                double curDistance = bPointsSortedByX[lowB].distanceTo(aPoint);
                if (curDistance < closestDistance)
                    closestDistance = curDistance;
                }
            //Arrays.sort(bPointsSortedByY, Point2D.Y_ORDER);
            return closestDistance;

        }

        int midA = lowA + (highA - lowA) / 2;
        int midB = -1;

        double bClosestDistanceA = Double.POSITIVE_INFINITY;

        Point2D medianA = aPointsSortedByX[midA];
        Point2D medianB = bPointsSortedByX[0];

        for (int i = 0; i < bPointsSortedByY.length; i++){
            if (Math.abs(bPointsSortedByX[i].x() - medianA.x()) < bClosestDistanceA){
                midB = i;
                if (bPointsSortedByX[midB].x() > medianA.x()){
                    medianB = bPointsSortedByX[midB];
                    bClosestDistanceA = Math.abs(bPointsSortedByX[i].x() - medianA.x());

                }
            }
        }


        double delta1 = closest(aPointsSortedByX, bPointsSortedByX, aPointsSortedByY, bPointsSortedByY, auxA, auxB, lowA, lowB, midA, midB);
        double delta2 = closest(aPointsSortedByX, bPointsSortedByX, aPointsSortedByY, bPointsSortedByY, auxA, auxB, midA + 1, midB + 1, highA, highB);
        double delta = Math.min(delta1, delta2);

        merge(aPointsSortedByY, auxA, lowA, midA, highA);
        merge(bPointsSortedByY, auxB, lowB, midB, highB);

        int k = 0;
        for (int i = lowA; i <= highA; i++) {
            if (Math.abs(aPointsSortedByY[i].x() - medianB.x()) < delta)
                auxA[k++] = aPointsSortedByY[i];
        }
        int l = 0;
        for (int i = lowB; i <= highB; i++) {
            if (Math.abs(bPointsSortedByY[i].x()- medianA.x()) < delta)
                auxB[l++] = bPointsSortedByY[i];
        }

        for (int i = 0; i < k; i++){
            for (int j = 0; j < l ; j++){
                if ((Math.abs(auxA[i].y() - auxB[j].y()) <= delta)){
                    double distance = auxA[i].distanceTo(auxB[j]);
                    if (distance < delta) {
                        delta = distance;
                        if (distance < closestDistance);
                        closestDistance = delta;
                }

                }
            }
        }


        return delta;
    }

    public double distance() {
        return closestDistance;
    }

    public long getCounter() {
        return counter;
    }

    // stably merge a[low .. mid] with a[mid+1 ..high] using aux[low .. high]
    // precondition: a[low .. mid] and a[mid+1 .. high] are sorted subarrays, namely sorted by y coordinate
    // this is the same as in ClosestPair
    private static void merge(Point2D[] a, Point2D[] aux, int low, int mid, int high) {
        // copy to aux[]
        // note this wipes out any values that were previously in aux in the [low,high] range we're currently using

        for (int k = low; k <= high; k++) {
            aux[k] = a[k];
        }

        int i = low, j = mid + 1;
        for (int k = low; k <= high; k++) {
            if (i > mid) a[k] = aux[j++];   // already finished with the low list ?  then dump the rest of high list
            else if (j > high) a[k] = aux[i++];   // already finished with the high list ?  then dump the rest of low list
            else if (aux[i].compareByY(aux[j]) < 0)
                a[k] = aux[i++]; // aux[i] should be in front of aux[j] ? position and increment the pointer
            else a[k] = aux[j++];
        }
    }
}
