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
//        String csvFilename = "test.csv";
//        int numPeople = 9;
//        int numDays = 3;
//        int numTimes = 4;

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

        // 2026 Spring, both courses.
        String csvFilename = "availabilities_both_2026-Q1-Spring.csv";
        int numPeople = 14 + 7;
        int numDays = 5;
        int numTimes = 32;

        // Read in the CSV.
        String[] names = AvailabilityImporter.loadNames(csvFilename, numPeople);
        String[][] timeTexts = AvailabilityImporter.loadTimeTexts(csvFilename, numDays, numTimes);
        boolean[][][] available = AvailabilityImporter.loadAvailability(csvFilename, numPeople, numDays, numTimes);

        // Visualize availability to check against When2Meet.
        //When2MeetRecreator.draw(available);

        // Load constraints for test case.
//        boolean[][][] doable = new boolean[1][numDays][numTimes];
//        for (int dayIndex = 0; dayIndex < numDays; dayIndex++)
//        {
//            for (int timeIndex = 0; timeIndex < numTimes; timeIndex++)
//            {
//                doable[0][dayIndex][timeIndex] = true;
//            }
//        }

        // 2026 Spring, Dr. Rueben's constraints (i.e., slots that are possible).
        String constraintsFilename = "availabilities_Rueben_2026-Q1-Spring.csv";
        int numPeopleConstraining = 1;
        boolean[][][] doable = AvailabilityImporter.loadAvailability(constraintsFilename, numPeopleConstraining, numDays, numTimes);

        // Set up ScheduleSurfer.
        int windowDuration = 4; // I.e., 1 hour.
        int numWindowsInSchedule = 4;
        ScheduleSurfer scheduleSurfer = new ScheduleSurfer(doable, numDays, numTimes, windowDuration, numWindowsInSchedule);

        // Set up ScheduleScorer.
        int minSlotsToCountAsAttendable = 2; // I.e., 30 minutes.
        ScheduleScorer scheduleScorer = new ScheduleScorerByWindowsThenSlots(available, minSlotsToCountAsAttendable);

        // Set up ScheduleSearch.
        boolean backtrackingOn = false;
        int numSchedulesToFind = 20; // I.e., the 20 best-scoring schedules will be retained.
        ScheduleSearch searchManager = new ScheduleSearchForMaxMin(scheduleScorer, backtrackingOn, numSchedulesToFind);
        scheduleSurfer.setSearchManager(searchManager);

        // Get search results!
        scheduleSurfer.findAllSchedules();

        // 1. Print best schedules with their scores.
        // 2. Allow user to inspect attendability of schedules.
        int numSchedulesToPrint = 20;
        int numScoresToPrint = 5;
        searchManager.printReport(names, timeTexts, numSchedulesToPrint, numScoresToPrint);
    }
}
