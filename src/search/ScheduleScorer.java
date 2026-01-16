package search;

import java.util.List;

public interface ScheduleScorer
{
    public List<Score> getScoresForSchedule(List<Window> schedule);
    public void printAttendabilityByPerson(List<Window> schedule);
}
