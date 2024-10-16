

# MLB 季後賽收益計算程序分析 | 概述

這個程序旨在計算 MLB（美國職棒大聯盟）球隊在季後賽中的潛在收益。它考慮了不同的比賽階段（季後賽和世界大賽）以及各種因素，如球場容量和上座率。

# team.java 程式碼分析 | 類別結構


### `Team` 類別

```java
class Team {
    String name;
    String stadium;
    int capacity;
    double playoffFillRate;
    double worldSeriesFillRate;

    public Team(String name, String stadium, int capacity, double playoffFillRate, double worldSeriesFillRate) {
        // 構造函數實現
    }
}
```

#### 特點：
- 使用斷言（assertions）確保輸入參數的有效性。
- 包含球隊的基本信息和相關統計數據。

### `PostseasonRevenue` 類別

```java
class PostseasonRevenue {
    // 常數定義
    private static final double TICKET_PRICE_PLAYOFF = 450;
    private static final double TICKET_PRICE_WORLD_SERIES = 800;
    private static final double REVENUE_SHARE_HOME = 0.85;
    private static final double REVENUE_SHARE_AWAY = 0.15;

    // 方法定義
    private static List<Team> initializeTeams() { ... }
    private static double calculateRevenue(Team team, int playoffGames, int worldSeriesGames) { ... }
    private static void calculateAndPrintRevenues(List<Team> teams) { ... }
    public static void main(String[] args) { ... }
}
```

## 主要方法分析

### `initializeTeams()`

- **功能**：初始化 MLB 球隊列表。
- **實現細節**：
  - 使用 `try-catch` 塊處理可能的 `AssertionError`。
  - 硬編碼球隊信息，包括名稱、球場、容量和上座率。

### `calculateRevenue()`

- **功能**：計算單個球隊的季後賽收益。
- **參數**：
  - `Team team`: 球隊對象
  - `int playoffGames`: 季後賽場次
  - `int worldSeriesGames`: 世界大賽場次
- **返回值**：計算得出的總收益。
- **異常處理**：
  - 拋出 `IllegalArgumentException` 如果比賽場次為負數。

### `calculateAndPrintRevenues()`

- **功能**：計算並打印所有球隊的收益情況。
- **實現細節**：
  - 遍歷球隊列表，計算每支球隊的最佳和最差收益情況。
  - 使用 `try-catch` 塊處理 `calculateRevenue()` 可能拋出的異常。

### `main()`

- **功能**：程序的入口點。
- **實現**：
  1. 調用 `initializeTeams()` 初始化球隊列表。
  2. 調用 `calculateAndPrintRevenues()` 計算並打印結果。

##  preventive programming

1. **斷言（Assertions）**：
   - 在 `Team` 類的構造函數中使用，確保輸入參數的有效性。

2. **異常處理**：
   - 在 `initializeTeams()` 中捕獲 `AssertionError`。
   - 在 `calculateRevenue()` 中拋出 `IllegalArgumentException`。
   - 在 `calculateAndPrintRevenues()` 中捕獲並處理異常。

3. **參數驗證**：
   - 在 `calculateRevenue()` 中檢查比賽場次的有效性。

4. **錯誤報告**：
   - 使用 `System.err.println()` 輸出錯誤信息。


## 





# Team2.java | 程式結構分析 


### 主要類別
1. `Team2`: 表示一支棒球隊伍
2. `PostseasonRevenue2`: 用於計算季後賽收益

### 配置文件
1. `teams.json`: 包含所有球隊的詳細資訊
2. `config.properties`: 包含全局設定和參數

## 程式碼分析

### Team2 類別
```java
class Team2 {
    String name;
    String stadium;
    int capacity;
    double playoffFillRate;
    double worldSeriesFillRate;

    // 構造函數
}
```
- 包含球隊的基本資訊
- 使用公共字段而非私有字段配合 getter/setter，可能存在封裝性問題

### PostseasonRevenue2 類別
```java
class PostseasonRevenue2 {
    private final double ticketPricePlayoff;
    private final double ticketPriceWorldSeries;
    private final double revenueShareHome;
    private final double revenueShareAway;

    // 構造函數、方法等
}
```
- 使用 `Properties` 對象讀取配置
- 包含計算收益的核心邏輯

