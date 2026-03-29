public class QuicksortDSA {
    public static void main(String[] args){

        //quick sort = moves smaller elements to the left of a pivot.
        //             recursively divide array in 2 partition

        //run-time complexity = best case O(n Log(n))
        //                      average case O(n Log(n))
        //                      worst case O(n^2) if already sorted

        // space complexity = O(log(n)) due to recursion


        int[] array = {8, 2, 5, 3, 9, 4, 7, 6, 1};

        quickSort(array, 0, array.length - 1);

        for (int i : array) {
            System.out.print(i + " ");
        }
    }

    private static void quickSort(int[] array, int start, int end) {
        if (end <= start) return;

        int pivot = partition(array, start, end);
        quickSort(array, start, pivot - 1);
        quickSort(array, pivot + 1, end);
    }
    private static int partition(int[] array, int start, int end) {

       int pivot = array[end];
       int i = start -1;

       for (int j = start; j <= end - 1; j++){
           if (array[j] < pivot){
               i++;
               int temp = array[i];
               array[i] = array[j];
               array[j] = temp;
           }
       }
        i++;
        int temp = array[i];
        array[i] = array[end];
        array[end] = temp;

       return  i;
    }
}
