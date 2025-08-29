package no.hvl.poll_manager.model;

import lombok.Data;
import lombok.NonNull;

@Data
public class VoteOption {
	@NonNull
	String caption;

	int presentationOrder;
}
