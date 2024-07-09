package application;

public class DriverSeason {
    private int year;
    private String forename;
    private String surname;
    private int wins;
    private double totalPoints;
    private int seasonRank;

    // Constructor
    public DriverSeason(int year, String forename, String surname, int wins, double totalPoints, int seasonRank) {
        this.year = year;
        this.forename = forename;
        this.surname = surname;
        this.wins = wins;
        this.totalPoints = totalPoints;
        this.seasonRank = seasonRank;
    }

    // Getters
    public int getYear() { return year; }
    public String getForename() { return forename; }
    public String getSurname() { return surname; }
    public int getWins() { return wins; }
    public double getTotalPoints() { return totalPoints; }
    public int getSeasonRank() { return seasonRank; }

    // Método para obtener el nombre completo
    public String getFullName() { return forename + " " + surname; }
}