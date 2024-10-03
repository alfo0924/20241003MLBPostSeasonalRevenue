import java.util.*;

class Team {
    String name;
    String stadium;
    int capacity;
    double playoffFillRate;
    double worldSeriesFillRate;

    public Team(String name, String stadium, int capacity, double playoffFillRate, double worldSeriesFillRate) {
        this.name = name;
        this.stadium = stadium;
        this.capacity = capacity;
        this.playoffFillRate = playoffFillRate;
        this.worldSeriesFillRate = worldSeriesFillRate;
    }
}

class PostseasonRevenue {
    private static final double TICKET_PRICE_PLAYOFF = 450;
    private static final double TICKET_PRICE_WORLD_SERIES = 800;
    private static final double REVENUE_SHARE_HOME = 0.85;
    private static final double REVENUE_SHARE_AWAY = 0.15;

    private static List<Team> initializeTeams() {
        List<Team> teams = new ArrayList<>();
        teams.add(new Team("LAD", "Dodger Stadium", 56000, 1.0, 1.0));
        teams.add(new Team("SD", "Petco Park", 40000, 1.0, 1.0));
        teams.add(new Team("MIL", "American Family Field", 42000, 0.97, 1.0));
        teams.add(new Team("NYM", "Citi Field", 41800, 1.0, 1.0));
        teams.add(new Team("ATL", "Truist Park", 41000, 1.0, 1.0));
        teams.add(new Team("BAL", "Oriole Park", 45000, 1.0, 1.0));
        teams.add(new Team("NYY", "Yankee Stadium", 47000, 1.0, 1.0));
        teams.add(new Team("CLE", "Progressive Field", 34800, 0.98, 1.0));
        teams.add(new Team("HOU", "Minute Maid Park", 41000, 1.0, 1.0));
        teams.add(new Team("KS", "Kauffman Stadium", 37000, 1.0, 1.0));
        teams.add(new Team("DET", "Comerica Park", 41000, 1.0, 1.0));
        return teams;
    }

    private static double calculateRevenue(Team team, int playoffGames, int worldSeriesGames) {
        double playoffRevenue = team.capacity * team.playoffFillRate * TICKET_PRICE_PLAYOFF * REVENUE_SHARE_AWAY * playoffGames;
        double worldSeriesRevenue = team.capacity * team.worldSeriesFillRate * TICKET_PRICE_WORLD_SERIES * REVENUE_SHARE_AWAY * worldSeriesGames;
        return playoffRevenue + worldSeriesRevenue;
    }

    private static void calculateAndPrintRevenues(List<Team> teams) {
        for (Team team : teams) {
            double worstRevenue = calculateRevenue(team, 2, 0); // Eliminated in Wild Card Series
            double bestRevenue = calculateRevenue(team, 12, 4); // Reaches World Series Game 7

            System.out.printf("Team: %s%n", team.name);
            System.out.printf("Worst scenario revenue: $%.2f (Eliminated in Wild Card Series)%n", worstRevenue);
            System.out.printf("Best scenario revenue: $%.2f (Reaches World Series Game 7)%n%n", bestRevenue);
        }
    }

    public static void main(String[] args) {
        List<Team> teams = initializeTeams();
        calculateAndPrintRevenues(teams);
    }
}