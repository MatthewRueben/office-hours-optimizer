package search;

import java.util.ArrayList;
import java.util.List;

public class GroupingSearchForMaxMin implements GroupingSearch
{
    private final CoavailabilityScorer scorer;
    private List<List<Integer>> bestGroupingSoFar;
    private int maxMinSoFar;
    //private int maxMinToBeatOrBacktrack;
    private boolean backtrackingOn;

    public GroupingSearchForMaxMin(CoavailabilityScorer scorer, boolean backtrackingOn, int initialMaxMin) //, int maxMinToBeatOrBacktrack)
    {
        this.scorer = scorer;
        this.bestGroupingSoFar = null;
        this.maxMinSoFar = initialMaxMin;
        //this.maxMinToBeatOrBacktrack = maxMinToBeatOrBacktrack;
        this.backtrackingOn = backtrackingOn;
    }

    public boolean backtrackingIsOn()
    {
        return this.backtrackingOn;
    }


    // -- Implementation of GroupingSearchDirection Interface --

    @Override
    public void submitValidGrouping(List<List<Integer>> grouping)
    {
        int min = this.calcMinScore(grouping);

        if (min > this.maxMinSoFar)
        {
            this.bestGroupingSoFar = deepCopyListOfLists(grouping);
            this.maxMinSoFar = min;
            System.out.println(this.bestGroupingSoFar + ", min score: " + this.maxMinSoFar);
        }

    }


    @Override
    public boolean shouldBacktrack(List<Integer> groupSoFar)
    {
        int groupScore = this.scorer.getGroupScore(groupSoFar);
        if (groupScore <= this.maxMinSoFar)
        {
            return true;
        }
        else
        {
            return false;
        }
    }


    // -- Helper Methods --

    private int calcMinScore(List<List<Integer>> grouping)
    {
        int minSoFar = Integer.MAX_VALUE;
        for (List<Integer> group : grouping)
        {
            int groupScore = this.scorer.getGroupScore(group);
            if (groupScore < minSoFar)
            {
                minSoFar = groupScore;
            }
        }
        return minSoFar;
    }


    private List<List<Integer>> deepCopyListOfLists(List<List<Integer>> originalList)
    {
        List<List<Integer>> deepCopyList = new ArrayList<>(originalList.size()); // Pre-size for potential performance
        for (List<Integer> sublist : originalList) {
            deepCopyList.add(new ArrayList<>(sublist)); // Copies each inner list
        }
        return deepCopyList;
    }




}


