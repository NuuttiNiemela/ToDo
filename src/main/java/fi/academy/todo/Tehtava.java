package fi.academy.todo;

import java.util.Date;

public class Tehtava {
    private int id;
    private String tehtava;
    private String tarkempiTehtava;
    private boolean valmis;
    private Date aikataulu;

    public Tehtava() {
        this.valmis = false;
        this.aikataulu = new Date();
    }

    public Tehtava(String tehtava) {
        this.tehtava = tehtava;
        this.valmis = false;
        this.aikataulu = new Date();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTehtava() {
        return tehtava;
    }

    public void setTehtava(String tehtava) {
        this.tehtava = tehtava;
    }

    public String getTarkempiTehtava() {
        return tarkempiTehtava;
    }

    public void setTarkempiTehtava(String tarkempitehtava) {
        this.tarkempiTehtava = tarkempitehtava;
    }

    public boolean isValmis() {
        return valmis;
    }

    public void setValmis(boolean valmis) {
        this.valmis = valmis;
    }

    public Date getAikataulu() {
        return aikataulu;
    }

    public void setAikataulu(Date aikataulu) {
        this.aikataulu = aikataulu;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Tehtava{");
        sb.append("id=").append(id);
        sb.append(", tehtava='").append(tehtava).append('\'');
        sb.append(", tarkempiTehtava='").append(tarkempiTehtava).append('\'');
        sb.append(", valmis='").append(valmis).append('\'');
        sb.append(", aikataulu='").append(aikataulu).append('\'');
        sb.append('}');
        return sb.toString();
    }

}
