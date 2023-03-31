package io.howstheairtoday.memberdomainrds.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QMember is a Querydsl query type for Member
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QMember extends EntityPathBase<Member> {

    private static final long serialVersionUID = -413420791L;

    public static final QMember member = new QMember("member1");

    public final QBaseTimeEntity _super = new QBaseTimeEntity(this);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> deletedAt = _super.deletedAt;

    public final StringPath email = createString("email");

    public final StringPath loginId = createString("loginId");

    public final StringPath loginPassword = createString("loginPassword");

    public final EnumPath<LoginRole> loginRole = createEnum("loginRole", LoginRole.class);

    public final EnumPath<LoginType> loginType = createEnum("loginType", LoginType.class);

    public final ComparablePath<java.util.UUID> memberId = createComparable("memberId", java.util.UUID.class);

    public final StringPath memberProfileImage = createString("memberProfileImage");

    public final StringPath nickname = createString("nickname");

    public final StringPath refreshToken = createString("refreshToken");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    public QMember(String variable) {
        super(Member.class, forVariable(variable));
    }

    public QMember(Path<? extends Member> path) {
        super(path.getType(), path.getMetadata());
    }

    public QMember(PathMetadata metadata) {
        super(Member.class, metadata);
    }

}

