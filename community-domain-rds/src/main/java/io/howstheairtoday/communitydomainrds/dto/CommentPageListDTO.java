package io.howstheairtoday.communitydomainrds.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CommentPageListDTO {

    private List<CommentPageDTO> commentPageDTOList;

    private boolean hasNext;

    private int currentPage;

    private int pageSize;

    private boolean isFirst;

    private boolean isLast;
}


