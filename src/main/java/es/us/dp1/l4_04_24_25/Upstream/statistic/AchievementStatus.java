package es.us.dp1.l4_04_24_25.Upstream.statistic;

import es.us.dp1.l4_04_24_25.Upstream.statistics.Statistics;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AchievementStatus {

    private String name;
    private String description;
    private String badgeImage;
    private Integer threshold;
    private Integer actualValue;
    private Metric metric;
    private Integer id;

    public AchievementStatus(Achievement achievement, Statistics statistic) {
        name = achievement.getName();
        metric = achievement.getMetric();
        id = achievement.getId();
        description = achievement.getActualDescription();
        badgeImage = achievement.getBadgeImage();
        threshold = achievement.getThreshold();
        switch (achievement.getMetric()) {
            case GAMES_PLAYED:
                actualValue = statistic.getMatchesPlayed();
                break;
            case VICTORIES:
                actualValue = statistic.getMatchesWon();
                break;
            default:
                break;
        }
    }



}