#### 主要方法
1. `readTeamsFromJson`: 從 JSON 文件讀取球隊數據
2. `calculateRevenue`: 計算單支球隊的收益
3. `calculateAndPrintRevenues`: 計算並打印所有球隊的收益
4. `main`: 程式入口點

## 配置文件分析

### teams.json
- 包含每支球隊的詳細資訊
- 使用 JSON 格式，易於讀取和修改

### config.properties
- 包含全局設定，如票價、收益分配比例等
- 使用 properties 格式，方便程式讀取






#  Team3.java | 程式結構分析

這個程式旨在計算 MLB（美國職棒大聯盟）球隊在季後賽中的潛在收益。它考慮了不同的比賽階段（外卡賽、分區系列賽、聯盟冠軍賽和世界大賽）以及各種因素，如球場容量、上座率和票價。

## 類別結構

### `Team3` 類別

```java
class Team3 {
    String name;
    String stadium;
    int capacity;
    double playoffFillRate;
    double worldSeriesFillRate;

    public Team3(String name, String stadium, int capacity, double playoffFillRate, double worldSeriesFillRate) {
        // 建構函數實現
    }
}
```

#### 特點：
- 使用斷言（assertions）確保輸入參數的有效性。
- 包含球隊的基本信息和相關統計數據。
- 區分了季後賽和世界大賽的上座率。

### `PostseasonRevenue3` 類別

```java
class PostseasonRevenue3 {
    private final double ticketPricePlayoff;
    private final double ticketPriceWorldSeries;
    private final double revenueShareHome;
    private final double revenueShareAway;

    // 方法定義
    public PostseasonRevenue3(Properties props) { ... }
    private double getDoubleProperty(Properties props, String key, double defaultValue) { ... }
    private static List<Team3> readTeamsFromJson(String filename) { ... }
    private double calculateRevenue(Team3 team, int homeGames, int awayGames, boolean isWorldSeries) { ... }
    public void calculateAndPrintRevenues(List<Team3> teams) { ... }
    public static void main(String[] args) { ... }
}
```

## 主要方法分析

### `PostseasonRevenue3` 建構函數

- **功能**：初始化收益計算所需的參數。
- **實現細節**：
  - 從配置文件讀取票價和收益分配比例。
  - 使用 `getDoubleProperty` 方法處理可能的解析錯誤。
  - 驗證收益分配比例總和是否為 1。

### `getDoubleProperty`

- **功能**：從 Properties 物件中安全地讀取 double 類型的屬性值。
- **錯誤處理**：
  - 如果屬性不存在，返回默認值。
  - 如果無法解析為 double，拋出 `IllegalArgumentException`。

### `readTeamsFromJson`

- **功能**：從 JSON 文件讀取球隊數據。
- **實現細節**：
  - 使用 JSON.simple 庫解析 JSON 文件。
  - 為每個球隊創建 `Team3` 物件。

### `calculateRevenue`

- **功能**：計算單支球隊在特定情況下的收益。
- **參數**：
  - `team`: 球隊物件
  - `homeGames`: 主場比賽場次
  - `awayGames`: 客場比賽場次
  - `isWorldSeries`: 是否為世界大賽
- **實現細節**：
  - 根據是否為世界大賽選擇適當的票價和上座率。
  - 分別計算主場和客場收益。

### `calculateAndPrintRevenues`

- **功能**：計算並打印所有球隊的收益情況。
- **實現細節**：
  - 計算四種不同情境的收益：
    1. 在外卡賽被淘汰（最差情況）
    2. 在分區系列賽被淘汰
    3. 在聯盟冠軍賽被淘汰
    4. 打到世界大賽第 7 場（最佳情況）
  - 使用 `printf` 格式化輸出結果。

### `main` 方法

- **功能**：程序的入口點。
- **實現**：
  1. 讀取配置文件。
  2. 創建 `PostseasonRevenue3` 物件。
  3. 從 JSON 文件讀取球隊數據。
  4. 計算並打印所有球隊的收益。
