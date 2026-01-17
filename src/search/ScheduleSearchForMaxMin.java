package search;

import java.util.*;

public class ScheduleSearchForMaxMin implements ScheduleSearch
{
    private final ScheduleScorer scorer;
    private final List<ScoredSchedule> bestSchedules;
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

                int comparison = thisScore.compareTo(thatScore);
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
        this.bestSchedules = new ArrayList<>();
        this.backtrackingOn = backtrackingOn;
        this.numSchedulesToFind = numSchedulesToFind;
    }


    /* Implementation of ScheduleSearch */

    @Override
    public void submitValidSchedule(List<Window> schedule)
    {
        ScoredSchedule scoredSchedule = new ScoredSchedule(schedule);
        if (this.bestSchedules.size() < this.numSchedulesToFind
                || scoredSchedule.compareTo(this.bestSchedules.getLast()) > 0)
        {
            this.bestSchedules.add(scoredSchedule);

            if (this.bestSchedules.size() > 1)
            {
                this.bestSchedules.sort(Comparator.reverseOrder());
            }
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
        List<ScoredSchedule> scoredSchedulesSorted = this.bestSchedules.stream().sorted(Comparator.reverseOrder()).toList();

        int numSchedulesToActuallyPrint = Math.min(numSchedulesToPrint, scoredSchedulesSorted.size());
        for (int index = 0; index < numSchedulesToActuallyPrint; index++)
        {
            ScoredSchedule scoredSchedule = scoredSchedulesSorted.get(index);
            List<Score> scoresSorted = scoredSchedule.scoresSorted.stream().sorted().toList();
            int numScoresToActuallyPrint = Math.min(numScoresToPrint, scoresSorted.size());

            System.out.print(index + " ");

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
            System.out.print(scoresSorted.subList(0, numScoresToActuallyPrint));
            System.out.println();

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
                List<Window> chosenSchedule = scoredSchedulesSorted.get(chosenScheduleIndex).schedule;
                this.scorer.printAttendabilityByPerson(chosenSchedule, names);
            }
        }
    }
}


