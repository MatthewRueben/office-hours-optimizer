package search;

import java.util.List;

public interface GroupingSearch
{
    public void submitValidGrouping(List<List<Integer>> grouping);
    public boolean backtrackingIsOn();
    public boolean shouldBacktrack(List<Integer> groupSoFar);
}
