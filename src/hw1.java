import java.util.*;
public class hw1
{
    public static void main(String[] args) throws Exception
    {
    	helper h = new helper();
    	PriorityQueue<Integer> pq = new PriorityQueue<Integer>(Collections.reverseOrder());
    	int selector = Integer.parseInt(args[0]);
        String queryFile = args[1];
        String databaseFile = args[2];
        String alphabetFile = args[3];
        String scorematrixFile = args[4];
        String[] queryRecords = h.parseSeqs(queryFile);
        String[] databaseRecords = h.parseSeqs(databaseFile);
        String alphabets = h.readAsString(alphabetFile);
        int[][] matrix = h.parseMatrixFile(scorematrixFile);
        int gap = Integer.parseInt(args[6]);
        int k = Integer.parseInt(args[5]);
        String[] qids = h.ids(queryFile);
        String[] dids = h.ids(databaseFile);
        		Map<Integer, String[]> smap = new HashMap<Integer, String[]>();
        		Map<Integer, String[]> idmap = new HashMap<Integer, String[]>();
        		Map<Integer, Integer[]> offsetmap = new HashMap<Integer, Integer[]>();

        switch(selector){
        case 1:
        	for(int i=0;i<queryRecords.length;i++){
            	for(int j=0;j<databaseRecords.length;j++){
            		String query = queryRecords[i];
            		String sequence = databaseRecords[j];
            		NeedlemanWunsch n = new NeedlemanWunsch(query, sequence, matrix, alphabets,gap);
            		smap.put(n.getScore(), n.getalignedstrands());
            		idmap.put(n.getScore(), new String[]{qids[i],dids[j]});
                    pq.offer(n.getScore());
            	}
            }
            for(int i=0;i<k;i++){
            	int temp = pq.poll();
            	System.out.println("score = "+temp);
            	System.out.println("id1 "+idmap.get(temp)[0]+" 0 "+smap.get(temp)[0]);
            	System.out.println("id2 "+idmap.get(temp)[1]+" 0 "+smap.get(temp)[1]);
            }
        	break;
        case 2:
        	for(int i=0;i<queryRecords.length;i++){
            	for(int j=0;j<databaseRecords.length;j++){
            		String query = queryRecords[i];
            		String sequence = databaseRecords[j];
            		SmithWaterman s = new SmithWaterman(query, sequence, matrix, alphabets,gap);
            		smap.put(s.getScore(), s.getalignedstrands());
            		idmap.put(s.getScore(), new String[]{qids[i],dids[j]});
            		offsetmap.put(s.getScore(), s.getstart());
                    pq.offer(s.getScore());
            	}
            }
            for(int i=0;i<k;i++){
            	int temp = pq.poll();
            	System.out.println("score = "+temp);
            	System.out.println("id1 "+idmap.get(temp)[0]+" "+offsetmap.get(temp)[0]+" "+offsetmap.get(temp)[1]+" " +smap.get(temp)[0]);
            	System.out.println("id2 "+idmap.get(temp)[1]+" "+offsetmap.get(temp)[2]+" "+offsetmap.get(temp)[3]+" "+smap.get(temp)[1]);
            }
        	break;
        case 3:
        	for(int i=0;i<queryRecords.length;i++){
            	for(int j=0;j<databaseRecords.length;j++){
            		String query = queryRecords[i];
            		String sequence = databaseRecords[j];
            		DoveTailAlignment d = new DoveTailAlignment(query, sequence, matrix, alphabets,gap);
            		int t = d.getScore();
            		smap.put(t, d.getalignedstrands());
            		idmap.put(t, new String[]{qids[i],dids[j]});
            		offsetmap.put(t, d.getstart());
                    pq.offer(t);
            	}
            }
            for(int i=0;i<k;i++){
            	int temp = pq.poll();
            	System.out.println("score = "+temp);
            	System.out.println("id1 "+idmap.get(temp)[0]+" "+offsetmap.get(temp)[0]+" "+offsetmap.get(temp)[1]+" " +smap.get(temp)[0]);
            	System.out.println("id2 "+idmap.get(temp)[1]+" "+offsetmap.get(temp)[2]+" "+offsetmap.get(temp)[3]+" "+smap.get(temp)[1]);
            }
        	break;
    }
    }
}