package search;

import java.util.List;

public interface ScheduleSearch
{
    public void submitValidSchedule(List<Window> schedule);
    public boolean backtrackingIsOn();
    public boolean shouldBacktrack(List<Window> scheduleSoFar);
    public void printReport();
}
