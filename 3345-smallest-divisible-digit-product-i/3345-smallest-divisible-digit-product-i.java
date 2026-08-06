class Solution {
    public int smallestNumber(int n, int t) {
        int x = n;
        while(true){
            int temp =x;
            int product =1;
            while(temp>0){
                int digit = temp%10;
                 product = product*digit;
                temp =temp/10;
            }
            if(product%t == 0){
                return x;
            }
            x++;
        }
        
        

        
    }
}