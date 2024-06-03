package com.cmux.entity;

import java.util.List;

import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Relationship;
import org.springframework.data.neo4j.core.schema.Relationship.Direction;


@Node(labels = {"User"})
public class User {

	@Id
    private Long userId;

	private String name;

	@Relationship(type = "SUBSCRIBED_TO", direction = Direction.OUTGOING)
	private List<User> subscriptions;

	@Relationship(type = "SUBSCRIBED_BY", direction = Direction.INCOMING)
	private List<User> subscribers;

	// More relationships can be added here
	
	
    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
