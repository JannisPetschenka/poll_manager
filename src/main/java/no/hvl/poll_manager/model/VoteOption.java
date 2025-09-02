package no.hvl.poll_manager.model;

import lombok.Data;
import lombok.NonNull;

@Data
public class VoteOption {
	@NonNull
	String caption;

	Integer presentationOrder;

	public VoteOption(String caption, Integer presentationOrder) {
		this.caption = caption;
		this.presentationOrder = presentationOrder;
	}

	public VoteOption() {
		this.caption = "";
		this.presentationOrder = 0;
	}
}
