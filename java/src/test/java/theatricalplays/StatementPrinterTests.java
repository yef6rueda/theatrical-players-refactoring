package theatricalplays;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.approvaltests.Approvals.verify;
import static org.junit.jupiter.api.Assertions.assertEquals;

class StatementPrinterTests {

    @Test
    void exampleStatement() {
        Map<String, Play> plays = Map.of(
                "hamlet",  new Play("Hamlet", "tragedy"),
                "as-like", new Play("As You Like It", "comedy"),
                "othello", new Play("Othello", "tragedy"));

        Invoice invoice = new Invoice("BigCo", List.of(
                new Performance("hamlet", 55),
                new Performance("as-like", 35),
                new Performance("othello", 40)));

        StatementPrinter statementPrinter = new StatementPrinter();
        var result = statementPrinter.print(invoice, plays);

        verify(result);
    }

    @Test
    void statementWithNewPlayTypes() {
        Map<String, Play> plays = Map.of(
                "henry-v",  new Play("Henry V", "history"),
                "as-like", new Play("As You Like It", "pastoral"));

        Invoice invoice = new Invoice("BigCo", List.of(
                new Performance("henry-v", 53),
                new Performance("as-like", 55)));

        StatementPrinter statementPrinter = new StatementPrinter();
        Error error = Assertions.assertThrows(Error.class,
            () -> statementPrinter.print(invoice, plays));
        assertEquals("unknown type: history", error.getMessage());
    }

    @Test
    void htmlStatement() {
        Map<String, Play> plays = Map.of(
                "hamlet",  new Play("Hamlet", "tragedy"),
                "as-like", new Play("As You Like It", "comedy"),
                "othello", new Play("Othello", "tragedy"));

        Invoice invoice = new Invoice("BigCo", List.of(
                new Performance("hamlet", 55),
                new Performance("as-like", 35),
                new Performance("othello", 40)));

        StatementPrinter statementPrinter = new StatementPrinter();
        var result = statementPrinter.htmlStatement(invoice, plays);

        var expected = String.join(System.lineSeparator(),
            "<h1>Statement for BigCo</h1>",
            "<table>",
            "<tr><th>play</th><th>seats</th><th>cost</th></tr>",
            "  <tr><td>Hamlet</td><td>55</td><td>$650.00</td></tr>",
            "  <tr><td>As You Like It</td><td>35</td><td>$580.00</td></tr>",
            "  <tr><td>Othello</td><td>40</td><td>$500.00</td></tr>",
            "</table>",
            "<p>Amount owed is <em>$1,730.00</em></p>",
            "<p>You earned <em>47</em> credits</p>"
        ) + System.lineSeparator();

        assertEquals(expected, result);
    }
}
