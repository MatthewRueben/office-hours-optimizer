package search;

import java.util.*;

public class ScheduleSearchForMaxMin implements ScheduleSearch
{
    private final ScheduleScorer scorer;
    private final TreeSet<ScoredSchedule> bestSchedules;
    private boolean backtrackingOn;
    private int numSchedulesToFind;

    private class ScoredSchedule implements Comparable<ScoredSchedule>
    {
        private final List<Window> schedule;
        private final List<Score> scoresSorted;

        private ScoredSchedule(List<Window> schedule) {
            this.schedule = schedule;

            ScheduleScorer scorer = ScheduleSearchForMaxMin.this.scorer;
            List<Score> scores = scorer.getScoresForSchedule(schedule);
            this.scoresSorted = scores.stream().sorted().toList();
        }

        @Override
        public int compareTo(ScoredSchedule other)
        {
            Iterator<Score> theseSortedScoresIterator = this.scoresSorted.iterator();
            Iterator<Score> thoseSortedScoresIterator = other.scoresSorted.iterator();
            while (theseSortedScoresIterator.hasNext() && thoseSortedScoresIterator.hasNext())
            {
                Score thisScore = theseSortedScoresIterator.next();
                Score thatScore = thoseSortedScoresIterator.next();

                int comparison = thisScore.compareTo(thatScore) * -1; // <- DESCENDING ORDER!
                if (comparison != 0)
                {
                    return comparison;
                }
                else
                {
                    // Proceed to the next-lowest score.
                }
            }
            // Only reached if all scores were equal.
            return 0;
        }
    }

    public ScheduleSearchForMaxMin(ScheduleScorer scorer, boolean backtrackingOn, int numSchedulesToFind)
    {
        this.scorer = scorer;
        this.bestSchedules = new TreeSet<>();
        this.backtrackingOn = backtrackingOn;
        this.numSchedulesToFind = numSchedulesToFind;
    }


    /* Implementation of ScheduleSearch */

    @Override
    public void submitValidSchedule(List<Window> schedule)
    {
        ScoredSchedule scoredSchedule = new ScoredSchedule(schedule);
        this.bestSchedules.add(scoredSchedule);
        if (this.bestSchedules.size() > this.numSchedulesToFind)
        {
            this.bestSchedules.removeLast();
        }
    }


    @Override
    public boolean backtrackingIsOn()
    {
        return this.backtrackingOn;
    }


    @Override
    public boolean shouldBacktrack(List<Window> scheduleSoFar)
    {
        return false; // CURRENTLY UNIMPLEMENTED.
    }


    @Override
    public void printReport(String[] names, String[][] timeTexts, int numSchedulesToPrint, int numScoresToPrint)
    {
        int numSchedulesToActuallyPrint = Math.min(numSchedulesToPrint, this.bestSchedules.size());
        Iterator<ScoredSchedule> it = this.bestSchedules.iterator();
        int numSchedulesPrinted = 0;
        while (numSchedulesPrinted < numSchedulesToActuallyPrint && it.hasNext())
        {
            ScoredSchedule scoredSchedule = it.next();

            int numScoresToActuallyPrint = Math.min(numScoresToPrint, scoredSchedule.scoresSorted.size());

            System.out.print(numSchedulesPrinted + " "); // I.e., the 0-indexed position of this schedule.

            System.out.print("[");
            for (Window window : scoredSchedule.schedule)
            {
                int windowEndTime = window.startTime + window.duration - 1;

                System.out.print(timeTexts[window.day][window.startTime]);
                System.out.print(" - ");
                System.out.print(timeTexts[window.day][windowEndTime]);

                System.out.print("; ");
            }
            System.out.print("]");

            System.out.print(" -> ");

            System.out.print("min scores: ");
            System.out.print(scoredSchedule.scoresSorted.subList(0, numScoresToActuallyPrint));
            System.out.println();

            numSchedulesPrinted++;
        }

        while (true)
        {
            System.out.print("Enter index of schedule to inspect (or -1 to quit): ");
            Scanner keyboard = new Scanner(System.in);
            int input = keyboard.nextInt();

            if (input == -1)
            {
                break;
            }
            else
            {
                int chosenScheduleIndex = input;
                int currentScheduleIndex = -1;
                it = this.bestSchedules.iterator();
                List<Window> chosenSchedule = null;
                while (currentScheduleIndex != chosenScheduleIndex && it.hasNext())
                {
                    chosenSchedule = it.next().schedule;
                    currentScheduleIndex++;
                }
                if (chosenSchedule == null)
                {
                    System.out.println("Something went wrong finding that schedule index!");
                }
                else
                {
                    this.scorer.printAttendabilityByPerson(chosenSchedule, names);
                }
            }
        }
    }
}


