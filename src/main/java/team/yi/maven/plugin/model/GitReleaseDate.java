package team.yi.maven.plugin.model;

import org.apache.commons.lang3.time.DateFormatUtils;

import java.util.Date;

public class GitReleaseDate {
    private Date date;
    private String longDate;
    private String shortDate;

    public GitReleaseDate() {
        this(new Date());
    }

    @SuppressWarnings("PMD.ConstructorCallsOverridableMethod")
    public GitReleaseDate(Date date) {
        this.setDate(date);
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;

        this.longDate = DateFormatUtils.format(this.date, "yyyy-MM-dd HH:mm:ss");
        this.shortDate = DateFormatUtils.format(this.date, "yyyy-MM-dd");
    }

    public String getLongDate() {
        return longDate;
    }

    public String getShortDate() {
        return shortDate;
    }

    @Override
    public String toString() {
        return this.longDate;
    }
}
