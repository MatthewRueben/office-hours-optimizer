package search;

import java.math.BigInteger;
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

    public ScheduleScorerByWindowsThenSlots(boolean[][][] available, int minAdjacentSlotsToCount)//, int initialSaveCapacity)
    {
        this.available = available;
        this.numPeople = available.length;
        this.numDays = available[0].length;
        this.numTimes = available[0][0].length;

        this.minSlotsToCountAsAttendable = minAdjacentSlotsToCount;

        //this.savedScores = new HashMap<>(initialSaveCapacity);
    }


    public static int calcNumPossibleGroupsOfOneSize(int numPeople, int groupSize) {
        BigInteger binom = BigInteger.ONE;
        for (int i = 1; i <= groupSize; i++) {
            binom = binom.multiply(BigInteger.valueOf(numPeople + 1 - i));
            binom = binom.divide(BigInteger.valueOf(i));
        }
        return binom.intValueExact();
    }


    public int[][] getScoresForSchedule(List<Window> schedule)
    {
//        if (personIndices.size() == 1)
//        {
//            System.err.println("Tried to find the coavailability score of a 1-person group!");
//            return -1;
//        }

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


    private int[][] calcScoresForSchedule(List<Window> schedule)//, boolean saveIt)
    {
        int[][] scheduleScores = new int[this.numPeople][2];

        for (int personIndex = 0; personIndex < this.numPeople; personIndex++)
        {
            int windowsCountedAsAttendable = 0;
            int attendableSlotsTotal = 0;
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
                attendableSlotsTotal += attendableSlotsOfThisWindow;

                if (attendableSlotsOfThisWindow >= minSlotsToCountAsAttendable)
                {
                    windowsCountedAsAttendable++;
                }
            }
            scheduleScores[personIndex][0] = windowsCountedAsAttendable;
            scheduleScores[personIndex][1] = attendableSlotsTotal;
        }

//        if (saveIt)
//        {
//            this.savedScores.put(personIndices, scheduleScores);
//        }

        return scheduleScores;
    }
}
