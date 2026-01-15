package search;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Matt Rueben
 * First metric for each respondent: # hours they can attend for at least 30 minutes.
 * Second (i.e., tiebreaker) metric: total minutes they can attend of proposed hours.
 */
public class ScheduleScorerByWindowsThenSlots implements ScheduleScorer
{
    private final boolean[][][] available;
    private final int numPeople;
    private final int numDays;
    private final int numTimes;

    private final int minSlotsToCountAsAttendable;

    //private final HashMap<List<Window>, List<Integer>> savedScores;

    public ScheduleScorerByWindowsThenSlots(boolean[][][] available, int minSlotsToCountAsAttendable)//, int initialSaveCapacity)
    {
        this.available = available;
        this.numPeople = available.length;
        this.numDays = available[0].length;
        this.numTimes = available[0][0].length;

        this.minSlotsToCountAsAttendable = minSlotsToCountAsAttendable;

        //this.savedScores = new HashMap<>(initialSaveCapacity);
    }


    public List<Score> getScoresForSchedule(List<Window> schedule)
    {
        //if (this.savedScores.containsKey(personIndices))
        //{
            //System.out.println("Retrieving saved score.");
            //return this.savedScores.get(personIndices);
        //}
        //else
        //{
            //System.out.println("Calculating and saving score.");
        return this.calcScoresForSchedule(schedule);//, true);
        //}
    }


    private List<Score> calcScoresForSchedule(List<Window> schedule)//, boolean saveIt)
    {
        List<Score> scheduleScores = new ArrayList<>(this.numPeople);

        for (int personIndex = 0; personIndex < this.numPeople; personIndex++)
        {
            int attendableWindows = 0;
            int attendableSlots = 0;
            for (Window window : schedule)
            {
                int attendableSlotsOfThisWindow = 0;
                int dayIndex = window.day;
                int endTime = window.startTime + (window.duration - 1);
                for (int timeIndex = window.startTime; timeIndex <= endTime; timeIndex++)
                {
                    if (this.available[personIndex][dayIndex][timeIndex])
                    {
                        attendableSlotsOfThisWindow++;
                    }
                }
                attendableSlots += attendableSlotsOfThisWindow;

                if (attendableSlotsOfThisWindow >= minSlotsToCountAsAttendable)
                {
                    attendableWindows++;
                }
            }
            Score scheduleScore = new Score(personIndex, attendableWindows, attendableSlots);
            scheduleScores.add(scheduleScore);
        }

//        if (saveIt)
//        {
//            this.savedScores.put(personIndices, scheduleScores);
//        }

        return scheduleScores;
    }
}
