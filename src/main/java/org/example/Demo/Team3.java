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

class Team3 {
    String name;
    String stadium;
    int capacity;
    double playoffFillRate;
    double worldSeriesFillRate;

    public Team3(String name, String stadium, int capacity, double playoffFillRate, double worldSeriesFillRate) {
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

class PostseasonRevenue3 {
    private final double ticketPricePlayoff;
    private final double ticketPriceWorldSeries;
    private final double revenueShareHome;
    private final double revenueShareAway;

    public PostseasonRevenue3(Properties props) throws IllegalArgumentException {
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

    private static List<Team3> readTeamsFromJson(String filename) throws IOException, ParseException {
        List<Team3> teams = new ArrayList<>();
        JSONParser parser = new JSONParser();

        try (FileReader reader = new FileReader(filename)) {
            JSONArray jsonTeams = (JSONArray) parser.parse(reader);

            for (Object obj : jsonTeams) {
                JSONObject jsonTeam = (JSONObject) obj;
                teams.add(new Team3(
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

    private double calculateHomeRevenue(Team3 team, int games, boolean isWorldSeries) {
        double ticketPrice = isWorldSeries ? ticketPriceWorldSeries : ticketPricePlayoff;
        double fillRate = isWorldSeries ? team.worldSeriesFillRate : team.playoffFillRate;
        return team.capacity * fillRate * ticketPrice * revenueShareHome * games;
    }

    private double calculateAwayRevenue(Team3 team, int games, boolean isWorldSeries) {
        double ticketPrice = isWorldSeries ? ticketPriceWorldSeries : ticketPricePlayoff;
        double fillRate = isWorldSeries ? team.worldSeriesFillRate : team.playoffFillRate;
        return team.capacity * fillRate * ticketPrice * revenueShareAway * games;
    }

    public void calculateAndPrintRevenues(List<Team3> teams) {
        for (Team3 team : teams) {
            try {
                // 主場收益計算
                double homeWorstRevenue = calculateHomeRevenue(team, 1, false); // 外卡賽至少1場主場
                double homeDivisionRevenue = calculateHomeRevenue(team, 2, false);
                double homeChampionshipRevenue = calculateHomeRevenue(team, 4, false);
                double homeWorldSeriesLossRevenue = calculateHomeRevenue(team, 4, false) + calculateHomeRevenue(team, 2, true);
                double homeWorldSeriesWinRevenue = calculateHomeRevenue(team, 4, false) + calculateHomeRevenue(team, 3, true);

                // 客場收益計算
                double awayWorstRevenue = calculateAwayRevenue(team, 2, false);
                double awayDivisionRevenue = calculateAwayRevenue(team, 3, false);
                double awayChampionshipRevenue = calculateAwayRevenue(team, 6, false);
                double awayWorldSeriesLossRevenue = calculateAwayRevenue(team, 6, false) + calculateAwayRevenue(team, 2, true);
                double awayWorldSeriesWinRevenue = calculateAwayRevenue(team, 6, false) + calculateAwayRevenue(team, 3, true);

                // 打印結果
                System.out.printf("%nTeam 隊伍: %s%n", team.name);
                System.out.println("主場收益範圍:");
                System.out.printf("  最差情況（外卡賽1場主場）: $%.2f%n", homeWorstRevenue);
                System.out.printf("  分區系列賽被淘汰: $%.2f%n", homeDivisionRevenue);
                System.out.printf("  聯盟冠軍賽被淘汰: $%.2f%n", homeChampionshipRevenue);
                System.out.printf("  打入世界大賽但輸掉: $%.2f%n", homeWorldSeriesLossRevenue);
                System.out.printf("  贏得世界大賽: $%.2f%n", homeWorldSeriesWinRevenue);

                System.out.println("客場收益範圍:");
                System.out.printf("  最差情況（外卡賽被淘汰）: $%.2f%n", awayWorstRevenue);
                System.out.printf("  分區系列賽被淘汰: $%.2f%n", awayDivisionRevenue);
                System.out.printf("  聯盟冠軍賽被淘汰: $%.2f%n", awayChampionshipRevenue);
                System.out.printf("  打入世界大賽但輸掉: $%.2f%n", awayWorldSeriesLossRevenue);
                System.out.printf("  贏得世界大賽: $%.2f%n", awayWorldSeriesWinRevenue);

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

            PostseasonRevenue3 revenue = new PostseasonRevenue3(props);

            String jsonPath = props.getProperty("teams.json.path", "src/main/resources/teams.json");
            List<Team3> teams = readTeamsFromJson(jsonPath);

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