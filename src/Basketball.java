public class Basketball implements Comparable<Basketball>{
    public String teamName;
    public int wins;
    public int losses;

    public Basketball(String teamName, int wins, int losses) {
        this.teamName = teamName;
        this.wins = wins;
        this.losses = losses;
    }

    @Override
    public int compareTo(Basketball other) {
        int thisWinPercentage = wins * 100 / (wins + losses);
        int otherWinPercentage = other.wins * 100 / (other.wins + other.losses);

        // Sort by win percentage in descending order
        return Integer.compare(otherWinPercentage, thisWinPercentage);
    }

    @Override
    public String toString() {
        return teamName + " (" + wins + "-" + losses + ")";
    }
}
