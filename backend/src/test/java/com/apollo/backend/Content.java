package com.apollo.backend;

import java.util.Arrays;

public class Content {

	public String[] content;

	public Content() { }

	public Content(String[] content) {
		this.content = content;
	}

	public String[] getContent() {
        return content;
    }

	public boolean containsEquals(Content otherContent) {
		for (String item : this.content){
			if(Arrays.asList(otherContent.getContent()).contains(item)){
				continue;
			} else {
				return false;
			}
		}

		return true;
	}
}