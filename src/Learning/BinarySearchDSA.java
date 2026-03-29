import java.util.Arrays;

public class BinarySearchDSA {
    public static void main(String[] args) {


        // binary search = Search algorithm that finds the position of a target
        // value within a sorted array.
        // half of the array is eliminated during each "step"
//
//        int[] array = new int[100000];
//        int target = 7777;
//
//        for (int i = 0; i < array.length; i++){
//            array[i] = i;
//        }
//
////        int index = Arrays.binarySearch(array, target);
//        int index = binarySearch(array, target);
//
//        if (index == -1){
//            System.out.println("Invalid input");
//        }else {
//            System.out.println("Element found at: " + index);
//        }

        int[] nums = {1, 3, 5, 6};
        int target = 4;

        int result = binarySearch(nums, target);

        System.out.println("Index: " + result);
    }

    private static int binarySearch(int[] nums, int target) {
                int low = 0;
                int high = nums.length - 1;
                while (low <= high) {
                    int mid = low + (high - low) / 2;
                    if (nums[mid] == target){
                        return mid;
                    } else if (nums[mid] < target) {
                        low =  mid + 1;
                    }else {
                        high = mid - 1;
                    }
                }
                return low;
    }


    //Find the first occurrence of 4.
//    private static int binarySearch(int[] nums, int target) {
//        int low = 0;
//        int high = nums.length - 1;
//        int result = -1;
//        while (low <= high) {
//            int middle = low + (high - low) / 2;
//            if (nums[middle] == target) {
//                result = middle;      // save position
//                high = middle - 1;    // search left side
//            }
//            else if (nums[middle] < target) {
//                low = middle + 1;
//            }
//            else {
//                high = middle - 1;
//            }
//        }
//        return result;
//    }

    //[1, 2, 4, 4, 4, 5, 6]
    //Find the first occurrence of 4.
//    private static int binarySearch(int[] nums, int target) {
//        int low = 0;
//        int high = nums.length - 1;
//        while (low <= high) {
//            int mid = low + (high - low) / 2;
//            if (nums[mid] == target) {
//                return mid;
//            }
//            else if (nums[mid] < target) {
//                low = mid + 1;
//            }
//            else {
//                high = mid - 1;
//            }
//        }
//        return -1;
//    }

//[2, 4, 6, 8, 10, 12, 14]
    //Return the index of 10 using binary search.
//    private static int binarySearch(int[] array, int target) {
//          int low = 0;
//          int high = array.length - 1;
//
//          while (low <= high) {
//              int middle = low + (high - low) / 2;
//              int value = array[middle];
//
//              if (value < target){
//                  low = middle + 1;
//              } else if (value > target) {
//                  high = middle - 1;
//              }else {
//                  return middle;
//              }
//          }
//          return  -1;
//    }

//    private static int binarySearch(int[] array, int target) {
//        int low = 0;
//        int high = array.length -1;
//
//        while (low <= high){
//            int middle = low + (high - low) /2;
//            int value = array[middle];
//
//            System.out.println("middle: " + value);
//
//            if (value < target) {
//                low = middle + 1;
//            }else if (value > target) {
//                high = middle - 1;
//            }else {
//                return middle;
//            }
//        }
//        return -1;
//    }
}
