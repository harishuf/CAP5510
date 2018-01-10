public class DoveTailAlignment
{
	private String strand1;
    private String strand2;
    private int gap_penalty;
    private int[][] matrix;
    private String alphabet;
    private int[][] dp;
    private int score;
    private String[] alignedStrands;
    public int[] start = new int[4];
    public DoveTailAlignment(String strand1, String strand2, int[][] matrix, String alphabet, int gap_penalty) {
        this.strand1 = strand1.toUpperCase();
        this.strand2 = strand2.toUpperCase();
        this.matrix = matrix;
        this.alphabet = alphabet;
        this.gap_penalty = gap_penalty;
        this.dp = finddp();
        this.score = findmaxinlastrowandlastcolumn(dp);
        this.alignedStrands = findPath();
    }
    //Runs in Linear O(n) time
    //Should be faster than the local alignment
    public int findmaxinlastrowandlastcolumn(int[][] dp) {
    	int max = Integer.MIN_VALUE;
    	for(int i=0;i<dp.length;i++){
    		max = Math.max(max,dp[i][dp[0].length-1]);
    	}
    	for(int j=0;j<dp[0].length;j++){
    		max = Math.max(max,dp[dp.length-1][j]);
    	}
    	return max;
	}

    public int[][] finddp()
    {
    	int[][] dp = new int[strand1.length()+1][strand2.length()+1];
    	for(int i = 1; i <= strand1.length(); i++)
    	{
            dp[i][0] = 0;
        }
        for(int j = 1; j <= strand2.length(); j++)
        {
            dp[0][j] = 0;
        }

        for(int i = 1; i < strand1.length() + 1; i++)
        {
            for(int j = 1; j < strand2.length() + 1; j++)
            {
            	dp[i][j] = Math.max(Math.max(dp[i][j-1] + gap_penalty,dp[i-1][j] + gap_penalty),
            			dp[i-1][j-1] + getValues(matrix,strand1.charAt(i-1),strand2.charAt(j-1),alphabet));
            }
        }
        return dp;
    }
    public String[] findPath() {
    	boolean col = false;
        String alignedStrand1 = "";
        String alignedStrand2 = "";
        int i = searchlastcolumn(dp);
        int j = dp[0].length-1;
        if(i==-1){
        	 col = true;
        	j = searchlastrow(dp);
        	i = dp.length-1;
        }
        if(col){
        	start[1] = dp.length-1;
        	start[2] = 0;
        	start[3] = j;
        }
        else{
        	start[0] = 0;
        	start[1] = i;
        	start[3] = dp[0].length-1;
        }
        while (i > 0  && j > 0)
        {
            if (dp[i-1][j-1] == dp[i][j] - getValues(matrix,strand1.charAt(i-1),strand2.charAt(j-1),alphabet))
            {
                alignedStrand1 = strand1.charAt(i-1) + alignedStrand1;
                alignedStrand2 = strand2.charAt(j-1) + alignedStrand2;
                i -= 1;
                j -= 1;
            }
            else if (dp[i][j-1] == dp[i][j] - gap_penalty)
            {
                alignedStrand1 = "-" + alignedStrand1;
                alignedStrand2 = strand2.charAt(j-1) + alignedStrand2;
                j -= 1;
            }
            else {
                alignedStrand1 = strand1.charAt(i-1) + alignedStrand1;
                alignedStrand2 = "-" + alignedStrand2;
                i -= 1;
            }
            if(col){
            start[0] = i;
            }
            else{
            	start[2] = j;
            }
        }
        return new String[] {alignedStrand1, alignedStrand2};
    }

    public int searchlastrow(int[][] dp) {
    	for(int j=0;j<dp[0].length;j++){
    		if(score == dp[dp.length-1][j])
    			return j;
    	}
    	return -1;
	}
	public int searchlastcolumn(int[][] dp) {
		for(int i=0;i<dp.length;i++){
    		if(score == dp[i][dp[0].length-1])
    			return i;
    	}
		return -1;
	}
    public int getScore()
    {
        return score;
    }
    public int[] getstartingpositions(int[][] dp){
    	return new int[0];
    }

    public String[] getalignedstrands()
    {
    	return alignedStrands;
    }
    public Integer[] getstart(){
		Integer[] ret = new Integer[4];
		ret[0] = start[0];
		ret[1] = start[1];
		ret[2] = start[2];
		ret[3] = start[3];
		return ret;
	}

    public int getValues(int[][] matrix, char a, char b, String s)
	{
    	int index1 = s.indexOf(a);
    	int index2 = s.indexOf(b);
    	return matrix[index1][index2];
    }
}