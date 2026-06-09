package theatricalplays;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class StatementPrinter {

    public String print(Invoice invoice, Map<String, Play> plays) {
        return renderPlainText(createStatementData(invoice, plays));
    }

    public StatementData createStatementData(Invoice invoice, Map<String, Play> plays) {
        List<PerformanceData> performances = new ArrayList<>();
        for (var perf : invoice.performances) {
            var play = plays.get(perf.playID);
            performances.add(new PerformanceData(
                perf.playID,
                play,
                perf.audience,
                calculateAmount(perf, play),
                calculateVolumeCredits(perf, play)
            ));
        }
        return new StatementData(invoice.customer, performances, getTotalAmount(performances), getTotalVolumeCredits(performances));
    }

    private String renderPlainText(StatementData data) {
        var result = String.format("Statement for %s%n", data.customer());

        NumberFormat frmt = NumberFormat.getCurrencyInstance(Locale.US);

        for (var perf : data.performances()) {
            // print line for this order
            result += String.format("  %s: %s (%s seats)%n", perf.play().name, frmt.format(perf.amount() / 100), perf.audience());
        }
        result += String.format("Amount owed is %s%n", frmt.format(data.totalAmount() / 100));
        result += String.format("You earned %s credits%n", data.totalVolumeCredits());
        return result;
    }

    private int getTotalAmount(List<PerformanceData> performances) {
        var result = 0;
        for (var perf : performances) {
            result += perf.amount();
        }
        return result;
    }

    private int getTotalVolumeCredits(List<PerformanceData> performances) {
        var result = 0;
        for (var perf : performances) {
            result += perf.volumeCredits();
        }
        return result;
    }

    private int calculateVolumeCredits(Performance perf, Play play) {
        var result = 0;
        result += Math.max(perf.audience - 30, 0);
        if ("comedy".equals(play.type)) result += Math.floor(perf.audience / 5);
        return result;
    }

    private int calculateAmount(Performance perf, Play play) {
        var result = 0;
        switch (play.type) {
            case "tragedy":
                result = 40000;
                if (perf.audience > 30) {
                    result += 1000 * (perf.audience - 30);
                }
                break;
            case "comedy":
                result = 30000;
                if (perf.audience > 20) {
                    result += 10000 + 500 * (perf.audience - 20);
                }
                result += 300 * perf.audience;
                break;
            default:
                throw new Error("unknown type: %s".formatted(play.type));
        }
        return result;
    }

}

record StatementData(String customer, List<PerformanceData> performances, int totalAmount, int totalVolumeCredits) {}
record PerformanceData(String playID, Play play, int audience, int amount, int volumeCredits) {}
