package com.book.model;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QHistory is a Querydsl query type for History
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QHistory extends EntityPathBase<History> {

    private static final long serialVersionUID = 2093706743L;

    public static final QHistory history = new QHistory("history");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath isbn13 = createString("isbn13");

    public final StringPath loginId = createString("loginId");

    public final NumberPath<Long> userId = createNumber("userId", Long.class);

    public QHistory(String variable) {
        super(History.class, forVariable(variable));
    }

    public QHistory(Path<? extends History> path) {
        super(path.getType(), path.getMetadata());
    }

    public QHistory(PathMetadata metadata) {
        super(History.class, metadata);
    }

}

