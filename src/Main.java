import search.ScheduleSurfer;
import when2meet.AvailabilityImporter;
import when2meet.When2MeetRecreator;

/**
 * @author Matthew Rueben
 */
public class Main
{
    public static void main(String[] args)
    {
        // Read in CSV.
        String csvFilename = "test.csv";
        int numPeople = 9;
        int numDays = 3;
        int numTimes = 4;
        boolean[][][] available = AvailabilityImporter.load(csvFilename, numPeople, numDays, numTimes);

        // Visualize availability to check against When2Meet.
        //When2MeetRecreator.draw(available);

        // Stretch feature: use "Dr. Rueben" entry as constraints.

        // Set up ScheduleSurfer.
        int windowDuration = 3; // I.e., 1 hour.
        int numWindowsInSchedule = 2;
        ScheduleSurfer scheduleSurfer = new ScheduleSurfer(numDays, numTimes, windowDuration, numWindowsInSchedule);

        // Set up ScheduleScorer.

        // Set up SearchDirector.
//        boolean backtrackingOn = true;
//        int initialMaxMin = -1;
//        GroupingSearchForMaxMin searchDirector = new GroupingSearchForMaxMin(scorer, backtrackingOn, initialMaxMin);
        // browser.setSearchDirector(searchDirector);

        // Run search!
        System.out.println("Trying to find " + numWindowsInSchedule + " windows of " + windowDuration + " slots each ...");
        long startTime = System.currentTimeMillis();
        scheduleSurfer.findAllScheduleCompletions();
        long endTime = System.currentTimeMillis();
        System.out.println("Full groupings explored: " + scheduleSurfer.getNumSchedulesFound());
        System.out.printf("Time taken for generation: %.2f seconds%n", (endTime - startTime) / 1000.0);

        // For ScheduleSurfer:
        // Exhaustive search without pruning.
        // First hour starts at earliest time.
        // Subsequent hours start just after end of previous hour.
        // End when last hour couldn't find any valid places to be.

        // For SearchDirector:
        // Objective function: maximize the minimum of first metric, then second metric.
        // Keep track of best 10 schedules.

        // User selects a schedule from the list.

        // Print out all respondents and how much of each hour they can make.
    }
}
