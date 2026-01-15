package search;

/**
 * @author Matt Rueben
 */
public class Window
{
    public int day; // Index.
    public int startTime; // Index.
    public int duration; // In slots.

    public Window(int day, int startTime, int duration)
    {
        this.day = day;
        this.startTime = startTime;
        this.duration = duration;
    }

    @Override
    public String toString()
    {
        int endTime = this.startTime + this.duration - 1;
        return "Day " + this.day + ": " + this.startTime + "-" + endTime;
    }
}
