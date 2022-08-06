package com.book.model;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QRecommend is a Querydsl query type for Recommend
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QRecommend extends EntityPathBase<Recommend> {

    private static final long serialVersionUID = 1271188831L;

    public static final QRecommend recommend = new QRecommend("recommend");

    public final StringPath approval = createString("approval");

    public final StringPath bkIsbn = createString("bkIsbn");

    public final StringPath commentBrief = createString("commentBrief");

    public final StringPath commentDetail = createString("commentDetail");

    public final StringPath createDate = createString("createDate");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath itemId = createString("itemId");

    public final StringPath updateDate = createString("updateDate");

    public final StringPath userId = createString("userId");

    public QRecommend(String variable) {
        super(Recommend.class, forVariable(variable));
    }

    public QRecommend(Path<? extends Recommend> path) {
        super(path.getType(), path.getMetadata());
    }

    public QRecommend(PathMetadata metadata) {
        super(Recommend.class, metadata);
    }

}

