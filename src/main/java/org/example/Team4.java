package org.example.Demo;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

class Team4 {
    String name;
    String stadium;
    int capacity;
    double playoffFillRate;
    double worldSeriesFillRate;

    public Team4(String name, String stadium, int capacity, double playoffFillRate, double worldSeriesFillRate) {
        assert name != null && !name.isEmpty() : "球隊名稱不能為空";
        assert stadium != null && !stadium.isEmpty() : "球場名稱不能為空";
        assert capacity > 0 : "球場容量必須大於0";
        assert playoffFillRate >= 0 && playoffFillRate <= 1 : "季後賽上座率必須在0到1之間";
        assert worldSeriesFillRate >= 0 && worldSeriesFillRate <= 1 : "世界大賽上座率必須在0到1之間";

        this.name = name;
        this.stadium = stadium;
        this.capacity = capacity;
        this.playoffFillRate = playoffFillRate;
        this.worldSeriesFillRate = worldSeriesFillRate;
    }
}

class PostseasonRevenue4 {
    private final double ticketPricePlayoff;
    private final double ticketPriceWorldSeries;
    private final double revenueShareHome;
    private final double revenueShareAway;

    public PostseasonRevenue4(Properties props) throws IllegalArgumentException {
        this.ticketPricePlayoff = getDoubleProperty(props, "ticket.price.playoff", 450);
        this.ticketPriceWorldSeries = getDoubleProperty(props, "ticket.price.worldseries", 800);
        this.revenueShareHome = getDoubleProperty(props, "revenue.share.home", 0.85);
        this.revenueShareAway = getDoubleProperty(props, "revenue.share.away", 0.15);

        if (Math.abs(this.revenueShareHome + this.revenueShareAway - 1.0) > 0.000001) {
            throw new IllegalArgumentException("收益分配比例總和必須為1");
        }
    }

    private double getDoubleProperty(Properties props, String key, double defaultValue) throws IllegalArgumentException {
        String value = props.getProperty(key);
        if (value == null) {
            return defaultValue;
        }
        try {
            return Double.parseDouble(value);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("無效的數值設定: " + key);
        }
    }

    private static List<Team4> readTeamsFromJson(String filename) throws IOException, ParseException {
        List<Team4> teams = new ArrayList<>();
        JSONParser parser = new JSONParser();

        try (FileReader reader = new FileReader(filename)) {
            JSONArray jsonTeams = (JSONArray) parser.parse(reader);

            for (Object obj : jsonTeams) {
                JSONObject jsonTeam = (JSONObject) obj;
                teams.add(new Team4(
                        (String) jsonTeam.get("name"),
                        (String) jsonTeam.get("stadium"),
                        ((Long) jsonTeam.get("capacity")).intValue(),
                        (Double) jsonTeam.get("playoffFillRate"),
                        (Double) jsonTeam.get("worldSeriesFillRate")
                ));
            }
        }

        return teams;
    }

    private double calculateHomeRevenue(Team4 team, int games, boolean isWorldSeries) {
        double ticketPrice = isWorldSeries ? ticketPriceWorldSeries : ticketPricePlayoff;
        double fillRate = isWorldSeries ? team.worldSeriesFillRate : team.playoffFillRate;
        return team.capacity * fillRate * ticketPrice * revenueShareHome * games;
    }

    private double calculateAwayRevenue(Team4 team, int games, boolean isWorldSeries) {
        double ticketPrice = isWorldSeries ? ticketPriceWorldSeries : ticketPricePlayoff;
        double fillRate = isWorldSeries ? team.worldSeriesFillRate : team.playoffFillRate;
        return team.capacity * fillRate * ticketPrice * revenueShareAway * games;
    }

    public void calculateAndPrintRevenues(List<Team4> teams) {
        for (Team4 team : teams) {
            try {
                // 主場收益計算
                double homeWildCardRevenue = calculateHomeRevenue(team, 3, false);
                double homeDivisionRevenue = homeWildCardRevenue + calculateHomeRevenue(team, 3, false);
                double homeChampionshipRevenue = homeDivisionRevenue + calculateHomeRevenue(team, 4, false);
                double homeWorldSeriesLossRevenue = homeChampionshipRevenue + calculateHomeRevenue(team, 4, true);
                double homeWorldSeriesWinRevenue = homeChampionshipRevenue + calculateHomeRevenue(team, 4, true);

                // 客場收益計算
                double awayWildCardRevenue = 0; // 外卡賽沒有客場比賽
                double awayDivisionRevenue = awayWildCardRevenue + calculateAwayRevenue(team, 2, false);
                double awayChampionshipRevenue = awayDivisionRevenue + calculateAwayRevenue(team, 3, false);
                double awayWorldSeriesLossRevenue = awayChampionshipRevenue + calculateAwayRevenue(team, 3, true);
                double awayWorldSeriesWinRevenue = awayChampionshipRevenue + calculateAwayRevenue(team, 3, true);

                // 打印結果
                System.out.printf("%nTeam 隊伍: %s%n", team.name);
                System.out.println("主場累積收益:");
                System.out.printf("  外卡賽: $%.2f%n", homeWildCardRevenue);
                System.out.printf("  分區系列賽: $%.2f%n", homeDivisionRevenue);
                System.out.printf("  聯盟冠軍賽: $%.2f%n", homeChampionshipRevenue);
                System.out.printf("  世界大賽（輸）: $%.2f%n", homeWorldSeriesLossRevenue);
                System.out.printf("  世界大賽（贏）: $%.2f%n", homeWorldSeriesWinRevenue);

                System.out.println("客場累積收益:");
                System.out.printf("  外卡賽: $%.2f%n", awayWildCardRevenue);
                System.out.printf("  分區系列賽: $%.2f%n", awayDivisionRevenue);
                System.out.printf("  聯盟冠軍賽: $%.2f%n", awayChampionshipRevenue);
                System.out.printf("  世界大賽（輸）: $%.2f%n", awayWorldSeriesLossRevenue);
                System.out.printf("  世界大賽（贏）: $%.2f%n", awayWorldSeriesWinRevenue);

            } catch (IllegalArgumentException e) {
                System.err.println("計算 " + team.name + " 的收益時發生錯誤: " + e.getMessage());
            }
        }
    }

    public static void main(String[] args) {
        try {
            Properties props = new Properties();
            try (InputStream input = PostseasonRevenue3.class.getClassLoader().getResourceAsStream("config.properties")) {
                if (input == null) {
                    throw new IOException("無法找到 config.properties 文件");
                }
                props.load(input);
            }

            PostseasonRevenue4 revenue = new PostseasonRevenue4(props);

            String jsonPath = props.getProperty("teams.json.path", "src/main/resources/teams.json");
            List<Team4> teams = readTeamsFromJson(jsonPath);

            revenue.calculateAndPrintRevenues(teams);
        } catch (IOException e) {
            System.err.println("讀取文件時發生錯誤: " + e.getMessage());
        } catch (ParseException e) {
            System.err.println("解析 JSON 文件時發生錯誤: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            System.err.println("配置文件中的參數無效: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("發生未預期的錯誤: " + e.getMessage());
            e.printStackTrace();
        }
    }
}