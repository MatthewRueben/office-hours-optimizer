import search.*;
import when2meet.AvailabilityImporter;
import when2meet.When2MeetRecreator;

/**
 * @author Matthew Rueben
 */
public class Main
{
    public static void main(String[] args)
    {
        // Hand-made test case.
        String csvFilename = "test.csv";
        int numPeople = 9;
        int numDays = 3;
        int numTimes = 4;

        // 2026 Spring, CS 371.
//        String csvFilename = "availabilities_cs371_2026-Q1-Spring.csv";
//        int numPeople = 7;
//        int numDays = 5;
//        int numTimes = 32;

        // 2026 Spring, CS 273.
//        String csvFilename = "availabilities_cs273_2026-Q1-Spring.csv";
//        int numPeople = 14;
//        int numDays = 5;
//        int numTimes = 32;

        // Read in the CSV.
        String[] names = AvailabilityImporter.loadNames(csvFilename, numPeople);
        for (String name : names)
        {
            System.out.println(name);
        }
        boolean[][][] available = AvailabilityImporter.load(csvFilename, numPeople, numDays, numTimes);

        // Visualize availability to check against When2Meet.
        When2MeetRecreator.draw(available);

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
