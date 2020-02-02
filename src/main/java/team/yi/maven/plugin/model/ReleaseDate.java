package team.yi.maven.plugin.model;

import lombok.Getter;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import team.yi.maven.plugin.config.ReleaseLogSettings;

import java.io.Serializable;
import java.util.Date;

@Getter
public class ReleaseDate implements Serializable {
    private static final long serialVersionUID = 4695877165574997080L;

    private Date date;
    private String longDate;
    private String shortDate;

    public ReleaseDate() {
        this(new Date(), null, null);
    }

    public ReleaseDate(Date date, String longDateFormat, String shortDateFormat) {
        this.date = date;

        final String longDatePattern = StringUtils.defaultIfEmpty(longDateFormat, ReleaseLogSettings.LONG_DATE_FORMAT);
        final String shortDatePattern = StringUtils.defaultIfEmpty(shortDateFormat, ReleaseLogSettings.SHORT_DATE_FORMAT);

        this.longDate = DateFormatUtils.format(this.date, longDatePattern);
        this.shortDate = DateFormatUtils.format(this.date, shortDatePattern);
    }

    @Override
    public String toString() {
        return this.longDate;
    }
}
