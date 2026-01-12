package search;

import java.util.List;

public interface ScheduleScorer
{
    public int[][] getScoresForSchedule(List<Window> schedule);
}