- **錯誤處理**：
  - 捕獲並處理可能的 IOException、ParseException 和 IllegalArgumentException。
  - 使用 `System.err` 輸出錯誤信息。

## Preventive-programing

1. **斷言（Assertions）**：
   - 在 `Team3` 類的建構函數中使用，確保輸入參數的有效性。

2. **異常處理**：
   - 在 `main` 方法中捕獲並處理多種可能的異常。
   - 在 `getDoubleProperty` 中處理數值解析錯誤。

3. **參數驗證**：
   - 在 `calculateRevenue` 中檢查比賽場次的有效性。

4. **配置文件使用**：
   - 使用 Properties 類讀取配置，增加程式的靈活性。

5. **資源管理**：
   - 使用 try-with-resources 語句確保文件讀取器被正確關閉。

## 計算邏輯

程式考慮了以下因素來計算收益：

- 球場容量
- 季後賽和世界大賽的不同上座率
- 不同階段的票價（季後賽和世界大賽）
- 主場和客場的收益分成比例
- 不同季後賽階段的比賽場次





#  Team4.java | 程式結構分析

## 程式結構

這個程式主要由兩個類別組成：`Team4` 和 `PostseasonRevenue4`。

### Team4 類別

```markdown
## Team4 類別

這個類別代表一支棒球隊伍。

### 屬性
- `name`: 球隊名稱
- `stadium`: 球場名稱
- `capacity`: 球場容量
- `playoffFillRate`: 季後賽上座率
- `worldSeriesFillRate`: 世界大賽上座率

### 建構函數
建構函數使用斷言（assert）來確保輸入參數的有效性，包括：
- 名稱和球場不能為空
- 容量必須大於0
- 上座率必須在0到1之間
```

### PostseasonRevenue3 類別

```markdown
## PostseasonRevenue3 類別

這個類別負責計算季後賽的收益。

### 屬性
- `ticketPricePlayoff`: 季後賽票價
- `ticketPriceWorldSeries`: 世界大賽票價
- `revenueShareHome`: 主場收益分成比例
- `revenueShareAway`: 客場收益分成比例

### 主要方法
1. `getDoubleProperty`: 從配置文件中讀取屬性值
2. `readTeamsFromJson`: 從 JSON 文件讀取球隊數據
3. `calculateRevenue`: 計算單支球隊在特定情況下的收益
4. `calculateAndPrintRevenues`: 計算並打印所有球隊的收益情況
```

## 收益計算邏輯

收益計算的核心邏輯在 `calculateAndPrintRevenues` 方法中。這個方法為每支球隊計算不同階段的收益，包括：

1. 外卡賽
2. 分區系列賽
3. 聯盟冠軍賽
4. 世界大賽

每個階段都分別計算主場和客場的收益，並考慮贏和輸兩種情況。

### 收益計算公式

```markdown
主場收益 = 球場容量 * 上座率 * 票價 * 主場收益分成比例 * 比賽場次
客場收益 = 球場容量 * 上座率 * 票價 * 客場收益分成比例 * 比賽場次
```

### 各階段比賽場次

1. 外卡賽：
   - 贏：2場主場，1場客場
   - 輸：1場主場，1場客場

2. 分區系列賽：
   - 贏：3場主場，2場客場
   - 輸：2場主場，1場客場

3. 聯盟冠軍賽：
   - 贏：4場主場，3場客場
   - 輸：3場主場，2場客場

4. 世界大賽：
   - 贏：4場主場，3場客場
   - 輸：3場主場，2場客場

## 主要功能

1. 從配置文件讀取票價和收益分成比例
2. 從 JSON 文件讀取球隊數據
3. 為每支球隊計算不同階段的收益
4. 打印每支球隊在不同階段的主場和客場收益

## 錯誤處理

程式包含了多層的錯誤處理機制：
- 使用斷言確保球隊數據的有效性
- 處理配置文件讀取和解析的可能錯誤
- 處理 JSON 文件讀取和解析的可能錯誤
- 處理收益計算過程中可能出現的錯誤



