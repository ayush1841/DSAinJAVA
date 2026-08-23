class Solution {
    public boolean isAnagram(String s, String t) {
        if(s.length()!=t.length()){
            return false;
        }
        int arr1[] =new int[256];
        int arr2[] =new int[256];
        for(int i=0;i<s.length();i++){
            char ch =s.charAt(i);
            char ph = t.charAt(i);
            arr1[ch]++;
            arr2[ph]++;
        
        } 
        for(int i = 0;i<256;i++){
            if(arr1[i]!=arr2[i]){
                return false;
            }
            
        }
        return true;
       
        
        
    }
}