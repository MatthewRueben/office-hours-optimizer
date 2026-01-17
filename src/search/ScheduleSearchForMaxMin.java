package search;

import java.util.*;

public class ScheduleSearchForMaxMin implements ScheduleSearch
{
    private final ScheduleScorer scorer;
    private final List<ScoredSchedule> scoredSchedules;
    private boolean backtrackingOn;

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

    public ScheduleSearchForMaxMin(ScheduleScorer scorer, boolean backtrackingOn)
    {
        this.scorer = scorer;
        this.scoredSchedules = new ArrayList<>();
        this.backtrackingOn = backtrackingOn;
    }


    /* Implementation of ScheduleSearch */

    @Override
    public void submitValidSchedule(List<Window> schedule)
    {
        ScoredSchedule scoredSchedule = new ScoredSchedule(schedule);
        this.scoredSchedules.add(scoredSchedule);
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
    public void printReport(String[] names)
    {
        List<ScoredSchedule> scoredSchedulesSorted = this.scoredSchedules.stream().sorted(Comparator.reverseOrder()).toList();

        int numScoresToPrint = 3;

        int index = 0;
        for (ScoredSchedule scoredSchedule : scoredSchedulesSorted)
        {
            List<Score> scoresSorted = scoredSchedule.scoresSorted.stream().sorted().toList();

            System.out.print(index + " ");
            System.out.print(scoredSchedule.schedule);
            System.out.print("; min scores: ");
            System.out.print(scoresSorted.subList(0, numScoresToPrint));
            System.out.println();

            index++;
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


