class Solution {
    public String removeOuterParentheses(String s) {
        StringBuilder sb =new StringBuilder();
        int cnt =0;
        for(char ch : s.toCharArray()){
            if(ch == ')'){
                cnt--;
            }
            if(cnt!=0){
                sb.append(ch);
            }
            if(ch =='('){
                cnt++;
            }
        }
        return sb.toString();
        
    }
}