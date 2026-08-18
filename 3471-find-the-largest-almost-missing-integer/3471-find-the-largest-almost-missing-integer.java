class Solution {
    public int largestInteger(int[] nums, int k) {
        int n = nums.length;
        int[] count = new int[51];
        for(int num : nums){
            count[num]++;

        }
        if(k==1){
            int ans =-1;
            for(int num:nums){
                if(count[num]==1){
                    ans =Math.max(ans,num);
                }
            }
            return ans;
        }
        if(k==n){
            int ans  = 0;
            for(int num:nums){
                ans = Math.max(ans,num);
            }
            return ans;
        }
        int ans =-1;
        for(int num:nums){
            if(count[nums[0]] ==1){
                ans  = Math.max(ans,nums[0]);
            }
            if(count[nums[n-1]]==1){
                ans = Math.max(ans,nums[n-1]);
            }
        }
        return ans;
        
    }
}