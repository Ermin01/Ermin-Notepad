package erkos.notepaderkos;

import java.time.LocalDate;

public class TodoItem {

    private String shortDeskripcija;
    private String detalji;
    private LocalDate deadLine;

    public TodoItem(String shortDeskripcija, String detalji, LocalDate deadLine) {
        this.shortDeskripcija = shortDeskripcija;
        this.detalji = detalji;
        if (deadLine == null) {
            throw new IllegalArgumentException("Deadline ne može biti null.");
        }
        this.deadLine = deadLine;
    }

    public String getShortDeskripcija() {
        return shortDeskripcija;
    }

    public void setShortDeskripcija(String shortDeskripcija) {
        this.shortDeskripcija = shortDeskripcija;
    }

    public String getDetalji() {
        return detalji;
    }

    public void setDetalji(String detalji) {
        this.detalji = detalji;
    }

    public LocalDate getDeadLine() {
        return deadLine;
    }

    public void setDeadLine(LocalDate deadLine) {
        this.deadLine = deadLine;
    }


}
