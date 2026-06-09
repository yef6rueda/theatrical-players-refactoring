package theatricalplays;

import java.text.NumberFormat;
import java.util.Locale;
import java.util.Map;

public class StatementPrinter {

    public String print(Invoice invoice, Map<String, Play> plays) {
        var result = String.format("Statement for %s%n", invoice.customer);

        NumberFormat frmt = NumberFormat.getCurrencyInstance(Locale.US);

        for (var perf : invoice.performances) {
            var play = plays.get(perf.playID);
            var thisAmount = calculateAmount(perf, play);

            // print line for this order
            result += String.format("  %s: %s (%s seats)%n", play.name, frmt.format(thisAmount / 100), perf.audience);
        }
        result += String.format("Amount owed is %s%n", frmt.format(getTotalAmount(invoice, plays) / 100));
        result += String.format("You earned %s credits%n", getTotalVolumeCredits(invoice, plays));
        return result;
    }

    private int getTotalAmount(Invoice invoice, Map<String, Play> plays) {
        var result = 0;
        for (var perf : invoice.performances) {
            var play = plays.get(perf.playID);
            result += calculateAmount(perf, play);
        }
        return result;
    }

    private int getTotalVolumeCredits(Invoice invoice, Map<String, Play> plays) {
        var result = 0;
        for (var perf : invoice.performances) {
            var play = plays.get(perf.playID);
            result += calculateVolumeCredits(perf, play);
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
