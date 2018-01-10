public class SmithWaterman
{
	private String strand1;
    private String strand2;
    private int gap_penalty;
    private int[][] matrix;
    private String alphabet;
    private int[][] dp;
    private int score;
    private String[] alignedStrands;
    public int[] start = new int[2];
    public SmithWaterman(String strand1, String strand2, int[][] matrix, String alphabet, int gap_penalty)
    {
        this.strand1 = strand1.toUpperCase();
        this.strand2 = strand2.toUpperCase();
        this.matrix = matrix;
        this.alphabet = alphabet;
        this.gap_penalty = gap_penalty;
        this.dp = finddp();
        this.score = getmaxscore(dp);
        this.alignedStrands = findPath();
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
            	dp[i][j] = Math.max(Math.max(Math.max(0,dp[i][j-1] + gap_penalty),dp[i-1][j] + gap_penalty),
            			dp[i-1][j-1] + getValues(matrix,strand1.charAt(i-1),strand2.charAt(j-1),alphabet));
            }
        }
        return dp;
    }
	 public String[] findPath()
	 {
	        String alignedStrand1 = "";
	        String alignedStrand2 = "";
	        int i = getmaxrow(dp);
	        int j = getmaxcolumn(dp);
	        while (i != 0  && j != 0 && dp[i][j]!=0)
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
		            start[0] = i;
		            start[1] = j;
		        }
	        return new String[] {alignedStrand1, alignedStrand2};
	    }

    public int getmaxcolumn(int[][] dp) {
    	for(int i=0;i<dp.length;i++){
			for(int j=0;j<dp[0].length;j++){
				if(score==dp[i][j])
					return j;
			}
		}
		return -1;
	}

	public int getmaxrow(int[][] dp) {
		for(int i=0;i<dp.length;i++){
			for(int j=0;j<dp[0].length;j++){
				if(score==dp[i][j])
					return i;
			}
		}
		return -1;
	}

	public int getmaxscore(int[][] dp) {
		int max = 0;
		for(int i=0;i<dp.length;i++){
			for(int j=0;j<dp[0].length;j++){
				max = Math.max(max,dp[i][j]);
			}
		}
		return max;
	}
	public int getScore(){
    	return score;
    }
	public Integer[] getstart(){
		Integer[] ret = new Integer[4];
		ret[0] = start[0];
		ret[1] = getmaxrow(dp);
		ret[2] = start[1];
		ret[3] = getmaxcolumn(dp);
		return ret;
	}
	public String[] getalignedstrands()
	    {
	    	return alignedStrands;
	    }
    public int getValues(int[][] matrix, char a, char b, String s)
	{
    	int index1 = s.indexOf(a);
    	int index2 = s.indexOf(b);
    	return matrix[index1][index2];
    }
}