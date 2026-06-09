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

    public String htmlStatement(Invoice invoice, Map<String, Play> plays) {
        return renderHtml(createStatementData(invoice, plays));
    }

    private String renderHtml(StatementData data) {
        var result = String.format("<h1>Statement for %s</h1>%n", data.customer());
        result += String.format("<table>%n");
        result += String.format("<tr><th>play</th><th>seats</th><th>cost</th></tr>%n");

        NumberFormat frmt = NumberFormat.getCurrencyInstance(Locale.US);

        for (var perf : data.performances()) {
            result += String.format("  <tr><td>%s</td><td>%s</td><td>%s</td></tr>%n", perf.play().name, perf.audience(), frmt.format(perf.amount() / 100));
        }
        result += String.format("</table>%n");
        result += String.format("<p>Amount owed is <em>%s</em></p>%n", frmt.format(data.totalAmount() / 100));
        result += String.format("<p>You earned <em>%s</em> credits</p>%n", data.totalVolumeCredits());
        return result;
    }

    public StatementData createStatementData(Invoice invoice, Map<String, Play> plays) {
        List<PerformanceData> performances = new ArrayList<>();
        for (var perf : invoice.performances) {
            var play = plays.get(perf.playID);
            var calculator = createPerformanceCalculator(perf, play);
            performances.add(new PerformanceData(
                perf.playID,
                play,
                perf.audience,
                calculator.amount(),
                calculator.volumeCredits()
            ));
        }
        return new StatementData(invoice.customer, performances, getTotalAmount(performances), getTotalVolumeCredits(performances));
    }

    private PerformanceCalculator createPerformanceCalculator(Performance performance, Play play) {
        switch (play.type) {
            case "tragedy":
                return new TragedyCalculator(performance, play);
            case "comedy":
                return new ComedyCalculator(performance, play);
            default:
                throw new Error("unknown type: %s".formatted(play.type));
        }
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

}

record StatementData(String customer, List<PerformanceData> performances, int totalAmount, int totalVolumeCredits) {}
record PerformanceData(String playID, Play play, int audience, int amount, int volumeCredits) {}

class PerformanceCalculator {
    protected final Performance performance;
    protected final Play play;

    public PerformanceCalculator(Performance performance, Play play) {
        this.performance = performance;
        this.play = play;
    }

    public int amount() {
        throw new Error("subclass responsibility");
    }

    public int volumeCredits() {
        return Math.max(performance.audience - 30, 0);
    }
}

class TragedyCalculator extends PerformanceCalculator {
    public TragedyCalculator(Performance performance, Play play) {
        super(performance, play);
    }

    @Override
    public int amount() {
        int result = 40000;
        if (performance.audience > 30) {
            result += 1000 * (performance.audience - 30);
        }
        return result;
    }
}

class ComedyCalculator extends PerformanceCalculator {
    public ComedyCalculator(Performance performance, Play play) {
        super(performance, play);
    }

    @Override
    public int amount() {
        int result = 30000;
        if (performance.audience > 20) {
            result += 10000 + 500 * (performance.audience - 20);
        }
        result += 300 * performance.audience;
        return result;
    }

    @Override
    public int volumeCredits() {
        return super.volumeCredits() + (int) Math.floor(performance.audience / 5);
    }
}
