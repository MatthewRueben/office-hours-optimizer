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

        // Input desired # of office hours.

        // Exhaustive search without pruning.
        // First hour starts at earliest time.
        // Subsequent hours start just after end of previous hour.
        // End when last hour couldn't find any valid places to be.
        // First metric for each respondent: # hours they can attend for at least 30 minutes.
        // Second (i.e., tiebreaker) metric: total minutes they can attend of proposed hours.
        // Objective function: maximize the minimum of first metric, then second metric.
        // Keep track of best 10 schedules.

        // User selects a schedule from the list.

        // Print out all respondents and how much of each hour they can make.
    }
}
