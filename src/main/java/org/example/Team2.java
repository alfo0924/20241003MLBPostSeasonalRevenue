package org.example;

import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import java.util.Properties;
import java.io.FileInputStream;

// 定義 Team 類別，用於表示一支棒球隊伍
class Team2 {
    String name;
    String stadium;
    int capacity;
    double playoffFillRate;
    double worldSeriesFillRate;

    public Team2(String name, String stadium, int capacity, double playoffFillRate, double worldSeriesFillRate) {
        this.name = name;
        this.stadium = stadium;
        this.capacity = capacity;
        this.playoffFillRate = playoffFillRate;
        this.worldSeriesFillRate = worldSeriesFillRate;
    }
}

// 定義 PostseasonRevenue 類別，用於計算季後賽收益
class PostseasonRevenue2 {
    private final double ticketPricePlayoff;
    private final double ticketPriceWorldSeries;
    private final double revenueShareHome;
    private final double revenueShareAway;

    public PostseasonRevenue2(Properties props) {
        this.ticketPricePlayoff = Double.parseDouble(props.getProperty("ticket.price.playoff", "450"));
        this.ticketPriceWorldSeries = Double.parseDouble(props.getProperty("ticket.price.worldseries", "800"));
        this.revenueShareHome = Double.parseDouble(props.getProperty("revenue.share.home", "0.85"));
        this.revenueShareAway = Double.parseDouble(props.getProperty("revenue.share.away", "0.15"));
    }

    // 從 JSON 文件讀取球隊數據
    private static List<Team> readTeamsFromJson(String filename) throws IOException, ParseException {
        List<Team> teams = new ArrayList<>();
        JSONParser parser = new JSONParser();

        try (FileReader reader = new FileReader(filename)) {
            JSONArray jsonTeams = (JSONArray) parser.parse(reader);

            for (Object obj : jsonTeams) {
                JSONObject jsonTeam = (JSONObject) obj;
                teams.add(new Team(
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

    // 計算單支球隊收益的方法
    private double calculateRevenue(Team team, int playoffGames, int worldSeriesGames) {
        if (playoffGames < 0 || worldSeriesGames < 0) {
            throw new IllegalArgumentException("比賽場次不能為負數");
        }

        double playoffRevenue = team.capacity * team.playoffFillRate * ticketPricePlayoff * revenueShareAway * playoffGames;
        double worldSeriesRevenue = team.capacity * team.worldSeriesFillRate * ticketPriceWorldSeries * revenueShareAway * worldSeriesGames;
        return playoffRevenue + worldSeriesRevenue;
    }

    // 計算並打印所有球隊收益的方法
    public void calculateAndPrintRevenues(List<Team> teams) {
        for (Team team : teams) {
            try {
                double worstRevenue = calculateRevenue(team, 2, 0);
                double bestRevenue = calculateRevenue(team, 12, 4);

                System.out.printf("Team: %s%n", team.name);
                System.out.printf("Worst scenario revenue: $%.2f (Eliminated in Wild Card Series)%n", worstRevenue);
                System.out.printf("Best scenario revenue: $%.2f (Reaches World Series Game 7)%n%n", bestRevenue);
            } catch (IllegalArgumentException e) {
                System.err.println("計算 " + team.name + " 的收益時發生錯誤: " + e.getMessage());
            }
        }
    }

    // 主方法
    public static void main(String[] args) {
        try {
            // 讀取配置文件
            Properties props = new Properties();
            props.load(new FileInputStream("src/main/resources/config.properties"));

            // 初始化 PostseasonRevenue 對象
            PostseasonRevenue2 revenue = new PostseasonRevenue2(props);

            // 從 JSON 文件讀取球隊數據
            List<Team> teams = readTeamsFromJson("src/main/resources/teams.json");

            // 計算並打印所有球隊的收益
            revenue.calculateAndPrintRevenues(teams);
        } catch (IOException | ParseException e) {
            System.err.println("讀取文件時發生錯誤: " + e.getMessage());
        }
    }
}