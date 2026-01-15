import search.*;
import when2meet.AvailabilityImporter;

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
        int minSlotsToCountAsAttendable = 2; // I.e., 30 minutes.
        ScheduleScorer scheduleScorer = new ScheduleScorerByWindowsThenSlots(available, minSlotsToCountAsAttendable);

        // Set up ScheduleSearch.
        boolean backtrackingOn = false;
        ScheduleSearch searchManager = new ScheduleSearchForMaxMin(scheduleScorer, backtrackingOn);
        scheduleSurfer.setSearchManager(searchManager);

        // Get search results!
        scheduleSurfer.findAllSchedules();
        searchManager.printReport();

        // User selects a schedule from the list.

        // Print out all respondents and how much of each hour they can make.
    }
}
