package com.walkthenight.wordpress;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown=true)
public class RawAndRendered {
	public String raw;
	public String rendered;
}