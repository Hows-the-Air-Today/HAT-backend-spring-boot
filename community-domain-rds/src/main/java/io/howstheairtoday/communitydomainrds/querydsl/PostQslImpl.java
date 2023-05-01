package io.howstheairtoday.communitydomainrds.querydsl;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.Tuple;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;

import io.howstheairtoday.communitydomainrds.entity.Comment;
import io.howstheairtoday.communitydomainrds.entity.Like;
import io.howstheairtoday.communitydomainrds.entity.Post;
import io.howstheairtoday.communitydomainrds.entity.QComment;
import io.howstheairtoday.communitydomainrds.entity.QLike;
import io.howstheairtoday.communitydomainrds.entity.QPost;
import io.howstheairtoday.communitydomainrds.entity.QPostImage;
import io.howstheairtoday.communitydomainrds.repository.PostQslRepository;
import jakarta.persistence.EntityManager;

@Repository
public class PostQslImpl extends QuerydslRepositorySupport implements PostQslRepository {

    private final JPAQueryFactory jpaQueryFactory;

    QPost post = QPost.post;
    QPostImage qPostImage = QPostImage.postImage;
    QComment qComment = QComment.comment;
    QLike qLike = QLike.like;

    public PostQslImpl(EntityManager entityManager) {
        super(Post.class);
        this.jpaQueryFactory = new JPAQueryFactory(entityManager);
    }

    @Override
    public List<Map<String, Object>> findByRegionList(String region, LocalDateTime createdAt, int limit) {

        BooleanBuilder builder = createPostQueryBuilder(region, createdAt);

        JPQLQuery<Post> posts = jpaQueryFactory.selectFrom(post)
            .leftJoin(post.imageArray, qPostImage)
            .where(builder)
            .orderBy(post.createdAt.desc())
            .limit(limit);

        // 조회한 게시물 및 이미지 배열 리스트를 반환
        List<Post> postList = posts.fetch();

        // 마지막 페이지 여부 판단
        boolean hasNext; // postList 개수가 limit 이상이면 다음 페이지가 있는 것으로 판단

        // limit 값과 데이터 개수가 일치하는 경우, 다음 페이지가 있는 것으로 처리
        if (postList.size() == limit) {
            hasNext = true;
        } else {
            hasNext = postList.size() >= limit;
        }

        // 마지막 인덱스의 createdAt 값을 가져옴
        LocalDateTime lastCreatedAt =
            postList.isEmpty() ? null : postList.get(postList.size() - 1).getCreatedAt().plusNanos(1);

        // 게시물과 hasNext 필드를 추가하여 반환
        return postList.stream()
            .map(post -> {
                Map<String, Object> resultMap = new HashMap<>();
                resultMap.put("post", post);
                resultMap.put("hasNext", hasNext && post.equals(postList.get(postList.size() - 1)));
                int comments = post.getComment().stream()
                    .filter(i -> i.getDeletedAt() == null)
                    .collect(Collectors.toList()).size();
                resultMap.put("commentCount", comments);
                List<Like> likedLikes = post.getLikes().stream()
                    .filter(Like::isLiked)
                    .collect(Collectors.toList());
                resultMap.put("likeCount", likedLikes.size());
                return resultMap;
            })
            .collect(Collectors.toList());
    }

    @Override
    public List<Post> findBylikesList(String region) {
        List<Post> list = jpaQueryFactory
            .selectDistinct(post)
            .from(post)
            .leftJoin(post.likes, qLike).fetchJoin()
            .where(post.deletedAt.isNull(), post.region.eq(region), qLike.liked.isTrue())
            .groupBy(post.id)
            .having(qLike.count().gt(0))
            .orderBy(qLike.count().desc(), post.createdAt.desc())
            .limit(5)
            .fetch();

        return list;
    }

    private BooleanBuilder createPostQueryBuilder(String region, LocalDateTime createdAt) {

        BooleanBuilder builder = new BooleanBuilder();

        // region이 존재하는 경우, region 조건을 추가
        if (region != null) {
            builder.and(post.region.eq(region));
        }

        // createdAt이 존재하는 경우, createdAt 조건을 추가
        if (createdAt != null) {
            builder.and(post.createdAt.lt(createdAt));
        }

        // deletedAt이 null인 경우, deletedAt 조건을 추가
        builder.and(post.deletedAt.isNull());

        return builder;
    }

    @Override
    public Slice<Post> findByRegion(String region, LocalDateTime createdAt, int limit) {
        BooleanBuilder builder = createPostQueryBuilder(region, createdAt);

        // Pageable 객체 생성
        Pageable pageable = PageRequest.of(0, limit, Sort.by(Sort.Direction.DESC, "createdAt"));

        // OFFSET 및 LIMIT 설정
        int offset = (int)pageable.getOffset();
        int pageSize = pageable.getPageSize();

        List<Post> posts = jpaQueryFactory.selectFrom(post)
            .leftJoin(post.imageArray, qPostImage).fetchJoin()
            .where(builder)
            .orderBy(post.createdAt.desc())
            .offset(offset) // OFFSET 설정
            .limit(pageSize) // LIMIT 설정
            .fetch();

        return new SliceImpl<>(posts, pageable, true);
    }
}