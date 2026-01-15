package search;

public class Score implements Comparable<Score>
{
    private final Integer personIndex;
    private final Integer attendableWindows;
    private final Integer attendableSlots;

    public Score(int personIndex, int attendableWindows, int attendableSlots)
    {
        this.personIndex = personIndex;
        this.attendableWindows = attendableWindows;
        this.attendableSlots = attendableSlots;
    }


    @Override
    public String toString()
    {
        return this.attendableWindows + "w:" + this.attendableSlots + "s";
    }


    /* Implementation of Comparable<Score> */
    @Override
    public int compareTo(Score other)
    {
        int comparedByWindows = this.attendableWindows.compareTo(other.attendableWindows);
        int comparedBySlots = this.attendableSlots.compareTo(other.attendableSlots);

        /* Compare by windows first; break ties with slots. */
        if (comparedByWindows != 0)
        {
            return comparedByWindows;
        }
        else
        {
            return comparedBySlots;
        }
    }
}
