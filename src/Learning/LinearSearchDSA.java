public class LinearSearchDSA {
    public static void main(String args[]) {

        // linear search = Iterate through a collection one element at a time

        // runtime complexity: O(n)

        // Disadvantages:
        // Slow for large data sets

        // Advantages:
        // Fast for searches of small to medium data sets
        // Does not need to sorted
        // Useful for data structures that do not have random access (Linked List)


        // linear search = Iterate through a collection one element at a time

//        int[] array = {4,7,2,1,6,9,3,8,5};
//        int index = linearSearch(array, 10);
//
//        if (index != -1 ){
//            System.out.println("Index found at " + index);
//        } else {
//            System.out.println("You passed in an invalid number! Element not found");
//        }
//
//
//    }
//
//    private static int linearSearch(int[] array, int value) {
//        for (int i = 0; i < array.length; i++){
//            if (array[i] == value){
//                return i;
//            }
//        } return -1;

//        int[] nums = {5, 8, 2, 9, 1};
//        int target = 9;
//
//        int index = linearSearch(nums, target);
//        System.out.println(index);

//        int[] nums = {3, 6, 1, 8, 4};
//        int target = 6;
//        int[] nums = {4,2,7,2,9,2};
//        int target = 2;

        int[] nums = {4, 7, 2, 7, 9, 7, 1};
         int target = 7;
//        int result = linearSearch(nums);

        System.out.println(linearSearch(nums));
//    }

//    private static int linearSearch(int[] nums) {
//
//    }
    }

    private static int linearSearch(int[] nums) {
        int bigNum = nums[0];
        for (int i = 1; i < nums.length; i++){
            if (nums[i] > bigNum){
               return nums[i];
            }
        }
        return - 1;
    }

//    Find the maximum sum of any contiguous subarray.
    // int[] nums = {-2,1,-3,4,-1,2,1,-5,4};
//    private static int linearSearch(int[] nums) {
//
//        int currentSum = nums[0];
//        int maxSum = nums[0];
//
//        for (int i = 1; i < nums.length; i++) {
//
//            currentSum = Math.max(nums[i], currentSum + nums[i]);
//
//            maxSum = Math.max(maxSum, currentSum);
//        }
//
//        return maxSum;
//    }


    //Find the length of the longest increasing sequence.
//    private static int linearSearch(int[] nums) {
//        int currentLength = 1;
//        int maxLength = 1;
//        for (int i = 1; i < nums.length; i++) {
//            if (nums[i] > nums[i - 1]) {
//                currentLength++;
//            } else {
//                currentLength = 1;
//            }
//            if (currentLength > maxLength) {
//                maxLength = currentLength;
//            }
//        }
//        return maxLength;
//    }



    //Find the maximum profit you could make.
//    private static int linearSearch(int[] prices) {
//        int minPrice = prices[0];
//        int maxProfit = 0;
//        for (int i = 1; i < prices.length; i++) {
//            if (prices[i] < minPrice) {
//                minPrice = prices[i];
//            }
//            int profit = prices[i] - minPrice;
//            if (profit > maxProfit) {
//                maxProfit = profit;
//            }
//        }
//        return maxProfit;
//    }

    //Find the majority element.
//    private static int linearSearch(int[] nums) {
//        for (int i = 0; i < nums.length; i++) {
//            int count = 0;
//            for (int j = 0; j < nums.length; j++) {
//                if (nums[j] == nums[i]) {
//                    count++;
//                }
//            }
//            if (count > nums.length / 2) {
//                return nums[i];
//            }
//        }
//        return -1;
//    }

    //Find the Missing Number,One number in the sequence 1 → n is missing.
    //Find the missing number.
//    private static int linearSearch(int[] nums) {
//        for (int i = 0; i < nums.length - 1; i++) {
//            if (nums[i + 1] - nums[i] > 1) {
//                return nums[i] + 1;
//            }
//        }
//        return -1;
//    }

    //Find the first duplicate number.
//    private static int linearSearch(int[] nums) {
//        for (int i = 0; i < nums.length; i++){
//           for (int j = 1 + 1; j < nums.length; j++ ){
//               if (nums[i] == nums[j]){
//                   return nums[i];
//               }
//           }
//        }
//        return -1;
//    }
// check if array is sorted
//    private static boolean linearSearch(int[] nums) {
//        for (int i = 1; i < nums.length; i++){
//            if (nums[i] > nums[i-1]){
//                return false;
//            }
//        }
//        return true;
//    }

//Find the difference between the largest and smallest numbers.

//    private static int linearSearch(int[] nums) {
//
//        int largest = nums[0];
//        int smallest = nums[0];
//
//        for (int i = 1; i < nums.length; i++) {
//
//            if (nums[i] > largest) {
//                largest = nums[i];
//            }
//
//            if (nums[i] < smallest) {
//                smallest = nums[i];
//            }
//
//        }
//
//        return largest - smallest;
//    }

//    private static int linearSearch(int[] nums) {
//        int index = 0;
//
//        for (int i = 1; i < nums.length; i++){
//            if (nums[i] > nums[index]){
//              index = i;
//            }
//        }
//        return index;
//    }
//Find the second largest number using linear search.
//    private static int linearSearch(int[] nums) {
//        int lagestNum = nums[0];
//        int seecondLagest = Integer.MIN_VALUE;
//
//        for (int i = 1; i < nums.length; i++){
//            if (nums[i] > lagestNum){
//                seecondLagest = lagestNum;
//                lagestNum = nums[i];
//
//            } else if (nums[i] > seecondLagest && nums[i] != lagestNum) {
//                seecondLagest = nums[i];
//            }
//        }return seecondLagest;
//    }


//Find the last index where the target appears.
//    private static int linearSearch(int[] nums, int target) {
//        for (int i = nums.length -1; i >= 0; i++){
//           if (nums[i] == target){
//               return i;
//           }
//        }
//        return -1;
//    }



    // to get the first negative number
//    private static int linearSearch(int[] nums) {
//          for (int i = 0; i < nums.length; i++) {
//              if (nums[i] < 0){
//                  return nums[i];
//              }
//          }
//        return -1;
//    }

    // to get the last negative number
//    private static int linearSearch(int[] nums) {
//
//        for (int i = nums.length - 1; i >= 0; i--) {
//
//            if (nums[i] < 0) {
//                return nums[i];
//            }
//        }
//        return -1;
//    }


//Print all indexes where the target appears.
//    private static void linearSearch(int[] nums, int target) {
//        for (int i = 0; i < nums.length; i++){
//            if (nums[i] == target) {
//                System.out.println(i);
//            }
//        }
//    }
//Count how many times the target appears in the array.
//    private static int linearSearch(int[] nums, int target) {
//        int count = 0;
//        for (int i = 0; i < nums.length; i++) {
//            if (nums[i] == target){
//                count++;
//            }
//        }
//        return count;
//    }

//Find the index of the smallest number using linear search.
//    private static int linearSearch(int[] nums) {
//        int smallNum =0;
//        for (int i = 1; i < nums.length; i++){
//           if (nums[i] < nums[smallNum]) {
//               smallNum = i;
//           }
//        }
//        return smallNum;
//    }

//    Find the index of the target value using linear search.
//    private static int linearSearch(int[] nums, int target) {
//        for (int i = 0; i < nums.length; i++) {
//            if (nums[i] == target){
//                return i;
//            }
//        }
//        return -1;
//    }

//    private static boolean linearSearch(int[] nums) {
//        int start = 0;
//        int end = nums.length -1;
//
//       while (start < end) {
//
//           int temp = nums[start];
//           nums[start] = nums[end];
//           nums[end] = temp;
//
//           start++;
//           end--;
//        }
//        return false;
//    }

//    private static void linearSearch(int[] nums, int target) {
//        for (int i = 0; i < nums.length; i++){
//            for (int j = i + 1; j < nums.length; j++){
//                if (nums[i] + nums[j] == target){
//                    System.out.println("Pair: " + nums[i] + " + " + nums[j] + " = " + target);
//                    System.out.println("Indexes: " + i + ", " + j);
//                    return;
//                }
//            }
//        }
//        System.out.println("No pair found");
//    }

//    private static int linearSearch(int[] nums) {
//        for (int i = 0; i < nums.length; i++){
//            for (int j = i + 1; j <  nums.length; j++){
//                if (nums[i] == nums[j]){
//                    System.out.println("Duplicate: " + nums[i]);
//                    break;
//                }
//            }
//        }
//        return -1;
//    }

//    private static int linearSearch(int[] nums) {
//        int largest = nums[0];
//        int secondLargest = Integer.MIN_VALUE;
//
//        for (int i = 0; i < nums.length; i++){
//            if (nums[i] > largest){
//             secondLargest = largest;
//             largest = nums[i];
//            } else if (nums[i] > secondLargest && nums[i] != largest)  {
//                secondLargest = nums[i];
//            }
//        } return secondLargest;
//    }

//    private static int linearSearch(int[] nums) {
//        for (int i = 0; i < nums.length; i++){
//            if (nums[i] < 0){
//                return nums[i];
//            }
//        }return -1;
//    }

//    private static int linearSearch(int[] nums) {
//        int index = 0;
//        for (int i = 1; i < nums.length; i++) {
//            if (nums[i] < nums[index]){
//                index = i;
//            }
//        } return index;
//    }

//    private static int linearSearch(int[] nums) {
//        int smallest = nums[0];
//        for (int i = 1; i < nums.length; i++ ){
//            if (nums[i] < smallest){
//                smallest = nums[i];
//            }
//        } return smallest;
//    }

//    private static int linearSearch(int[] nums) {
//        int bigNum = nums[0];
//         for (int i = 1; i < nums.length; i++){
//             if (nums[i] > bigNum){
//                 bigNum = nums[i];
//             }
//         }return bigNum;
//    }

//    private static int linearSearch(int[] nums, int target) {
//        int counter = 0;
//        for (int i = 1; i < nums.length; i++){
//            if (nums[i] == target) {
//                counter++;
//            }
//        }  return counter;
//    }
//
//    private static boolean linearSearch(int[] nums, int target) {
//        for (int i = 0; i < nums.length; i++ ){
//            if (nums[i] == target) {
//                return true;
//            }else {
//            }
//        }
//        return false;
//    }

//    private static int linearSearch(int[] nums, int target) {
//        for (int i = 0; i < nums.length; i++) {
//                if (nums[i] == target) {
//                    return i;
//                }
//            }
//            return -1;
//    }
}
