import java.math.BigInteger;
import java.util.HashMap;
import java.util.List;

public class CoavailabilityScorer
{
    private final boolean[][][] available;
    private final int numPeople;
    private final int numDays;
    private final int numTimes;

    private final int minAdjacentSlotsToCount;

    private final HashMap<List<Integer>, Integer> savedScores;

    public CoavailabilityScorer(boolean[][][] available, int minAdjacentSlotsToCount, int initialSaveCapacity)
    {
        this.available = available;
        this.numPeople = available.length;
        this.numDays = available[0].length;
        this.numTimes = available[0][0].length;

        this.minAdjacentSlotsToCount = minAdjacentSlotsToCount;

        this.savedScores = new HashMap<>(initialSaveCapacity);
    }


    public static int calcNumPossibleGroupsOfOneSize(int numPeople, int groupSize) {
        BigInteger binom = BigInteger.ONE;
        for (int i = 1; i <= groupSize; i++) {
            binom = binom.multiply(BigInteger.valueOf(numPeople + 1 - i));
            binom = binom.divide(BigInteger.valueOf(i));
        }
        return binom.intValueExact();
    }


    public int getGroupScore(List<Integer> personIndices)
    {
        if (personIndices.size() == 1)
        {
            System.err.println("Tried to find the coavailability score of a 1-person group!");
            return -1;
        }

        if (this.savedScores.containsKey(personIndices))
        {
            //System.out.println("Retrieving saved score.");
            return this.savedScores.get(personIndices);
        }
        else
        {
            //System.out.println("Calculating and saving score.");
            return this.calcGroupScore(personIndices, true);
        }
    }


    private int calcGroupScore(List<Integer> personIndices, boolean saveIt)
    {
        int coavailabilityScore = 0;
        for (int dayIndex = 0; dayIndex < this.numDays; dayIndex++)
        {
            int adjacentAvailableSlotsCount = 0;
            for (int timeIndex = 0; timeIndex < this.numTimes; timeIndex++)
            {
                if (this.areAvailable(personIndices, dayIndex, timeIndex))
                {
                    adjacentAvailableSlotsCount++;
                }
                else // Party's over.
                {
                    if (adjacentAvailableSlotsCount > 0)
                    {
                        if (adjacentAvailableSlotsCount >= this.minAdjacentSlotsToCount)
                        {
                            coavailabilityScore += adjacentAvailableSlotsCount;
                        }
                        adjacentAvailableSlotsCount = 0;
                    }
                }
            }
            // Happens when a run goes up to the end of a day.
            if (adjacentAvailableSlotsCount >= this.minAdjacentSlotsToCount)
            {
                coavailabilityScore += adjacentAvailableSlotsCount;
            }
        }

        if (saveIt)
        {
            this.savedScores.put(personIndices, coavailabilityScore);
        }

        return coavailabilityScore;
    }


    private boolean areAvailable(List<Integer> personIndices, int dayIndex, int timeIndex)
    {
        for (int personIndex : personIndices)
        {
            if (!this.available[personIndex][dayIndex][timeIndex])
            {
                return false;
            }
        }
        return true;
    }
}
