package com.takeaway.gameofthree.utils;

import com.takeaway.gameofthree.enums.ValidationStatus;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class ValidatedNumber {

	ValidationStatus status;
	Integer number;
	Integer move;

}
