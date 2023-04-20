package io.howstheairtoday.communitydomainrds.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Repository;

import io.howstheairtoday.communitydomainrds.entity.Post;

public interface PostQslRepository {
    Slice<Post> findByRegion(String region, LocalDateTime createdAt, int limit);

    List<Map<String, Object>> findByRegionList(String region, LocalDateTime createdAt, int limit);

}