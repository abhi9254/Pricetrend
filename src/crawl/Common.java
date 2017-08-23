package crawl;

import java.sql.Timestamp;
import java.time.LocalDateTime;

public class Common {
	public static Timestamp getTimestamp() {
		return Timestamp.valueOf((LocalDateTime.now()));
	}
}
