package crawl;

import java.sql.Timestamp;

public class Domain {
	private String domainHash;
	private String domainUrl;
	private boolean activated;
	private Timestamp created;
	private Timestamp modified;

	public String getDomainHash() {
		return domainHash;
	}

	public String getDomainUrl() {
		return domainUrl;
	}

	public boolean isActivated() {
		return activated;
	}

	public Timestamp getCreated() {
		return created;
	}

	public Timestamp getModified() {
		return modified;
	}

	public Domain(String domainHash, String domainUrl, boolean activated, Timestamp created, Timestamp modified) {
		this.domainHash = domainHash;
		this.domainUrl = domainUrl;
		this.activated = activated;
		this.created = created;
		this.modified = modified;
	}

	public Domain(String domainUrl) throws Exception {
		this.domainHash = Hasher.toSHA256(domainUrl);
		this.domainUrl = domainUrl;
		this.activated = true;
		this.modified = Common.getTimestamp();
		this.created = Common.getTimestamp();

	}

}
