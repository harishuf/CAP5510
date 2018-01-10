public class NeedlemanWunsch
{
	private String strand1;
    private String strand2;
    private int gap_penalty;
    private int[][] matrix;
    private String alphabet;
    private int[][] dp;
    private int score;
    private String[] alignedStrands;
    public NeedlemanWunsch(String strand1, String strand2, int[][] matrix, String alphabet, int gap_penalty) {
        this.strand1 = strand1.toUpperCase();
        this.strand2 = strand2.toUpperCase();
        this.matrix = matrix;
        this.alphabet = alphabet;
        this.gap_penalty = gap_penalty;
        this.dp = finddp();
        this.score = dp[dp.length-1][dp[0].length-1];
        this.alignedStrands = findPath();
    }

    public int[][] finddp()
    {
        int[][] dp = new int[strand1.length()+1][strand2.length()+1];
        for (int i = 1; i <= strand2.length(); i++)
        {
            dp[0][i] = dp[0][i-1] + gap_penalty;
        }
        for (int i = 1; i <= strand1.length(); i++)
        {
            dp[i][0] = dp[i-1][0] + gap_penalty;
        }
        for (int i = 1; i <= strand1.length(); i++)
        {
            for (int j = 1; j <= strand2.length(); j++)
            {
                dp[i][j] = Math.max(Math.max(dp[i][j-1] + gap_penalty, dp[i-1][j] + gap_penalty), dp[i-1][j-1] + getValues(matrix,strand1.charAt(i-1),strand2.charAt(j-1),alphabet));
            }
        }
        return dp;
    }

    public String[] findPath() {
        String alignedStrand1 = "";
        String alignedStrand2 = "";
        int i = dp.length - 1;
        int j = dp[0].length - 1;
        while (i != 0  && j != 0)
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
        }
        if (i == 0)
        {
            for (int k = 0; k < j; k++) {
                alignedStrand1 = "-" + alignedStrand1;
                alignedStrand2 = strand2.charAt(j-k) + alignedStrand2;
            }
        }
        else {
            for (int k = 0; k < i; k++) {
                alignedStrand1 = strand1.charAt(i-k) + alignedStrand1;
                alignedStrand2 = "-" + alignedStrand2;
            }
        }

        return new String[] {alignedStrand1, alignedStrand2};
    }

    public void printStrandInfo()
    {
    	System.out.println("The score for this alignment is: " + score);
        System.out.println(alignedStrands[0]);
        System.out.println(alignedStrands[1]);
    }
    public int getScore()
    {
        return score;
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