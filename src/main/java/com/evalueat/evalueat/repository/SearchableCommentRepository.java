package com.evalueat.evalueat.repository;

import com.evalueat.evalueat.domain.Comment;
import java.util.List;

public interface SearchableCommentRepository {
	public List<Comment> searchByExample(Comment comment);
}
